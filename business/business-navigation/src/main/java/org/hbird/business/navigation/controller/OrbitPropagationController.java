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
package org.hbird.business.navigation.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IOrbitPrediction;
import org.hbird.business.navigation.api.OrbitPropagation;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.interfaces.IPart;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.hbird.exchange.tasking.ControllerTask;
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
public class OrbitPropagationController extends ControllerTask {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2033213698026151689L;

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

	/**
	 * Constructor
	 * 
	 * @param issuedBy The name of the component that started the task
	 * @param name The name of the task
	 * @param description A description of the task
	 * @param executionDelay The delay between checks that the required orbital states are available.
	 * @param leadTime The minimum time from NOW that orbital states should be available at all times
	 * @param satellite The satellite that the controller should predict the orbital data for
	 * @param locations The locations tof which contact data should be calculated
	 */
	public OrbitPropagationController(String issuedBy, String name, String description, long executionDelay, long leadTime, Satellite satellite, List<String> locations) {
		super(issuedBy, name, description, executionDelay);
		this.leadTime = leadTime;
		this.satellite = satellite.getQualifiedName();
		this.locations = locations;
	}

	public OrbitPropagationController(String issuedBy, String name, String description, long executionDelay, long leadTime, IPart satellite, List<IPart> locations) {
		super(issuedBy, name, description, executionDelay);
		this.leadTime = leadTime;
		this.satellite = satellite.getQualifiedName();

		this.locations = new ArrayList<String>();
		for (IPart location : locations) {
			this.locations.add(location.getQualifiedName());
		}
	}

	/** Constructor using the default value of the lead time (6 hours) and using all known locations.
	 * 
	 * @param issuedBy The name of the component that started the task
	 * @param name The name of the task
	 * @param description A description of the task
	 * @param executionDelay The delay between checks that the required orbital states are available.
	 * @param satellite The satellite that the controller should predict the orbital data for
	 */
	public OrbitPropagationController(String issuedBy, String name, String description, long executionDelay, String satellite) {
		super(issuedBy, name, description, executionDelay);
		this.satellite = satellite;
	}


	@Override
	protected List<Named> onFirstExecution() {

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

		/** Create local logger. Dont want to serialize the logger class hierachy when sending around this object... */
		Logger LOG = LoggerFactory.getLogger(OrbitPropagationController.class);
		LOG.info("Propagating orbit of satellite '" + satellite + "' (first execution).");

		/** Get the latest TLE */
		IDataAccess api = ApiFactory.getDataAccessApi(this.name);
		TleOrbitalParameters tleParameters = api.retrieveTleFor(satellite);

		if (tleParameters == null) {
			/** If there are no TLE parameters, then we cant*/
			// TODO
		}

		/** Get the latest orbital state */
		OrbitalState state = api.retrieveOrbitalStateFor(satellite);

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
			doRequest(from, to);
		}

		return new ArrayList<Named>();
	}

	@Override
	protected List<Named> onExecution() {
		/** Check the last orbital state in the system. */
		/** Get the latest orbital state */

		/** Create local logger. Dont want to serialize the logger class hierachy when sending around this object... */
		Logger LOG = LoggerFactory.getLogger(OrbitPropagationController.class);
		LOG.info("Propagating orbit of satellite '" + satellite + "' (propagation #" + count + ").");

		IDataAccess api = ApiFactory.getDataAccessApi(this.name);
		OrbitalState state = api.retrieveOrbitalStateFor(satellite);

		/** If we for some reason do not have a state, then initialize. Should have been done automatically already. But someone
		 * could have deleted part of the DB or similar. */

		long now = (new Date()).getTime();

		if (state == null) {
			long from = now;
			long to = now + leadTime + executionDelay;
			LOG.info("No state. Requesting TLE based from '" + from + "' (NOW) to '" + to + "'");

			doRequest(from, to);
		}		
		else if (now + leadTime + executionDelay > state.getTimestamp()) {
			long delta = now + leadTime + executionDelay - state.getTimestamp();
			long from = state.getTimestamp();
			long to = state.getTimestamp() + delta;

			LOG.info("No state. Requesting TLE based from '" + from + "' to '" + to + "'");

			doRequest(from, to);
		}

		return new ArrayList<Named>();
	}	

	/**
	 * Helper method to send an orbital propagation request.
	 * 
	 * @param from The start time of the request
	 * @param to The end time of the request
	 */
	protected void doRequest(long from, long to) {
		IOrbitPrediction predictionApi = new OrbitPropagation(this.getName());

		/** Request a propagation of the orbit. We use the 'stream' version of the method which means the result
		 * is published directly by the propegator to the system. */
		predictionApi.requestOrbitPropagationStream(satellite, locations, from, to);		
	}
}


