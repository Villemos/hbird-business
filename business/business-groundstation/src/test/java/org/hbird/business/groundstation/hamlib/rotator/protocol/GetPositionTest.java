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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.camel.TypeConverter;
import org.hbird.business.groundstation.base.DriverContext;
import org.hbird.business.groundstation.configuration.RotatorDriverConfiguration;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.interfaces.INamed;
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
public class GetPositionTest {

    private static final String OK_RESPONSE = "get_pos:\nAzimuth: -30.4050\nElevation: 123.456\nRPRT 0";
    private static final String ERROR_RESPONSE = "get_pos:\nRPRT -1";
    private static final String ID = "GS_ID";

    @Mock
    private DriverContext<RotatorDriverConfiguration, String, String> context;

    @Mock
    private TypeConverter typeConverter;

    @Mock
    private IPart part;

    private GetPosition getPosition;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        getPosition = new GetPosition();
        inOrder = inOrder(context, typeConverter, part);
        when(context.getTypeConverter()).thenReturn(typeConverter);
        when(context.getPart()).thenReturn(part);
        when(part.getID()).thenReturn(ID);
        when(typeConverter.convertTo(Double.class, "-30.4050")).thenReturn(-30.4050D);
        when(typeConverter.convertTo(Double.class, "123.456")).thenReturn(123.456D);
    }

    @Test
    public void testGetKey() throws Exception {
        assertEquals(GetPosition.KEY, getPosition.getKey());
    }

    @Test
    public void testHandleError() throws Exception {
        List<INamed> result = getPosition.handle(context, ERROR_RESPONSE);
        assertNotNull(result);
        assertEquals(0, result.size());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testHandle() throws Exception {
        List<INamed> result = getPosition.handle(context, OK_RESPONSE);
        assertNotNull(result);
        assertEquals(2, result.size());

        Parameter p = (Parameter) result.get(0);
        assertEquals(ID, p.getIssuedBy());
        assertEquals("Azimuth", p.getName());
        assertEquals("Azimuth of the antenna rotator", p.getDescription());
        assertEquals(-30.4050D, p.getValue());

        p = (Parameter) result.get(1);
        assertEquals(ID, p.getIssuedBy());
        assertEquals("Elevation", p.getName());
        assertEquals("Elevation of the antenna rotator", p.getDescription());
        assertEquals(123.456D, p.getValue());

        inOrder.verify(context, times(1)).getPart();
        inOrder.verify(part, times(1)).getID();
        inOrder.verify(context, times(1)).getTypeConverter();
        inOrder.verify(typeConverter, times(1)).convertTo(Double.class, "-30.4050");
        inOrder.verify(typeConverter, times(1)).convertTo(Double.class, "123.456");

        inOrder.verifyNoMoreInteractions();
    }

}
