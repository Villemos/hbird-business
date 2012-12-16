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
package org.hbird.exchange.navigation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;

public class TlePropagationRequest extends Command {

	private static final long serialVersionUID = 3877529427922153061L;

	{
		arguments.put("satellite", new CommandArgument("satellite", "The name of the satellite.", "String", "", null, true));
		arguments.put("starttime", new CommandArgument("starttime", "The start time of the propagation.", "long", "Seconds", null, true));
		arguments.put("locations", new CommandArgument("locations", "The name of the location(s) to which contact shall be calculated. If left empty all Locations registered in the archive will be taken.", "List<String>", "", new ArrayList<String>(), false));
		arguments.put("deltaPropagation", new CommandArgument("deltaPropagation", "The delta propagation from the starttime.", "Long", "Seconds", 2 * 60 * 60d, true));
		arguments.put("stepSize", new CommandArgument("stepSize", "The propagation step size.", "Long", "Seconds", 60d, true));
		arguments.put("contactDataStepSize", new CommandArgument("contactDataStepSize", "The propagation step size when calculating Contact Data between a location and a satellite between which visibility exist.", "Long", "Milliseconds", 500l, true));
		arguments.put("tleparameters", new CommandArgument("tleparameters", "The two line elements of a specific satellite. If left empty the latest TLE for the satellite will be taken.", "TleOrbitalParameters", "", null, false));
		arguments.put("stream", new CommandArgument("stream", "Flag indicating whether the propagation data should be returned as a stream to the monitoring topic (=true) or as a list (=false).", "Boolean", "", true, false));
	}

	
	public TlePropagationRequest(String issuedBy, String satellite) {
		super(issuedBy, "OrbitPredictor", "TlePropagationRequest", "A request for orbit prediction.");
		
		addArgument("satellite", satellite);	
		addArgument("starttime", ((new Date()).getTime()));
	}

	/**
	 * Constructor of a orbital prediction request.
	 * 
	 * @param satellite The satellite that should be predicted.
	 * @param position The current position of the satellite.
	 * @param velocity The current velocity of the satellite.
	 * @param starttime The start time at which the prediction should start. This must correspond to the time of the position and velocity.
	 * @param locations A list of locations, to which orbital events (establishment / loss of contact, etc) should be calculated and issued.
	 */
	public TlePropagationRequest(String issuedBy, String satellite, String tleLine1, String tleLine2, Long starttime, List<String> locations) {
		super(issuedBy, "OrbitPredictor", "TlePropagationRequest", "A request for orbit prediction.");
		
		addArgument("satellite", satellite);	
		addArgument("starttime", starttime);
		addArgument("locations", locations);
		addArgument("tleparameters", new TleOrbitalParameters(issuedBy, satellite, tleLine1, tleLine2));
	}

	/**
	 * Constructor based on a current Orbital State.
	 * 
	 * @param name The name of the request.
	 * @param satellite The satellite for which the prediction is done.
	 * @param state The initial orbital state.
	 * @param locations List of locations for which contact events shall be generated.
	 */
	public TlePropagationRequest(String issuedBy, Satellite satellite, TleOrbitalParameters state, List<String> locations) {
		super(issuedBy, "OrbitPredictor", "TlePropagationRequest", "A request for orbit prediction.");

		addArgument("satellite", satellite);
		addArgument("starttime", (new Date()).getTime());
		addArgument("locations", locations);
		addArgument("tleparameters", state);
	}
	
	public String getSatellite() {
		return (String) getArgument("satellite");
	}
	
	public Long getContactDataStepSize() {
		return (Long) getArgument("contactDataStepSize");
	}

	public Long getStartTime() {
		return (Long) getArgument("starttime");
	}
	
	public Double getDeltaPropagation() {
		return (Double) getArgument("deltaPropagation");
	}
		
	public Double getStepSize() {
		return (Double) getArgument("stepSize");
	}
	
	public List<String> getLocations() {
		return (List<String>) getArgument("locations");
	}

	public TleOrbitalParameters getTleParameters() {
		return (TleOrbitalParameters) getArgument("tleparameters");
	}
}
