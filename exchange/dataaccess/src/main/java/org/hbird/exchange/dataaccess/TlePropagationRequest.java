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

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.navigation.TleOrbitalParameters;

public class TlePropagationRequest extends DataRequest {

    private static final long serialVersionUID = 8912576417486920829L;

    public static final String DESCRIPTION = "A request for orbit prediction.";

    public TlePropagationRequest(String ID) {
        super(ID, TlePropagationRequest.class.getSimpleName());
        setDescription(DESCRIPTION);
        setArgumentValue(StandardArguments.START_TIME, System.currentTimeMillis());
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

    public void setDeltaPropagation(Long deltaPropagation) {
        setArgumentValue(StandardArguments.DELTA_PROPAGATION, deltaPropagation);
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

	/**
	 * @param satellite
	 */
	public void setSatellite(String satellite) {
        setArgumentValue(StandardArguments.SATELLITE_NAME, satellite);
	}
	
	/**
	 * @param satellite
	 */
	public void setLocation(String location) {
		List<String> names = new ArrayList<String>();
		names.add(location);
        setArgumentValue(StandardArguments.GROUND_STATION_NAMES, names);
	}
	
	/**
	 * @param satellite
	 */
	public void setLocations(List<String> locations) {
        setArgumentValue(StandardArguments.GROUND_STATION_NAMES, locations);
	}	
}
