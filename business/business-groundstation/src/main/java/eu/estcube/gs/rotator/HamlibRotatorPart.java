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
import java.util.Date;
import java.util.List;

import org.hbird.business.navigation.NavigationUtilities;
import org.hbird.exchange.groundstation.Antenna;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.groundstation.NativeCommand;
import org.hbird.exchange.groundstation.Rotator;
import org.hbird.exchange.groundstation.Track;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.PointingData;
import org.hbird.exchange.navigation.Satellite;
import org.orekit.errors.OrekitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.estcube.gs.base.HamlibNativeCommand;
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
public class HamlibRotatorPart extends Rotator {

    /**
     * 
     */
    private static final long serialVersionUID = -4198169440814503234L;
    
    private static final Logger LOG = LoggerFactory.getLogger(HamlibRotatorPart.class);

    /**
     * @param thresholdElevation
     * @param minAzimuth
     * @param maxAzimuth
     * @param minElevation
     * @param maxElevation
     */
    public HamlibRotatorPart(String name, int thresholdElevation, int minAzimuth, int maxAzimuth, int minElevation, int maxElevation) {
        super(name, thresholdElevation, minAzimuth, maxAzimuth, minElevation, maxElevation, HamlibRotatorDriver.class.getName());
    }

    protected Antenna antenna = null;

    protected long preDelta = 10000;
    
    protected long postDelta = 10000;

    protected boolean failOldRequests = true;

    protected List<NativeCommand> commands = null;
    
    /* (non-Javadoc)
     * @see org.hbird.exchange.navigation.ICommandableAntennaPart#parse(org.hbird.exchange.navigation.LocationContactEvent, org.hbird.exchange.navigation.LocationContactEvent, java.util.List)
     */
    @Override
    public List<NativeCommand> track(Track command) {
        commands = new ArrayList<NativeCommand>();

        LocationContactEvent start = command.getArgumentValue("start", LocationContactEvent.class);
        LocationContactEvent end = command.getArgumentValue("end", LocationContactEvent.class);
        Satellite satellite = command.getArgumentValue("satellite", Satellite.class);

        if (start.getTimestamp() < (new Date()).getTime() && failOldRequests == true) {
            return commands;
        }   
        
        /** Generate the pointing information, including azimuth, elevation and doppler. */
        List<PointingData> pointingData = null;
        try {
            pointingData = NavigationUtilities.calculateContactData(start, end, (GroundStation) antenna.getIsPartOf(), satellite, 500);
        } catch (OrekitException e) {
            e.printStackTrace();
        }

        /** Pre tracking preparation. */
        LOG.info("First nativecommand for part 'Radio' derived from 'Track' command will execute at '" + (start.getTimestamp() - preDelta) + " (" + (new Date(start.getTimestamp() - preDelta)).toLocaleString() + ")'.");
        commands.add(new HamlibNativeCommand(Reset.createMessageString(1).toString(), start.getTimestamp() - preDelta, command.getUuid(), "PreTracking"));
        // commands.add(new NativeCommand(SetConfig.createMessageString("Test", 1l).toString(), start.getTimestamp() - preDelta + 1, command.getUuid(), "PreTracking"));
        commands.add(new HamlibNativeCommand(SetPosition.createMessageString(pointingData.get(0).getAzimuth(), pointingData.get(0).getElevation()).toString(), start.getTimestamp() - preDelta + 2, command.getUuid(), "PreTracking"));

        /** For each point, except the first which has been set in the preParse function, create a native pointing command. */
        for (int index = 1; index < pointingData.size(); index++) {
            commands.add(new HamlibNativeCommand(SetPosition.createMessageString(pointingData.get(index).getAzimuth(), pointingData.get(index).getElevation()).toString(), pointingData.get(index).getTimestamp(), command.getUuid(), "Tracking"));
        }
        
        /** Park the antenna. */
        commands.add(new HamlibNativeCommand(Park.createMessageString().toString(), start.getTimestamp() + postDelta, command.getUuid(), "PostTracking"));        

        return commands;
    }
       
    /* (non-Javadoc)
     * @see org.hbird.exchange.navigation.ICommandableAntennaPart#stop()
     */
    @Override
    public List<NativeCommand> stop() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.hbird.exchange.navigation.ICommandableAntennaPart#park()
     */
    @Override
    public List<NativeCommand> park() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.hbird.exchange.navigation.ICommandableAntennaPart#pointTo()
     */
    @Override
    public List<NativeCommand> pointTo() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isFailOldRequests() {
        return failOldRequests;
    }

    public void setFailOldRequests(boolean failOldRequests) {
        this.failOldRequests = failOldRequests;
    }
    
    
}
