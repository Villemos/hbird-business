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
package org.hbird.business.systemtest;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IOrbitPrediction;
import org.hbird.business.api.IPublish;
import org.hbird.business.navigation.api.OrbitPropagation;
import org.hbird.business.solr.api.Publish;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.navigation.PointingData;
import org.hbird.exchange.navigation.Location;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;

public class NavigationTester extends SystemTest {

	private static org.apache.log4j.Logger LOG = Logger.getLogger(NavigationTester.class);
	
	@Handler
	public void process() throws InterruptedException {

		LOG.info("------------------------------------------------------------------------------------------------------------");
		LOG.info("Starting");
		
		startMonitoringArchive();
		startOrbitPredictor();

		Thread.sleep(2000);

		orbitalStateListener.elements.clear();
		
		IPublish publishApi = ApiFactory.getPublishApi("SystemTest");
		
		/** Store a set of locations */
		publishApi.publishLocation("TARTU", "Test location 1", Math.toRadians(58.3000D), Math.toRadians(26.7330D), 59.0D, 146.92 * 1000000);
		publishApi.publishLocation("Aalborg", "Test location 2", Math.toRadians(55.659306D), Math.toRadians(12.587585D), 59.0D, 136.92 * 1000000);
		publishApi.publishLocation("Darmstadt", "Test location 3", Math.toRadians(49.831605D), Math.toRadians(8.673706D), 59.0D, 126.92 * 1000000);
		publishApi.publishLocation("New York", "Test location 4", Math.toRadians(40.66564D), Math.toRadians(-74.036865D), 59.0D, 116.92 * 1000000);

		/** Store a set of satellites */
		publishApi.publishSatellite("ESTcube", "Test satellite 1");
		publishApi.publishSatellite("DKcube", "Test satellite 2");
		publishApi.publishSatellite("DEcube", "Test satellite 3");

		List<String> locations = new ArrayList<String>();
		locations.add("TARTU");
		locations.add("Aalborg");

		/** CReate the TLE elements */
		String tleLine1 = "1 27842U 03031C   12330.56671446  .00000340  00000-0  17580-3 0  5478";
		String tleLine2 = "2 27842 098.6945 336.9241 0009991 090.9961 269.2361 14.21367546487935";
		TleOrbitalParameters parameters = new TleOrbitalParameters("SystemTest", "ESTcube", tleLine1, tleLine2);
		publishApi.publish(parameters);
		
		/** Insert a metadata comment. */
		publishApi.publichMetadata(parameters, "Author", "This file was approved by Gert Villemos the " + (new Date()).toString());
		
		/** Send command to commit all changes. */
		forceCommit();
		
		orbitalStateListener.elements.clear();

		/** Send a TLE request for a satellite and a subset of locations */
		IOrbitPrediction api = ApiFactory.getOrbitPredictionApi("SystemTest");
		api.requestOrbitPropagationStream("ESTcube", locations, 1355385448149l, 1355385448149l + 2 * 60 * 60 * 1000);

		int totalSleep = 0;
		while (totalSleep < 120000 && orbitalStateListener.elements.size() != 121) {
			Thread.sleep(2000);
			totalSleep += 2000;
		}

		azzert(orbitalStateListener.elements.size() == 121, "Expect to receive 121 orbital states. Received " + orbitalStateListener.elements.size());
		print(orbitalStateListener.elements);
			
		azzert(locationEventListener.elements.size() == 5, "Expect to receive 5 location events. Received " + locationEventListener.elements.size());
		print(locationEventListener.elements);

		/** Send command to commit all changes. */
		forceCommit();
		
		/** Retrieve the next set of TARTU events and check them. */
		IDataAccess dataApi = ApiFactory.getDataAccessApi("SystemTest");
		List<LocationContactEvent> contactEvents = dataApi.retrieveNextLocationContactEventsFor("TARTU", 1355385522265l);
		
		azzert(contactEvents.size() == 2);
		azzert(contactEvents.get(0).getTimestamp() == 1355390844725l);
		azzert(contactEvents.get(1).getTimestamp() == 1355391059951l);

		/** Check the contact events with Aalborg. Notice that there is one LOST contact event first. The retrieval should NOT get this. */
		contactEvents = dataApi.retrieveNextLocationContactEventsFor("Aalborg", 1355385522265l);
		
		
		azzert(contactEvents.size() == 2);
		azzert(contactEvents.get(0).getTimestamp() == 1355390970221l);
		azzert(contactEvents.get(1).getTimestamp() == 1355391211998l);

		/** See if we can get the metadata */
		IDataAccess dataAccessApi = ApiFactory.getDataAccessApi("SystemTest");
		List<Named> response = dataAccessApi.getMetadata(parameters);
		azzert(response.size() == 1, "Expected to receive 1 piece of metadata. Received " + response.size());
		
		LOG.info("Finished");
	}
	
	protected void print(List<Named> elements) {
		
		TreeMap<Long, Named> sorted = new TreeMap<Long, Named>();
		
		for (Named element : elements) {
			sorted.put(element.getTimestamp(), element);
		}
		
		Iterator<Entry<Long, Named>> it = sorted.entrySet().iterator();
		while (it.hasNext()) {
			Named element = it.next().getValue();
			
			if (element instanceof TleOrbitalParameters) {
				continue;
			}
			
			if (element instanceof OrbitalState) {
				OrbitalState state = (OrbitalState) element;
				System.out.println(new Date(element.getTimestamp()) +"   Orbital State:  " + state.getPosition().p1 + ", " + state.getPosition().p2);
			}
			else if (element instanceof LocationContactEvent) {
				LocationContactEvent event = (LocationContactEvent) element;
				if (event.isVisible == true) {
					System.out.println(new Date(element.getTimestamp()) +" Location Event: " + event.getTimestamp() + " " + event.location + " got contact to " + event.satellite);
				}
				else {
					System.out.println(new Date(element.getTimestamp()) +" Location Event: " + event.getTimestamp() + " " + event.location + " lost contact to " + event.satellite);
				}
			}
			else if (element instanceof PointingData) {
				PointingData event = (PointingData) element;
				System.out.println(new Date(element.getTimestamp()) +"     Contact Data: sat=" + event.getSatellite() + ", loc=" + event.getLocation() + ", azm=" + event.getAzimuth() + ", ele=" + event.getElevation() + ", dop=" + event.getDoppler() + ", dopshift=" + event.getDopplerShift());
			}
		}
	}
}
