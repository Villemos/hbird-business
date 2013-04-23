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
package eu.estcube.gs.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IOrbitPrediction;
import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.configurator.StopComponent;
import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.BusinessCard;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandBase;
import org.hbird.exchange.groundstation.GroundStation;
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

import eu.estcube.gs.configuration.GroundStationDriverConfiguration;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GroundStationTrackingDeviceTest {

    private static final String DESCRIPTION = "description";
    private static final String DRIVER_NAME = "driver";
    private static final String NAME = "name";
    private static final long NOW = System.currentTimeMillis();
    private static final String GS_NAME = "TETS-GS";
    private static final long STEP = 1500;
    private static final long HEARTBEAT_INTERVAL = 9001L;

    @Mock
    private GroundStationDriverConfiguration configuration;

    @Mock
    private CommandBase command1;

    @Mock
    private CommandBase command2;

    @Mock
    private CommandBase command3;

    @Mock
    private CommandBase command4;

    @Mock
    private CommandBase command5;

    @Mock
    private CommandBase command6;

    @Mock
    private GroundStation gs;

    @Mock
    private Satellite sat1;

    @Mock
    private Satellite sat2;

    @Mock
    private Track trackCommand;

    @Mock
    private PointingData pd1;

    @Mock
    private PointingData pd2;

    @Mock
    private LocationContactEvent start;

    @Mock
    private LocationContactEvent end;

    @Mock
    private IOrbitPrediction prediction;

    @Mock
    private IDataAccess dao;

    @Mock
    private IPointingDataOptimizer<GroundStationDriverConfiguration> optimizer;

    private GroundStationTrackingDevice<GroundStationDriverConfiguration> trackingDevice;

    private List<CommandBase> preContactCommands;
    private List<CommandBase> contactCommands;
    private List<CommandBase> postContactCommands;

    private List<PointingData> pointingData;

    private List<PointingData> optimizedPointingData;

    private List<String> missingArguments;

    private RuntimeException exception;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @SuppressWarnings("serial")
    @Before
    public void setUp() throws Exception {
        trackingDevice = new GroundStationTrackingDevice<GroundStationDriverConfiguration>(NAME, DESCRIPTION, DRIVER_NAME, configuration, dao, prediction,
                optimizer) {

            @Override
            protected List<CommandBase> createContactCommands(GroundStation gs, Satellite sat, List<PointingData> pointingData,
                    GroundStationDriverConfiguration configuration, Track trackCommand) {
                return GroundStationTrackingDeviceTest.this.gs.equals(gs) &&
                        GroundStationTrackingDeviceTest.this.sat1.equals(sat) &&
                        GroundStationTrackingDeviceTest.this.pointingData.equals(pointingData) &&
                        GroundStationTrackingDeviceTest.this.configuration.equals(configuration) &&
                        GroundStationTrackingDeviceTest.this.trackCommand == trackCommand
                        ? contactCommands : null;
            }

            @Override
            protected List<CommandBase> createPreContactCommands(GroundStation gs, Satellite sat, List<PointingData> pointingData,
                    GroundStationDriverConfiguration configuration, Track trackCommand) {
                return GroundStationTrackingDeviceTest.this.gs.equals(gs) &&
                        GroundStationTrackingDeviceTest.this.sat1.equals(sat) &&
                        GroundStationTrackingDeviceTest.this.pointingData.equals(pointingData) &&
                        GroundStationTrackingDeviceTest.this.configuration.equals(configuration) &&
                        GroundStationTrackingDeviceTest.this.trackCommand == trackCommand
                        ? preContactCommands : null;
            }

            @Override
            protected List<CommandBase> createPostContactCommands(GroundStation gs, Satellite sat, List<PointingData> pointingData,
                    GroundStationDriverConfiguration configuration, Track trackCommand) {
                return GroundStationTrackingDeviceTest.this.gs.equals(gs) &&
                        GroundStationTrackingDeviceTest.this.sat1.equals(sat) &&
                        GroundStationTrackingDeviceTest.this.pointingData.equals(pointingData) &&
                        GroundStationTrackingDeviceTest.this.configuration.equals(configuration) &&
                        GroundStationTrackingDeviceTest.this.trackCommand == trackCommand
                        ? postContactCommands : null;
            }

            @Override
            protected boolean isTrackingPossible(LocationContactEvent start, LocationContactEvent end, GroundStation gs, Satellite sat) {
                return GroundStationTrackingDeviceTest.this.start.equals(start) &&
                        GroundStationTrackingDeviceTest.this.end.equals(end) &&
                        GroundStationTrackingDeviceTest.this.gs.equals(gs) &&
                        GroundStationTrackingDeviceTest.this.sat1.equals(sat);
            }

            @Override
            public List<CommandBase> emergencyStop(Stop command) {
                return null;
            }

        };

        trackingDevice.setIsPartOf(GS_NAME);
        trackingDevice.setFailOldRequests(true);

        preContactCommands = new ArrayList<CommandBase>();
        preContactCommands.add(command1);
        preContactCommands.add(command2);

        contactCommands = new ArrayList<CommandBase>();
        contactCommands.add(command3);
        contactCommands.add(command4);

        postContactCommands = new ArrayList<CommandBase>();
        postContactCommands.add(command5);
        postContactCommands.add(command6);

        pointingData = new ArrayList<PointingData>();
        pointingData.add(pd1);
        pointingData.add(pd2);

        optimizedPointingData = new ArrayList<PointingData>();
        optimizedPointingData.add(pd2);
        optimizedPointingData.add(pd1);

        missingArguments = new ArrayList<String>();
        missingArguments.add(StandardArguments.START);
        missingArguments.add(StandardArguments.END);
        missingArguments.add(StandardArguments.SATELLITE);

        exception = new RuntimeException("Mutchos problemos");

        inOrder = inOrder(configuration, command1, command2, command3, command4, command5, command6, gs, sat1, sat2, trackCommand, pd1, pd2, start, end,
                prediction, dao, optimizer);
    }

    /**
     * Test method for {@link eu.estcube.gs.base.GroundStationTrackingDevice#getConfiguration()}.
     */
    @Test
    public void testGetConfiguration() {
        assertEquals(configuration, trackingDevice.getConfiguration());
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.base.GroundStationTrackingDevice#track(org.hbird.exchange.groundstation.Track)}.
     */
    @Test
    public void testTrackMissingCommandArguments() {
        when(trackCommand.checkArguments()).thenReturn(missingArguments);
        assertEquals(GroundStationTrackingDevice.NO_COMMANDS, trackingDevice.track(trackCommand));
        inOrder.verify(trackCommand, times(1)).checkArguments();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.base.GroundStationTrackingDevice#track(org.hbird.exchange.groundstation.Track)}.
     */
    @Test
    public void testTrackCommandArgumentStartIsNull() {
        when(trackCommand.checkArguments()).thenReturn(Collections.<String> emptyList());
        when(trackCommand.getArgumentValue(StandardArguments.START, LocationContactEvent.class)).thenReturn(null);
        when(trackCommand.getArgumentValue(StandardArguments.END, LocationContactEvent.class)).thenReturn(end);
        when(trackCommand.getArgumentValue(StandardArguments.SATELLITE, Satellite.class)).thenReturn(sat1);
        assertEquals(GroundStationTrackingDevice.NO_COMMANDS, trackingDevice.track(trackCommand));
        inOrder.verify(trackCommand, times(1)).checkArguments();
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.START, LocationContactEvent.class);
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.END, LocationContactEvent.class);
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.SATELLITE, Satellite.class);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.base.GroundStationTrackingDevice#track(org.hbird.exchange.groundstation.Track)}.
     */
    @Test
    public void testTrackCommandArgumentEndIsNull() {
        when(trackCommand.checkArguments()).thenReturn(Collections.<String> emptyList());
        when(trackCommand.getArgumentValue(StandardArguments.START, LocationContactEvent.class)).thenReturn(start);
        when(trackCommand.getArgumentValue(StandardArguments.END, LocationContactEvent.class)).thenReturn(null);
        when(trackCommand.getArgumentValue(StandardArguments.SATELLITE, Satellite.class)).thenReturn(sat1);
        assertEquals(GroundStationTrackingDevice.NO_COMMANDS, trackingDevice.track(trackCommand));
        inOrder.verify(trackCommand, times(1)).checkArguments();
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.START, LocationContactEvent.class);
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.END, LocationContactEvent.class);
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.SATELLITE, Satellite.class);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.base.GroundStationTrackingDevice#track(org.hbird.exchange.groundstation.Track)}.
     */
    @Test
    public void testTrackCommandArgumentSatIsNull() {
        when(trackCommand.checkArguments()).thenReturn(Collections.<String> emptyList());
        when(trackCommand.getArgumentValue(StandardArguments.START, LocationContactEvent.class)).thenReturn(start);
        when(trackCommand.getArgumentValue(StandardArguments.END, LocationContactEvent.class)).thenReturn(end);
        when(trackCommand.getArgumentValue(StandardArguments.SATELLITE, Satellite.class)).thenReturn(null);
        assertEquals(GroundStationTrackingDevice.NO_COMMANDS, trackingDevice.track(trackCommand));
        inOrder.verify(trackCommand, times(1)).checkArguments();
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.START, LocationContactEvent.class);
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.END, LocationContactEvent.class);
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.SATELLITE, Satellite.class);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.base.GroundStationTrackingDevice#track(org.hbird.exchange.groundstation.Track)}.
     */
    @Test
    public void testTrackFailedToResolveGroundStation() {
        when(trackCommand.checkArguments()).thenReturn(Collections.<String> emptyList());
        when(trackCommand.getArgumentValue(StandardArguments.START, LocationContactEvent.class)).thenReturn(start);
        when(trackCommand.getArgumentValue(StandardArguments.END, LocationContactEvent.class)).thenReturn(end);
        when(trackCommand.getArgumentValue(StandardArguments.SATELLITE, Satellite.class)).thenReturn(sat1);
        when(dao.resolveNamed(GS_NAME)).thenThrow(exception);
        assertEquals(GroundStationTrackingDevice.NO_COMMANDS, trackingDevice.track(trackCommand));
        inOrder.verify(trackCommand, times(1)).checkArguments();
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.START, LocationContactEvent.class);
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.END, LocationContactEvent.class);
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.SATELLITE, Satellite.class);
        inOrder.verify(dao, times(1)).resolveNamed(GS_NAME);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.base.GroundStationTrackingDevice#track(org.hbird.exchange.groundstation.Track)}.
     */
    @Test
    public void testTrackGroundStationNull() {
        when(trackCommand.checkArguments()).thenReturn(Collections.<String> emptyList());
        when(trackCommand.getArgumentValue(StandardArguments.START, LocationContactEvent.class)).thenReturn(start);
        when(trackCommand.getArgumentValue(StandardArguments.END, LocationContactEvent.class)).thenReturn(end);
        when(trackCommand.getArgumentValue(StandardArguments.SATELLITE, Satellite.class)).thenReturn(sat1);
        when(dao.resolveNamed(GS_NAME)).thenReturn(null);
        assertEquals(GroundStationTrackingDevice.NO_COMMANDS, trackingDevice.track(trackCommand));
        inOrder.verify(trackCommand, times(1)).checkArguments();
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.START, LocationContactEvent.class);
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.END, LocationContactEvent.class);
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.SATELLITE, Satellite.class);
        inOrder.verify(dao, times(1)).resolveNamed(GS_NAME);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.base.GroundStationTrackingDevice#track(org.hbird.exchange.groundstation.Track)}.
     */
    @Test
    public void testTrackOutdatedCommands() {
        when(trackCommand.checkArguments()).thenReturn(Collections.<String> emptyList());
        when(trackCommand.getArgumentValue(StandardArguments.START, LocationContactEvent.class)).thenReturn(start);
        when(trackCommand.getArgumentValue(StandardArguments.END, LocationContactEvent.class)).thenReturn(end);
        when(trackCommand.getArgumentValue(StandardArguments.SATELLITE, Satellite.class)).thenReturn(sat1);
        when(dao.resolveNamed(GS_NAME)).thenReturn(gs);
        when(start.getTimestamp()).thenReturn(NOW - 1);
        assertEquals(GroundStationTrackingDevice.NO_COMMANDS, trackingDevice.track(trackCommand));
        inOrder.verify(trackCommand, times(1)).checkArguments();
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.START, LocationContactEvent.class);
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.END, LocationContactEvent.class);
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.SATELLITE, Satellite.class);
        inOrder.verify(dao, times(1)).resolveNamed(GS_NAME);
        inOrder.verify(start, times(1)).getTimestamp();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.base.GroundStationTrackingDevice#track(org.hbird.exchange.groundstation.Track)}.
     */
    @Test
    public void testTrackNotPossible() {
        when(trackCommand.checkArguments()).thenReturn(Collections.<String> emptyList());
        when(trackCommand.getArgumentValue(StandardArguments.START, LocationContactEvent.class)).thenReturn(start);
        when(trackCommand.getArgumentValue(StandardArguments.END, LocationContactEvent.class)).thenReturn(end);
        when(trackCommand.getArgumentValue(StandardArguments.SATELLITE, Satellite.class)).thenReturn(sat2);
        when(dao.resolveNamed(GS_NAME)).thenReturn(gs);
        when(start.getTimestamp()).thenReturn(NOW + 1000L * 60 * 60);
        assertEquals(GroundStationTrackingDevice.NO_COMMANDS, trackingDevice.track(trackCommand));
        inOrder.verify(trackCommand, times(1)).checkArguments();
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.START, LocationContactEvent.class);
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.END, LocationContactEvent.class);
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.SATELLITE, Satellite.class);
        inOrder.verify(dao, times(1)).resolveNamed(GS_NAME);
        inOrder.verify(start, times(1)).getTimestamp();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.base.GroundStationTrackingDevice#track(org.hbird.exchange.groundstation.Track)}.
     * 
     * @throws Exception
     */
    @Test
    public void testTrackPredictionException() throws Exception {
        when(trackCommand.checkArguments()).thenReturn(Collections.<String> emptyList());
        when(trackCommand.getArgumentValue(StandardArguments.START, LocationContactEvent.class)).thenReturn(start);
        when(trackCommand.getArgumentValue(StandardArguments.END, LocationContactEvent.class)).thenReturn(end);
        when(trackCommand.getArgumentValue(StandardArguments.SATELLITE, Satellite.class)).thenReturn(sat1);
        when(dao.resolveNamed(GS_NAME)).thenReturn(gs);
        when(start.getTimestamp()).thenReturn(NOW + 1000L * 60 * 60);
        when(configuration.getCommandInterval()).thenReturn(STEP);
        when(prediction.requestPointingDataFor(start, end, gs, sat1, STEP)).thenThrow(exception);
        assertEquals(GroundStationTrackingDevice.NO_COMMANDS, trackingDevice.track(trackCommand));
        inOrder.verify(trackCommand, times(1)).checkArguments();
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.START, LocationContactEvent.class);
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.END, LocationContactEvent.class);
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.SATELLITE, Satellite.class);
        inOrder.verify(dao, times(1)).resolveNamed(GS_NAME);
        inOrder.verify(start, times(1)).getTimestamp();
        inOrder.verify(configuration, times(1)).getCommandInterval();
        inOrder.verify(prediction, times(1)).requestPointingDataFor(start, end, gs, sat1, STEP);
        inOrder.verify(gs, times(1)).getGroundStationId();
        inOrder.verify(sat1, times(1)).getSatelliteId();
        inOrder.verify(start, times(1)).getTimestamp();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.base.GroundStationTrackingDevice#track(org.hbird.exchange.groundstation.Track)}.
     * 
     * @throws Exception
     */
    @Test
    public void testTrack() throws Exception {
        when(trackCommand.checkArguments()).thenReturn(Collections.<String> emptyList());
        when(trackCommand.getArgumentValue(StandardArguments.START, LocationContactEvent.class)).thenReturn(start);
        when(trackCommand.getArgumentValue(StandardArguments.END, LocationContactEvent.class)).thenReturn(end);
        when(trackCommand.getArgumentValue(StandardArguments.SATELLITE, Satellite.class)).thenReturn(sat1);
        when(dao.resolveNamed(GS_NAME)).thenReturn(gs);
        when(start.getTimestamp()).thenReturn(NOW + 1000L * 60 * 60);
        when(configuration.getCommandInterval()).thenReturn(STEP);
        when(prediction.requestPointingDataFor(start, end, gs, sat1, STEP)).thenReturn(pointingData);
        when(optimizer.optimize(pointingData, configuration)).thenReturn(pointingData);

        List<CommandBase> trackingCommands = trackingDevice.track(trackCommand);
        assertNotNull(trackingCommands);
        assertEquals(6, trackingCommands.size());
        assertEquals(command1, trackingCommands.get(0));
        assertEquals(command2, trackingCommands.get(1));
        assertEquals(command3, trackingCommands.get(2));
        assertEquals(command4, trackingCommands.get(3));
        assertEquals(command5, trackingCommands.get(4));
        assertEquals(command6, trackingCommands.get(5));

        inOrder.verify(trackCommand, times(1)).checkArguments();
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.START, LocationContactEvent.class);
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.END, LocationContactEvent.class);
        inOrder.verify(trackCommand, times(1)).getArgumentValue(StandardArguments.SATELLITE, Satellite.class);
        inOrder.verify(dao, times(1)).resolveNamed(GS_NAME);
        inOrder.verify(start, times(1)).getTimestamp();
        inOrder.verify(configuration, times(1)).getCommandInterval();
        inOrder.verify(prediction, times(1)).requestPointingDataFor(start, end, gs, sat1, STEP);
        inOrder.verify(optimizer, times(1)).optimize(pointingData, configuration);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.base.GroundStationTrackingDevice#createContactCommands(org.hbird.exchange.groundstation.GroundStation, org.hbird.exchange.navigation.Satellite, java.util.List, eu.estcube.gs.configuration.GroundStationDriverConfiguration)}
     * .
     */
    @Test
    public void testCreateContactCommands() {
        assertEquals(contactCommands, trackingDevice.createContactCommands(gs, sat1, pointingData, configuration, trackCommand));
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.base.GroundStationTrackingDevice#isTrackingPossible(org.hbird.exchange.navigation.LocationContactEvent, org.hbird.exchange.navigation.LocationContactEvent, org.hbird.exchange.groundstation.GroundStation, org.hbird.exchange.navigation.Satellite)}
     * .
     */
    @Test
    public void testIsTrackingPossible() {
        assertTrue(trackingDevice.isTrackingPossible(start, end, gs, sat1));
    }

    /**
     * Test method for {@link eu.estcube.gs.base.GroundStationTrackingDevice#validateByTime(long, long, boolean)}.
     */
    @Test
    public void testValidateByTime() {
        assertFalse(trackingDevice.validateByTime(NOW - 1, NOW, true));
        assertTrue(trackingDevice.validateByTime(NOW - 1, NOW, false));
        assertTrue(trackingDevice.validateByTime(NOW + 1, NOW, true));
        assertTrue(trackingDevice.validateByTime(NOW + 1, NOW, false));
        assertTrue(trackingDevice.validateByTime(NOW, NOW, true));
        assertTrue(trackingDevice.validateByTime(NOW, NOW, false));
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.base.GroundStationTrackingDevice#createPreContactCommands(org.hbird.exchange.groundstation.GroundStation, org.hbird.exchange.navigation.Satellite, java.util.List, eu.estcube.gs.configuration.GroundStationDriverConfiguration)}
     * .
     */
    @Test
    public void testCreatePreContactCommands() {
        assertEquals(preContactCommands, trackingDevice.createPreContactCommands(gs, sat1, pointingData, configuration, trackCommand));
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.base.GroundStationTrackingDevice#createPostContactCommands(org.hbird.exchange.groundstation.GroundStation, org.hbird.exchange.navigation.Satellite, java.util.List, eu.estcube.gs.configuration.GroundStationDriverConfiguration)}
     * .
     */
    @Test
    public void testCreatePostContactCommandsOnMock() {
        assertEquals(postContactCommands, trackingDevice.createPostContactCommands(gs, sat1, pointingData, configuration, trackCommand));
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.base.GroundStationTrackingDevice#createPostContactCommands(org.hbird.exchange.groundstation.GroundStation, org.hbird.exchange.navigation.Satellite, java.util.List, eu.estcube.gs.configuration.GroundStationDriverConfiguration)}
     * .
     */
    @SuppressWarnings("serial")
    @Test
    public void testDefaultImplementation() {
        trackingDevice = new GroundStationTrackingDevice<GroundStationDriverConfiguration>(NAME, DESCRIPTION, DRIVER_NAME, configuration, dao, prediction,
                optimizer) {

            @Override
            protected List<CommandBase> createContactCommands(GroundStation gs, Satellite sat, List<PointingData> pointingData,
                    GroundStationDriverConfiguration configuration, Track trackCommand) {
                return null;
            }

            @Override
            public List<CommandBase> emergencyStop(Stop command) {
                return null;
            }
        };
        assertEquals(GroundStationTrackingDevice.NO_COMMANDS, trackingDevice.createPreContactCommands(gs, sat1, pointingData, configuration, trackCommand));
        assertEquals(GroundStationTrackingDevice.NO_COMMANDS, trackingDevice.createPostContactCommands(gs, sat1, pointingData, configuration, trackCommand));
        assertTrue(trackingDevice.isTrackingPossible(start, end, gs, sat1));
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.base.GroundStationTrackingDevice#validateArgument(java.lang.Object, java.lang.String)}.
     */
    @Test
    public void testValidateArgument() {
        assertFalse(trackingDevice.validateArgument(null, "name"));
        assertTrue(trackingDevice.validateArgument(new Object(), "name"));
    }

    /**
     * Test method for {@link eu.estcube.gs.base.GroundStationTrackingDevice#isFailOldRequests()}
     */
    @Test
    public void testIsFailOldRequest() {
        testSetFailOldRequest();
    }

    /**
     * Test method for {@link eu.estcube.gs.base.GroundStationTrackingDevice#setFailOldRequests(boolean)}
     */
    @Test
    public void testSetFailOldRequest() {
        assertTrue(trackingDevice.isFailOldRequests());
        trackingDevice.setFailOldRequests(false);
        assertFalse(trackingDevice.isFailOldRequests());
    }

    /**
     * Test method for {@link eu.estcube.gs.base.GroundStationTrackingDevice#createCommandList(List)}
     */
    @Test
    public void testCreateCommandList() {
        List<Command> commands = trackingDevice.getCommands();
        assertEquals(4, commands.size());
        assertEquals(StartComponent.class.getSimpleName(), commands.get(0).getName());
        assertEquals(StopComponent.class.getSimpleName(), commands.get(1).getName());
        assertEquals(Track.class.getSimpleName(), commands.get(2).getName());
        assertEquals(Stop.class.getSimpleName(), commands.get(3).getName());
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.base.GroundStationTrackingDevice#optimze(List, GroundStationDriverConfiguration, IPointingDataOptimizer)}
     */
    @Test
    public void testOptimizeNoOptimizer() {
        assertEquals(pointingData, trackingDevice.optimze(pointingData, configuration, null));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.base.GroundStationTrackingDevice#optimze(List, GroundStationDriverConfiguration, IPointingDataOptimizer)}
     * 
     * @throws Exception
     */
    @Test
    public void testOptimizeWithException() throws Exception {
        when(optimizer.optimize(pointingData, configuration)).thenThrow(exception);
        assertEquals(pointingData, trackingDevice.optimze(pointingData, configuration, optimizer));
        inOrder.verify(optimizer, times(1)).optimize(pointingData, configuration);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.base.GroundStationTrackingDevice#optimze(List, GroundStationDriverConfiguration, IPointingDataOptimizer)}
     * 
     * @throws Exception
     */
    @Test
    public void testOptimize() throws Exception {
        when(optimizer.optimize(pointingData, configuration)).thenReturn(optimizedPointingData);
        assertEquals(optimizedPointingData, trackingDevice.optimze(pointingData, configuration, optimizer));
        inOrder.verify(optimizer, times(1)).optimize(pointingData, configuration);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link eu.estcube.gs.base.GroundStationTrackingDevice#getHeartbeat()}
     * 
     * @throws Exception
     */
    @Test
    public void testGetHeartbeat() {
        when(configuration.getHeartBeatInterval()).thenReturn(HEARTBEAT_INTERVAL);
        assertEquals(HEARTBEAT_INTERVAL, trackingDevice.getHeartbeat());
        inOrder.verify(configuration, times(1)).getHeartBeatInterval();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link eu.estcube.gs.base.GroundStationTrackingDevice#setHeartbeat(long)}
     * 
     * @throws Exception
     */
    @Test
    public void testSetHeartbeat() {
        trackingDevice.setHeartbeat(HEARTBEAT_INTERVAL);
        inOrder.verify(configuration, times(1)).setHeartBeatInterval(HEARTBEAT_INTERVAL);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link eu.estcube.gs.base.GroundStationTrackingDevice#getBusinessCard()}
     * 
     * @throws Exception
     */
    @Test
    public void testGetBusinessCard() {
        when(configuration.getHeartBeatInterval()).thenReturn(HEARTBEAT_INTERVAL);
        BusinessCard card = trackingDevice.getBusinessCard();
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
