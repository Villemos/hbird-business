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

import org.hbird.exchange.core.Named;


/** As the name indicates, a 'Location' is a fixed place on (or near) earths surface.
 *  The location is defined through its;
 *  * position. A vector from the centre of the earth measured in meters.
 *  * name. The name of the location.
 *  * type. The type of location. Can be used to group locations logically, but is not used for processing. 
 *  * description. A description of the location.
 *  * thresholdElevation. The elevation that a satellite must be above the horizon to see this location.
 *  
 */
public class Location extends Named {

	/** Unique UID.	 */
	private static final long serialVersionUID = -2884807949988009796L;

	
	/** Constructor of the location. 
	 * @param name The name of the location. 
	 * @param description A free text description of the location.
	 */
	public Location(String issuedBy, String name, String description, D3Vector position) {
		super(issuedBy, name, description);
		this.position = position;
	}

	/** Constructor of the location. 
	 * @param name The name of the location. 
	 * @param description A free text description of the location.
	 * @param timestamp The time of the location.
	 */
	public Location(String issuedBy, String name, String description, long timestamp, D3Vector position) {
		super(issuedBy, name, description, timestamp);
		this.position = position;
	}
	
	{
		this.name = "Location";
		this.description = "";
	}

	protected String locationname;
	
	protected String locationdescription;
	
	/** The position of the location. The format of the location, i.e. whether cartasian or another
	 *  reference frame, is defined through the Unit of the position attributes. */
	protected D3Vector position;
	
	/** The elevation above the horizon that a satellite must have for this location
	 *  to be visible. Is used among others to calculate when the location becomes
	 *  into contact and leave contact. */
	protected double thresholdElevation = Math.toRadians(5.);
	
	public Location() {};
	
	/**
	 * Getter of the position of the location, as a 3D vector.
	 * 
	 * @return The position, described as a 3D vector.
	 */
	public D3Vector getPosition() {
		return position;
	}
	
	/**
	 * Getter of the threshold elevation. The elevation defines how high a satellite must
	 * be above the horizon to be visible from this location.
	 * 
	 * @return The elevation.
	 */
	public double getThresholdElevation() {
		return thresholdElevation;
	}
}
