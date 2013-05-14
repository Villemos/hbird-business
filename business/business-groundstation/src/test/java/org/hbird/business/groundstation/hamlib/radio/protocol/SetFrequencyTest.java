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

import org.apache.camel.TypeConverter;
import org.hbird.business.groundstation.base.DriverContext;
import org.hbird.business.groundstation.configuration.RadioDriverConfiguration;
import org.hbird.business.groundstation.hamlib.radio.RadioState;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.interfaces.IEntityInstance;
import org.hbird.exchange.interfaces.IStartableEntity;
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
public class SetFrequencyTest {

    private static final String OK_RESPONSE = "set_freq: 145000009\nRPRT 0\n";
    private static final String ERROR_RESPONSE = "set_freq: a\nRPRT -1\n";
    private static final String ID = "GS_ID";
    private static final long FREQUENCY = 1000000001L;
    private static final double DOPPLER = 1.32D;

    @Mock
    private DriverContext<RadioDriverConfiguration, String, String> context;

    @Mock
    private TypeConverter converter;

    @Mock
    private IStartableEntity part;

    @Mock
    private RadioDriverConfiguration config;

    @Mock
    private RadioState state;

    private SetFrequency setFrequency;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        setFrequency = new SetFrequency();
        inOrder = inOrder(context, converter, part, config, state);
        when(context.getTypeConverter()).thenReturn(converter);
        when(context.getPart()).thenReturn(part);
        when(part.getID()).thenReturn(ID);
        when(converter.convertTo(Long.class, "145000009")).thenReturn(145000009L);
        when(context.getConfiguration()).thenReturn(config);
        when(context.getDeviceState()).thenReturn(state);
        when(config.getDownlinkVfo()).thenReturn("VFOA");
        when(config.getUplinkVfo()).thenReturn("VFOB");
    }

    @Test
    public void testGetKey() throws Exception {
        assertEquals(SetFrequency.KEY, setFrequency.getKey());
    }

    @Test
    public void testHandleDownlink() throws Exception {
        when(state.getCurrentVfo()).thenReturn("VFOA");
        List<IEntityInstance> result = setFrequency.handle(context, OK_RESPONSE);
        assertNotNull(result);
        assertEquals(1, result.size());
        Parameter p = (Parameter) result.get(0);
        assertEquals(ID, p.getIssuedBy());
        assertEquals(145000009L, p.getValue());
        assertEquals("Downlink Target Frequency", p.getName());
        assertEquals("Downlink Target Frequency for the radio", p.getDescription());
        assertEquals("Hz", p.getUnit());
        inOrder.verify(context, times(1)).getTypeConverter();
        inOrder.verify(context, times(1)).getConfiguration();
        inOrder.verify(context, times(1)).getDeviceState();
        inOrder.verify(config, times(1)).getDownlinkVfo();
        inOrder.verify(config, times(1)).getUplinkVfo();
        inOrder.verify(state, times(1)).getCurrentVfo();
        inOrder.verify(context, times(1)).getPart();
        inOrder.verify(part, times(1)).getID();
        inOrder.verify(converter, times(1)).convertTo(Long.class, "145000009");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testHandleUplink() throws Exception {
        when(state.getCurrentVfo()).thenReturn("VFOB");
        List<IEntityInstance> result = setFrequency.handle(context, OK_RESPONSE);
        assertNotNull(result);
        assertEquals(1, result.size());
        Parameter p = (Parameter) result.get(0);
        assertEquals(ID, p.getIssuedBy());
        assertEquals(145000009L, p.getValue());
        assertEquals("Uplink Target Frequency", p.getName());
        assertEquals("Uplink Target Frequency for the radio", p.getDescription());
        assertEquals("Hz", p.getUnit());
        inOrder.verify(context, times(1)).getTypeConverter();
        inOrder.verify(context, times(1)).getConfiguration();
        inOrder.verify(context, times(1)).getDeviceState();
        inOrder.verify(config, times(1)).getDownlinkVfo();
        inOrder.verify(config, times(1)).getUplinkVfo();
        inOrder.verify(state, times(1)).getCurrentVfo();
        inOrder.verify(context, times(1)).getPart();
        inOrder.verify(part, times(1)).getID();
        inOrder.verify(converter, times(1)).convertTo(Long.class, "145000009");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testHandleUnknown() throws Exception {
        when(state.getCurrentVfo()).thenReturn("VFOC");
        List<IEntityInstance> result = setFrequency.handle(context, OK_RESPONSE);
        assertNotNull(result);
        assertEquals(1, result.size());
        Parameter p = (Parameter) result.get(0);
        assertEquals(ID, p.getIssuedBy());
        assertEquals(145000009L, p.getValue());
        assertEquals("VFOC Target Frequency", p.getName());
        assertEquals("VFOC Target Frequency for the radio", p.getDescription());
        assertEquals("Hz", p.getUnit());
        inOrder.verify(context, times(1)).getTypeConverter();
        inOrder.verify(context, times(1)).getConfiguration();
        inOrder.verify(context, times(1)).getDeviceState();
        inOrder.verify(config, times(1)).getDownlinkVfo();
        inOrder.verify(config, times(1)).getUplinkVfo();
        inOrder.verify(state, times(1)).getCurrentVfo();
        inOrder.verify(context, times(1)).getPart();
        inOrder.verify(part, times(1)).getID();
        inOrder.verify(converter, times(1)).convertTo(Long.class, "145000009");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testHandleErrorResponse() throws Exception {
        List<IEntityInstance> result = setFrequency.handle(context, ERROR_RESPONSE);
        assertNotNull(result);
        assertEquals(0, result.size());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCreateCommandFrequency() {
        assertEquals("+F 1000000001\n", SetFrequency.createCommand(FREQUENCY));
    }

    @Test
    public void testCreateCommandFrequencyAndDoppler() {
        assertEquals("+F 999999996\n", SetFrequency.createCommand(FREQUENCY, DOPPLER));
    }
}
