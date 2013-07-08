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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class StateTest {

    private static final String ISSUED_BY = "issuer";
    private static final String NAME = "name";
    private static final String ID = "name:type:timestamp";
    private static final String DESCRIPTION = "description";
    private static final String STATE_OF = "zoe";
    private static final boolean STATE = true;

    private State state;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        state = new State(ID, NAME);
        state.setIssuedBy(ISSUED_BY);
        state.setDescription(DESCRIPTION);
        state.setApplicableTo(STATE_OF);
        state.setValue(STATE);
    }

    /**
     * Test method for {@link org.hbird.exchange.core.State#getName(java.lang.String)}.
     */
    @Test
    public void testgetNameString() {
        assertEquals(NAME, state.getName());
    }

    /**
     * -
     * Test method for {@link org.hbird.exchange.core.State#toString}.
     */
    @Test
    public void testToString() {
        String s = state.toString();
        assertNotNull(s);
        assertTrue(s.contains(ID));
        assertTrue(s.contains(NAME));
        assertTrue(s.contains(ISSUED_BY));
        assertFalse(s.contains(DESCRIPTION));
        assertTrue(s.contains(STATE_OF));
        assertTrue(s.contains(String.valueOf(STATE)));
        assertTrue(s.contains(String.valueOf(state.getVersion())));
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
        assertEquals(STATE_OF, state.getApplicableTo());
        assertEquals(NAME, state.getName());
        long diff = System.currentTimeMillis() - state.getTimestamp();
        assertTrue("diff=" + diff, diff >= 0 && diff < 1000L * 30);
        assertEquals(state.getTimestamp(), state.getVersion());
        assertNotNull(state.getID());
        assertEquals(STATE, state.getValue());
    }

    @Test
    public void testGetApplicableTo() {
        testSetApplicableTo();
    }

    @Test
    public void testSetApplicableTo() {
        assertEquals(STATE_OF, state.getApplicableTo());
        state.setApplicableTo(null);
        assertNull(state.getApplicableTo());
        state.setApplicableTo(STATE_OF + STATE_OF);
        assertEquals(STATE_OF + STATE_OF, state.getApplicableTo());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.State#getValue()}.
     */
    @Test
    public void testGetValue() {
        testSetValue();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.State#setValue()}.
     */
    @Test
    public void testSetValue() {
        assertTrue(state.getValue());
        state.setValue(false);
        assertFalse(state.getValue());
        state.setValue(null);
        assertNull(state.getValue());
        state.setValue(Boolean.TRUE);
        assertTrue(state.getValue());
    }
}
