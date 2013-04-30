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
import org.hbird.business.groundstation.base.GroundStationConstants;
import org.hbird.business.groundstation.configuration.RadioDriverConfiguration;
import org.hbird.business.groundstation.hamlib.radio.RadioState;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.interfaces.IEntityInstance;
import org.hbird.exchange.interfaces.IPart;
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
public class GetFrequencyTest {

    private static final String OK_REOPNSE = "get_freq:\nFrequency: 145000009\nRPRT 0";
    private static final String ERROR_REOPNSE = "get_freq:\nFrequency: N/A\nRPRT -1";
    private static final String ID = "GS-ID";

    @Mock
    private DriverContext<RadioDriverConfiguration, String, String> context;

    @Mock
    private TypeConverter converter;

    @Mock
    private RadioDriverConfiguration config;

    @Mock
    private RadioState radioState;

    @Mock
    private IPart part;

    private GetFrequency getFrequency;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        getFrequency = new GetFrequency();
        inOrder = inOrder(context, converter, config, radioState, part);
        when(context.getTypeConverter()).thenReturn(converter);
        when(context.getConfiguration()).thenReturn(config);
        when(context.getDeviceState()).thenReturn(radioState);
        when(context.getPart()).thenReturn(part);
        when(part.getID()).thenReturn(ID);
        when(converter.convertTo(Long.class, "145000009")).thenReturn(145000009L);
        when(config.getDownlinkVfo()).thenReturn("VFOA");
        when(config.getUplinkVfo()).thenReturn("VFOB");
    }

    @Test
    public void testGetKey() throws Exception {
        assertEquals(GetFrequency.KEY, getFrequency.getKey());
    }

    @Test
    public void testHandleErrorResponse() throws Exception {
        List<IEntityInstance> result = getFrequency.handle(context, ERROR_REOPNSE);
        assertNotNull(result);
        assertEquals(0, result.size());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testHandleDownlinkFrequency() throws Exception {
        when(radioState.getCurrentVfo()).thenReturn("VFOA");
        List<IEntityInstance> result = getFrequency.handle(context, OK_REOPNSE);
        assertNotNull(result);
        assertEquals(1, result.size());
        Parameter p = (Parameter) result.get(0);
        assertEquals(ID, p.getIssuedBy());
        assertEquals(145000009L, p.getValue());
        assertEquals("Downlink Frequency", p.getName());
        assertEquals("Downlink Frequency of the radio", p.getDescription());

        inOrder.verify(context, times(1)).getTypeConverter();
        inOrder.verify(context, times(1)).getConfiguration();
        inOrder.verify(context, times(1)).getDeviceState();
        inOrder.verify(config, times(1)).getDownlinkVfo();
        inOrder.verify(config, times(1)).getUplinkVfo();
        inOrder.verify(radioState, times(1)).getCurrentVfo();
        inOrder.verify(part, times(1)).getID();
        inOrder.verify(converter, times(1)).convertTo(Long.class, "145000009");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testHandleUplinkFrequency() throws Exception {
        when(radioState.getCurrentVfo()).thenReturn("VFOB");
        List<IEntityInstance> result = getFrequency.handle(context, OK_REOPNSE);
        assertNotNull(result);
        assertEquals(1, result.size());
        Parameter p = (Parameter) result.get(0);
        assertEquals(ID, p.getIssuedBy());
        assertEquals(145000009L, p.getValue());
        assertEquals("Uplink Frequency", p.getName());
        assertEquals("Uplink Frequency of the radio", p.getDescription());

        inOrder.verify(context, times(1)).getTypeConverter();
        inOrder.verify(context, times(1)).getConfiguration();
        inOrder.verify(context, times(1)).getDeviceState();
        inOrder.verify(config, times(1)).getDownlinkVfo();
        inOrder.verify(config, times(1)).getUplinkVfo();
        inOrder.verify(radioState, times(1)).getCurrentVfo();
        inOrder.verify(part, times(1)).getID();
        inOrder.verify(converter, times(1)).convertTo(Long.class, "145000009");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testHandleUnknownFrequency() throws Exception {
        when(radioState.getCurrentVfo()).thenReturn("VFOC");
        List<IEntityInstance> result = getFrequency.handle(context, OK_REOPNSE);
        assertNotNull(result);
        assertEquals(1, result.size());
        Parameter p = (Parameter) result.get(0);
        assertEquals(ID, p.getIssuedBy());
        assertEquals(145000009L, p.getValue());
        assertEquals("VFOC Frequency", p.getName());
        assertEquals("VFOC Frequency of the radio", p.getDescription());

        inOrder.verify(context, times(1)).getTypeConverter();
        inOrder.verify(context, times(1)).getConfiguration();
        inOrder.verify(context, times(1)).getDeviceState();
        inOrder.verify(config, times(1)).getDownlinkVfo();
        inOrder.verify(config, times(1)).getUplinkVfo();
        inOrder.verify(radioState, times(1)).getCurrentVfo();
        inOrder.verify(part, times(1)).getID();
        inOrder.verify(converter, times(1)).convertTo(Long.class, "145000009");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetLinkName() {
        when(radioState.getCurrentVfo()).thenReturn("VFOA", "VFOB", "VFOC");
        assertEquals(GroundStationConstants.DOWNLINK, GetFrequency.getLinkName(context));
        assertEquals(GroundStationConstants.UPLINK, GetFrequency.getLinkName(context));
        assertEquals("VFOC", GetFrequency.getLinkName(context));

        inOrder.verify(context, times(1)).getConfiguration();
        inOrder.verify(context, times(1)).getDeviceState();
        inOrder.verify(config, times(1)).getDownlinkVfo();
        inOrder.verify(config, times(1)).getUplinkVfo();
        inOrder.verify(radioState, times(1)).getCurrentVfo();

        inOrder.verify(context, times(1)).getConfiguration();
        inOrder.verify(context, times(1)).getDeviceState();
        inOrder.verify(config, times(1)).getDownlinkVfo();
        inOrder.verify(config, times(1)).getUplinkVfo();
        inOrder.verify(radioState, times(1)).getCurrentVfo();

        inOrder.verify(context, times(1)).getConfiguration();
        inOrder.verify(context, times(1)).getDeviceState();
        inOrder.verify(config, times(1)).getDownlinkVfo();
        inOrder.verify(config, times(1)).getUplinkVfo();
        inOrder.verify(radioState, times(1)).getCurrentVfo();

        inOrder.verifyNoMoreInteractions();
    }
}
