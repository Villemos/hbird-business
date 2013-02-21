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

import org.hbird.exchange.core.DerivedNamed;
import org.hbird.exchange.core.ILocationSpecific;
import org.hbird.exchange.core.ISatelliteSpecific;


/**
 * An event, signalising the establishment or loss of contact between a satellite and
 * a location on earth. The location would typically be a ground station, but could also
 * be a ship, or anything else.
 * 
 * The location event is typically calculated by a prediction module or could be reported
 * by a station or satellite.
 * 
 */
public class LocationContactEvent extends DerivedNamed implements ILocationSpecific, ISatelliteSpecific {

	private static final long serialVersionUID = 6129893135305263533L; 

	protected long generationTime = 0;

	/** The location that contact has been established /lost with. */
	public String location = null;
	
	/** The satellite that contact has been established /lost with. */
	public String satellite = null;

	/** The state of the satellite as the event occurs. */
	public OrbitalState satelliteState = null;
	
	/** Flag indicating whether the visibility (contact) is now established or lost. */
	public boolean isVisible = true;
	
	/**
	 * Constructor of a location contact event.
	 * 
	 * @param name The name of the location event.
	 * @param description A description of the event.
	 * @param timestamp The time that this event occured or is predicted to occure.
	 * @param datasetidentifier An identifier of the data set that this is part of. Should be set to 0 if this event is not part of a data series.
	 * @param location The location to which contact has been established / lost.
	 * @param satellite The satellite to which contact has been established / lost. 
	 */
	public LocationContactEvent(String issuedBy, long timestamp, String location, String satellite, boolean isVisible, OrbitalState satelliteState, String derivedFromName, long derivedFromTimestamp, String derivedFromType) {
		super(issuedBy, "ContactEvent", "LocationContactEvent", "A contact event between a satellite and a location", timestamp, derivedFromName, derivedFromTimestamp, derivedFromType);
		this.location = location;
		this.satellite = satellite;
		this.isVisible = isVisible;
		this.satelliteState = satelliteState;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSatellite() {
		return satellite;
	}

	public void setSatellite(String satellite) {
		this.satellite = satellite;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	public String prettyPrint() {
		return "class=" + this.getClass().getSimpleName() + ", name=" + name + ", timestamp=" + timestamp + ", location=" + getLocation() + ", visibility=" + isVisible;
	}
}
