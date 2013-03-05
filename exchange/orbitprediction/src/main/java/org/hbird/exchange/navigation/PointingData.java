/**
* Licensed to the Hummingbird Foundation (HF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The HF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License. */
package org.hbird.exchange.navigation;

import org.hbird.exchange.core.DerivedNamed;
import org.hbird.exchange.core.ILocationSpecific;
import org.hbird.exchange.core.ISatelliteSpecific;

/**
 * Class describing the attributes between two objects, such as
 * a specific satellite and a specific location (antenna).
 * 
 * @author Admin
 *
 */
public class PointingData extends DerivedNamed implements ISatelliteSpecific, ILocationSpecific {

	private static final long serialVersionUID = -6892848291146482728L;

	protected Double azimuth;
	protected Double elevation;
	protected Double doppler;
	protected Double dopplerShift;

	/** One of the objects. */
	protected String satellite;

	/** The other object. */
	protected String location;
	
	public PointingData(String issuedBy, String type, long timestamp, Double azimuth, Double elevation, Double doppler, Double dopplerShift, String satellite, String location, String derivedFromName, long derivedFromTimestamp, String derivedFromType) {
		super(issuedBy, "PointingData", type, "Contact data between a satellite and a location", timestamp, derivedFromName, derivedFromTimestamp, derivedFromType);

		this.azimuth = azimuth;
		this.elevation = elevation;
		this.doppler = doppler;
		this.dopplerShift = dopplerShift;
		
		this.satellite = satellite;
		this.location = location;
	}

	public Double getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(Double azimuth) {
		this.azimuth = azimuth;
	}

	public Double getElevation() {
		return elevation;
	}

	public void setElevation(Double elevation) {
		this.elevation = elevation;
	}

	public Double getDoppler() {
		return doppler;
	}

	public void setDoppler(Double doppler) {
		this.doppler = doppler;
	}

	public Double getDopplerShift() {
		return dopplerShift;
	}

	public void setDopplerShift(Double dopplerShift) {
		this.dopplerShift = dopplerShift;
	}

	public String getSatelliteName() {
		return satellite;
	}

	public void setSatelliteName(String satellite) {
		this.satellite = satellite;
	}

	public String getLocationName() {
		return location;
	}

	public void setLocationName(String location) {
		this.location = location;
	}
	
	public String prettyPrint() {
		return "class=" + this.getClass().getSimpleName() + ", name=" + name + ", timestamp=" + timestamp + ", location=" + getLocationName() + ", satellite=" + satellite + ", para=" + this.azimuth + ":" + this.doppler + ":" + this.dopplerShift;
	}
}
