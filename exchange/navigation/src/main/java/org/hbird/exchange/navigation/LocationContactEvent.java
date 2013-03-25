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

import org.hbird.exchange.core.Event;
import org.hbird.exchange.core.NamedInstanceIdentifier;
import org.hbird.exchange.interfaces.IAntennaSpecific;
import org.hbird.exchange.interfaces.IDerivedFrom;
import org.hbird.exchange.interfaces.ISatelliteSpecific;


/**
 * The ground station control will, based on the mission timeline, orchestra the ground station.
 * 
 * The specific commands are generated in the drivers. The station control will trigger the actions
 * of the drivers as necesarry.
 * 
 * The ground station will
 * - Retrieve the mission timeline, in particular the assigned contact events.
 * - Constraint Propagation. Apply rules and constraints, such as that a specific request must be send to drive X, Y seconds prior to/after contact.
 * - Execute activities.
 * 
 * An event, signalising the establishment or loss of contact between a satellite and
 * a location on earth. The location would typically be a ground station, but could also
 * be a ship, or anything else.
 * 
 * The location event is typically calculated by a prediction module or could be reported
 * by a station or satellite.
 * 
 */
public class LocationContactEvent extends Event implements IAntennaSpecific, ISatelliteSpecific, IDerivedFrom {

	private static final long serialVersionUID = 6129893135305263533L; 

	protected long generationTime = 0;

	/** The location that contact has been established /lost with. */
	protected String location = null;
	
	/** The antenna at the location. The same location may have multiple antennas. This event if for a specific
	 * groundstation / antenna pair.*/
	protected String antenna = null;
	
	/** The satellite that contact has been established /lost with. */
	protected String satellite = null;

	/** The state of the satellite as the event occurs. */
	protected OrbitalState satelliteState = null;
	
	/** Flag indicating whether the visibility (contact) is now established or lost. */
	protected boolean isVisible = true;
	
	protected NamedInstanceIdentifier from = null;
	
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
	public LocationContactEvent(String issuedBy, String type, long timestamp, String location, String antenna, String satellite, boolean isVisible, OrbitalState satelliteState, String derivedFromName, long derivedFromTimestamp, String derivedFromType) {
		super(issuedBy, "ContactEvent", "Event", "A contact event between a satellite and a location", timestamp);
		this.location = location;
		this.antenna = antenna;
		this.satellite = satellite;
		this.isVisible = isVisible;
		this.satelliteState = satelliteState;
		this.from = new NamedInstanceIdentifier(derivedFromName, derivedFromTimestamp, derivedFromType);
	}

	public String getGroundStationName() {
		return location;
	}

	public void setGroundStationName(String location) {
		this.location = location;
	}

	public String getSatelliteName() {
		return satellite;
	}

	public void setSatelliteName(String satellite) {
		this.satellite = satellite;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	public String prettyPrint() {
		return this.getClass().getSimpleName() + "[name=" + name + ", timestamp=" + timestamp + ", location=" + getGroundStationName() + ", satellite=" + satellite + ", visibility=" + isVisible + "]";
	}

	public String getAntennaName() {
		return antenna;
	}

	public void setAntennaName(String antenna) {
		this.antenna = antenna;
	}

	public OrbitalState getSatelliteState() {
		return satelliteState;
	}

	public void setSatelliteState(OrbitalState satelliteState) {
		this.satelliteState = satelliteState;
	}

	/* (non-Javadoc)
	 * @see org.hbird.exchange.interfaces.IDerived#from()
	 */
	@Override
	public NamedInstanceIdentifier from() {
		return from;
	}
}
