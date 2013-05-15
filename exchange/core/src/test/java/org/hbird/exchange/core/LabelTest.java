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
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class LabelTest {

    private static final String ISSUED_BY = "ISSUER";
    private static final String ID = "ID";
    private static final String NAME = "the name";
    private static final String DESCRIPTION = "description";
    private static final String VALUE = "THE VALUE";

    @Mock
    private Label template;

    private Label label;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        label = new Label(ID, NAME);
        label.setIssuedBy(ISSUED_BY);
        label.setDescription(DESCRIPTION);
        label.setValue(VALUE);
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Label#toString()}.
     */
    @Test
    public void testToString() {
        String s = label.toString();
        assertNotNull(s);
        assertTrue(s.contains(ID));
        assertTrue(s.contains(NAME));
        assertTrue(s.contains(ISSUED_BY));
        assertFalse(s.contains(DESCRIPTION));
        assertTrue(s.contains(VALUE));
        assertTrue(s.contains(String.valueOf(label.getTimestamp())));
    }

    /**
     * Test method for
     * {@link org.hbird.exchange.core.Label#Label(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testLabelStringStringStringString() {
        assertEquals(ISSUED_BY, label.getIssuedBy());
        assertEquals(NAME, label.getName());
        assertEquals(DESCRIPTION, label.getDescription());
        assertEquals(VALUE, label.getValue());
        assertEquals(NAME, label.getName());
        long diff = System.currentTimeMillis() - label.getTimestamp();
        assertTrue("diff=" + diff, diff >= 0 && diff < 1000L * 30);
        assertNotNull(label.getID());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Label#getValue()}.
     */
    @Test
    public void testGetValue() {
        testSetValue();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Label#setValue(java.lang.String)}.
     */
    @Test
    public void testSetValue() {
        assertEquals(VALUE, label.getValue());
        label.setValue(null);
        assertNull(label.getValue());
        label.setValue(VALUE + VALUE);
        assertEquals(VALUE + VALUE, label.getValue());
    }
}
