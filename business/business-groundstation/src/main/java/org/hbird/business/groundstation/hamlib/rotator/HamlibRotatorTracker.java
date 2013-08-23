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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IOrbitDataCalculator;
import org.hbird.business.groundstation.base.TrackingSupport;
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
import org.hbird.exchange.navigation.PointingData;
import org.hbird.exchange.navigation.Satellite;

/**
 *
 */
public class HamlibRotatorTracker extends TrackingSupport<RotatorDriverConfiguration> {

    public HamlibRotatorTracker(RotatorDriverConfiguration config, IDataAccess dao, IOrbitDataCalculator calculator,
            IPointingDataOptimizer<RotatorDriverConfiguration> optimizer) {
        super(config, dao, calculator, optimizer);
    }

    /**
     * @see org.hbird.exchange.groundstation.ITrackingDevice#emergencyStop(org.hbird.exchange.groundstation.Stop)
     */
    @Override
    public List<CommandBase> emergencyStop(Stop command) {
        // TODO - 23.04.2013, kimmell - implement this
        return NO_COMMANDS;
    }

    @Override
    protected List<CommandBase> createContactCommands(GroundStation gs, Satellite sat, List<PointingData> pointingData,
            RotatorDriverConfiguration configuration, Track trackCommand) {
        List<CommandBase> commands = new ArrayList<CommandBase>(pointingData.size());
        String derivedFrom = trackCommand.getID();
        for (int index = 1; index < pointingData.size(); index++) {
            PointingData pd = pointingData.get(index);
            commands.add(new HamlibNativeCommand(SetPosition.createCommand(pd.getAzimuth(), pd.getElevation()), pd.getTimestamp(), derivedFrom,
                    HamlibNativeCommand.STAGE_TRACKING));
        }
        return commands;
    }

    @Override
    protected List<CommandBase> createPreContactCommands(GroundStation gs, Satellite sat, List<PointingData> pointingData,
            RotatorDriverConfiguration configuration, Track trackCommand) {

        String derivedFrom = trackCommand.getID();
        long firstCommandTime = trackCommand.getLocationContactEvent().getStartTime()
                - configuration.getPreContactDelta();

        PointingData pd = pointingData.get(0);
        CommandBase reset = new HamlibNativeCommand(Reset.ALL, firstCommandTime, derivedFrom, HamlibNativeCommand.STAGE_PRE_TRACKING);
        CommandBase toStartPositon = new HamlibNativeCommand(SetPosition.createCommand(pd.getAzimuth(), pd.getElevation()), firstCommandTime
                + configuration.getDelayInCommandGroup(), derivedFrom,
                HamlibNativeCommand.STAGE_PRE_TRACKING);

        return Arrays.asList(reset, toStartPositon);
    }

    @Override
    protected List<CommandBase> createPostContactCommands(GroundStation gs, Satellite sat, List<PointingData> pointingData,
            RotatorDriverConfiguration configuration, Track trackCommand) {

        long end = trackCommand.getLocationContactEvent().getEndTime();
        CommandBase cmd = new HamlibNativeCommand(Park.COMMAND, end + configuration.getPostContactDelta(), trackCommand.getID(),
                HamlibNativeCommand.STAGE_POST_TRACKING);
        return Arrays.asList(cmd);
    }
}
