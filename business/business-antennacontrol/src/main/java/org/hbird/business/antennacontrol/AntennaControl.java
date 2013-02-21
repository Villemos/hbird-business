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
package org.hbird.business.antennacontrol;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IQueueManagement;
import org.hbird.business.api.impl.DataAccess;
import org.hbird.business.api.impl.QueueManagerApi;
import org.hbird.business.navigation.NavigationUtilities;
import org.hbird.exchange.movementcontrol.PointingRequest;
import org.hbird.exchange.navigation.PointingData;
import org.hbird.exchange.navigation.Location;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.orekit.errors.OrekitException;



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
 * 
 * @author Gert Villemos
 *
 */
public class AntennaControl {

	/** The definition of this Location. */
	protected Location location = null;

	/** The name of the satellite to track. */
	protected String satellite = null;
	
	/** The name of this component. */
	protected String name;

	/** The name of the activemq queue holding the antenna schedule. */
	protected String queueName = "hbird.antennaschedule";
	
	/** The name of the route used to inject data to the schedule. The route must be available in the context. */
	protected String injectUri = "seda:inject.schedule.";
	
	/** API for retrieving contact data. */
	protected IDataAccess api = null;
	
	/** API to manage the antenna schedule queue. */
	protected IQueueManagement queueApi = null;
	
	/** The last retrieved contact events for the location / satellite. */
	protected List<LocationContactEvent> nextContactEvents = new ArrayList<LocationContactEvent>();

	public AntennaControl(String name, Location location, String satellite, String queueName) {
		this.name = name;
		this.location = location;
		this.satellite = satellite;
		this.queueName = queueName;
		injectUri += name;
		
		api = new DataAccess(this.name);		
		queueApi = new QueueManagerApi(this.name);
	}
	
	
	/**
	 * Method to be called at intervals.
	 * 
	 * @param context The context of the processor. Must contain a 'from' route as defined by the 'injectName' attribute.
	 * @throws OrekitException
	 */
	public void process(CamelContext context) throws OrekitException {
		
		/** Retrieve the next set of contact events (start-end) for this station. */
		List<LocationContactEvent> contactEvents = api.retrieveNextLocationContactEventsFor(location.getName(), satellite);

		/** If there are contact events. */
		if (contactEvents.size() == 2) {

			/** Check if we already have events. If yes; see if they are different. */
			if (nextContactEvents.isEmpty() || nextContactEvents.get(0).getTimestamp() != contactEvents.get(0).getTimestamp()) {

				/** Purge the existing schedule. */
				try {
					queueApi.clearQueue(queueName);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				/** Create the new schedule. */
				nextContactEvents = contactEvents;
				
				ProducerTemplate template = context.createProducerTemplate();
				
				/** Create the commands to be executed PRE parse, i.e. for setup / configuration. */
				// TODO
				
				/** Calculate the contact data details based on the contact events. Create the WHILE parse commands. */
				for (PointingData point : NavigationUtilities.calculateContactData(contactEvents.get(0), contactEvents.get(1), location, 500)) {
					PointingRequest command = new PointingRequest(this.name, "", point.getAzimuth(), point.getElevation(), point.getDoppler(), point.getDopplerShift());
					command.setReleaseTime(point.getTimestamp());
					template.sendBody(injectUri, command);
					System.out.println(point.prettyPrint());
				}
				
				/** Create the commands to be executed AFTER parse. */
				// TODO
			}			
		}
	}


	public String getInjectUri() {
		return injectUri;
	}
}
