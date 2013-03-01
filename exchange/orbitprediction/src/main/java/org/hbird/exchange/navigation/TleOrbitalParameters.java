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

import org.hbird.exchange.core.ISatelliteSpecific;
import org.hbird.exchange.core.Named;

public class TleOrbitalParameters extends Named implements ISatelliteSpecific {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2247881108684516270L;

	/** The name of the satellite. */
	protected String satellite;
	
	/** TLE line element 1. */
	protected String tleLine1;
	
	/** TLE line element 2. */
	protected String tleLine2;
	
	public TleOrbitalParameters(String issuedBy, String name, String description, long timestamp, String datasetidentifier, String satellite, String tleLine1, String tleLine2) {
		super(issuedBy, name, "TleOrbitalParameters", description, timestamp, datasetidentifier);
		
		this.satellite = satellite;
		this.tleLine1 = tleLine1;
		this.tleLine2 = tleLine2;		
	}
	
	public TleOrbitalParameters(String issuedBy, String name, String description, String satellite, String tleLine1, String tleLine2) {
		super(issuedBy, name, "TleOrbitalParameters", description);
		
		this.satellite = satellite;
		this.tleLine1 = tleLine1;
		this.tleLine2 = tleLine2;		
	}

	public TleOrbitalParameters(String issuedBy, String satellite, String tleLine1, String tleLine2) {
		super(issuedBy, "TLE/" + satellite, "TleOrbitalParameters", "The TLE state of the satellite.");
		
		this.satellite = satellite;
		this.tleLine1 = tleLine1;
		this.tleLine2 = tleLine2;		
		this.datasetidentifier = "TLE/" + timestamp;
	}

	public String getSatelliteName() {
		return satellite;
	}

	public void setSatelliteName(String satellite) {
		this.satellite = satellite;
	}

	public String getTleLine1() {
		return tleLine1;
	}

	public void setTleLine1(String tleLine1) {
		this.tleLine1 = tleLine1;
	}

	public String getTleLine2() {
		return tleLine2;
	}

	public void setTleLine2(String tleLine2) {
		this.tleLine2 = tleLine2;
	}
	
	
}
