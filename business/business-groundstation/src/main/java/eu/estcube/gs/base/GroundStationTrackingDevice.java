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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IOrbitPrediction;
import org.hbird.business.core.StartablePart;
import org.hbird.business.core.util.Dates;
import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandBase;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.groundstation.ITrackingDevice;
import org.hbird.exchange.groundstation.Stop;
import org.hbird.exchange.groundstation.Track;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.PointingData;
import org.hbird.exchange.navigation.Satellite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.estcube.gs.configuration.GroundStationDriverConfiguration;

/**
 * Abstract base class for ground station tracking devices.
 */
public abstract class GroundStationTrackingDevice<C extends GroundStationDriverConfiguration> extends StartablePart implements ITrackingDevice {

    private static final long serialVersionUID = -654554148983566139L;

    private static final Logger LOG = LoggerFactory.getLogger(GroundStationTrackingDevice.class);

    protected static final List<CommandBase> NO_COMMANDS = Collections.emptyList();

    protected final C configuration;

    protected boolean failOldRequests;

    protected IDataAccess dataAccess;

    protected IOrbitPrediction orbitPrediction;

    protected IPointingDataOptimizer<C> optimizer;

    /**
     * @param name
     * @param description
     * @param driverName
     */
    public GroundStationTrackingDevice(String name, String description, String driverName, C configuration, IDataAccess dataAccess,
            IOrbitPrediction orbitPrediction, IPointingDataOptimizer<C> optimizer) {
        super(name, description, driverName);
        this.configuration = configuration;
        this.dataAccess = dataAccess;
        this.orbitPrediction = orbitPrediction;
        this.optimizer = optimizer;
    }

    /**
     * @return the configuration
     */
    public C getConfiguration() {
        return configuration;
    }

    /**
     * @return the failOldRequests
     */
    public boolean isFailOldRequests() {
        return failOldRequests;
    }

    /**
     * @param failOldRequests the failOldRequests to set
     */
    public void setFailOldRequests(boolean failOldRequests) {
        this.failOldRequests = failOldRequests;
    }

    /**
     * @see org.hbird.exchange.groundstation.ITrackingDevice#track(org.hbird.exchange.groundstation.Track)
     */
    @Override
    public List<CommandBase> track(Track command) {

        List<String> missing = command.checkArguments();
        if (!missing.isEmpty()) {
            LOG.error("Missing command arguments for command {} - {}; tracking not possible", command.getClass().getSimpleName(), missing);
            return NO_COMMANDS;
        }

        LocationContactEvent start = command.getArgumentValue(StandardArguments.START, LocationContactEvent.class);
        LocationContactEvent end = command.getArgumentValue(StandardArguments.END, LocationContactEvent.class);
        Satellite satellite = command.getArgumentValue(StandardArguments.SATELLITE, Satellite.class);

        if (!validateArgument(start, StandardArguments.START)) {
            return NO_COMMANDS;
        }
        if (!validateArgument(end, StandardArguments.END)) {
            return NO_COMMANDS;
        }
        if (!validateArgument(satellite, StandardArguments.SATELLITE)) {
            return NO_COMMANDS;
        }

        GroundStation groundStation = null;
        try {
            groundStation = (GroundStation) dataAccess.resolveNamed(getIsPartOf());
        }
        catch (Exception e) {
            LOG.error("Failed to resolve groundstation from ID {}", this.getIsPartOf(), e);
            return NO_COMMANDS;
        }

        if (groundStation == null) {
            LOG.error("GroundStation not found for ID {}", this.getIsPartOf());
            return NO_COMMANDS;
        }

        long now = System.currentTimeMillis();

        if (!validateByTime(start.getTimestamp(), now, failOldRequests)) {
            return NO_COMMANDS;
        }

        if (!isTrackingPossible(start, end, groundStation, satellite)) {
            return NO_COMMANDS;
        }

        List<PointingData> pointingData = null;
        try {
            pointingData = orbitPrediction.requestPointingDataFor(start, end, groundStation, satellite, configuration.getCommandInterval());
        }
        catch (Exception e) {
            LOG.error("Failed to calculate pointing data for the overpass; ground station: {}; satellite: {}; over pass start time: {}; Exception: ",
                    new Object[] {
                            groundStation.getGroundStationId(),
                            satellite.getSatelliteId(),
                            Dates.toIso8601DateFormat(start.getTimestamp()),
                            e
                    });
            return NO_COMMANDS;
        }

        pointingData = optimze(pointingData, configuration, optimizer);

        List<CommandBase> commands = new LinkedList<CommandBase>();
        commands.addAll(createPreContactCommands(groundStation, satellite, pointingData, configuration, command));
        commands.addAll(createContactCommands(groundStation, satellite, pointingData, configuration, command));
        commands.addAll(createPostContactCommands(groundStation, satellite, pointingData, configuration, command));

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
    protected boolean isTrackingPossible(LocationContactEvent start, LocationContactEvent end, GroundStation gs, Satellite satellite) {
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
            LOG.info("Start event timestamp ({}) is before current moment ({}) and failOldRequests is set to {}; skipping the overpass",
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
            LOG.error("Command argument {} is null; tracking not possible", name);
            return false;
        }
        return true;
    }

    protected List<PointingData> optimze(List<PointingData> pointingData, C configuration, IPointingDataOptimizer<C> optimizer) {
        if (optimizer == null) {
            return pointingData;
        }
        try {
            return optimizer.optimize(pointingData, configuration);
        }
        catch (Exception e) {
            LOG.error("Optimizre {} failed; using unoptimized pointing data for tracking; {}", optimizer.getClass().getName(), e);
            return pointingData;
        }
    }

    /**
     * @see org.hbird.business.core.StartablePart#createCommandList(java.util.List)
     */
    @Override
    protected List<Command> createCommandList(List<Command> commands) {
        commands = super.createCommandList(commands);
        commands.add(new Track("", "", null, null, null));
        commands.add(new Stop(""));
        return commands;
    }

    /**
     * @see org.hbird.business.core.StartablePart#getHeartbeat()
     */
    @Override
    public long getHeartbeat() {
        return configuration.getHeartBeatInterval();
    }

    /**
     * @see org.hbird.business.core.StartablePart#setHeartbeat(long)
     */
    @Override
    public void setHeartbeat(long heartbeat) {
        configuration.setHeartBeatInterval(heartbeat);
    }
}
