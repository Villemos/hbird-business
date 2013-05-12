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
import org.hbird.exchange.interfaces.IEntityInstance;
import org.hbird.exchange.interfaces.IStartablePart;
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
public class SetPositionTest {

    private static final String OK_RESPONSE = "set_pos: 123.982 15.2302\nRPRT 0\n";
    private static final String ERROR_RESPONSE = "set_pos: -123.982 -1523.02\nRPRT -1\n";
    private static final String ID = "GS-ID";

    @Mock
    private DriverContext<RotatorDriverConfiguration, String, String> context;

    @Mock
    private TypeConverter converter;

    @Mock
    private IStartablePart part;

    private SetPosition setPosition;

    private InOrder inOrder;

    @Before
    public void setup() {
        setPosition = new SetPosition();
        inOrder = inOrder(context, converter, part);
        when(context.getTypeConverter()).thenReturn(converter);
        when(converter.convertTo(Double.class, "123.982")).thenReturn(123.982D);
        when(converter.convertTo(Double.class, "15.2302")).thenReturn(15.2302D);
        when(context.getPart()).thenReturn(part);
        when(part.getID()).thenReturn(ID);
    }

    @Test
    public void testGetKey() {
        assertEquals(SetPosition.KEY, setPosition.getKey());
    }

    @Test
    public void testHandleError() {
        List<IEntityInstance> result = setPosition.handle(context, ERROR_RESPONSE);
        assertNotNull(result);
        assertEquals(0, result.size());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testHandle() {
        List<IEntityInstance> result = setPosition.handle(context, OK_RESPONSE);
        assertNotNull(result);
        assertEquals(2, result.size());

        Parameter p = (Parameter) result.get(0);
        assertEquals(ID, p.getIssuedBy());
        assertEquals("Target Azimuth", p.getName());
        assertEquals("Target Azimuth of the antenna rotator", p.getDescription());
        assertEquals("Degree", p.getUnit());
        assertEquals(123.982D, p.getValue());

        p = (Parameter) result.get(1);
        assertEquals(ID, p.getIssuedBy());
        assertEquals("Target Elevation", p.getName());
        assertEquals("Target Elevation of the antenna rotator", p.getDescription());
        assertEquals("Degree", p.getUnit());
        assertEquals(15.2302D, p.getValue());

        inOrder.verify(context, times(1)).getTypeConverter();
        inOrder.verify(context, times(1)).getPart();
        inOrder.verify(part, times(1)).getID();
        inOrder.verify(converter, times(1)).convertTo(Double.class, "123.982");
        inOrder.verify(converter, times(1)).convertTo(Double.class, "15.2302");

        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.business.groundstation.hamlib.rotator.protocol.SetPosition#createCommand(java.lang.Double, java.lang.Double)}
     * .
     */
    @Test
    public void testCreateCommand() {
        assertEquals("+P 0 0\n", SetPosition.createCommand(0.0D, 0.0D));
        assertEquals("+P 0.000001 0.000001\n", SetPosition.createCommand(0.000001D, 0.000001D));
        assertEquals("+P 0.000001 0.000001\n", SetPosition.createCommand(0.0000011D, 0.0000011D));
        assertEquals("+P 0.000002 0.000002\n", SetPosition.createCommand(0.0000019D, 0.0000019D));
        assertEquals("+P 100000.000002 100000.000002\n", SetPosition.createCommand(100000.0000019D, 100000.0000019D));
        assertEquals("+P 1.1 1.1\n", SetPosition.createCommand(1.1D, 1.1D));
        assertEquals("+P 1.01 1.01\n", SetPosition.createCommand(1.01D, 1.01D));
        assertEquals("+P 1.001 1.001\n", SetPosition.createCommand(1.001D, 1.001D));
        assertEquals("+P 1.0001 1.0001\n", SetPosition.createCommand(1.0001D, 1.0001D));
        assertEquals("+P 1.00001 1.00001\n", SetPosition.createCommand(1.00001D, 1.00001D));
        assertEquals("+P 1.000001 1.000001\n", SetPosition.createCommand(1.000001D, 1.000001D));
        assertEquals("+P 1 1\n", SetPosition.createCommand(1.0000001D, 1.0000001D));
        assertEquals("+P 10 10\n", SetPosition.createCommand(10D, 10D));
        assertEquals("+P 100 100\n", SetPosition.createCommand(100D, 100D));
        assertEquals("+P 1000 1000\n", SetPosition.createCommand(1000D, 1000D));
        assertEquals("+P 10000 10000\n", SetPosition.createCommand(10000D, 10000D));
        assertEquals("+P 100000 100000\n", SetPosition.createCommand(100000D, 100000D));
        assertEquals("+P 1000000 1000000\n", SetPosition.createCommand(1000000D, 1000000D));
        assertEquals("+P 10000000 10000000\n", SetPosition.createCommand(10000000D, 10000000D));

        assertEquals("+P 1 2\n", SetPosition.createCommand(1D, 2D));
        assertEquals("+P 1 2\n", SetPosition.createCommand(1.0D, 2.0D));
        assertEquals("+P 1 2\n", SetPosition.createCommand(01.00D, 02.00D));

        assertEquals("+P -0 -0\n", SetPosition.createCommand(-0.0D, -0.0D));
        assertEquals("+P -0.1 -0.1\n", SetPosition.createCommand(-0.1D, -0.1D));
        assertEquals("+P -0.000001 -0.000001\n", SetPosition.createCommand(-0.000001D, -0.000001D));
        assertEquals("+P -0.000001 -0.000001\n", SetPosition.createCommand(-0.0000011D, -0.0000011D));
        assertEquals("+P -0.000002 -0.000002\n", SetPosition.createCommand(-0.0000019D, -0.0000019D));
        assertEquals("+P -100000.000002 -100000.000002\n", SetPosition.createCommand(-100000.0000019D, -100000.0000019D));

    }
}
