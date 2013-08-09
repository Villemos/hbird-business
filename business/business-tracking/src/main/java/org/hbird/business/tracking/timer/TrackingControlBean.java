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
package org.hbird.business.tracking.timer;

import org.apache.camel.CamelContext;
import org.hbird.business.api.IDataAccess;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.groundstation.Track;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.Satellite;
import org.orekit.errors.OrekitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The antenna control will generate a schedule for a specific antenna (location) for tracking of a
 * satellite based on navigation data.
 * 
 * The antenna control will poll the archive at intervals to get the next contact event pair (Start/End of Contact) of
 * the location/satellite. Based on the events it will generate the detailed contact data (pointing info as well
 * as doppler) and an antenna schedule.
 * 
 * The antenna control depends on the navigation data being generated and published by another component. The
 * antenna control retrieves the data. It does not trigger its generation. This also means that the navigation data
 * may change in the background; a new TLE file may arrive, which leads to the generation of a new set of
 * contact events.
 * 
 * To keep the component as simple as possible the component is not event based. It will instead poll the archive
 * to check whether new contact events are available.
 * 
 * @author Gert Villemos
 * 
 */
public class TrackingControlBean {

	private static final Logger LOG = LoggerFactory.getLogger(TrackingControlBean.class);

	/** The definition of {@link GroundStation}. */
	protected final String groundStationId;

	/** The definition of the {@link Satellite} to track. */
	protected final String satelliteId;
	protected Satellite satellite = null;

	/** The name of this component. */
	protected final String name;

	/** API for retrieving contact data. */
	protected IDataAccess api = null;

	/** The last retrieved contact events for the location / satellite. */
	protected LocationContactEvent nextContactEvents = null;

	/**
	 * Constructor.
	 * 
	 * @param name The name of this component. Used when issue requests (issuedBy).
	 * @param location The location (antenna) that this controller is controlling.
	 * @param satelliteName The satellite that the controller manages the schedule for.
	 * @param queueName The name of the queue into which the antenna control commands shall be injected.
	 */
	public TrackingControlBean(String name, String groundStationName, String satelliteName, IDataAccess api) {
		this.name = name;
		this.groundStationId = groundStationName;
		this.satelliteId = satelliteName;
		this.api = api;
	}

	/**
	 * Method to be called at intervals. The method will, based on the next set of contact events for
	 * the location and satellite, create a schedule of time-tagged commands and inject them into the
	 * queue of the antenna.
	 * 
	 * @param context The context of the processor. Must contain a 'from' route as defined by the 'injectName'
	 *            attribute.
	 * @throws OrekitException
	 * 
	 * @return List of commands to the antenna parts
	 */
	public Command process(CamelContext context) throws OrekitException {
		Track command = null;

		try {
			/** Retrieve the next set of contact events (start-end) for this station. */
			LocationContactEvent contactEvents = api.getNextLocationContactEventFor(groundStationId, satelliteId);

			/** Check if we already have events. If yes; see if they are different. */
			if (nextContactEvents == null || nextContactEvents.getTimestamp() != contactEvents.getTimestamp()) {

				LOG.info("Found start/stop location contact events for location / satellite " + groundStationId + " / " +
						satelliteId + ". Will generate 'Track' command.");

				nextContactEvents = contactEvents;

				/** Get the definition of the satellite. */
				if (satellite == null) {
					try {
						satellite = api.getById(satelliteId, Satellite.class);
					} catch(Exception e ) {
						LOG.error("Error retrieving satellite " + satelliteId, e);
						return command;
					}
				}

				/** Schedule the tracking. */
				command = new Track("TRACK");
				command.setDestination(groundStationId);
				command.setLocationContactEvent(nextContactEvents);
				command.setIssuedBy(this.name);
				command.setSatellite(satellite);
				
				LOG.info("Creating Track command for ground station '" + groundStationId + "' to track satellite '" + satelliteId + "'");
			}
		} catch(Exception e) {
			LOG.info("Did not find start/stop location contact events for groundstation '" + groundStationId +
					"' and satellite '" + satelliteId + "'.");
			LOG.info("Reason: ", e);
		}

		/** Return command for scheduling. */
		return command;
	}

	double getFrequency(GroundStation groundStation, Satellite satellite) {
		// TODO - 05.03.2013, kimmell - implement
		return -1.0D;
	}
}
