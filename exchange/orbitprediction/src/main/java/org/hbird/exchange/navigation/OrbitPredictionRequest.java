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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;

/** 
 * Request for orbit predictions. The prediction will result in a stream of 'OrbitalState'
 * and 'OrbitalEvent' objects to be returned. The orbital states defines the orbit (position
 * and velocity) of the satellite at specific intervals in time. The orbital events defines
 * specific occurrences, such as start of contact as well as end of contact.  
 */
public class OrbitPredictionRequest extends Command {

	/** The unique UID */
	private static final long serialVersionUID = -3613096294375848828L;

	{
		arguments.put("satellite", new CommandArgument("satellite", "The name of the satellite.", "String", "", null, true));
		arguments.put("starttime", new CommandArgument("starttime", "The start time of the propagation.", "long", "Seconds", null, true));
		arguments.put("locations", new CommandArgument("locations", "The name of the location(s) to which contact shall be calculated.", "List<String>", "", null, true));
		arguments.put("deltaPropagation", new CommandArgument("deltaPropagation", "The delta propagation from the starttime.", "Long", "Seconds", 2 * 60 * 60d, true));
		arguments.put("stepSize", new CommandArgument("stepSize", "The propagation step size.", "Long", "Seconds", 60d, true));
		arguments.put("contactDataStepSize", new CommandArgument("contactDataStepSize", "The propagation step size when calculating Contact Data between a location and a satellite between which visibility exist.", "Long", "Milliseconds", 500l, true));
		arguments.put("initialstate", new CommandArgument("initialstate", "The initial orbital state (time, position, velocity) from which shall be propagated. Default is last known state of the satellite.", "OrbitalState", "", null, false));
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
	public OrbitPredictionRequest(String issuedBy, String destination, String name, String description, String satellite, D3Vector position, D3Vector velocity, D3Vector momentum, Long starttime, List<String> locations) {
		super(issuedBy, destination, name, description);

		addArgument("satellite", satellite);
		addArgument("starttime", starttime);
		addArgument("locations", locations);
		
		addArgument("initialstate", new OrbitalState(issuedBy, name, description, starttime, starttime, satellite, position, velocity, momentum));
	}

	/**
	 * Constructor based on a current Orbital State.
	 * 
	 * @param name The name of the request.
	 * @param satellite The satellite for which the prediction is done.
	 * @param state The initial orbital state.
	 * @param locations List of locations for which contact events shall be generated.
	 */
	public OrbitPredictionRequest(String issuedBy, String destination, String name, String description, String satellite, OrbitalState state, List<String> locations) {
		super(issuedBy, destination, name, description);

		addArgument("satellite", satellite);
		addArgument("starttime", state.getTimestamp());
		addArgument("locations", locations);		
		
		addArgument("initialstate", state);		
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
		super(issuedBy, destination, "Orbital Request", "An orbital request.");

		addArgument("satellite", satellite);
		addArgument("starttime", ((new Date()).getTime()));
		addArgument("locations", Arrays.asList(location));
	}

	public String getSatellite() {
		return (String) getArgument("satellite");
	}

	public void setSatellite(Satellite satellite) {
		addArgument("satellite", satellite);
	}

	public Long getStarttime() {
		return (Long) getArgument("starttime");
	}

	public void setStarttime(Long starttime) {
		addArgument("starttime", starttime);
	}

	/** The time (s) between each orbital state. The number of orbital state objects created will
	 * thus be N=deltaPropagation / stepSize.  
	 * Time is measured in seconds. */
	public double getDeltaPropagation() {
		return (Double) getArgument("deltaPropagation");
	}

	public void setDeltaPropagation(double deltaPropagation) {
		addArgument("deltaPropagation", deltaPropagation);
	}

	public double getStepSize() {
		return (Double) getArgument("stepSize");
	}

	/** The time interval (s) from the start time that the orbit shall be propagated. Default is 2 hours. 
	 *  Time is measured in seconds. */
	public void setStepSize(double stepSize) {
		addArgument("stepSize", stepSize);
	}


	public long getContactDataStepSize() {
		return (Long) getArgument("contactDataStepSize");
	}

	/** The time interval (s) from the start time that the orbit shall be propagated. Default is 2 hours. 
	 *  Time is measured in seconds. */
	public void setContactDataStepSize(long contactDataStepSize) {
		addArgument("contactDataStepSize", contactDataStepSize);
	}


	public List<String> getLocations() {
		return (List<String>) getArgument("locations");
	}

	public void setLocations(List<Location> locations) {		
		addArgument("locations", locations);
	}	
}
