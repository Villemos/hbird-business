/**
 * Licensed to the Hummingbird Foundation (HF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The HF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hbird.business.groundstation.hamlib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.component.netty.NettyConfiguration;
import org.hbird.business.api.ICatalogue;
import org.hbird.business.groundstation.base.DefaultPointingDataOptimizer;
import org.hbird.business.groundstation.base.DriverContext;
import org.hbird.business.groundstation.base.TrackingSupport;
import org.hbird.business.groundstation.configuration.GroundStationDriverConfiguration;
import org.hbird.business.groundstation.device.response.ResponseHandler;
import org.hbird.business.groundstation.device.response.ResponseHandlersMap;
import org.hbird.business.groundstation.hamlib.protocol.HamlibCommandEncoder;
import org.hbird.business.groundstation.hamlib.protocol.HamlibErrorLogger;
import org.hbird.business.groundstation.hamlib.protocol.HamlibLineDecoder;
import org.hbird.business.groundstation.hamlib.protocol.HamlibResponseBufferer;
import org.hbird.business.navigation.orekit.PointingDataCalculator;
import org.hbird.exchange.groundstation.IPointingDataOptimizer;
import org.hbird.exchange.interfaces.IStartableEntity;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class HamlibDriverTest {

    private static final String HOST = "localhost";
    private static final int PORT = 4532;
    private static final String KEY1 = "set_freq";
    private static final String KEY2 = "set_pos";

    @Mock
    private DriverContext<GroundStationDriverConfiguration, String, String> driverContext;

    @Mock
    private GroundStationDriverConfiguration config;

    @Mock
    private ResponseHandler<GroundStationDriverConfiguration, String, String> handler1;

    @Mock
    private ResponseHandler<GroundStationDriverConfiguration, String, String> handler2;

    private List<ResponseHandler<GroundStationDriverConfiguration, String, String>> handlers;

    private HamlibDriver<GroundStationDriverConfiguration> driver;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        handlers = Arrays.asList(handler1, handler2);
        driver = new HamlibDriver<GroundStationDriverConfiguration>() {

            @Override
            protected TrackingSupport<GroundStationDriverConfiguration> createTrackingSupport(GroundStationDriverConfiguration config, ICatalogue catalogue,
                    PointingDataCalculator calculator, IPointingDataOptimizer<GroundStationDriverConfiguration> optimizer) {
                return null;
            }

            @Override
            protected List<ResponseHandler<GroundStationDriverConfiguration, String, String>> createResponseHandlers() {
                return handlers;
            }

            @Override
            protected DriverContext<GroundStationDriverConfiguration, String, String> createDriverContext(CamelContext camelContext, IStartableEntity part) {
                return null;
            }
        };

        inOrder = inOrder(driverContext, config, handler1, handler2);
    }

    @Test
    public void testAsRoute() throws Exception {
        assertEquals("timer://A?delay=20", HamlibDriver.asRoute("timer://%s?delay=%s", "A", 20));
        assertEquals("timer://B", HamlibDriver.asRoute("timer://%s", "B"));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCreateOptimizerNettyConfiguration() throws Exception {
        when(config.getDeviceHost()).thenReturn(HOST);
        when(config.getDevicePort()).thenReturn(PORT);
        NettyConfiguration nettyConfig = driver.createNettyConfiguration(config);
        assertEquals(HOST, nettyConfig.getHost());
        assertEquals(PORT, nettyConfig.getPort());
        assertTrue(nettyConfig.isSync());
        assertFalse(nettyConfig.isAllowDefaultCodec());
        assertEquals(4, nettyConfig.getDecoders().size());
        assertEquals(HamlibLineDecoder.class, nettyConfig.getDecoders().get(0).getClass());
        assertEquals(StringDecoder.class, nettyConfig.getDecoders().get(1).getClass());
        assertEquals(HamlibResponseBufferer.class, nettyConfig.getDecoders().get(2).getClass());
        assertEquals(HamlibErrorLogger.class, nettyConfig.getDecoders().get(3).getClass());
        assertEquals(1, nettyConfig.getEncoders().size());
        assertEquals(HamlibCommandEncoder.class, nettyConfig.getEncoders().get(0).getClass());
    }

    @Test
    public void testCreateOptimizerHamlibProtocolEncoders() throws Exception {
        List<ChannelHandler> encoders = driver.createHamlibProtocolEncoders();
        assertNotNull(encoders);
        assertEquals(1, encoders.size());
        assertEquals(HamlibCommandEncoder.class, encoders.get(0).getClass());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCreateOptimizerHamlibProtocolDecoders() throws Exception {
        List<ChannelHandler> decoders = driver.createHamlibProtocolDecoders();
        assertNotNull(decoders);
        assertEquals(4, decoders.size());
        assertEquals(HamlibLineDecoder.class, decoders.get(0).getClass());
        assertEquals(StringDecoder.class, decoders.get(1).getClass());
        assertEquals(HamlibResponseBufferer.class, decoders.get(2).getClass());
        assertEquals(HamlibErrorLogger.class, decoders.get(3).getClass());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetResponseHandlerMap() throws Exception {
        when(handler1.getKey()).thenReturn(KEY1);
        when(handler2.getKey()).thenReturn(KEY2);
        ResponseHandlersMap<GroundStationDriverConfiguration, String, String> map = driver.getResponseHandlerMap(driverContext);
        assertNotNull(map);
        inOrder.verify(handler1, times(1)).getKey();
        inOrder.verify(handler2, times(1)).getKey();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCreateOptimizerNoName() {
        assertNull(driver.createOptimizer(null));
    }

    @Test
    public void testCreateOptimizerUnknownName() {
        assertNull(driver.createOptimizer("org.hbird.pole.olemas.olnud.ja.ei.hakkagi.kunagi.olemas.Olema"));
    }

    @Test
    public void tesCreateOptimizer() {
        IPointingDataOptimizer<GroundStationDriverConfiguration> op = driver.createOptimizer(DefaultPointingDataOptimizer.class.getName());
        assertNotNull(op);
    }
}
