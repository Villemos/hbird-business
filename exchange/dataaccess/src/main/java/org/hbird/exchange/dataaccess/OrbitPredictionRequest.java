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
package org.hbird.exchange.dataaccess;

import static org.hbird.exchange.dataaccess.Arguments.CONTACT_DATA_STEP_SIZE;
import static org.hbird.exchange.dataaccess.Arguments.DELTA_PROPAGATION;
import static org.hbird.exchange.dataaccess.Arguments.GROUND_STATION_NAMES;
import static org.hbird.exchange.dataaccess.Arguments.INITIAL_STATE;
import static org.hbird.exchange.dataaccess.Arguments.PUBLISH;
import static org.hbird.exchange.dataaccess.Arguments.SATELLITE_NAME;
import static org.hbird.exchange.dataaccess.Arguments.START_TIME;
import static org.hbird.exchange.dataaccess.Arguments.STEP_SIZE;
import static org.hbird.exchange.dataaccess.Arguments.create;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.constants.StandardComponents;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.navigation.D3Vector;
import org.hbird.exchange.navigation.Location;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.Satellite;

/**
 * Request for orbit predictions. The prediction will result in a stream of 'OrbitalState'
 * and 'OrbitalEvent' objects to be returned. The orbital states defines the orbit (position
 * and velocity) of the satellite at specific intervals in time. The orbital events defines
 * specific occurrences, such as start of contact as well as end of contact.
 */
public class OrbitPredictionRequest extends Command {

    public static final String DESCRIPTION = "An orbital request.";

    private static final long serialVersionUID = 2656918657590876976L;

    /**
     * Constructor of a orbital prediction request.
     * 
     * @param satellite The satellite that should be predicted.
     * @param position The current position of the satellite.
     * @param velocity The current velocity of the satellite.
     * @param starttime The start time at which the prediction should start. This must correspond to the time of the
     *            position and velocity.
     * @param locations A list of locations, to which orbital events (establishment / loss of contact, etc) should be
     *            calculated and issued.
     */
    public OrbitPredictionRequest(String issuedBy, String destination, String name, String description, String satellite, D3Vector position, D3Vector velocity,
            D3Vector momentum, Long starttime, List<String> locations) {
        super(issuedBy, destination, name, description);
        setArgumentValue(StandardArguments.SATELLITE_NAME, satellite);
        setArgumentValue(StandardArguments.START_TIME, starttime);
        setArgumentValue(StandardArguments.GROUND_STATION_NAMES, locations);
        setArgumentValue(StandardArguments.INITIAL_STATE, new OrbitalState(issuedBy, name, "Initial", description, starttime, satellite, position, velocity, momentum, "", 0, ""));
    }

    /**
     * Constructor based on a current Orbital State.
     * 
     * @param name The name of the request.
     * @param satellite The satellite for which the prediction is done.
     * @param state The initial orbital state.
     * @param locations List of locations for which contact events shall be generated.
     */
    public OrbitPredictionRequest(String issuedBy, String destination, String name, String description, String satellite, OrbitalState state,
            List<String> locations) {
        super(issuedBy, destination, name, description);
        setArgumentValue(StandardArguments.SATELLITE_NAME, satellite);
        setArgumentValue(StandardArguments.START_TIME, state.getTimestamp());
        setArgumentValue(StandardArguments.GROUND_STATION_NAMES, locations);
        setArgumentValue(StandardArguments.INITIAL_STATE, state);
    }

    /**
     * Constructor based on a current Orbital State.
     * 
     * @param name The name of the request.
     * @param satellite The satellite for which the prediction is done.
     * @param state The initial orbital state.
     * @param locations List of locations for which contact events shall be generated.
     */
    public OrbitPredictionRequest(String issuedBy, String destination, String satellite, String location) {
        super(issuedBy, destination, OrbitPredictionRequest.class.getSimpleName(), DESCRIPTION);
        setArgumentValue(StandardArguments.SATELLITE_NAME, satellite);
        setArgumentValue(StandardArguments.START_TIME, ((new Date()).getTime()));
        setArgumentValue(StandardArguments.GROUND_STATION_NAMES, Arrays.asList(location));
    }

    /**
     * Constructor based on a current Orbital State.
     * 
     * @param issuedBy The name of the request.
     * @param satellite The satellite for which the prediction is done.
     */
    public OrbitPredictionRequest(String issuedBy, String satellite) {
        super(issuedBy, StandardComponents.ORBIT_PREDICTOR, OrbitPredictionRequest.class.getSimpleName(), DESCRIPTION);
        setArgumentValue(StandardArguments.SATELLITE_NAME, satellite);
        setArgumentValue(StandardArguments.START_TIME, System.currentTimeMillis());
    }

    public OrbitPredictionRequest(String issuedBy, String satellite, long from, long to) {
        super(issuedBy, StandardComponents.ORBIT_PREDICTOR, OrbitPredictionRequest.class.getSimpleName(), DESCRIPTION);
        setArgumentValue(StandardArguments.SATELLITE_NAME, satellite);
        setArgumentValue(StandardArguments.START_TIME, from);
        setArgumentValue(StandardArguments.DELTA_PROPAGATION, (to - from) / 1000);
    }

    public OrbitPredictionRequest(String issuedBy, String satellite, String location, long from, long to) {
        super(issuedBy, StandardComponents.ORBIT_PREDICTOR, OrbitPredictionRequest.class.getSimpleName(), DESCRIPTION);
        setArgumentValue(StandardArguments.SATELLITE_NAME, satellite);
        setArgumentValue(StandardArguments.START_TIME, ((new Date()).getTime()));
        setArgumentValue(StandardArguments.DELTA_PROPAGATION, (to - from) / 1000);
        setArgumentValue(StandardArguments.GROUND_STATION_NAMES, Arrays.asList(location));
    }

    public OrbitPredictionRequest(String issuedBy, String satellite, List<String> locations, long from, long to) {
        super(issuedBy, StandardComponents.ORBIT_PREDICTOR, OrbitPredictionRequest.class.getSimpleName(), DESCRIPTION);
        setArgumentValue(StandardArguments.SATELLITE_NAME, satellite);
        setArgumentValue(StandardArguments.START_TIME, System.currentTimeMillis());
        setArgumentValue(StandardArguments.DELTA_PROPAGATION, (to - from) / 1000);
        setArgumentValue(StandardArguments.GROUND_STATION_NAMES, locations);
    }

    /**
     * @see org.hbird.exchange.core.Command#getArgumentDefinitions()
     */
    @Override
    protected List<CommandArgument> getArgumentDefinitions() {
        List<CommandArgument> args = super.getArgumentDefinitions();
        args.add(create(SATELLITE_NAME));
        args.add(create(START_TIME));
        args.add(create(GROUND_STATION_NAMES));
        args.add(create(DELTA_PROPAGATION));
        args.add(create(STEP_SIZE));
        args.add(create(CONTACT_DATA_STEP_SIZE));
        args.add(create(INITIAL_STATE));
        args.add(create(PUBLISH));
        return args;
    }

    public String getSatellite() {
        return getArgumentValue(StandardArguments.SATELLITE_NAME, String.class);
    }

    // TODO - 27.02.2013, kimmell - CHECK THIS!
    public void setSatellite(Satellite satellite) {
        setArgumentValue(StandardArguments.SATELLITE_NAME, satellite);
    }

    public Long getStarttime() {
        return getArgumentValue(StandardArguments.START_TIME, Long.class);
    }

    public void setStarttime(Long starttime) {
        setArgumentValue(StandardArguments.START_TIME, starttime);
    }

    /**
     * The time (s) between each orbital state. The number of orbital state objects created will
     * thus be N=deltaPropagation / stepSize.
     * Time is measured in seconds.
     */
    public double getDeltaPropagation() {
        return getArgumentValue(StandardArguments.DELTA_PROPAGATION, Double.class);
    }

    public void setDeltaPropagation(double deltaPropagation) {
        setArgumentValue(StandardArguments.DELTA_PROPAGATION, deltaPropagation);
    }

    public double getStepSize() {
        return getArgumentValue(StandardArguments.STEP_SIZE, Double.class);
    }

    /**
     * The time interval (s) from the start time that the orbit shall be propagated. Default is 2 hours.
     * Time is measured in seconds.
     */
    public void setStepSize(double stepSize) {
        setArgumentValue(StandardArguments.STEP_SIZE, stepSize);
    }

    public long getContactDataStepSize() {
        return getArgumentValue(StandardArguments.CONTACT_DATA_STEP_SIZE, Long.class);
    }

    /**
     * The time interval (s) from the start time that the orbit shall be propagated. Default is 2 hours.
     * Time is measured in seconds.
     */
    public void setContactDataStepSize(long contactDataStepSize) {
        setArgumentValue(StandardArguments.CONTACT_DATA_STEP_SIZE, contactDataStepSize);
    }

    public List<String> getLocations() {
        return getArgumentValue(StandardArguments.GROUND_STATION_NAMES, List.class);
    }

    // TODO - 28.02.2013, kimmell - check this!
    public void setLocations(List<Location> locations) {
        setArgumentValue(StandardArguments.GROUND_STATION_NAMES, locations);
    }
}
