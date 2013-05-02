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
import static org.mockito.Mockito.inOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hbird.exchange.util.LocalHostNameResolver;
import org.junit.After;
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
public class BusinessCardTest {

    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String ISSUED_BY = "issuer";
    private static final String HOST = "host";
    private static final long PERIOD = 12345L;
    private static final String CMD_1 = "Command-1";
    private static final String CMD_2 = "Command-2";

    @Mock
    private Map<String, Command> commands;

    @Mock
    private Map<String, Event> events;

    @Mock
    private Map<String, EntityInstance> data;

    @Mock
    private Command cmd1;

    @Mock
    private Command cmd2;

    private List<Command> commandsIn;

    private InOrder inOrder;

    private BusinessCard card;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        card = new BusinessCard(ID, NAME);
        card.setIssuedBy(ISSUED_BY);
        card.setPeriod(PERIOD);
        commandsIn = new ArrayList<Command>(2);
        commandsIn.add(cmd1);
        commandsIn.add(cmd2);
        inOrder = inOrder(commands, events, data, cmd1, cmd2);
    }

    @After
    public void tearDown() {
        inOrder.verifyNoMoreInteractions();
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

    /**
     * Test method for {@link org.hbird.exchange.core.BusinessCard#getCommandsIn()}.
     */
    @Test
    public void testGetCommandsIn() {
        testSetCommandsIn();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.BusinessCard#setCommandsIn(Map)}.
     */
    @Test
    public void testSetCommandsIn() {
        Map<String, Command> map = card.getCommandsIn();
        assertNotNull(map);
        assertTrue(map.isEmpty());
        card.setCommandsIn(commands);
        assertEquals(commands, card.getCommandsIn());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.BusinessCard#getCommandsOut()}.
     */
    @Test
    public void testGetCommandsOut() {
        testSetCommandsOut();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.BusinessCard#setCommandsOut(Map)}.
     */
    @Test
    public void testSetCommandsOut() {
        Map<String, Command> map = card.getCommandsOut();
        assertNotNull(map);
        assertTrue(map.isEmpty());
        card.setCommandsOut(commands);
        assertEquals(commands, card.getCommandsOut());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.BusinessCard#getEventsIn()}.
     */
    @Test
    public void testGetEventsIn() {
        testSetEventsIn();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.BusinessCard#setEventsIn(Map)}.
     */
    @Test
    public void testSetEventsIn() {
        Map<String, Event> map = card.getEventsIn();
        assertNotNull(map);
        assertTrue(map.isEmpty());
        card.setEventsIn(events);
        assertEquals(events, card.getEventsIn());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.BusinessCard#getEventsOut()}.
     */
    @Test
    public void testGetEventsOut() {
        testSetEventsOut();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.BusinessCard#setEventsOut(Map)}.
     */
    @Test
    public void testSetEventsOut() {
        Map<String, Event> map = card.getEventsOut();
        assertNotNull(map);
        assertTrue(map.isEmpty());
        card.setEventsOut(events);
        assertEquals(events, card.getEventsOut());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.BusinessCard#getDataIn()}.
     */
    @Test
    public void testGetDataIn() {
        testSetDataIn();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.BusinessCard#setSataIn(Map)}.
     */
    @Test
    public void testSetDataIn() {
        Map<String, EntityInstance> map = card.getDataIn();
        assertNotNull(map);
        assertTrue(map.isEmpty());
        card.setDataIn(data);
        assertEquals(data, card.getDataIn());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.BusinessCard#getDataOut()}.
     */
    @Test
    public void testGetDataOut() {
        testSetDataOut();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.BusinessCard#setSataOut(Map)}.
     */
    @Test
    public void testSetDataOut() {
        Map<String, EntityInstance> map = card.getDataOut();
        assertNotNull(map);
        assertTrue(map.isEmpty());
        card.setDataOut(data);
        assertEquals(data, card.getDataOut());
    }
}
