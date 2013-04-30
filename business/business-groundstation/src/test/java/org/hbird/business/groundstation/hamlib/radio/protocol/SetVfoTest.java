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
package org.hbird.business.groundstation.hamlib.radio.protocol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.List;

import org.hbird.business.groundstation.base.DriverContext;
import org.hbird.business.groundstation.configuration.RadioDriverConfiguration;
import org.hbird.business.groundstation.hamlib.radio.RadioState;
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
public class SetVfoTest {

    public static final String OK_RESPONSE = "set_vfo: VFOA\nRPRT 0";
    public static final String ERROR_RESPONSE = "set_vfo: N/A\nRPRT -1";

    @Mock
    private DriverContext<RadioDriverConfiguration, String, String> context;

    @Mock
    private RadioState state;

    private SetVfo setVfo;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        setVfo = new SetVfo();
        inOrder = inOrder(context, state);
        when(context.getDeviceState()).thenReturn(state);
    }

    @Test
    public void testGetKey() throws Exception {
        assertEquals(SetVfo.KEY, setVfo.getKey());
    }

    @Test
    public void testHandle() throws Exception {
        List<IEntityInstance> result = setVfo.handle(context, OK_RESPONSE);
        assertNotNull(result);
        assertEquals(0, result.size());
        inOrder.verify(context, times(1)).getDeviceState();
        inOrder.verify(state, times(1)).setCurrentVfo("VFOA");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testHandleError() throws Exception {
        List<IEntityInstance> result = setVfo.handle(context, ERROR_RESPONSE);
        assertNotNull(result);
        assertEquals(0, result.size());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCreateCommand() throws Exception {
        assertEquals("+V VFOA\n", SetVfo.createCommand("VFOA"));
        assertEquals("+V VFOB\n", SetVfo.createCommand("VFOB"));
    }
}
