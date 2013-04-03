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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 *
 */
public class EventTest {

    private static final String ISSUER1 = "issuer";
    private static final String ISSUER2 = "issuer";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final Long NOW = System.currentTimeMillis();
    private static final Long NOW_PLUS_ONE = NOW + 1;

    /**
     * Test method for
     * {@link org.hbird.exchange.core.Event#Event(java.lang.String, java.lang.String, java.lang.String, java.lang.String, long)}
     * .
     */
    @Test
    public void testEventStringStringStringStringLong() {
        Event event = new Event(ISSUER1, NAME, DESCRIPTION, NOW);
        assertEquals(ISSUER1, event.getIssuedBy());
        assertEquals(NAME, event.getName());
        assertEquals(DESCRIPTION, event.getDescription());
        assertEquals(NOW.longValue(), event.getTimestamp());
        assertNotNull(event.getID());
        assertEquals(ISSUER1 + Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + NAME, event.getQualifiedName());
    }

    /**
     * Test method for
     * {@link org.hbird.exchange.core.Event#Event(java.lang.String, org.hbird.exchange.core.Event, long)}.
     */
    @Test
    public void testEventStringEventLong() {
        Event template = new Event(ISSUER1, NAME, DESCRIPTION, NOW);
        assertEquals(ISSUER1, template.getIssuedBy());
        assertEquals(NAME, template.getName());
        assertEquals(DESCRIPTION, template.getDescription());
        assertEquals(NOW.longValue(), template.getTimestamp());
        assertNotNull(template.getID());
        assertEquals(ISSUER1 + Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + NAME, template.getQualifiedName());

        Event event = new Event(ISSUER2, template, NOW_PLUS_ONE);
        assertEquals(ISSUER2, event.getIssuedBy());
        assertEquals(NAME, event.getName());
        assertEquals(DESCRIPTION, event.getDescription());
        assertEquals(NOW_PLUS_ONE.longValue(), event.getTimestamp());
        assertNotNull(event.getID());
        assertNotSame(template.getID(), event.getID());
        assertEquals(ISSUER2 + Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + NAME, event.getQualifiedName());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Event#Event(java.lang.String, org.hbird.exchange.core.Event)}.
     */
    @Test
    public void testEventStringEvent() {
        Event template = new Event(ISSUER1, NAME, DESCRIPTION, NOW);
        assertEquals(ISSUER1, template.getIssuedBy());
        assertEquals(NAME, template.getName());
        assertEquals(DESCRIPTION, template.getDescription());
        assertEquals(NOW.longValue(), template.getTimestamp());
        assertNotNull(template.getID());
        assertEquals(ISSUER1 + Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + NAME, template.getQualifiedName());

        Event event = new Event(ISSUER2, template);
        assertEquals(ISSUER2, event.getIssuedBy());
        assertEquals(NAME, event.getName());
        assertEquals(DESCRIPTION, event.getDescription());
        assertTrue(event.getTimestamp() >= NOW);
        assertNotNull(event.getID());
        assertNotSame(template.getID(), event.getID());
        assertEquals(ISSUER2 + Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + NAME, event.getQualifiedName());
    }
}
