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

import java.util.Date;
import java.util.List;

import org.hbird.exchange.core.Named;

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
public class OrbitPredictionRequest extends Named {

	/** The unique UID */
	private static final long serialVersionUID = -3613096294375848828L;

	{
		this.name = "";
		this.description = "";
	}
	
	public OrbitPredictionRequest() {};
	
	/**
	 * Constructor of a orbital prediction request.
	 * 
	 * @param satellite The satellite that should be predicted.
	 * @param position The current position of the satellite.
	 * @param velocity The current velocity of the satellite.
	 * @param starttime The start time at which the prediction should start. This must correspond to the time of the position and velocity.
	 * @param locations A list of locations, to which orbital events (establishment / loss of contact, etc) should be calculated and issued.
	 */
	public OrbitPredictionRequest(String name, Satelitte satellite, D3Vector position, D3Vector velocity, Long starttime, List<Location> locations) {
		this.name = name;
		this.satellite = satellite;
		this.position = position;
		this.velocity = velocity;
		this.starttime = starttime;
		this.locations = locations;
	}

	/**
	 * Constructor based on a current Orbital State.
	 * 
	 * @param name The name of the request.
	 * @param satellite The satellite for which the prediction is done.
	 * @param state The initial orbital state.
	 * @param locations List of locations for which contact events shall be generated.
	 */
	public OrbitPredictionRequest(String name, Satelitte satellite, OrbitalState state, List<Location> locations) {
		this.name = name;
		this.satellite = satellite;
		this.position = state.position;
		this.velocity = state.velocity;
		this.starttime = (new Date()).getTime();
		this.locations = locations;
	}
	
	/**
	 * The satellite for which the provider should provide predictions.  
	 * */
	public Satelitte satellite = null;
	
	/** The current position of the satellite, from which the orbit prediction shall be made. The
	 *  location may be null, in which case the provider shall take the current position of the
	 *  satellite from another source. */
	public D3Vector position = null;

	/** The current velocity of the satellite, from which the orbit prediction shall be made. The
	 *  velocity may be null, in which case the provider shall take the current velocity of the
	 *  satellite from another source. */
	public D3Vector velocity = null;
	
	/** The time at which the prediction shall begin. The timestamp may be null, in which case the
	 *  provider shall take the current time. */
	public Long starttime = null;
	
	/** The time interval (s) from the start time that the orbit shall be propagated. Default is 2 hours. */
	public double deltaPropagation = 2 * 60 * 60 * 1000;
	
	/** The time (s) between each orbital state. The number of orbital state objects created will
	 * thus be N=deltaPropagation / stepSize.  */
	public double stepSize = 60.;
	
	/** A specific set of locations for which orbital events shall be calculated. Can be used to
	 * extend the existing set of locations, for example to validate configuration of ground stations. 
	 * The locations may be null, in which case the provider shall use the configured locations of
	 * the system. */
	public List<Location> locations = null;	
}
