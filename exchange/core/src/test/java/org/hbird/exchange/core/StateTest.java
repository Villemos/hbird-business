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
package org.hbird.exchange.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
public class StateTest {

    private static final String ISSUED_BY = "issuer";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String STATE_OF = "zoe";
    private static final boolean STATE = true;
    private static final long NOW = System.currentTimeMillis();

    @Mock
    private Named isStateOf;

    private State state;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        inOrder = inOrder(isStateOf);
        state = new State(ISSUED_BY, NAME, DESCRIPTION, STATE_OF, STATE);
    }

    /**
     * Test method for {@link org.hbird.exchange.core.State#getQualifiedName(java.lang.String)}.
     */
    @Test
    public void testGetQualifiedNameString() {
        assertEquals(STATE_OF + "/" + NAME, state.getQualifiedName("/"));
    }

    /**
     * Test method for {@link org.hbird.exchange.core.State#prettyPrint()}.
     */
    @Test
    public void testPrettyPrint() {
        assertNotNull(state.prettyPrint());
    }

    /**
     * Test method for
     * {@link org.hbird.exchange.core.State#State(java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)}
     * .
     */
    @Test
    public void testStateStringStringStringStringBoolean() {
        assertEquals(ISSUED_BY, state.getIssuedBy());
        assertEquals(NAME, state.getName());
        assertEquals(DESCRIPTION, state.getDescription());
        assertEquals(STATE_OF, state.getIsStateOf());
        assertEquals(STATE_OF + Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + NAME, state.getQualifiedName());
        long diff = System.currentTimeMillis() - state.getTimestamp();
        assertTrue("diff=" + diff, diff >= 0 && diff < 1000L * 30);
        assertNotNull(state.getID());
        assertEquals(STATE, state.getValue());
    }

    /**
     * Test method for
     * {@link org.hbird.exchange.core.State#State(java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, long)}
     * .
     */
    @Test
    public void testStateStringStringStringStringBooleanLong() {
        state = new State(ISSUED_BY, NAME, DESCRIPTION, STATE_OF, STATE, NOW);
        assertEquals(ISSUED_BY, state.getIssuedBy());
        assertEquals(NAME, state.getName());
        assertEquals(DESCRIPTION, state.getDescription());
        assertEquals(STATE_OF, state.getIsStateOf());
        assertEquals(STATE_OF + Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + NAME, state.getQualifiedName());
        assertEquals(NOW, state.getTimestamp());
        assertNotNull(state.getID());
        assertEquals(STATE, state.getValue());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.State#getIsStateOf()}.
     */
    @Test
    public void testGetIsStateOf() {
        testSetIsStateOfString();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.State#setIsStateOf(java.lang.String)}.
     */
    @Test
    public void testSetIsStateOfString() {
        assertEquals(STATE_OF, state.getIsStateOf());
        state.setIsStateOf((String) null);
        assertNull(state.getIsStateOf());
        state.setIsStateOf(STATE_OF + STATE_OF);
        assertEquals(STATE_OF + STATE_OF, state.getIsStateOf());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.State#getValue()}.
     */
    @Test
    public void testGetValue() {
        testSetValue();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.State#setValid()}.
     */
    @Test
    public void testSetValid() {
        assertTrue(state.getValue());
        state.setValue(false);
        assertFalse(state.getValue());
        state.setValid();
        assertTrue(state.getValue());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.State#setInvalid()}.
     */
    @Test
    public void testSetInvalid() {
        assertTrue(state.getValue());
        state.setInvalid();
        assertFalse(state.getValue());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.State#setValue(java.lang.Boolean)}.
     */
    @Test
    public void testSetValue() {
        assertTrue(state.getValue());
        state.setValue(Boolean.FALSE);
        assertFalse(state.getValue());
        state.setValue(true);
        assertTrue(state.getValue());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.State#setIsStateOf(org.hbird.exchange.core.Named)}.
     */
    @Test
    public void testSetIsStateOfNamed() {
        when(isStateOf.getQualifiedName()).thenReturn(NAME);
        state.setIsStateOf(isStateOf);
        assertEquals(NAME, state.isStateOf);
        inOrder.verify(isStateOf, times(1)).getQualifiedName();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.State#setIsStateOf(org.hbird.exchange.core.Named)}.
     */
    @Test
    public void testSetIsStateOfNamedNull() {
        assertEquals(STATE_OF, state.getIsStateOf());
        state.setIsStateOf((Named) null);
        assertNull(state.getIsStateOf());
    }
}
