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
package org.hbird.business.groundstation.hamlib.radio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.TypeConverter;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IPointingData;
import org.hbird.business.api.IPublisher;
import org.hbird.business.groundstation.base.DriverContext;
import org.hbird.business.groundstation.base.TrackingSupport;
import org.hbird.business.groundstation.configuration.RadioDriverConfiguration;
import org.hbird.business.groundstation.device.response.ResponseHandler;
import org.hbird.business.groundstation.hamlib.protocol.HamlibResponseKeyExtractor;
import org.hbird.business.groundstation.hamlib.radio.protocol.GetFrequency;
import org.hbird.business.groundstation.hamlib.radio.protocol.SetFrequency;
import org.hbird.business.groundstation.hamlib.radio.protocol.SetVfo;
import org.hbird.exchange.groundstation.IPointingDataOptimizer;
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
public class HamlibRadioDriverTest {

    @Mock
    private CamelContext camelContext;

    @Mock
    private TypeConverter converter;

    @Mock
    private HamlibRadioPart part;

    @Mock
    private RadioDriverConfiguration driverConfig;

    @Mock
    private IDataAccess dao;

    @Mock
    private IPointingData calculator;
    
    @Mock
    private IPublisher publisher;

    @Mock
    private IPointingDataOptimizer<RadioDriverConfiguration> optimizer;

    private HamlibRadioDriver driver;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        driver = new HamlibRadioDriver(publisher, dao, calculator);
        inOrder = inOrder(camelContext, part, driverConfig, dao, optimizer, converter, calculator);
        when(camelContext.getTypeConverter()).thenReturn(converter);
        when(part.getConfiguration()).thenReturn(driverConfig);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateResponseHandlers() throws Exception {
        List<ResponseHandler<RadioDriverConfiguration, String, String>> handlers = driver.createResponseHandlers();
        assertEquals(3, handlers.size());
        List<?> expectedHandlers = Arrays.asList(GetFrequency.class, SetFrequency.class, SetVfo.class);
        for (ResponseHandler<RadioDriverConfiguration, String, String> handler : handlers) {
            assertTrue("Unexpected handler " + handler.getClass().getSimpleName(), expectedHandlers.contains(handler.getClass()));
        }
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCreateDriverContext() throws Exception {
        DriverContext<RadioDriverConfiguration, String, String> context = driver.createDriverContext(camelContext, part);
        assertNotNull(context);
        assertEquals(converter, context.getTypeConverter());
        assertEquals(part, context.getPart());
        assertEquals(driverConfig, context.getConfiguration());
        assertEquals(RadioState.class, context.getDeviceState().getClass());
        assertEquals(HamlibResponseKeyExtractor.class, context.getKeyExtractor().getClass());

        inOrder.verify(part, times(1)).getConfiguration();
        inOrder.verify(camelContext, times(1)).getTypeConverter();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCreateTrackingSupport() throws Exception {
        TrackingSupport<RadioDriverConfiguration> tracking = driver.createTrackingSupport(driverConfig, dao, calculator, optimizer);
        assertNotNull(tracking);
        inOrder.verifyNoMoreInteractions();
    }
}
