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
package org.hbird.business.groundstation.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.hbird.business.groundstation.configuration.GroundStationDriverConfiguration;
import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.configurator.StopComponent;
import org.hbird.exchange.core.BusinessCard;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.groundstation.Stop;
import org.hbird.exchange.groundstation.Track;
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
public class GroundStationPartTest {

    private static final String DESCRIPTION = "description";
    private static final String DRIVER_NAME = "driver";
    private static final String NAME = "name";
    private static final long HEARTBEAT_INTERVAL = 9001L;

    @Mock
    private GroundStationDriverConfiguration configuration;

    private GroundStationPart<GroundStationDriverConfiguration> part;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        part = new GroundStationPart<GroundStationDriverConfiguration>("", NAME, DESCRIPTION, DRIVER_NAME, configuration);
        inOrder = inOrder(configuration);
    }

    @Test
    public void testGetConfiguration() throws Exception {
        assertEquals(configuration, part.getConfiguration());
    }

    @Test
    public void testCreateCommandList() {
        List<Command> commands = part.getCommands();
        assertEquals(4, commands.size());
        assertEquals(StartComponent.class.getSimpleName(), commands.get(0).getName());
        assertEquals(StopComponent.class.getSimpleName(), commands.get(1).getName());
        assertEquals(Track.class.getSimpleName(), commands.get(2).getName());
        assertEquals(Stop.class.getSimpleName(), commands.get(3).getName());
    }

    @Test
    public void testGetHeartbeat() {
        when(configuration.getHeartBeatInterval()).thenReturn(HEARTBEAT_INTERVAL);
        assertEquals(HEARTBEAT_INTERVAL, part.getHeartbeat());
        inOrder.verify(configuration, times(1)).getHeartBeatInterval();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testSetHeartbeat() {
        part.setHeartbeat(HEARTBEAT_INTERVAL);
        inOrder.verify(configuration, times(1)).setHeartBeatInterval(HEARTBEAT_INTERVAL);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetBusinessCard() {
        when(configuration.getHeartBeatInterval()).thenReturn(HEARTBEAT_INTERVAL);
        BusinessCard card = part.getBusinessCard();
        assertEquals(NAME, card.getName());
        assertEquals(NAME, card.getIssuedBy());
        assertEquals(HEARTBEAT_INTERVAL, card.getPeriod());
        Map<String, Command> commands = card.getCommandsIn();
        assertEquals(4, commands.size());
        assertTrue(commands.containsKey(StartComponent.class.getSimpleName()));
        assertTrue(commands.containsKey(StopComponent.class.getSimpleName()));
        assertTrue(commands.containsKey(Track.class.getSimpleName()));
        assertTrue(commands.containsKey(Stop.class.getSimpleName()));
        assertNotNull(commands.get(StartComponent.class.getSimpleName()));
        assertNotNull(commands.get(StopComponent.class.getSimpleName()));
        assertNotNull(commands.get(Track.class.getSimpleName()));
        assertNotNull(commands.get(Stop.class.getSimpleName()));
    }
}
