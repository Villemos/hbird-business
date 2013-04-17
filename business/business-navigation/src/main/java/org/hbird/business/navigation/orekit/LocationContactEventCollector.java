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
package org.hbird.business.navigation.orekit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.IPublish;
import org.hbird.exchange.constants.StandardComponents;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.orekit.errors.OrekitException;
import org.orekit.frames.TopocentricFrame;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.events.ElevationDetector;
import org.orekit.time.TimeScalesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This camel route processor will receive callbacks from the orekit library
 * notifying of events such as the establishment / loss of contact. The processor
 * will create the corresponding OrbitalEvent and send it to the consumer.
 */
public class LocationContactEventCollector extends ElevationDetector {

    /** The unique UID */
    private static final long serialVersionUID = 801203905525890103L;

    private static final Logger LOG = LoggerFactory.getLogger(LocationContactEventCollector.class);
    
    /** The ground station / location that comes into contact. */
    protected String groundStation = null;

    /** The name of the antenna. */
    protected String antenna = null;
    
    /** The satellite. */
    protected String satellite = null;

    /** FIXME I don't know what this does but OREKIT needs it... */
    public static final double maxcheck = 1.;

    protected IPublish api = null;

    protected boolean publish = false;

    protected TleOrbitalParameters parameters = null;

    protected List<Named> events = new ArrayList<Named>();

    /**
     * COnstructor of an injector of location contact events.
     * 
     * @param elevation The degrees above the horizon that the satellite must be to be visible from this location.
     * @param topo The topocentric framework used.
     * @param satellite The satellite whose orbit we are predicting.
     * @param location The location to which contact has been established / lost if this event occurs.
     * @param contactDataStepSize
     */
    public LocationContactEventCollector(double elevation, TopocentricFrame topo, String satellite, String location, String antenna, TleOrbitalParameters parameters,
            boolean publish) {
        super(maxcheck, elevation, topo);

        this.publish = publish;
        if (publish) {
        	// TODO set proper name
            api = ApiFactory.getPublishApi("");
        }

        this.satellite = satellite;
        this.groundStation = location;
        this.antenna = antenna;
        this.parameters = parameters;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.orekit.propagation.events.ElevationDetector#eventOccurred(org.orekit.propagation.SpacecraftState,
     * boolean)
     */
    @Override
    public int eventOccurred(final SpacecraftState state, final boolean increasing) throws OrekitException {

    	long atTime = state.getDate().toDate(TimeScalesFactory.getUTC()).getTime();
        LocationContactEvent event = new LocationContactEvent(StandardComponents.ORBIT_PROPAGATOR_NAME, atTime, groundStation, antenna, satellite, increasing, NavigationUtilities.toOrbitalState(state, parameters)); 
        events.add(event);

        /** If stream mode, then deliver the data as a stream. */
        if (publish) {
        	LOG.info("Injecting " + (increasing == true? "START" : "END") + " contact event at '" + atTime + " (" + (new Date(atTime)).toLocaleString() + ")' for satellite '" + satellite + "' and groundstation '" + groundStation + "'");
            api.publish(event);
        }

        /** Continue listening for events. */
        return CONTINUE;
    }

    public List<Named> getDataSet() {
        return events;
    }
}
