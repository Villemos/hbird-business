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

import java.util.Date;
import java.util.List;

import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IOrbitPrediction;
import org.hbird.business.core.StartableEntity;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller to manage the propagation of an predicted orbit for a satellite and a set of
 * locations. The controller is a scheduled task which will automatically issue orbital 
 * prediction requests, to ensure that an orbital prediction is always available in the system.
 * 
 * The minimum interval for which a propagation will be available is 
 * <li>NOW to NOW + [Lead time], where the leadtime is typically 6 hours.</li>
 * 
 * A precondition for the controller to work is that the TLE parameters for the satellite have been 
 * published to the system.
 * 
 * The required orbital prediction is configured through
 * <li>Satellite. The name of the satellite that the orbit should be predicted for</li>
 * <li>Location(s). The location(s) that contact data shall be calculated for. If empty, contact data will be calculated for all known locations.</li>
 * <li>Lead Time. The </li>
 * 
 * The delta time to propagate the leadTime when executed is the 'executionDelay'. Default is 1 hour.
 * Means that the task will execute every hour and will propagate the orbit.
 */
public class OrbitPropagationComponent extends StartableEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2033213698026151689L;

	protected static final Logger LOG = LoggerFactory.getLogger(OrbitPropagationComponent.class);

	
	/**
	 * The interval (ms) for which propagation should be available. The controller will ensure that there
	 * is always orbital data available for the period
	 * <li>[NOW] to [NOW] + lead time + execution delay. </li>
	 */
	protected long leadTime = 6 * 60 * 60 * 1000;

	/**
	 * The satellite for which to propagate
	 */
	protected String satellite;


	/**
	 * The locations for which contact events should be calculated. If null, all locations inthe archive
	 * will be used.
	 */
	protected List<String> locations = null;

	protected long executionDelay = 1 * 60 * 1000;

	protected int count = 0;

	/**
	 * Default constructor.
	 */
	public OrbitPropagationComponent(String ID, String name) {
		super(ID, name);
	}

	
	public synchronized void execute() {

		/** Propagate the orbit from NOW to 'NOW + leadTime + executionDelay. This ensures that
		 * there will always be as a minimum 'leadTime' orbit prediction available and as a 
		 * maximum 'leadTime + executionDelay'.
		 * 
		 * Notice that if the task is started again, then the same prediction might be created. There
		 * are two conditions
		 * 1. The TLE is the same. In this case the same orbits will be calculated again, injected into 
		 * the system and stored. The values will override the existing values (... but will be exactly the same).
		 * 2. The TLE is different. In this case a new orbit is calculated. There might thus be multiple sets of
		 * orbital data at the same time, corresponding to different TLEs. It is the responsibility of the
		 * users of the orbit data to selected the latest set. 
		 * 
		 * */

		LOG.info("Propagating orbit of satellite '" + satellite + "'.");

		/** Get the latest TLE */
		IDataAccess api = ApiFactory.getDataAccessApi(this.name);
		TleOrbitalParameters tleParameters = api.getTleFor(satellite);

		if (tleParameters == null) {
			/** If there are no TLE parameters, then we cant do anything*/
			LOG.error("Failed to find TLE for satellite '" + satellite + "'. Cannot propagate orbital state, sorry.");
			return;
		}

		/** Get the latest orbital state */
		OrbitalState state = api.getOrbitalStateFor(satellite);

		/** If the TLE has changed, then we should clear the orbital states and recalculate them. */
		if (state != null && tleParameters.getID().equals(state.getDerivedFrom()) == false) {
			/** Clear all states of this satellite */
			LOG.info("Latest orbital state based on TLE '" + state.getDerivedFrom() + "'. Latest TLE in archive is '" + tleParameters.getID() + "'. Deleting stored orbital states.");
			ApiFactory.getArchiveManagerApi(this.name).deleteOrbitalStates(satellite);
			state = null;
		}

		long from = 0;
		long to = 0;
		long now = (new Date()).getTime();

		/** If the latest orbital state is BEFORE now, then propagate from now.*/
		if (state == null || state.getTimestamp() < now) {
			from = now;
			to = now + leadTime + executionDelay;
			LOG.info("No state or old state. Requesting TLE based propagating from '" + from + "' (NOW) to '" + to + "'");
		}
		/** If the latest orbital state is AFTER now and BEFORE the required leadTime, then propagate from state to now + leadTime.*/
		else if (now < state.getTimestamp() && state.getTimestamp() < now + leadTime + executionDelay) {
			from = state.getTimestamp();
			to = now + leadTime + executionDelay;
			LOG.info("Need to extend. Requesting TLE based from '" + from + "' to '" + to + "'");
		}
		/** Else there is nothing to do... */

		if (from != 0 && to != 0) {
			/** Request a propagation of the orbit. We use the 'stream' version of the method which means the result
			 * is published directly by the propegator to the system. */
			IOrbitPrediction predictionApi = ApiFactory.getOrbitPredictionApi(this.getName());

			/** Request a propagation of the orbit. We use the 'stream' version of the method which means the result
			 * is published directly by the propegator to the system. */
			predictionApi.requestOrbitPropagationStream(satellite, locations, from, to);		
		}
	}

	public long getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(long leadTime) {
		this.leadTime = leadTime;
	}

	public String getSatellite() {
		return satellite;
	}

	public void setSatellite(String satellite) {
		this.satellite = satellite;
	}

	public List<String> getLocations() {
		return locations;
	}

	public void setLocations(List<String> locations) {
		this.locations = locations;
	}

	public long getExecutionDelay() {
		return executionDelay;
	}

	public void setExecutionDelay(long executionDelay) {
		this.executionDelay = executionDelay;
	}

	public int getCount() {
		return count;
	}
}


