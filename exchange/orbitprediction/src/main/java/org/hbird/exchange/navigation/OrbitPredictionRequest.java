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

/** 
 * Request for orbit predictions. The prediction will result in a stream of 'OrbitalState'
 * and 'OrbitalEvent' objects to be returned. The orbital states defines the orbit (position
 * and velocity) of the satellite at specific intervals in time. The orbital events defines
 * specific occurrences, such as start of contact as well as end of contact.  
 * 
 * A request is defined through;
 * - Initial position. The stating position for the prediction. Will default to the current position.
 * - Initial velocity. The starting velocity. Will default to the current velocity. 
 * - start time. The starting time. Defaults to the current time.
 * - delta propagation. The delta time that the prediction should propagate from the start time. 
 * - Locations. Locations whose coverage should also be covered as part of the prediction. 
 */
public class OrbitPredictionRequest extends Command {

	/** The unique UID */
	private static final long serialVersionUID = -3613096294375848828L;

	/**
	 * Constructor of a orbital prediction request.
	 * 
	 * @param satellite The satellite that should be predicted.
	 * @param position The current position of the satellite.
	 * @param velocity The current velocity of the satellite.
	 * @param starttime The start time at which the prediction should start. This must correspond to the time of the position and velocity.
	 * @param locations A list of locations, to which orbital events (establishment / loss of contact, etc) should be calculated and issued.
	 */
	public OrbitPredictionRequest(String issuedBy, String destination, String name, String description, Satellite satellite, D3Vector position, D3Vector velocity, Long starttime, List<Location> locations) {
		super(issuedBy, destination, name, description);
		
		addArgument("satellite", satellite);
		
		addArgument("locations", locations);

		addArgument("deltaPropagation", 2 * 60 * 60d);
		addArgument("stepSize", 60d);
		
		addArgument("initialstate", new KeplianOrbitalState(issuedBy, name, description, starttime, satellite, position, velocity));
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
	public OrbitPredictionRequest(String issuedBy, String destination, String name, String description, Satellite satellite, String tleLine1, String tleLine2, Long starttime, List<Location> locations) {
		super(issuedBy, destination, name, description);
		
		addArgument("satellite", satellite);
		
		addArgument("locations", locations);

		addArgument("deltaPropagation", 2 * 60 * 60d);
		addArgument("stepSize", 60d);
		
		addArgument("initialstate", new TleOrbitalState(issuedBy, name, description, starttime, satellite, tleLine1, tleLine2));
	}

	/**
	 * Constructor based on a current Orbital State.
	 * 
	 * @param name The name of the request.
	 * @param satellite The satellite for which the prediction is done.
	 * @param state The initial orbital state.
	 * @param locations List of locations for which contact events shall be generated.
	 */
	public OrbitPredictionRequest(String issuedBy, String destination, String name, String description, Satellite satellite, KeplianOrbitalState state, List<Location> locations) {
		super(issuedBy, destination, name, description);

		addArgument("satellite", satellite);

		addArgument("initialstate", state);
		
		addArgument("locations", locations);
		
		addArgument("deltaPropagation", 2 * 60 * 60d);
		addArgument("stepSize", 60d);
	}

	/**
	 * Constructor based on a current Orbital State.
	 * 
	 * @param name The name of the request.
	 * @param satellite The satellite for which the prediction is done.
	 * @param state The initial orbital state.
	 * @param locations List of locations for which contact events shall be generated.
	 */
	public OrbitPredictionRequest(String issuedBy, String destination, String name, String description, Satellite satellite, TleOrbitalState state, List<Location> locations) {
		super(issuedBy, destination, name, description);

		addArgument("satellite", satellite);

		addArgument("initialstate", state);
		
		addArgument("locations", locations);
		
		addArgument("deltaPropagation", 2 * 60 * 60d);
		addArgument("stepSize", 60d);
	}

	/**
	 * Constructor based on a current Orbital State.
	 * 
	 * @param name The name of the request.
	 * @param satellite The satellite for which the prediction is done.
	 * @param state The initial orbital state.
	 * @param locations List of locations for which contact events shall be generated.
	 */
	public OrbitPredictionRequest(String issuedBy, String destination, Satellite satellite, Location location) {
		super(issuedBy, destination, "Orbital Request", "An orbital request.");

		addArgument("satellite", satellite);
		addArgument("locations", Arrays.asList(location));
		
		addArgument("deltaPropagation", 2 * 60 * 60d);
		addArgument("stepSize", 60d);
	}

	public Satellite getSatellite() {
		return (Satellite) getArguments().get("satellite");
	}

	public void setSatellite(Satellite satellite) {
		addArgument("satellite", satellite);
	}

	public Long getStarttime() {
		if (getArguments().containsKey("starttime")) {	
			return (Long) getArguments().get("starttime");
	
		}
		
		return (new Date()).getTime();
	}
	
	public void setStarttime(Long starttime) {
		addArgument("starttime", starttime);
	}

	/** The time (s) between each orbital state. The number of orbital state objects created will
	 * thus be N=deltaPropagation / stepSize.  
	 * Time is measured in seconds. */
	public double getDeltaPropagation() {
		return (Double) getArguments().get("deltaPropagation");
	}

	public void setDeltaPropagation(double deltaPropagation) {
		addArgument("deltaPropagation", deltaPropagation);
	}

	public double getStepSize() {
		return (Double) getArguments().get("stepSize");
	}

	/** The time interval (s) from the start time that the orbit shall be propagated. Default is 2 hours. 
	 *  Time is measured in seconds. */
	public void setStepSize(double stepSize) {
		addArgument("stepSize", stepSize);
	}

	public List<Location> getLocations() {
		return (List<Location>) getArguments().get("locations");
	}

	public void setLocations(List<Location> locations) {		
		addArgument("locations", locations);
	}	
}
