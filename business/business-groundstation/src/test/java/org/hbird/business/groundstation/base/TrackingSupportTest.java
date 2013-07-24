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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IPointingData;
import org.hbird.business.groundstation.configuration.GroundStationDriverConfiguration;
import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.CommandBase;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.groundstation.IPointingDataOptimizer;
import org.hbird.exchange.groundstation.Stop;
import org.hbird.exchange.groundstation.Track;
import org.hbird.exchange.navigation.ContactParameterRange;
import org.hbird.exchange.navigation.ExtendedContactParameterRange;
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
public class TrackingSupportTest {

    private static final long NOW = System.currentTimeMillis();
    private static final String GS_ID = "TETS-GS";
    private static final long STEP = 1500;
    private static final double AZIMUTH_START = Math.random() * 360.0D;
    private static final double AZIMUTH_END = Math.random() * 360.0D;
    private static final double ELEVATION_MAX = Math.random() * 90.0D;
    private static final double DOPPLER_START = Math.random() * 6000.0D;
    private static final double DOPPLER_END = Math.random() * 6000.0D;

    @Mock
    private GroundStationDriverConfiguration configuration;

    @Mock
    private IPointingData calculator;

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
    private LocationContactEvent contact;

    @Mock
    private IDataAccess dao;

    @Mock
    private IPointingDataOptimizer<GroundStationDriverConfiguration> optimizer;

    @Mock
    private ContactParameterRange azimuth;

    @Mock
    private ExtendedContactParameterRange elevation;

    @Mock
    private ContactParameterRange doppler;

    private TrackingSupport<GroundStationDriverConfiguration> trackingDevice;

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
    @Before
    public void setUp() throws Exception {
        trackingDevice = new TrackingSupport<GroundStationDriverConfiguration>(configuration, dao, calculator, optimizer) {

            @Override
            protected List<CommandBase> createContactCommands(GroundStation gs, Satellite sat, List<PointingData> pointingData,
                    GroundStationDriverConfiguration configuration, Track trackCommand) {
                return TrackingSupportTest.this.gs.equals(gs) &&
                        TrackingSupportTest.this.sat1.equals(sat) &&
                        TrackingSupportTest.this.pointingData.equals(pointingData) &&
                        TrackingSupportTest.this.configuration.equals(configuration) &&
                        TrackingSupportTest.this.trackCommand == trackCommand
                        ? contactCommands : null;
            }

            @Override
            protected List<CommandBase> createPreContactCommands(GroundStation gs, Satellite sat, List<PointingData> pointingData,
                    GroundStationDriverConfiguration configuration, Track trackCommand) {
                return TrackingSupportTest.this.gs.equals(gs) &&
                        TrackingSupportTest.this.sat1.equals(sat) &&
                        TrackingSupportTest.this.pointingData.equals(pointingData) &&
                        TrackingSupportTest.this.configuration.equals(configuration) &&
                        TrackingSupportTest.this.trackCommand == trackCommand
                        ? preContactCommands : null;
            }

            @Override
            protected List<CommandBase> createPostContactCommands(GroundStation gs, Satellite sat, List<PointingData> pointingData,
                    GroundStationDriverConfiguration configuration, Track trackCommand) {
                return TrackingSupportTest.this.gs.equals(gs) &&
                        TrackingSupportTest.this.sat1.equals(sat) &&
                        TrackingSupportTest.this.pointingData.equals(pointingData) &&
                        TrackingSupportTest.this.configuration.equals(configuration) &&
                        TrackingSupportTest.this.trackCommand == trackCommand
                        ? postContactCommands : null;
            }

            @Override
            protected boolean isTrackingPossible(LocationContactEvent locationContactEvent, GroundStation gs, Satellite sat) {
                return TrackingSupportTest.this.contact.equals(locationContactEvent) &&
                        TrackingSupportTest.this.gs.equals(gs) &&
                        TrackingSupportTest.this.sat1.equals(sat);
            }

            @Override
            public List<CommandBase> emergencyStop(Stop command) {
                return null;
            }

        };

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
        missingArguments.add(StandardArguments.SATELLITE_ID);

        exception = new RuntimeException("Mutchos problemos");

        inOrder = inOrder(configuration, command1, command2, command3, command4, command5, command6, gs, sat1, sat2, trackCommand, pd1, pd2, contact,
                dao, optimizer, calculator, azimuth, elevation, doppler);

        when(contact.getAzimuth()).thenReturn(azimuth);
        when(contact.getElevation()).thenReturn(elevation);
        when(contact.getDoppler()).thenReturn(doppler);
        when(azimuth.getStart()).thenReturn(AZIMUTH_START);
        when(azimuth.getEnd()).thenReturn(AZIMUTH_END);
        when(elevation.getMax()).thenReturn(ELEVATION_MAX);
        when(doppler.getStart()).thenReturn(DOPPLER_START);
        when(doppler.getEnd()).thenReturn(DOPPLER_END);

    }

    @Test
    public void testTrackMissingCommandArguments() {
        when(trackCommand.checkArguments()).thenReturn(missingArguments);
        assertEquals(TrackingSupport.NO_COMMANDS, trackingDevice.track(trackCommand));
        inOrder.verify(trackCommand, times(1)).checkArguments();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testTrackContactIsNull() {
        when(trackCommand.checkArguments()).thenReturn(Collections.<String> emptyList());
        when(trackCommand.getLocationContactEvent()).thenReturn(null);
        when(trackCommand.getSatellite()).thenReturn(sat1);
        assertEquals(TrackingSupport.NO_COMMANDS, trackingDevice.track(trackCommand));
        inOrder.verify(trackCommand, times(1)).checkArguments();
        inOrder.verify(trackCommand, times(1)).getLocationContactEvent();
        inOrder.verify(trackCommand, times(1)).getSatellite();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testTrackSatIsNull() {
        when(trackCommand.checkArguments()).thenReturn(Collections.<String> emptyList());
        when(trackCommand.getLocationContactEvent()).thenReturn(contact);
        when(trackCommand.getSatellite()).thenReturn(null);
        assertEquals(TrackingSupport.NO_COMMANDS, trackingDevice.track(trackCommand));
        inOrder.verify(trackCommand, times(1)).checkArguments();
        inOrder.verify(trackCommand, times(1)).getLocationContactEvent();
        inOrder.verify(trackCommand, times(1)).getSatellite();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testTrackFailedToResolveGroundStation() throws Exception {
        when(trackCommand.checkArguments()).thenReturn(Collections.<String> emptyList());
        when(trackCommand.getLocationContactEvent()).thenReturn(contact);
        when(trackCommand.getSatellite()).thenReturn(sat1);
        when(configuration.getGroundstationId()).thenReturn(GS_ID);
        when(dao.getById(GS_ID, GroundStation.class)).thenThrow(exception);
        assertEquals(TrackingSupport.NO_COMMANDS, trackingDevice.track(trackCommand));
        inOrder.verify(trackCommand, times(1)).checkArguments();
        inOrder.verify(trackCommand, times(1)).getLocationContactEvent();
        inOrder.verify(trackCommand, times(1)).getSatellite();
        inOrder.verify(configuration, times(1)).getGroundstationId();
        inOrder.verify(dao, times(1)).getById(GS_ID, GroundStation.class);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testTrackGroundStationNull() throws Exception {
        when(trackCommand.checkArguments()).thenReturn(Collections.<String> emptyList());
        when(trackCommand.getLocationContactEvent()).thenReturn(contact);
        when(trackCommand.getSatellite()).thenReturn(sat1);
        when(configuration.getGroundstationId()).thenReturn(GS_ID);
        when(dao.getById(GS_ID, GroundStation.class)).thenReturn(null);
        assertEquals(TrackingSupport.NO_COMMANDS, trackingDevice.track(trackCommand));
        inOrder.verify(trackCommand, times(1)).checkArguments();
        inOrder.verify(trackCommand, times(1)).getLocationContactEvent();
        inOrder.verify(trackCommand, times(1)).getSatellite();
        inOrder.verify(configuration, times(1)).getGroundstationId();
        inOrder.verify(dao, times(1)).getById(GS_ID, GroundStation.class);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testTrackOutdatedCommands() throws Exception {
        when(trackCommand.checkArguments()).thenReturn(Collections.<String> emptyList());
        when(trackCommand.getLocationContactEvent()).thenReturn(contact);
        when(trackCommand.getSatellite()).thenReturn(sat1);
        when(configuration.getGroundstationId()).thenReturn(GS_ID);
        when(dao.getById(GS_ID, GroundStation.class)).thenReturn(gs);
        when(contact.getStartTime()).thenReturn(NOW - 1);
        when(configuration.isSkipOutDatedCommands()).thenReturn(true);
        assertEquals(TrackingSupport.NO_COMMANDS, trackingDevice.track(trackCommand));
        inOrder.verify(trackCommand, times(1)).checkArguments();
        inOrder.verify(trackCommand, times(1)).getLocationContactEvent();
        inOrder.verify(trackCommand, times(1)).getSatellite();
        inOrder.verify(configuration, times(1)).getGroundstationId();
        inOrder.verify(dao, times(1)).getById(GS_ID, GroundStation.class);
        inOrder.verify(contact, times(1)).getStartTime();
        inOrder.verify(configuration, times(1)).isSkipOutDatedCommands();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testTrackNotPossible() throws Exception {
        when(trackCommand.checkArguments()).thenReturn(Collections.<String> emptyList());
        when(trackCommand.getLocationContactEvent()).thenReturn(contact);
        when(trackCommand.getSatellite()).thenReturn(sat2);
        when(configuration.getGroundstationId()).thenReturn(GS_ID);
        when(dao.getById(GS_ID, GroundStation.class)).thenReturn(gs);
        when(contact.getStartTime()).thenReturn(NOW + 1000L * 60 * 60);
        when(configuration.isSkipOutDatedCommands()).thenReturn(true);
        assertEquals(TrackingSupport.NO_COMMANDS, trackingDevice.track(trackCommand));
        inOrder.verify(trackCommand, times(1)).checkArguments();
        inOrder.verify(trackCommand, times(1)).getLocationContactEvent();
        inOrder.verify(trackCommand, times(1)).getSatellite();
        inOrder.verify(configuration, times(1)).getGroundstationId();
        inOrder.verify(dao, times(1)).getById(GS_ID, GroundStation.class);
        inOrder.verify(contact, times(1)).getStartTime();
        inOrder.verify(configuration, times(1)).isSkipOutDatedCommands();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testTrackPredictionException() throws Exception {
        when(trackCommand.checkArguments()).thenReturn(Collections.<String> emptyList());
        when(trackCommand.getLocationContactEvent()).thenReturn(contact);
        when(trackCommand.getSatellite()).thenReturn(sat1);
        when(configuration.getGroundstationId()).thenReturn(GS_ID);
        when(dao.getById(GS_ID, GroundStation.class)).thenReturn(gs);
        when(contact.getStartTime()).thenReturn(NOW + 1000L * 60 * 60);
        when(contact.getEndTime()).thenReturn(NOW + 1000L * 60 * 70);
        when(configuration.getCommandInterval()).thenReturn(STEP);
        when(calculator.calculateContactData(contact, gs, STEP)).thenThrow(exception);
        assertEquals(TrackingSupport.NO_COMMANDS, trackingDevice.track(trackCommand));
        inOrder.verify(trackCommand, times(1)).checkArguments();
        inOrder.verify(trackCommand, times(1)).getLocationContactEvent();
        inOrder.verify(trackCommand, times(1)).getSatellite();
        inOrder.verify(configuration, times(1)).getGroundstationId();
        inOrder.verify(dao, times(1)).getById(GS_ID, GroundStation.class);
        inOrder.verify(contact, times(1)).getStartTime();
        inOrder.verify(configuration, times(1)).isSkipOutDatedCommands();
        inOrder.verify(contact, times(1)).getAzimuth();
        inOrder.verify(contact, times(1)).getElevation();
        inOrder.verify(contact, times(1)).getDoppler();
        inOrder.verify(contact, times(1)).getEndTime();
        inOrder.verify(azimuth, times(1)).getStart();
        inOrder.verify(azimuth, times(1)).getEnd();
        inOrder.verify(elevation, times(1)).getMax();
        inOrder.verify(doppler, times(1)).getStart();
        inOrder.verify(doppler, times(1)).getEnd();
        inOrder.verify(configuration, times(1)).getCommandInterval();
        inOrder.verify(calculator, times(1)).calculateContactData(contact, gs, STEP);
        inOrder.verify(gs, times(1)).getGroundStationID();
        inOrder.verify(sat1, times(1)).getSatelliteID();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testTrack() throws Exception {
        when(trackCommand.checkArguments()).thenReturn(Collections.<String> emptyList());
        when(trackCommand.getLocationContactEvent()).thenReturn(contact);
        when(trackCommand.getSatellite()).thenReturn(sat1);
        when(configuration.getGroundstationId()).thenReturn(GS_ID);
        when(dao.getById(GS_ID, GroundStation.class)).thenReturn(gs);
        when(contact.getStartTime()).thenReturn(NOW + 1000L * 60 * 60);
        when(contact.getEndTime()).thenReturn(NOW + 1000L * 60 * 70);
        when(configuration.getCommandInterval()).thenReturn(STEP);
        when(calculator.calculateContactData(contact, gs, STEP)).thenReturn(pointingData);
        when(optimizer.optimize(pointingData, configuration)).thenReturn(pointingData);
        when(command1.getExecutionTime()).thenReturn(NOW + 1000L * 60 * 60);
        when(command6.getExecutionTime()).thenReturn(NOW + 1000L * 60 * 61);

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
        inOrder.verify(trackCommand, times(1)).getLocationContactEvent();
        inOrder.verify(trackCommand, times(1)).getSatellite();
        inOrder.verify(configuration, times(1)).getGroundstationId();
        inOrder.verify(dao, times(1)).getById(GS_ID, GroundStation.class);
        inOrder.verify(contact, times(1)).getStartTime();
        inOrder.verify(configuration, times(1)).isSkipOutDatedCommands();
        inOrder.verify(contact, times(1)).getAzimuth();
        inOrder.verify(contact, times(1)).getElevation();
        inOrder.verify(contact, times(1)).getDoppler();
        inOrder.verify(contact, times(1)).getEndTime();
        inOrder.verify(azimuth, times(1)).getStart();
        inOrder.verify(azimuth, times(1)).getEnd();
        inOrder.verify(elevation, times(1)).getMax();
        inOrder.verify(doppler, times(1)).getStart();
        inOrder.verify(doppler, times(1)).getEnd();
        inOrder.verify(configuration, times(1)).getCommandInterval();
        inOrder.verify(calculator, times(1)).calculateContactData(contact, gs, STEP);
        inOrder.verify(optimizer, times(1)).optimize(pointingData, configuration);
        inOrder.verify(command1, times(1)).getExecutionTime();
        inOrder.verify(command6, times(1)).getExecutionTime();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCreateContactCommands() {
        assertEquals(contactCommands, trackingDevice.createContactCommands(gs, sat1, pointingData, configuration, trackCommand));
    }

    @Test
    public void testIsTrackingPossible() {
        assertTrue(trackingDevice.isTrackingPossible(contact, gs, sat1));
    }

    @Test
    public void testValidateByTime() {
        assertFalse(trackingDevice.validateByTime(NOW - 1, NOW, true));
        assertTrue(trackingDevice.validateByTime(NOW - 1, NOW, false));
        assertTrue(trackingDevice.validateByTime(NOW + 1, NOW, true));
        assertTrue(trackingDevice.validateByTime(NOW + 1, NOW, false));
        assertTrue(trackingDevice.validateByTime(NOW, NOW, true));
        assertTrue(trackingDevice.validateByTime(NOW, NOW, false));
    }

    @Test
    public void testCreatePreContactCommands() {
        assertEquals(preContactCommands, trackingDevice.createPreContactCommands(gs, sat1, pointingData, configuration, trackCommand));
    }

    @Test
    public void testCreatePostContactCommandsOnMock() {
        assertEquals(postContactCommands, trackingDevice.createPostContactCommands(gs, sat1, pointingData, configuration, trackCommand));
    }

    @Test
    public void testDefaultImplementation() {
        trackingDevice = new TrackingSupport<GroundStationDriverConfiguration>(configuration, dao, calculator, optimizer) {

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
        assertEquals(TrackingSupport.NO_COMMANDS, trackingDevice.createPreContactCommands(gs, sat1, pointingData, configuration, trackCommand));
        assertEquals(TrackingSupport.NO_COMMANDS, trackingDevice.createPostContactCommands(gs, sat1, pointingData, configuration, trackCommand));
        assertTrue(trackingDevice.isTrackingPossible(contact, gs, sat1));
    }

    @Test
    public void testValidateArgument() {
        assertFalse(trackingDevice.validateArgument(null, "name"));
        assertTrue(trackingDevice.validateArgument(new Object(), "name"));
    }

    @Test
    public void testOptimizeNoOptimizer() {
        assertEquals(pointingData, trackingDevice.optimize(pointingData, configuration, null));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testOptimizeWithException() throws Exception {
        when(optimizer.optimize(pointingData, configuration)).thenThrow(exception);
        assertEquals(pointingData, trackingDevice.optimize(pointingData, configuration, optimizer));
        inOrder.verify(optimizer, times(1)).optimize(pointingData, configuration);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testOptimize() throws Exception {
        when(optimizer.optimize(pointingData, configuration)).thenReturn(optimizedPointingData);
        assertEquals(optimizedPointingData, trackingDevice.optimize(pointingData, configuration, optimizer));
        inOrder.verify(optimizer, times(1)).optimize(pointingData, configuration);
        inOrder.verifyNoMoreInteractions();
    }
}
