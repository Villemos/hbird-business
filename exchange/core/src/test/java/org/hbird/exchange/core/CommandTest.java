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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class CommandTest {

    private static final long NOW = System.currentTimeMillis();
    private static final long DELAY = 1000 * 60 * 60;
    private static final String ISSUER = "issuer";
    private static final String DESTINATION = "Command destination";
    private static final String DESCRIPTION = "Command description";
    private static final String NAME = "Command name";
    private static final String ID = "ID";
    private static final Long RELEASE_TIME = NOW + DELAY;
    private static final Long EXEC_TIME = NOW + DELAY * 2;
    private static final String ARG1 = "A";
    private static final String ARG2 = "B";

    private Command command;

    @Mock
    private CommandArgument arg1;

    @Mock
    private CommandArgument arg2;

    @Mock
    private List<CommandArgument> in;

    private InOrder inOrder;

    Map<String, CommandArgument> map = new HashMap<String, CommandArgument>();

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        command = new Command(ID, NAME);
        command.setIssuedBy(ISSUER);
        command.setDestination(DESTINATION);
        command.setDescription(DESCRIPTION);
        command.setTransferTime(RELEASE_TIME);
        command.setExecutionTime(EXEC_TIME);

        inOrder = inOrder(arg1, arg2, in);
        when(arg1.getName()).thenReturn(ARG1);
        when(arg2.getName()).thenReturn(ARG2);
        map.put(ARG1, arg1);
        map.put(ARG2, arg2);
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Command#toString()}.
     */
    @Test
    public void testToString() {
        String s = command.toString();
        assertNotNull(s);
        assertTrue(s.contains(ID));
        assertTrue(s.contains(NAME));
        assertTrue(s.contains(ISSUER));
        assertTrue(s.contains(DESTINATION));
        assertFalse(s.contains(DESCRIPTION));
        assertTrue(s.contains(String.valueOf(command.getTimestamp())));
    }

    /**
     * Test method for
     * {@link org.hbird.exchange.core.Command#Command(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testCommandStringStringStringString() {
        Command c = new Command(ID, NAME);
        assertEquals(NAME, c.getName());
        assertEquals(ID, c.getID());
    }

    /**
     * Test method for
     * {@link org.hbird.exchange.core.Command#Command(java.lang.String, java.lang.String, java.lang.String, java.lang.String, long, long)}
     * .
     */
    @Test
    public void testCommandStringStringStringStringLongLong() {
        assertEquals(ISSUER, command.getIssuedBy());
        assertEquals(DESTINATION, command.getDestination());
        assertEquals(NAME, command.getName());
        assertEquals(DESCRIPTION, command.getDescription());
        assertEquals(RELEASE_TIME.longValue(), command.getTransferTime());
        assertEquals(EXEC_TIME.longValue(), command.getExecutionTime());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Command#createArgumentMap()}.
     */
    @SuppressWarnings("serial")
    @Test
    public void testCreateArgumentMap() {
        Command cmd = new Command(ID, NAME) {
            @Override
            protected List<CommandArgument> getArgumentDefinitions(List<CommandArgument> args) {
                args.add(arg1);
                args.add(arg2);
                return args;
            }
        };
        inOrder.verify(arg1, times(1)).getName();
        inOrder.verify(arg2, times(1)).getName();
        inOrder.verifyNoMoreInteractions();

        Map<String, CommandArgument> args = cmd.getArguments();
        assertNotNull(args);
        assertEquals(2, args.size());
        assertTrue(args.containsKey(ARG1));
        assertTrue(args.containsKey(ARG1));
        assertEquals(arg1, args.get(ARG1));
        assertEquals(arg2, args.get(ARG2));
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Command#getArgumentDefinitions()}.
     */
    @Test
    public void testGetArgumentDefinitions() {
        List<CommandArgument> args = command.getArgumentDefinitions(in);
        inOrder.verifyNoMoreInteractions();
        assertNotNull(args);
        assertSame(in, args);
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Command#getArguments()}.
     */
    @Test
    public void testGetArguments() {
        testAddArguments();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Command#addArguments(java.util.Map)}.
     */
    @Test
    public void testAddArguments() {
        Map<String, CommandArgument> args = command.getArguments();
        assertNotNull(args);
        assertEquals(0, args.size());

    }

    /**
     * Test method for {@link org.hbird.exchange.core.Command#getTransferTime()}.
     */
    @Test
    public void testGetTransferTime() {
        assertEquals(RELEASE_TIME.longValue(), command.getTransferTime());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Command#getExecutionTime()}.
     */
    @Test
    public void testGetExecutionTime() {
        assertEquals(EXEC_TIME.longValue(), command.getExecutionTime());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Command#setTransferTime(long)}.
     */
    @Test
    public void testSetTransferTime() {
        command.setTransferTime(NOW);
        assertEquals(NOW, command.getTransferTime());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Command#setExecutionTime(long)}.
     */
    @Test
    public void testSetExecutionTime() {
        command.setExecutionTime(NOW);
        assertEquals(NOW, command.getExecutionTime());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Command#getDestination()}.
     */
    @Test
    public void testGetDestination() {
        assertEquals(DESTINATION, command.getDestination());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Command#setDestination(java.lang.String)}.
     */
    @Test
    public void testSetDestination() {
        assertEquals(DESTINATION, command.getDestination());
        command.setDestination(null);
        assertNull(command.getDestination());
        command.setDestination(DESTINATION);
        assertEquals(DESTINATION, command.getDestination());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Command#setArgumentValue(java.lang.String, java.lang.Object)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetArgumentValueWithException() {
        command.setArgumentValue("NONE", NOW);
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Command#setArgumentValue(java.lang.String, java.lang.Object)}.
     */
    @Test
    public void testSetArgumentValueWithWrongType() {
        command.addArguments(map);

        doReturn(String.class).when(arg1).getType();
        try {
            command.setArgumentValue(ARG1, new Object());
            fail("exception expected");
        }
        catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
        inOrder.verify(arg1, times(1)).getType();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Command#setArgumentValue(java.lang.String, java.lang.Object)}.
     */
    @Test
    public void testSetArgumentValue() {
        doReturn(Object.class).when(arg1).getType();
        doReturn(Number.class).when(arg2).getType();
        command.addArguments(map);
        command.setArgumentValue(ARG1, NAME);
        command.setArgumentValue(ARG2, NOW);
        inOrder.verify(arg1, times(1)).setValue(NAME);
        inOrder.verify(arg2, times(1)).setValue(NOW);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Command#hasArgument(java.lang.String)}.
     */
    @Test
    public void testHasArgument() {
        assertFalse(command.hasArgument(ARG1));
        assertFalse(command.hasArgument(ARG2));
        assertFalse(command.hasArgument("NONE"));
        command.addArguments(map);
        assertTrue(command.hasArgument(ARG1));
        assertTrue(command.hasArgument(ARG2));
        assertFalse(command.hasArgument("NONE"));
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Command#hasArgumentValue(java.lang.String)}.
     */
    @Test
    public void testHasArgumentValue() {
        assertFalse(command.hasArgumentValue(ARG1));
        assertFalse(command.hasArgumentValue(ARG2));
        assertFalse(command.hasArgumentValue("NONE"));
        command.addArguments(map);
        assertFalse(command.hasArgumentValue(ARG1));
        assertFalse(command.hasArgumentValue(ARG2));
        assertFalse(command.hasArgumentValue("NONE"));
        when(arg1.getValue()).thenReturn(NAME);
        assertTrue(command.hasArgumentValue(ARG1));
        assertFalse(command.hasArgumentValue(ARG2));
        assertFalse(command.hasArgumentValue("NONE"));
        when(arg2.getValue()).thenReturn(NOW);
        assertTrue(command.hasArgumentValue(ARG1));
        assertTrue(command.hasArgumentValue(ARG2));
        assertFalse(command.hasArgumentValue("NONE"));
        inOrder.verify(arg1, times(1)).getValue();
        inOrder.verify(arg2, times(1)).getValue();
        inOrder.verify(arg1, times(1)).getValue();
        inOrder.verify(arg2, times(1)).getValue();
        inOrder.verify(arg1, times(1)).getValue();
        inOrder.verify(arg2, times(1)).getValue();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Command#getArgumentValue(java.lang.String, java.lang.Class)}.
     */
    @Test
    public void testGetArgumentValueStringClassOfTWithException() {
        assertNull(command.getArgumentValue(ARG1, Double.class));
        command.addArguments(map);
        when(arg1.getValue()).thenReturn(NAME);
        doReturn(Double.class).when(arg1).getType();
        try {
            @SuppressWarnings("unused")
            Double value = command.getArgumentValue(ARG1, Double.class);
            fail("Exception expected");
        }
        catch (ClassCastException cce) {
            assertNotNull(cce.getMessage());
        }
        inOrder.verify(arg1, times(1)).getType();
        inOrder.verify(arg1, times(1)).getValue();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Command#getArgumentValue(java.lang.String, java.lang.Class)}.
     */
    @Test
    public void testGetArgumentValueStringClassOfT() {
        assertNull(command.getArgumentValue(ARG1, String.class));
        assertNull(command.getArgumentValue(ARG2, Long.class));
        command.addArguments(map);
        when(arg1.getValue()).thenReturn(NAME);
        doReturn(String.class).when(arg1).getType();
        doReturn(Long.class).when(arg2).getType();
        when(arg2.getValue()).thenReturn(NOW);
        assertEquals(NAME, command.getArgumentValue(ARG1, String.class));
        assertEquals(new Long(NOW), command.getArgumentValue(ARG2, Long.class));
        inOrder.verify(arg1, times(1)).getType();
        inOrder.verify(arg1, times(1)).getValue();
        inOrder.verify(arg2, times(1)).getType();
        inOrder.verify(arg2, times(1)).getValue();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Command#getArgumentValue(java.lang.String, java.lang.Class)}.
     */
    @Test
    public void testGetArgumentValueStringClassOfTWithWrongType() {
        command.addArguments(map);
        doReturn(Number.class).when(arg1).getType();
        when(arg1.getValue()).thenReturn(123L);
        assertEquals(123L, command.getArgumentValue(ARG1, Number.class));
        assertEquals(123L, command.getArgumentValue(ARG1, Object.class));
        try {
            command.getArgumentValue(ARG1, Long.class);
            fail("Exception expected");
        }
        catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
        inOrder.verify(arg1, times(1)).getType();
        inOrder.verify(arg1, times(1)).getValue();
        inOrder.verify(arg1, times(1)).getType();
        inOrder.verify(arg1, times(1)).getValue();
        inOrder.verify(arg1, times(1)).getType();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Command#getArgumentValue(java.lang.String)}.
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testGetArgumentValueString() {
        assertNull(command.getArgumentValue(ARG1));
        assertNull(command.getArgumentValue(ARG2));
        command.addArguments(map);
        when(arg1.getValue()).thenReturn(NAME);
        assertEquals(NAME, command.getArgumentValue(ARG1));
        assertNull(command.getArgumentValue(ARG2));
        when(arg2.getValue()).thenReturn(NOW);
        assertEquals(NAME, command.getArgumentValue(ARG1));
        assertEquals(NOW, command.getArgumentValue(ARG2));
        inOrder.verify(arg1, times(1)).getValue();
        inOrder.verify(arg2, times(1)).getValue();
        inOrder.verify(arg1, times(1)).getValue();
        inOrder.verify(arg2, times(1)).getValue();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Command#checkArguments()}.
     */
    @Test
    public void testCheckArguments() {
        List<String> list = command.checkArguments();
        assertNotNull(list);
        assertEquals(0, list.size());
        when(arg1.getMandatory()).thenReturn(Boolean.TRUE);
        when(arg1.getName()).thenReturn(ARG1);
        command.addArguments(map);
        list = command.checkArguments();
        assertNotNull(list);
        assertEquals(1, list.size());
        assertTrue(list.contains(ARG1));
        inOrder.verify(arg1, times(1)).getMandatory();
        inOrder.verify(arg1, times(1)).getValue();
        inOrder.verify(arg2, times(1)).getMandatory();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Command#getTransferTime()}.
     */
    @Test
    public void testGetTrancferTime() {
        assertEquals(RELEASE_TIME.longValue(), command.getTransferTime());
    }
}
