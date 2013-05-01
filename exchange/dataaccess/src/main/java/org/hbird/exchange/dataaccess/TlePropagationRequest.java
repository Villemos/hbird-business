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
import static org.hbird.exchange.dataaccess.Arguments.PUBLISH;
import static org.hbird.exchange.dataaccess.Arguments.SATELLITE_NAME;
import static org.hbird.exchange.dataaccess.Arguments.START_TIME;
import static org.hbird.exchange.dataaccess.Arguments.STEP_SIZE;
import static org.hbird.exchange.dataaccess.Arguments.TLE_PARAMETERS;
import static org.hbird.exchange.dataaccess.Arguments.create;

import java.util.Arrays;
import java.util.List;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.navigation.TleOrbitalParameters;

public class TlePropagationRequest extends DataRequest {

    private static final long serialVersionUID = 8912576417486920829L;

    public static final String DESCRIPTION = "A request for orbit prediction.";

    public TlePropagationRequest(String issuedBy, String satellite) {
        super(issuedBy, TlePropagationRequest.class.getSimpleName(), DESCRIPTION);
        setArgumentValue(StandardArguments.SATELLITE_NAME, satellite);
        setArgumentValue(StandardArguments.START_TIME, System.currentTimeMillis());
    }

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
    public TlePropagationRequest(String issuedBy, String satellite, String tleLine1, String tleLine2, Long starttime, List<String> locations) {
        super(issuedBy, TlePropagationRequest.class.getSimpleName(), DESCRIPTION);
        setArgumentValue(StandardArguments.SATELLITE_NAME, satellite);
        setArgumentValue(StandardArguments.START_TIME, starttime);
        setArgumentValue(StandardArguments.GROUND_STATION_NAMES, locations);
        setArgumentValue(StandardArguments.TLE_PARAMETERS, new TleOrbitalParameters(issuedBy, satellite + "/TLE", satellite, tleLine1, tleLine2));
    }

    /**
     * Constructor based on a current Orbital State.
     * 
     * @param name The name of the request.
     * @param satellite The satellite for which the prediction is done.
     * @param state The initial orbital state.
     * @param locations List of locations for which contact events shall be generated.
     */
    public TlePropagationRequest(String issuedBy, String satellite, TleOrbitalParameters state, List<String> locations) {
        this(issuedBy, satellite);
        setArgumentValue(StandardArguments.GROUND_STATION_NAMES, locations);
        setArgumentValue(StandardArguments.TLE_PARAMETERS, state);
    }

    public TlePropagationRequest(String issuedBy, String satellite, long from, long to) {
        super(issuedBy, TlePropagationRequest.class.getSimpleName(), DESCRIPTION);
        setArgumentValue(StandardArguments.SATELLITE_NAME, satellite);
        setArgumentValue(StandardArguments.START_TIME, from);
        setArgumentValue(StandardArguments.DELTA_PROPAGATION, (to - from) / 1000);
    }

    public TlePropagationRequest(String issuedBy, String satellite, String location, long from, long to) {
        this(issuedBy, satellite, Arrays.asList(location), from, to);
    }

    public TlePropagationRequest(String issuedBy, String satellite, List<String> locations, long from, long to) {
        super(issuedBy, TlePropagationRequest.class.getSimpleName(), DESCRIPTION);
        setArgumentValue(StandardArguments.SATELLITE_NAME, satellite);
        setArgumentValue(StandardArguments.START_TIME, from);
        setArgumentValue(StandardArguments.DELTA_PROPAGATION, (to - from) / 1000);
        setArgumentValue(StandardArguments.GROUND_STATION_NAMES, locations);
    }

    /**
     * @see org.hbird.exchange.core.Command#getArgumentDefinitions()
     */
    @Override
    protected List<CommandArgument> getArgumentDefinitions(List<CommandArgument> args) {
        args = super.getArgumentDefinitions(args);
        args.add(create(SATELLITE_NAME));
        args.add(create(START_TIME));
        args.add(create(GROUND_STATION_NAMES));
        args.add(create(DELTA_PROPAGATION));
        args.add(create(STEP_SIZE));
        args.add(create(CONTACT_DATA_STEP_SIZE));
        args.add(create(TLE_PARAMETERS));
        args.add(create(PUBLISH));
        return args;
    }

    public String getSatelliteId() {
        return getArgumentValue(StandardArguments.SATELLITE_NAME, String.class);
    }

    public Long getContactDataStepSize() {
        return getArgumentValue(StandardArguments.CONTACT_DATA_STEP_SIZE, Long.class);
    }

    public Long getStartTime() {
        return getArgumentValue(StandardArguments.START_TIME, Long.class);
    }

    public Long getDeltaPropagation() {
        return getArgumentValue(StandardArguments.DELTA_PROPAGATION, Long.class);
    }

    public Long getStepSize() {
        return getArgumentValue(StandardArguments.STEP_SIZE, Long.class);
    }

    @SuppressWarnings("unchecked")
    public List<String> getLocations() {
        return getArgumentValue(StandardArguments.GROUND_STATION_NAMES, List.class);
    }

    public TleOrbitalParameters getTleParameters() {
        return getArgumentValue(StandardArguments.TLE_PARAMETERS, TleOrbitalParameters.class);
    }

    public boolean getPublish() {
        return getArgumentValue(StandardArguments.PUBLISH, Boolean.class);
    }
}
