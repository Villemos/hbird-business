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

import org.hbird.exchange.navigation.LocationContactEvent;
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
public class LocationContactEventInjector extends ElevationDetector {

	/** The unique UID */
	private static final long serialVersionUID = 801203905525890103L;

	/** The location that comes into contact. */
	protected String location = null;
	
	/** The satellite. */
	protected String satellite = null;
	
	protected KeplianOrbitPredictor predictor = null;

	protected OrbitalStateInjector stateInjector = null;
	
	/** FIXME I don't know what this does but OREKIT needs it...*/
	public static final double maxcheck = 1.;

	/**
	 * COnstructor of an injector of location contact events.
	 * 
	 * @param maxCheck Sorry, I dont know... TODO
	 * @param elevation The degrees above the horizon that the satellite must be to be visible from this location.
	 * @param topo The topocentric framework used.
	 * @param satellite The satellite whose orbit we are predicting.
	 * @param location The location to which contact has been established / lost if this event occurs.
	 * @param generationTime 
	 */
	public LocationContactEventInjector(double elevation, TopocentricFrame topo, String satellite, String location, KeplianOrbitPredictor predictor, OrbitalStateInjector stateInjector) {
		super(maxcheck, elevation, topo);
		this.satellite = satellite;
		this.location = location;
		this.predictor = predictor;
		this.stateInjector = stateInjector;
	}
	
	/* (non-Javadoc)
	 * @see org.orekit.propagation.events.ElevationDetector#eventOccurred(org.orekit.propagation.SpacecraftState, boolean)
	 */
	public int eventOccurred(final SpacecraftState s, final boolean increasing) throws OrekitException {
		
		/** Create orbital event and send it on the response stream. */
		predictor.addResult(new LocationContactEvent("OrbitPredictor", "Visibility", "", s.getDate().toDate(TimeScalesFactory.getUTC()).getTime(), 0l, "", location, satellite, increasing));

		stateInjector.registerVisibleLocation(location, increasing);
		
		/** Continue listening for events. */
		return CONTINUE;
	}
}