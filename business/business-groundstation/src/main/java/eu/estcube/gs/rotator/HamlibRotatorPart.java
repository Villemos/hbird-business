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
package eu.estcube.gs.rotator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IOrbitPrediction;
import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.CommandBase;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.groundstation.Stop;
import org.hbird.exchange.groundstation.Track;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.PointingData;
import org.hbird.exchange.navigation.Satellite;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.estcube.gs.base.GroundStationTrackingDevice;
import eu.estcube.gs.base.HamlibNativeCommand;
import eu.estcube.gs.base.IPointingDataOptimizer;
import eu.estcube.gs.configuration.RotatorDriverConfiguration;
import eu.estcube.gs.rotator.nativecommands.Park;
import eu.estcube.gs.rotator.nativecommands.Reset;
import eu.estcube.gs.rotator.nativecommands.SetPosition;

/**
 * Class defining a HAMLIB rotator.
 * 
 * This class implements the methods required to schedule a HAMLIB rotator for
 * the parse of a satellite.
 * 
 * @author Gert Villemos
 * 
 */
public class HamlibRotatorPart extends GroundStationTrackingDevice<RotatorDriverConfiguration> {

    private static final long serialVersionUID = -6021178723974020064L;

    public static final String DESCRIPTION = "A rotator of an antenna.";

    private static final Logger LOG = LoggerFactory.getLogger(HamlibRotatorPart.class);

    public HamlibRotatorPart(String name, RotatorDriverConfiguration configuration, IDataAccess dataAccess, IOrbitPrediction prediction,
            IPointingDataOptimizer<RotatorDriverConfiguration> optimizer) {
        super(name, DESCRIPTION, HamlibRotatorDriver.class.getName(), configuration, dataAccess, prediction, optimizer);
    }

    /**
     * @see org.hbird.exchange.groundstation.ITrackingDevice#emergencyStop(org.hbird.exchange.groundstation.Stop)
     */
    @Override
    public List<CommandBase> emergencyStop(Stop command) {
        // TODO - 23.04.2013, kimmell - implement this
        return NO_COMMANDS;
    }

    /**
     * @see eu.estcube.gs.base.GroundStationTrackingDevice#createContactCommands(org.hbird.exchange.groundstation.GroundStation
     *      , org.hbird.exchange.navigation.Satellite, java.util.List,
     *      eu.estcube.gs.configuration.GroundStationDriverConfiguration)
     */
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

    /**
     * @see eu.estcube.gs.base.GroundStationTrackingDevice#createPreContactCommands(org.hbird.exchange.groundstation.
     *      GroundStation, org.hbird.exchange.navigation.Satellite, java.util.List,
     *      eu.estcube.gs.configuration.GroundStationDriverConfiguration, org.hbird.exchange.groundstation.Track)
     */
    @Override
    protected List<CommandBase> createPreContactCommands(GroundStation gs, Satellite sat, List<PointingData> pointingData,
            RotatorDriverConfiguration configuration, Track trackCommand) {

        String derivedFrom = trackCommand.getID();
        long firstCommandTime = trackCommand.getArgumentValue(StandardArguments.START, LocationContactEvent.class).getTimestamp()
                - configuration.getPreContactDelta();
        String firstCommand = new DateTime(firstCommandTime).toString(ISODateTimeFormat.dateTime());
        LOG.info("First nativecommand for part '{}' derived from 'Track' command will execute at '{} ({})'.",
                new Object[] { getName(), firstCommandTime, firstCommand });

        PointingData pd = pointingData.get(0);
        CommandBase reset = new HamlibNativeCommand(Reset.ALL, firstCommandTime, derivedFrom, HamlibNativeCommand.STAGE_PRE_TRACKING);
        CommandBase toStartPositon = new HamlibNativeCommand(SetPosition.createCommand(pd.getAzimuth(), pd.getElevation()), firstCommandTime
                + configuration.getDelayInCommandGroup(), derivedFrom,
                HamlibNativeCommand.STAGE_PRE_TRACKING);

        return Arrays.asList(reset, toStartPositon);
    }

    /**
     * @see eu.estcube.gs.base.GroundStationTrackingDevice#createPostContactCommands(org.hbird.exchange.groundstation.
     *      GroundStation, org.hbird.exchange.navigation.Satellite, java.util.List,
     *      eu.estcube.gs.configuration.GroundStationDriverConfiguration, org.hbird.exchange.groundstation.Track)
     */
    @Override
    protected List<CommandBase> createPostContactCommands(GroundStation gs, Satellite sat, List<PointingData> pointingData,
            RotatorDriverConfiguration configuration, Track trackCommand) {

        LocationContactEvent end = trackCommand.getArgumentValue(StandardArguments.END, LocationContactEvent.class);
        CommandBase cmd = new HamlibNativeCommand(Park.COMMAND, end.getTimestamp() + configuration.getPostContactDelta(), trackCommand.getID(),
                HamlibNativeCommand.STAGE_POST_TRACKING);
        return Arrays.asList(cmd);
    }

}
