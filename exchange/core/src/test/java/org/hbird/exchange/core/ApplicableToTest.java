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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class ApplicableToTest {

    private static final String ID = "An Identifier";
    private static final String NAME = "A Name";
    private static final String APPLICABLE_TO = "Another Identifier";

    private ApplicableTo applicable;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        applicable = new ApplicableTo(ID, NAME);
    }

    @Test
    public void testApplicableTo() {
        assertNotNull(applicable);
        assertEquals(ID, applicable.getID());
        assertEquals(NAME, applicable.getName());
        assertTrue(applicable.getTimestamp() <= System.currentTimeMillis());
        assertNull(applicable.getApplicableTo());
        assertEquals(ID + ":" + applicable.getTimestamp(), applicable.getInstanceID());
    }

    @Test
    public void testPrettyPrint() throws Exception {
        applicable.setApplicableTo(APPLICABLE_TO);
        String s = applicable.toString();
        assertNotNull(s);
        assertTrue(s.contains(ApplicableTo.class.getSimpleName()));
        assertTrue(s.contains(ID));
        assertTrue(s.contains(NAME));
        assertTrue(s.contains(APPLICABLE_TO));
    }

    @Test
    public void testGetApplicableTo() throws Exception {
        testSetApplicableTo();
    }

    @Test
    public void testSetApplicableTo() throws Exception {
        assertNull(applicable.getApplicableTo());
        applicable.setApplicableTo(APPLICABLE_TO);
        assertEquals(APPLICABLE_TO, applicable.getApplicableTo());
        applicable.setApplicableTo(ID);
        assertEquals(ID, applicable.getApplicableTo());
    }
}
