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
package org.hbird.business.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.configurator.StopComponent;
import org.hbird.exchange.core.BusinessCard;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Part;
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
public class StartablePartTest {

    public static final String NAME = "name";
    public static final String ISSUED_BY = "issuer";
    public static final String DESCRIPTION = "description";
    public static final String DRIVER_NAME = "driver name";
    public static final String CONFIGURATOR = "configurator";
    public static final long PERIOD = 12000L;
    public static final String CMD_1 = "Command-1";
    public static final String CMD_2 = "Command-2";

    @Mock
    private Command cmd1;

    @Mock
    private Command cmd2;

    private StartablePart part;

    private List<Command> commands;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        commands = new ArrayList<Command>();
        commands.add(cmd1);
        commands.add(cmd2);
        part = new StartablePart(new Part("root", ""), ISSUED_BY, NAME, DESCRIPTION, DRIVER_NAME);
        inOrder = inOrder(cmd1, cmd2);
        when(cmd1.getName()).thenReturn(CMD_1);
        when(cmd2.getName()).thenReturn(CMD_2);
    }

    @After
    public void tearDown() {
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.business.core.StartablePart#StartablePart(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testStartablePartIssuedBy() {
        assertEquals(NAME, part.getName());
        assertEquals(ISSUED_BY, part.getIssuedBy());
        assertNull(part.getConfigurator());
        assertEquals(DESCRIPTION, part.getDescription());
        assertEquals(DRIVER_NAME, part.getDriverName());
        assertEquals(StartablePart.DEFAULT_HEARTBEAT, part.getHeartbeat());
    }

    /**
     * Test method for {@link org.hbird.business.core.StartablePart#StartablePart(String, String, String, String)} .
     */
    @Test
    public void testStartablePart() {
        part = new StartablePart(new Part("root", ""), NAME, DESCRIPTION, DRIVER_NAME);
        assertEquals(NAME, part.getName());
        assertEquals(Part.DEFAULT_ISSUED_BY, part.getIssuedBy());
        assertNull(part.getConfigurator());
        assertEquals(DESCRIPTION, part.getDescription());
        assertEquals(DRIVER_NAME, part.getDriverName());
        assertEquals(StartablePart.DEFAULT_HEARTBEAT, part.getHeartbeat());
    }

    /**
     * Test method for {@link org.hbird.business.core.StartablePart#getDriverName()}.
     */
    @Test
    public void testGetDriverName() {
        testSetDriverName();
    }

    /**
     * Test method for {@link org.hbird.business.core.StartablePart#setDriverName(java.lang.String)}.
     */
    @Test
    public void testSetDriverName() {
        assertEquals(DRIVER_NAME, part.getDriverName());
        part.setDriverName(null);
        assertNull(part.getDriverName());
        part.setDriverName(DRIVER_NAME + DRIVER_NAME);
        assertEquals(DRIVER_NAME + DRIVER_NAME, part.getDriverName());
    }

    /**
     * Test method for {@link org.hbird.business.core.StartablePart#getConfigurator()}.
     */
    @Test
    public void testGetConfigurator() {
        testSetConfigurator();
    }

    /**
     * Test method for {@link org.hbird.business.core.StartablePart#setConfigurator(java.lang.String)}.
     */
    @Test
    public void testSetConfigurator() {
        assertNull(part.getConfigurator());
        part.setConfigurator(CONFIGURATOR);
        assertEquals(CONFIGURATOR, part.getConfigurator());
    }

    /**
     * Test method for {@link org.hbird.business.core.StartablePart#getHeartbeat()}.
     */
    @Test
    public void testGetHeartbeat() {
        testSetHeartbeat();
    }

    /**
     * Test method for {@link org.hbird.business.core.StartablePart#setHeartbeat(long)}.
     */
    @Test
    public void testSetHeartbeat() {
        assertEquals(StartablePart.DEFAULT_HEARTBEAT, part.getHeartbeat());
        part.setHeartbeat(0);
        assertEquals(0, part.getHeartbeat());
        part.setHeartbeat(-1 * StartablePart.DEFAULT_HEARTBEAT);
        assertEquals(-1 * StartablePart.DEFAULT_HEARTBEAT, part.getHeartbeat());
    }

    /**
     * Test method for {@link org.hbird.business.core.StartablePart#createBusinessCard(String, long, java.util.List)}.
     */
    @Test
    public void testCreateBusinessCard() {
        BusinessCard card = part.createBusinessCard(NAME, PERIOD, commands);
        assertNotNull(card);
        assertEquals(NAME, card.getName());
        assertEquals(NAME, card.getIssuedBy());
        assertEquals(PERIOD, card.getPeriod());
        Map<String, Command> map = card.getCommandsIn();
        assertNotNull(map);
        assertEquals(2, map.size());
        assertTrue(map.containsKey(CMD_1));
        assertEquals(cmd1, map.get(CMD_1));
        assertTrue(map.containsKey(CMD_2));
        assertEquals(cmd2, map.get(CMD_2));
        inOrder.verify(cmd1, times(1)).getName();
        inOrder.verify(cmd2, times(1)).getName();
    }

    /**
     * Test method for {@link org.hbird.business.core.StartablePart#getBusinessCard()}.
     */
    @Test
    public void testGetBusinessCard() {
        assertNull(part.card);
        BusinessCard card = part.getBusinessCard();
        assertNotNull(card);
        assertEquals(NAME, card.getName());
        assertEquals(NAME, card.getIssuedBy());
        assertEquals(StartablePart.DEFAULT_HEARTBEAT, card.getPeriod());
        Map<String, Command> map = card.getCommandsIn();
        assertNotNull(map);
        assertEquals(2, map.size());
        assertTrue(map.containsKey(StartComponent.class.getSimpleName()));
        assertNotNull(map.get(StartComponent.class.getSimpleName()));
        assertTrue(map.containsKey(StopComponent.class.getSimpleName()));
        assertNotNull(map.get(StopComponent.class.getSimpleName()));
        assertEquals(card, part.card);
        assertEquals(card, part.getBusinessCard());
    }
}
