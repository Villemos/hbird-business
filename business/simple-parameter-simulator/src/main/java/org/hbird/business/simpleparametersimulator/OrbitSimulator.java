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
package org.hbird.business.simpleparametersimulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hbird.business.navigation.KeplianOrbitPredictor;
import org.hbird.exchange.navigation.D3Vector;
import org.hbird.exchange.navigation.OrbitPredictionRequest;
import org.hbird.exchange.navigation.OrbitalState;
import org.orekit.errors.OrekitException;

public class OrbitSimulator {

	/** The class logger. */
	protected static Logger LOG = Logger.getLogger(OrbitSimulator.class);
	
	protected List<OrbitalState> orbitalstates = null;

	protected int index = 0;

	protected String satellite = null;

	protected String type = "Measured Orbital State";
	
	protected List<String> locations = null;

	protected OrbitalState initialState = null;

	protected String issuedBy = "ES5EC";
	
	/**
	 * Default constructor. Will create a simulator with a single satellite 'ESTcube' and propagate
	 * its orbit for 2 hours with 1 minute steps.
	 * 
	 */
	public OrbitSimulator() {	
		satellite = "ESTcube";
		
		locations = new ArrayList<String>();
		locations.add("ES5EC");

		D3Vector position = new D3Vector("", "Initial Position", "Position", "Initial position of ESTcube", -6142438.668, 3492467.560, -25767.25680);
		D3Vector velocity = new D3Vector("", "Initial Velocity", "Velocity", "Initial velocity of ESTcube", 505.8479685, 942.7809215, 7435.922231);
		D3Vector momentum = new D3Vector("", "Initial Velocity", "Velocity", "Initial velocity of ESTcube", 100., 100., 100.);
		
		initialState = new OrbitalState("Simulator", "Measured Orbital State", "Initial state", (new Date()).getTime(), (new Date()).getTime(), "Test Data", satellite, position, velocity, momentum, "", 0, "");
	}

	public OrbitSimulator(String issuedBy, String type, String satellite, List<String> locations, OrbitalState initialState) {
		this.issuedBy = issuedBy;
		this.type = type;
		this.satellite = satellite;
		this.locations = locations;
		this.initialState = initialState;
	}

	public OrbitalState process() {		

		/** If first time, then initzialize*/
		if (orbitalstates == null) {
			initialize();
		}
		/** If we have read all orbits, then take the next one. */
		else if (index == orbitalstates.size() - 1) {
			initialState = orbitalstates.get(index);
			orbitalstates.clear();
			index = 0;
			initialize();
		}

		/** TODO add error, to simulate that the orbit is not as predicted. */

		LOG.debug("Sending Orbital Message.");

		return orbitalstates.get(index++);
	}

	protected void initialize() {
		OrbitPredictionRequest request = new OrbitPredictionRequest(issuedBy, "", "Measured Orbital State", "A simulated orbit.", satellite, initialState, locations);

		KeplianOrbitPredictor orbitPredictor = new KeplianOrbitPredictor();
//		try {
//			/** The results will contain Orbital States as well as orbital events. Only take the states. 
//			 * 
//			 * TODO Update to also allow the simulator to issue the other events. */
////			orbitalstates = new ArrayList<OrbitalState>();
////			for (Object obj : orbitPredictor.predictOrbit(request)) {
////				if (obj instanceof OrbitalState) {
////					orbitalstates.add((OrbitalState) obj);
////				}
////			}
//		} catch (OrekitException e) {
//			e.printStackTrace();
//		}	
	}
}
