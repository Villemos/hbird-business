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
package org.hbird.business.groundstation.hamlib.rotator.protocol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.inOrder;

import java.util.List;

import org.hbird.business.groundstation.base.DriverContext;
import org.hbird.business.groundstation.configuration.RotatorDriverConfiguration;
import org.hbird.exchange.interfaces.IEntityInstance;
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
public class ParkTest {

    public static final String OK_RESPONSE = "park:\nRPRT 0";
    public static final String ERROR_RESPONSE = "park:\nRPRT -1";

    @Mock
    private DriverContext<RotatorDriverConfiguration, String, String> context;

    private Park park;

    private InOrder inOrder;

    @Before
    public void setup() {
        park = new Park();
        inOrder = inOrder(context);
    }

    @Test
    public void testGetKey() {
        assertEquals(Park.KEY, park.getKey());
    }

    @Test
    public void testHandleError() {
        List<IEntityInstance> result = park.handle(context, ERROR_RESPONSE);
        assertNotNull(result);
        assertEquals(0, result.size());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testHandle() {
        List<IEntityInstance> result = park.handle(context, OK_RESPONSE);
        assertNotNull(result);
        assertEquals(0, result.size());
        inOrder.verifyNoMoreInteractions();
    }

    // Just to make sure you think twice before changing the value
    @Test
    public void testValue() throws Exception {
        assertEquals("+K\n", Park.COMMAND);
    }
}
