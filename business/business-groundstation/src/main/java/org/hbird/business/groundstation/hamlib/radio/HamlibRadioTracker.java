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
package org.hbird.business.groundstation.hamlib.radio;

import java.util.ArrayList;
import java.util.List;

import org.hbird.business.api.ICatalogue;
import org.hbird.business.api.IOrbitPrediction;
import org.hbird.business.groundstation.base.TrackingSupport;
import org.hbird.business.groundstation.configuration.RadioDriverConfiguration;
import org.hbird.business.groundstation.hamlib.HamlibNativeCommand;
import org.hbird.business.groundstation.hamlib.radio.protocol.SetFrequency;
import org.hbird.business.groundstation.hamlib.radio.protocol.SetVfo;
import org.hbird.exchange.core.CommandBase;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.groundstation.IPointingDataOptimizer;
import org.hbird.exchange.groundstation.Stop;
import org.hbird.exchange.groundstation.Track;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.PointingData;
import org.hbird.exchange.navigation.Satellite;

/**
 *
 */
public class HamlibRadioTracker extends TrackingSupport<RadioDriverConfiguration> {

    public HamlibRadioTracker(RadioDriverConfiguration configuration, ICatalogue catalogue, IOrbitPrediction prediction,
            IPointingDataOptimizer<RadioDriverConfiguration> optimizer) {
        super(configuration, catalogue, prediction, optimizer);
    }

    /**
     * Creates set of {@link HamlibNativeCommand}s for the radio device.
     * 
     * For each pointing data entry 4 commands has to be created:
     * <ol>
     * <li><tt>V Main\n</tt></li>
     * <li><tt>F ${uplink.frequency}\n</tt></li>
     * <li><tt>V Sub\n</tt></li>
     * <li><tt>F ${downlink.frequency}\n</tt></li>
     * </ol>
     * 
     * 
     * @param satellite {@link Satellite} to communicate with
     * @param pointingData {@link PointingData} entry calculated by navigation component (Orekit)
     * @param track {@link Track} command issued by ?
     * @param stage contact stage
     * @param delta delta time between individual commands
     * @return {@link List} of generated {@link HamlibNativeCommand}s
     */
    List<CommandBase> createCommands(RadioDriverConfiguration config, Satellite satellite, PointingData pointingData, Track track, String stage, long delta) {
        List<CommandBase> commands = new ArrayList<CommandBase>(4);
        long timestamp = pointingData.getTimestamp();
        double doppler = pointingData.getDoppler();
        String derivedFrom = track.getID();
        commands.add(new HamlibNativeCommand(SetVfo.createCommand(config.getUplinkVfo()), timestamp, derivedFrom, stage));
        commands.add(new HamlibNativeCommand(SetFrequency.createCommand(satellite.getUplinkFrequency(), doppler), timestamp + 1 * delta, derivedFrom, stage));
        commands.add(new HamlibNativeCommand(SetVfo.createCommand(config.getDownlinkVfo()), timestamp + 2 * delta, derivedFrom, stage));
        commands.add(new HamlibNativeCommand(SetFrequency.createCommand(satellite.getDownlinkFrequency(), doppler), timestamp + 3 * delta, derivedFrom, stage));
        return commands;
    }

    /**
     * @see org.hbird.exchange.groundstation.ITrackingDevice#emergencyStop(org.hbird.exchange.groundstation.Stop)
     */
    @Override
    public List<CommandBase> emergencyStop(Stop command) {
        return NO_COMMANDS;
    }

    @Override
    protected List<CommandBase> createContactCommands(GroundStation gs, Satellite sat, List<PointingData> pointingData, RadioDriverConfiguration configuration,
            Track trackCommand) {

        List<CommandBase> commands = new ArrayList<CommandBase>((pointingData.size() - 1) * 4);
        long delayInGroup = configuration.getDelayInCommandGroup();
        for (int index = 1; index < pointingData.size(); index++) {
            commands.addAll(createCommands(configuration, sat, pointingData.get(index), trackCommand, HamlibNativeCommand.STAGE_TRACKING,
                    delayInGroup));
        }
        return commands;
    }

    @Override
    protected boolean isTrackingPossible(LocationContactEvent locationContactEvent, GroundStation gs, Satellite satellite) {
        // TODO - 16.04.2013, kimmell - check if minFrequency and max frequency are matching with the satellite
        // frequencies
        // TODO - 16.04.2013, kimmell - check if uplink is supported
        // TODO - 16.04.2013, kimmell - check if downlink is supported
        return super.isTrackingPossible(locationContactEvent, gs, satellite);
    }

    @Override
    protected List<CommandBase> createPreContactCommands(GroundStation gs, Satellite sat, List<PointingData> pointingData,
            RadioDriverConfiguration configuration, Track trackCommand) {

        return createCommands(configuration, sat, pointingData.get(0), trackCommand, HamlibNativeCommand.STAGE_PRE_TRACKING,
                configuration.getDelayInCommandGroup());
    }
}
