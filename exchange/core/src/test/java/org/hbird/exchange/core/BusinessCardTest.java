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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.hbird.exchange.util.LocalHostNameResolver;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class BusinessCardTest {

    private static final String ISSUED_BY = "issuer";
    private static final String HOST = "host";
    private static final long PERIOD = 12345L;

    private BusinessCard card;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        card = new BusinessCard(ISSUED_BY, PERIOD);
    }

    /**
     * Test method for {@link org.hbird.exchange.core.BusinessCard#BusinessCard(java.lang.String, java.util.List, long)}
     * .
     */
    @Test
    public void testBusinessCardStringListOfCommandLong() {
        card = new BusinessCard(ISSUED_BY, PERIOD);
        assertEquals(ISSUED_BY, card.getIssuedBy());
        assertEquals(ISSUED_BY, card.getName());
        assertTrue(card.getTimestamp() <= System.currentTimeMillis() && card.getTimestamp() > System.currentTimeMillis() - 3 * 1000L);
        assertEquals(BusinessCard.DESCRIPTION, card.getDescription());
        assertEquals(PERIOD, card.getPeriod());
        assertNotNull(card.getID());
        assertEquals(ISSUED_BY + Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + ISSUED_BY, card.getQualifiedName());
        assertEquals(LocalHostNameResolver.getLocalHostName(), card.getHost());
    }

    /**
     * Test method for
     * {@link org.hbird.exchange.core.BusinessCard#BusinessCard(java.lang.String, java.lang.String, java.util.List, long)}
     * .
     */
    @Test
    public void testBusinessCardStringStringListOfCommandLong() {
        card = new BusinessCard(ISSUED_BY, PERIOD);
        assertEquals(ISSUED_BY, card.getIssuedBy());
        assertEquals(ISSUED_BY, card.getName());
        assertTrue(card.getTimestamp() <= System.currentTimeMillis() && card.getTimestamp() > System.currentTimeMillis() - 3 * 1000L);
        assertEquals(BusinessCard.DESCRIPTION, card.getDescription());
        assertEquals(PERIOD, card.getPeriod());
        assertNotNull(card.getID());
        assertEquals(ISSUED_BY + Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + ISSUED_BY, card.getQualifiedName());
        assertEquals(LocalHostNameResolver.getLocalHostName(), card.getHost());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.BusinessCard#getHost()}.
     */
    @Test
    public void testGetHost() {
        testSetHost();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.BusinessCard#setHost(java.lang.String)}.
     */
    @Test
    public void testSetHost() {
        assertEquals(LocalHostNameResolver.getLocalHostName(), card.getHost());
        card.setHost(null);
        assertNull(card.getHost());
        card.setHost(HOST);
        assertEquals(HOST, card.getHost());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.BusinessCard#getPeriod()}.
     */
    @Test
    public void testGetPeriod() {
        testSetPeriod();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.BusinessCard#setPeriod(long)}.
     */
    @Test
    public void testSetPeriod() {
        assertEquals(PERIOD, card.getPeriod());
        card.setPeriod(0);
        assertEquals(0, card.getPeriod());
        card.setPeriod(-1 * PERIOD);
        assertEquals(-1 * PERIOD, card.getPeriod());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.BusinessCard#toString()}.
     */
    @Test
    public void testToString() {
        String str = card.toString();
        assertNotNull(str);
        assertTrue(str.contains(HOST));
        assertTrue(str.contains(ISSUED_BY));
        assertTrue(str.contains(String.valueOf(PERIOD)));
    }

    /**
     * Test method for {@link org.hbird.exchange.core.BusinessCard#touch()}.
     */
    @Test
    public void testTouch() {
        long timestamp1 = card.getTimestamp();
        BusinessCard touched = card.touch();
        long timestamp2 = touched.getTimestamp();
        assertEquals(card, touched);
        assertNotSame(timestamp1, timestamp2);
    }
}
