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

import org.junit.Test;

/**
 *
 */
public class EventTest {

    private static final String ISSUER1 = "issuer";
    private static final String ISSUER2 = "issuer";
    private static final String ID = "ID";
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
        Event event = new Event(ID, NAME);
        assertEquals(ID, event.getID());
        assertEquals(NAME, event.getName());
    }

}
