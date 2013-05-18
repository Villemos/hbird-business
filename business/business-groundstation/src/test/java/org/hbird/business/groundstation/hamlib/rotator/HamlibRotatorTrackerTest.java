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
package org.hbird.business.groundstation.hamlib.rotator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.hbird.business.api.ICatalogue;
import org.hbird.business.api.IPointingData;
import org.hbird.business.groundstation.configuration.RotatorDriverConfiguration;
import org.hbird.business.groundstation.hamlib.HamlibNativeCommand;
import org.hbird.business.groundstation.hamlib.rotator.protocol.Park;
import org.hbird.business.groundstation.hamlib.rotator.protocol.Reset;
import org.hbird.business.groundstation.hamlib.rotator.protocol.SetPosition;
import org.hbird.exchange.core.CommandBase;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.groundstation.IPointingDataOptimizer;
import org.hbird.exchange.groundstation.Stop;
import org.hbird.exchange.groundstation.Track;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.PointingData;
import org.hbird.exchange.navigation.Satellite;
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
public class HamlibRotatorTrackerTest {

    private static final String ID = "ID";
    private static final double AZIMUTH_1 = 123.213D;
    private static final double ELEVATION_1 = 34.5678D;
    private static final double AZIMUTH_2 = -13.32D;
    private static final double ELEVATION_2 = 152.0002D;
    private static final double AZIMUTH_3 = -44.4445D;
    private static final double ELEVATION_3 = 101.10101D;
    private static final long NOW = System.currentTimeMillis();
    private static final long PRE_DELTA = 15051L;
    private static final long POST_DELTA = 15000L;
    private static final long DELAY = 45L;

    @Mock
    private RotatorDriverConfiguration configuration;

    @Mock
    private ICatalogue catalogue;

    @Mock
    private IPointingDataOptimizer<RotatorDriverConfiguration> optimizer;

    @Mock
    private Stop stop;

    @Mock
    private Track track;

    @Mock
    private GroundStation gs;

    @Mock
    private Satellite sat;

    @Mock
    private PointingData pd1;

    @Mock
    private PointingData pd2;

    @Mock
    private PointingData pd3;

    @Mock
    private LocationContactEvent contact;

    @Mock
    private IPointingData calculator;

    private List<PointingData> pointingData;

    private HamlibRotatorTracker tracker;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        tracker = new HamlibRotatorTracker(configuration, catalogue, calculator, optimizer);
        pointingData = Arrays.asList(pd1, pd2, pd3);
        inOrder = inOrder(configuration, catalogue, optimizer, stop, track, gs, sat, pd1, pd2, pd3, contact, calculator);
        when(pd1.getAzimuth()).thenReturn(AZIMUTH_1);
        when(pd1.getElevation()).thenReturn(ELEVATION_1);
        when(pd1.getTimestamp()).thenReturn(NOW + 1);
        when(pd2.getAzimuth()).thenReturn(AZIMUTH_2);
        when(pd2.getElevation()).thenReturn(ELEVATION_2);
        when(pd2.getTimestamp()).thenReturn(NOW + 2);
        when(pd3.getAzimuth()).thenReturn(AZIMUTH_3);
        when(pd3.getElevation()).thenReturn(ELEVATION_3);
        when(pd3.getTimestamp()).thenReturn(NOW + 3);
        when(track.getID()).thenReturn(ID);
        when(track.getLocationContactEvent()).thenReturn(contact);
        when(contact.getStartTime()).thenReturn(NOW);
        when(contact.getEndTime()).thenReturn(NOW + 10);
        when(configuration.getPreContactDelta()).thenReturn(PRE_DELTA);
        when(configuration.getPostContactDelta()).thenReturn(POST_DELTA);
        when(configuration.getDelayInCommandGroup()).thenReturn(DELAY);
    }

    /**
     * Test method for
     * {@link org.hbird.business.groundstation.hamlib.rotator.HamlibRotatorPart#emergencyStop(org.hbird.exchange.groundstation.Stop)}
     * .
     */
    @Test
    public void testEmergencyStop() {
        List<CommandBase> commands = tracker.emergencyStop(stop);
        assertNotNull(commands);
        assertEquals(0, commands.size());
    }

    /**
     * Test method for
     * {@link org.hbird.business.groundstation.hamlib.rotator.HamlibRotatorPart#createContactCommands(org.hbird.exchange.groundstation.GroundStation, org.hbird.exchange.navigation.Satellite, java.util.List, org.hbird.business.groundstation.configuration.RotatorDriverConfiguration, org.hbird.exchange.groundstation.Track)}
     * .
     */
    @Test
    public void testCreateContactCommandsGroundStationSatelliteListOfPointingDataRotatorDriverConfigurationTrack() {
        List<CommandBase> result = tracker.createContactCommands(gs, sat, pointingData, configuration, track);
        assertNotNull(result);
        assertEquals(2, result.size());
        HamlibNativeCommand cmd = (HamlibNativeCommand) result.get(0);
        assertEquals(ID, cmd.getDerivedFrom());
        assertEquals(NOW + 2, cmd.getExecutionTime());
        assertEquals(HamlibNativeCommand.STAGE_TRACKING, cmd.getStage());
        assertEquals(SetPosition.createCommand(AZIMUTH_2, ELEVATION_2), cmd.getCommandToExecute());
        cmd = (HamlibNativeCommand) result.get(1);
        assertEquals(ID, cmd.getDerivedFrom());
        assertEquals(NOW + 3, cmd.getExecutionTime());
        assertEquals(HamlibNativeCommand.STAGE_TRACKING, cmd.getStage());
        assertEquals(SetPosition.createCommand(AZIMUTH_3, ELEVATION_3), cmd.getCommandToExecute());

        inOrder.verify(track, times(1)).getID();
        inOrder.verify(pd2, times(1)).getAzimuth();
        inOrder.verify(pd2, times(1)).getElevation();
        inOrder.verify(pd2, times(1)).getTimestamp();
        inOrder.verify(pd3, times(1)).getAzimuth();
        inOrder.verify(pd3, times(1)).getElevation();
        inOrder.verify(pd3, times(1)).getTimestamp();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.business.groundstation.hamlib.rotator.HamlibRotatorPart#createPreContactCommands(org.hbird.exchange.groundstation.GroundStation, org.hbird.exchange.navigation.Satellite, java.util.List, org.hbird.business.groundstation.configuration.RotatorDriverConfiguration, org.hbird.exchange.groundstation.Track)}
     * .
     */
    @Test
    public void testCreatePreContactCommandsGroundStationSatelliteListOfPointingDataRotatorDriverConfigurationTrack() {
        List<CommandBase> result = tracker.createPreContactCommands(gs, sat, pointingData, configuration, track);
        assertNotNull(result);
        assertEquals(2, result.size());
        HamlibNativeCommand cmd = (HamlibNativeCommand) result.get(0);
        assertEquals(ID, cmd.getDerivedFrom());
        assertEquals(NOW - PRE_DELTA, cmd.getExecutionTime());
        assertEquals(HamlibNativeCommand.STAGE_PRE_TRACKING, cmd.getStage());
        assertEquals(Reset.ALL, cmd.getCommandToExecute());
        cmd = (HamlibNativeCommand) result.get(1);
        assertEquals(ID, cmd.getDerivedFrom());
        assertEquals(NOW - PRE_DELTA + DELAY, cmd.getExecutionTime());
        assertEquals(HamlibNativeCommand.STAGE_PRE_TRACKING, cmd.getStage());
        assertEquals(SetPosition.createCommand(AZIMUTH_1, ELEVATION_1), cmd.getCommandToExecute());

        inOrder.verify(track, times(1)).getID();
        inOrder.verify(track, times(1)).getLocationContactEvent();
        inOrder.verify(contact, times(1)).getStartTime();
        inOrder.verify(configuration, times(1)).getPreContactDelta();
        inOrder.verify(pd1, times(1)).getAzimuth();
        inOrder.verify(pd1, times(1)).getElevation();
        inOrder.verify(configuration, times(1)).getDelayInCommandGroup();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.business.groundstation.hamlib.rotator.HamlibRotatorPart#createPostContactCommands(org.hbird.exchange.groundstation.GroundStation, org.hbird.exchange.navigation.Satellite, java.util.List, org.hbird.business.groundstation.configuration.RotatorDriverConfiguration, org.hbird.exchange.groundstation.Track)}
     * .
     */
    @Test
    public void testCreatePostContactCommandsGroundStationSatelliteListOfPointingDataRotatorDriverConfigurationTrack() {
        List<CommandBase> result = tracker.createPostContactCommands(gs, sat, pointingData, configuration, track);
        assertNotNull(result);
        assertEquals(1, result.size());
        HamlibNativeCommand cmd = (HamlibNativeCommand) result.get(0);
        assertEquals(ID, cmd.getDerivedFrom());
        assertEquals(NOW + 10 + POST_DELTA, cmd.getExecutionTime());
        assertEquals(HamlibNativeCommand.STAGE_POST_TRACKING, cmd.getStage());
        assertEquals(Park.COMMAND, cmd.getCommandToExecute());

        inOrder.verify(track, times(1)).getLocationContactEvent();
        inOrder.verify(contact, times(1)).getEndTime();
        inOrder.verify(configuration, times(1)).getPostContactDelta();
        inOrder.verify(track, times(1)).getID();
        inOrder.verifyNoMoreInteractions();
    }
}
