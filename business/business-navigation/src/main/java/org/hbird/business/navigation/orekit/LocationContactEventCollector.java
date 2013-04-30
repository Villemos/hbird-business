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
import java.util.List;

import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.IPublish;
import org.hbird.business.navigation.NavigationComponent;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.orekit.errors.OrekitException;
import org.orekit.frames.TopocentricFrame;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.events.ElevationDetector;
import org.orekit.time.TimeScalesFactory;
import org.orekit.tle.TLE;
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
    protected final String groundStationId;

    /** The satellite. */
    protected final String satelliteId;

    /** FIXME I don't know what this does but OREKIT needs it... */
    public static final double maxcheck = 10.0D; // calculation step in seconds?

    protected IPublish api;

    protected boolean publish;

    protected TleOrbitalParameters tleParameters;

    protected List<EntityInstance> events = new ArrayList<EntityInstance>();

    protected SpacecraftState lastStartState;

    /**
     * COnstructor of an injector of location contact events.
     * 
     * @param elevation The degrees above the horizon that the satellite must be to be visible from this location.
     * @param topo The topocentric framework used.
     * @param satellite The satellite whose orbit we are predicting.
     * @param location The location to which contact has been established / lost if this event occurs.
     * @param contactDataStepSize
     */
    public LocationContactEventCollector(double elevation, TopocentricFrame topo, String satelliteId, String groundStationId, TleOrbitalParameters parameters,
            boolean publish) {
        super(maxcheck, elevation, topo);
        this.publish = publish;
        this.satelliteId = satelliteId;
        this.groundStationId = groundStationId;
        this.tleParameters = parameters;
    }

    /**
     * @see org.orekit.propagation.events.ElevationDetector#eventOccurred(org.orekit.propagation.SpacecraftState,
     *      boolean)
     */
    @Override
    public int eventOccurred(final SpacecraftState state, final boolean increasing) throws OrekitException {

        if (increasing) {
            // we have new start state; tore it
            lastStartState = state;
        }
        else if (!increasing && lastStartState != null) {
            // we have both - start & end; create new LocationContactEvent
            LocationContactEvent event = createEvent(groundStationId, satelliteId, tleParameters, lastStartState, state);
            events.add(event);
            if (publish) {
                publish(event);
            }
            lastStartState = null;
        }

        // Continue listening for events.
        return CONTINUE;
    }

    public List<EntityInstance> getDataSet() {
        return events;
    }

    LocationContactEvent createEvent(String groundStationId, String satelliteId, TleOrbitalParameters tleParameters, SpacecraftState startState,
            SpacecraftState endState) throws OrekitException {
        long now = System.currentTimeMillis();
        long startTime = startState.getDate().toDate(TimeScalesFactory.getUTC()).getTime();
        long endTime = endState.getDate().toDate(TimeScalesFactory.getUTC()).getTime();
        TLE tle = new TLE(tleParameters.getTleLine1(), tleParameters.getTleLine2());
        long orbitNumber = NavigationUtilities.calculateOrbitNumber(tle, startState.getDate());
        LocationContactEvent event = new LocationContactEvent(NavigationComponent.ORBIT_PROPAGATOR_NAME, now, groundStationId, satelliteId,
                tleParameters.getID(), startTime, endTime, orbitNumber);
        event.setSatelliteStateAtStart(NavigationUtilities.toOrbitalState(startState, tleParameters));
        return event;
    }

    void publish(LocationContactEvent event) {
        LOG.info("Injecting new LocationContactEvent {}", event.prettyPrint());
        getPublisher().publish(event);
    }

    IPublish getPublisher() {
        if (api == null) {
            api = ApiFactory.getPublishApi(NavigationComponent.ORBIT_PROPAGATOR_NAME);
        }
        return api;
    }

}
