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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IOrbitDataCalculator;
import org.hbird.business.groundstation.configuration.GroundStationDriverConfiguration;
import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.CommandBase;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.groundstation.IPointingDataOptimizer;
import org.hbird.exchange.groundstation.ITrackingDevice;
import org.hbird.exchange.groundstation.Track;
import org.hbird.exchange.navigation.ContactParameterRange;
import org.hbird.exchange.navigation.ExtendedContactParameterRange;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.PointingData;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.util.Dates;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public abstract class TrackingSupport<C extends GroundStationDriverConfiguration> implements ITrackingDevice {

    private static final Logger LOG = LoggerFactory.getLogger(TrackingSupport.class);

    protected static final List<CommandBase> NO_COMMANDS = Collections.emptyList();

    protected final C configuration;

    protected final IDataAccess dao;

    protected final IPointingDataOptimizer<C> optimizer;

    protected IOrbitDataCalculator orbitDataCalculator;

    /**
     * @param configuration
     * @param failOldRequests
     * @param dataAccess
     * @param orbitPrediction
     * @param optimizer
     */
    public TrackingSupport(C configuration, IDataAccess dao, IOrbitDataCalculator orbitDataCalculator, IPointingDataOptimizer<C> optimizer) {
        this.configuration = configuration;
        this.dao = dao;
        this.optimizer = optimizer;
        this.orbitDataCalculator = orbitDataCalculator;
    }

    /**
     * @see org.hbird.exchange.groundstation.ITrackingDevice#track(org.hbird.exchange.groundstation.Track)
     */
    @Override
    public List<CommandBase> track(Track command) {
        LOG.debug("Creating tracking commands ...");
        List<String> missing = command.checkArguments();
        if (!missing.isEmpty()) {
            LOG.error("Missing command arguments for the command '{}' - '{}'; tracking not possible", command.getClass().getSimpleName(), missing);
            return NO_COMMANDS;
        }

        LocationContactEvent contact = command.getLocationContactEvent();
        Satellite satellite = command.getSatellite();

        if (!validateArgument(contact, StandardArguments.START)) {
            return NO_COMMANDS;
        }
        if (!validateArgument(satellite, StandardArguments.SATELLITE_ID)) {
            return NO_COMMANDS;
        }

        String groundStationId = configuration.getGroundstationId();
        GroundStation groundStation = null;
        try {
            groundStation = dao.getById(groundStationId, GroundStation.class);
        }
        catch (Exception e) {
            LOG.error("Failed to resolve groundstation for the ID '{}'", groundStationId, e);
            return NO_COMMANDS;
        }

        if (groundStation == null) {
            LOG.error("GroundStation not found for the ID '{}'", groundStationId);
            return NO_COMMANDS;
        }

        long now = System.currentTimeMillis();
        long contactStartTime = contact.getStartTime();

        if (!validateByTime(contactStartTime, now, configuration.isSkipOutDatedCommands())) {
            return NO_COMMANDS;
        }

        if (!isTrackingPossible(contact, groundStation, satellite)) {
            return NO_COMMANDS;
        }

        ContactParameterRange azimuth = contact.getAzimuth();
        ExtendedContactParameterRange elevation = contact.getElevation();
        ContactParameterRange doppler = contact.getDoppler();
        LOG.info("Creating tracking commands for the satellite '{}' in the ground station '{}'", satellite, groundStation);
        LOG.info("   Contact - start: {}; end: {}", Dates.toDefaultDateFormat(contactStartTime), Dates.toDefaultDateFormat(contact.getEndTime()));
        LOG.info("   Azimuth - start: {}; end: {}", azimuth.getStart(), azimuth.getEnd());
        LOG.info("   Elevation - max: {}", elevation.getMax());
        LOG.info("   Doppler - start: {}; end: {}", doppler.getStart(), doppler.getEnd());

        List<PointingData> pointingData = null;
        try {
            pointingData = orbitDataCalculator.calculateContactData(contact, groundStation, false, configuration.getCommandInterval());
        }
        catch (Exception e) {
            LOG.error("Failed to calculate pointing data for the contact; ground station: '{}'; satellite: '{}'; contact start time: '{}'; Exception: ",
                    new Object[] {
                            groundStation.getGroundStationID(),
                            satellite.getSatelliteID(),
                            Dates.toIso8601DateFormat(contactStartTime),
                            e
                    });
            return NO_COMMANDS;
        }

        LOG.debug("Optimizing pointing data");
        pointingData = optimize(pointingData, configuration, optimizer);
        LOG.debug("Pointing data optimized");

        List<CommandBase> commands = new LinkedList<CommandBase>();
        commands.addAll(createPreContactCommands(groundStation, satellite, pointingData, configuration, command));
        commands.addAll(createContactCommands(groundStation, satellite, pointingData, configuration, command));
        commands.addAll(createPostContactCommands(groundStation, satellite, pointingData, configuration, command));

        int size = commands.size();
        String firstCommand = new DateTime(commands.get(0).getExecutionTime()).toString(ISODateTimeFormat.dateTime());
        String lastCommand = new DateTime(commands.get(size - 1).getExecutionTime()).toString(ISODateTimeFormat.dateTime());
        LOG.info("Created {} tracking commands for the satellite '{}' in groundstation '{}'; first command at {}; last command at {}", new Object[] { size,
                satellite, groundStation, firstCommand, lastCommand });

        return commands;
    }

    protected abstract List<CommandBase> createContactCommands(GroundStation gs, Satellite sat, List<PointingData> pointingData, C configuration,
            Track trackCommand);

    /**
     * Check if tracking is possible for the device.
     * 
     * By default returns true. Override for device specific checks.
     * 
     * @param start start of the contact
     * @param end end of the contact
     * @param gs {@link GroundStation}
     * @param satellite {@link Satellite}
     * @return true if tracking is possible
     */
    protected boolean isTrackingPossible(LocationContactEvent contactLocationEvent, GroundStation gs, Satellite satellite) {
        return true;
    }

    /**
     * Validates contact start event using current time stamp and value of failOldRequest flag.
     * 
     * @param commandTimestamp
     * @param now
     * @param failOldRequests
     * @return
     */
    protected boolean validateByTime(long commandTimestamp, long now, boolean failOldRequests) {
        if (commandTimestamp < now && failOldRequests) {
            LOG.info("Start event timestamp ({}) is before current moment ({}) and failOldRequests is set to {}; skipping the contact",
                    new Object[] { Dates.toIso8601DateFormat(commandTimestamp), Dates.toIso8601DateFormat(now), failOldRequests });
            return false;
        }
        return true;
    }

    /**
     * Create setup commands to execute before the contact.
     * 
     * Setup command for the device. By default returns empty list. Override for device if needed.
     * 
     * @param gs
     * @param sat
     * @param pointingData
     * @return
     */
    protected List<CommandBase> createPreContactCommands(GroundStation gs, Satellite sat, List<PointingData> pointingData, C configuration, Track trackCommand) {
        return NO_COMMANDS;
    }

    protected List<CommandBase> createPostContactCommands(GroundStation gs, Satellite sat, List<PointingData> pointingData, C configuration, Track trackCommand) {
        return NO_COMMANDS;
    }

    protected boolean validateArgument(Object arg, String name) {
        if (arg == null) {
            LOG.error("Command argument '{}' is null; tracking not possible", name);
            return false;
        }
        return true;
    }

    protected List<PointingData> optimize(List<PointingData> pointingData, C configuration, IPointingDataOptimizer<C> optimizer) {
        if (optimizer == null) {
            return pointingData;
        }
        try {
            return optimizer.optimize(pointingData, configuration);
        }
        catch (Exception e) {
            LOG.error("Optimizre '{}' failed; using unoptimized pointing data for tracking", optimizer.getClass().getName(), e);
            return pointingData;
        }
    }
}
