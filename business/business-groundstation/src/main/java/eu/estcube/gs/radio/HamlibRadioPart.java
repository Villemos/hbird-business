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
package eu.estcube.gs.radio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hbird.business.navigation.orekit.NavigationUtilities;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.groundstation.NativeCommand;
import org.hbird.exchange.groundstation.Track;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.PointingData;
import org.hbird.exchange.navigation.Satellite;
import org.orekit.errors.OrekitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.estcube.gs.base.HamlibDriver;
import eu.estcube.gs.base.HamlibNativeCommand;
import eu.estcube.gs.base.RadioDevice;
import eu.estcube.gs.radio.nativecommands.SetFrequency;

/**
 * 
 * 
 * @author Gert Villemos
 */
public class HamlibRadioPart extends RadioDevice {

    /**
     * 
     */
    private static final long serialVersionUID = 6349745253601876608L;

    private static final Logger LOG = LoggerFactory.getLogger(HamlibRadioPart.class);

    /**
     * @param minFrequency
     * @param maxFrequency
     * @param isUplink
     * @param isDownlink
     * @param gain
     */
    public HamlibRadioPart(String ID, String name, long minFrequency, long maxFrequency, boolean isUplink, boolean isDownlink, long gain, long port, String host) {
        super(ID, name, minFrequency, maxFrequency, isUplink, isDownlink, gain, HamlibDriver.class.getName());
        this.port = port;
        this.host = host;
    }

    protected long preDelta = 10000;
    
    protected long postDelta = 10000;

    protected boolean failOldRequests = true;

    protected long port;
    protected String host;
    
    /* (non-Javadoc)
     * @see org.hbird.exchange.navigation.ICommandableAntennaPart#parse(org.hbird.exchange.navigation.LocationContactEvent, org.hbird.exchange.navigation.LocationContactEvent, java.util.List)
     */
    @Override
    public List<NativeCommand> track(Track command) {
        List<NativeCommand> commands = new ArrayList<NativeCommand>();

        LocationContactEvent start = command.getArgumentValue("start", LocationContactEvent.class);
        LocationContactEvent end = command.getArgumentValue("end", LocationContactEvent.class);
        Satellite satellite = command.getArgumentValue("satellite", Satellite.class);

        if (start.getTimestamp() < (new Date()).getTime() && failOldRequests == true) {
            return commands;
        }
        
        /** Generate the pointing information, including azimuth, elevation and doppler. */
        List<PointingData> pointingData = null;
        try {
            pointingData = NavigationUtilities.calculateContactData(start, end, (GroundStation) parent.getIsPartOf(), satellite, 500);
        } catch (OrekitException e) {
            e.printStackTrace();
        }
        
        /** Set initial frequency. */       
        LOG.info("First nativecommand for part 'Radio' derived from 'Track' command will execute at '" + (start.getTimestamp() - preDelta) + " (" + (new Date(start.getTimestamp() - preDelta)).toLocaleString() + ")'.");
        commands.add(new HamlibNativeCommand(SetFrequency.createMessageString(satellite.getFrequency() * pointingData.get(0).getDoppler()).toString(), start.getTimestamp() - preDelta, command.getID(), "PreTracking"));

        /** For each point, except the first which has been set in the preParse function, create a native pointing command. */
        for (int index = 1; index < pointingData.size(); index++) {
            commands.add(new HamlibNativeCommand(SetFrequency.createMessageString(satellite.getFrequency() * pointingData.get(index).getDoppler()).toString(), pointingData.get(index).getTimestamp(), command.getID(), "Tracking"));
        }
        
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

	public long getPort() {
		return port;
	}

	public void setPort(long port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
}
