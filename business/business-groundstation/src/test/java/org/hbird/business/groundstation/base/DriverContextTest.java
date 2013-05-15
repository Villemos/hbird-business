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
package org.hbird.business.groundstation.base;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.inOrder;

import org.apache.camel.TypeConverter;
import org.hbird.business.groundstation.configuration.GroundStationDriverConfiguration;
import org.hbird.business.groundstation.device.response.ResponseKeyExtractor;
import org.hbird.exchange.interfaces.IStartableEntity;
import org.junit.After;
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
public class DriverContextTest {
    @Mock
    private GroundStationDriverConfiguration configuration;

    @Mock
    private Object deviceState;

    @Mock
    private ResponseKeyExtractor<Object, Object> keyExtractor;

    @Mock
    private IStartableEntity part;

    @Mock
    private TypeConverter typeConverter;

    private DriverContext<GroundStationDriverConfiguration, Object, Object> driverContext;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        driverContext = new DriverContext<GroundStationDriverConfiguration, Object, Object>(part, configuration, keyExtractor, typeConverter, deviceState);
        inOrder = inOrder(configuration, deviceState, keyExtractor, part, typeConverter);
    }

    @After
    public void tearDown() {
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetConfiguration() throws Exception {
        assertEquals(configuration, driverContext.getConfiguration());
    }

    @Test
    public void testGetDeviceState() throws Exception {
        assertEquals(deviceState, driverContext.getDeviceState());
    }

    @Test
    public void testGetPart() throws Exception {
        assertEquals(part, driverContext.getPart());
    }

    @Test
    public void testGetKeyExtractor() throws Exception {
        assertEquals(keyExtractor, driverContext.getKeyExtractor());
    }

    @Test
    public void testGetTypeConverter() throws Exception {
        assertEquals(typeConverter, driverContext.getTypeConverter());
    }
}
