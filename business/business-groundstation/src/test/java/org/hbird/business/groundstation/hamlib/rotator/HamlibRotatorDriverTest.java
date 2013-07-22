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
package org.hbird.business.groundstation.hamlib.rotator;

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
import org.hbird.business.api.IPointingData;
import org.hbird.business.api.deprecated.IDataAccess;
import org.hbird.business.groundstation.base.DriverContext;
import org.hbird.business.groundstation.base.TrackingSupport;
import org.hbird.business.groundstation.configuration.RotatorDriverConfiguration;
import org.hbird.business.groundstation.device.response.ResponseHandler;
import org.hbird.business.groundstation.hamlib.protocol.HamlibResponseKeyExtractor;
import org.hbird.business.groundstation.hamlib.rotator.protocol.GetPosition;
import org.hbird.business.groundstation.hamlib.rotator.protocol.Park;
import org.hbird.business.groundstation.hamlib.rotator.protocol.Reset;
import org.hbird.business.groundstation.hamlib.rotator.protocol.SetPosition;
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
public class HamlibRotatorDriverTest {

    @Mock
    private CamelContext camelContext;

    @Mock
    private TypeConverter converter;

    @Mock
    private HamlibRotatorPart part;

    @Mock
    private RotatorDriverConfiguration driverConfig;

    @Mock
    private IDataAccess dao;

    @Mock
    private IPointingDataOptimizer<RotatorDriverConfiguration> optimizer;

    @Mock
    private IPointingData calculator;

    private HamlibRotatorDriver driver;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        driver = new HamlibRotatorDriver();
        inOrder = inOrder(camelContext, part, driverConfig, dao, optimizer, converter, calculator);
        when(camelContext.getTypeConverter()).thenReturn(converter);
        when(part.getConfiguration()).thenReturn(driverConfig);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateResponseHandlers() throws Exception {
        List<ResponseHandler<RotatorDriverConfiguration, String, String>> handlers = driver.createResponseHandlers();
        assertEquals(4, handlers.size());
        List<?> expectedHandlers = Arrays.asList(GetPosition.class, SetPosition.class, Reset.class, Park.class);
        for (ResponseHandler<RotatorDriverConfiguration, String, String> handler : handlers) {
            assertTrue("Unexpected handler " + handler.getClass().getSimpleName(), expectedHandlers.contains(handler.getClass()));
        }
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCreateDriverContext() throws Exception {
        DriverContext<RotatorDriverConfiguration, String, String> context = driver.createDriverContext(camelContext, part);
        assertNotNull(context);
        assertEquals(converter, context.getTypeConverter());
        assertEquals(part, context.getPart());
        assertEquals(driverConfig, context.getConfiguration());
        assertEquals(RotatorState.class, context.getDeviceState().getClass());
        assertEquals(HamlibResponseKeyExtractor.class, context.getKeyExtractor().getClass());

        inOrder.verify(part, times(1)).getConfiguration();
        inOrder.verify(camelContext, times(1)).getTypeConverter();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCreateTrackingSupport() throws Exception {
        TrackingSupport<RotatorDriverConfiguration> tracking = driver.createTrackingSupport(driverConfig, dao, calculator, optimizer);
        assertNotNull(tracking);
        inOrder.verifyNoMoreInteractions();
    }
}
