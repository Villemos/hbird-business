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
/**
 * 
 */
package org.hbird.exchange.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

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
public class CommandArgumentTest {

    private static final String NAME = "Argument name";
    private static final String DESCRIPTION = "Description of the argument";
    private static final String UNIT = "Unit of the argument";
    private static final Boolean MANDATORY = Boolean.TRUE;

    @Mock
    private CommandArgument copyFrom;

    @Mock
    private Object value;

    private CommandArgument commandArgument;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        commandArgument = new CommandArgument(NAME, DESCRIPTION, Object.class, UNIT, value, MANDATORY);
        inOrder = inOrder(copyFrom, value);
    }

    /**
     * Test method for
     * {@link org.hbird.exchange.core.CommandArgument#CommandArgument(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Object, java.lang.Boolean)}
     * .
     */
    @Test
    public void testCommandArgument() {
        testGetName();
        testGetDescription();
        testGetType();
        testGetUnit();
        testGetValue();
        testGetMandatory();
    }

    /**
     * Test method for
     * {@link org.hbird.exchange.core.CommandArgument#CommandArgument(org.hbird.exchange.core.CommandArgument)}.
     */
    @Test
    public void testCommandArgumentCommandArgument() {
        when(copyFrom.getName()).thenReturn("N");
        when(copyFrom.getDescription()).thenReturn("D");
        doReturn(Float.class).when(copyFrom).getType();
        when(copyFrom.getUnit()).thenReturn("U");
        when(copyFrom.getValue()).thenReturn(value);
        when(copyFrom.getMandatory()).thenReturn(Boolean.FALSE);
        CommandArgument ca = new CommandArgument(copyFrom);
        inOrder.verify(copyFrom, times(1)).getName();
        inOrder.verify(copyFrom, times(1)).getDescription();
        inOrder.verify(copyFrom, times(1)).getType();
        inOrder.verify(copyFrom, times(1)).getUnit();
        inOrder.verify(copyFrom, times(1)).getValue();
        inOrder.verify(copyFrom, times(1)).getMandatory();
        inOrder.verifyNoMoreInteractions();
        assertEquals("N", ca.getName());
        assertEquals("D", ca.getDescription());
        assertEquals(Float.class, ca.getType());
        assertEquals("U", ca.getUnit());
        assertEquals(value, ca.getValue());
        assertEquals(Boolean.FALSE, ca.getMandatory());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.CommandArgument#getName()}.
     */
    @Test
    public void testGetName() {
        assertEquals(NAME, commandArgument.getName());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.CommandArgument#getDescription()}.
     */
    @Test
    public void testGetDescription() {
        assertEquals(DESCRIPTION, commandArgument.getDescription());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.CommandArgument#getType()}.
     */
    @Test
    public void testGetType() {
        assertEquals(Object.class, commandArgument.getType());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.CommandArgument#getUnit()}.
     */
    @Test
    public void testGetUnit() {
        assertEquals(UNIT, commandArgument.getUnit());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.CommandArgument#getValue()}.
     */
    @Test
    public void testGetValue() {
        assertEquals(value, commandArgument.getValue());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.CommandArgument#getMandatory()}.
     */
    @Test
    public void testGetMandatory() {
        assertEquals(MANDATORY, commandArgument.getMandatory());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.CommandArgument#setValue(java.lang.Object)}.
     */
    @Test
    public void testSetValue() {
        assertEquals(value, commandArgument.getValue());
        commandArgument.setValue(null);
        assertNull(commandArgument.getValue());
        commandArgument.setValue(copyFrom);
        assertEquals(copyFrom, commandArgument.getValue());
        inOrder.verifyNoMoreInteractions();
    }

}
