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
package org.hbird.business.navigation;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.hbird.exchange.constants.StandardComponents;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.orekit.errors.OrekitException;
import org.orekit.frames.TopocentricFrame;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.events.ElevationDetector;
import org.orekit.time.TimeScalesFactory;

/**
 * This camel route processor will receive callbacks from the orekit library
 * notifying of events such as the establishment / loss of contact. The processor
 * will create the corresponding OrbitalEvent and send it to the consumer.
 */
public class LocationContactEventCollector extends ElevationDetector {

    /** The unique UID */
    private static final long serialVersionUID = 801203905525890103L;

    /** The ground station / location that comes into contact. */
    protected String groundStation = null;

    /** The name of the antenna. */
    protected String antenna = null;
    
    /** The satellite. */
    protected String satellite = null;

    /** FIXME I don't know what this does but OREKIT needs it... */
    public static final double maxcheck = 1.;

    protected ProducerTemplate producer = null;

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
            CamelContext context, boolean publish) {
        super(maxcheck, elevation, topo);

        this.publish = publish;
        if (publish) {
            producer = context.createProducerTemplate();
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

        LocationContactEvent event = new LocationContactEvent(StandardComponents.ORBIT_PREDICTOR, "Predicted", state.getDate().toDate(TimeScalesFactory.getUTC()).getTime(),
                groundStation, antenna, satellite, increasing, NavigationUtilities.toOrbitalState(state, satellite, parameters.getName(), parameters.getTimestamp(),
                        parameters.getType()), parameters.getName(), parameters.getTimestamp(), parameters.getType());
        events.add(event);

        /** If stream mode, then deliver the data as a stream. */
        if (publish) {
            producer.sendBody("direct:navigationinjection", event);
        }

        /** Continue listening for events. */
        return CONTINUE;
    }

    public List<Named> getDataSet() {
        return events;
    }
}
