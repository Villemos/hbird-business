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
import org.hbird.exchange.core.Named;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.OrbitalState;
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

		publishGroundStationsAndSatellites();
		TleOrbitalParameters parameters = publishTleParameters();

		List<String> locations = new ArrayList<String>();
		locations.add(es5ec.getName());
		locations.add(gsAalborg.getName());

		/** Send command to commit all changes. */
		forceCommit();

		orbitalStateListener.elements.clear();

		/** Send a TLE request for a satellite and a subset of locations */
		IOrbitPrediction api = ApiFactory.getOrbitPredictionApi("SystemTest");
		api.requestOrbitPropagationStream(estcube1.getID(), locations, 1355385448149l, 1355385448149l + 2 * 60 * 60 * 1000);

		int totalSleep = 0;
		while (totalSleep < 120000 && orbitalStateListener.elements.size() != 121) {
			Thread.sleep(2000);
			totalSleep += 2000;
		}

		azzert(orbitalStateListener.elements.size() == 121, "Expect to receive 121 orbital states. Received " + orbitalStateListener.elements.size());
		print(orbitalStateListener.elements);

		azzert(locationEventListener.elements.size() == 6, "Expect to receive 6 location events. Received " + locationEventListener.elements.size());
		print(locationEventListener.elements);

		/** Send command to commit all changes. */
		forceCommit();

		/** Retrieve the next set of TARTU events and check them. */
		IDataAccess dataApi = ApiFactory.getDataAccessApi("SystemTest");
		List<LocationContactEvent> contactEvents = dataApi.getNextLocationContactEventsForLocation(es5ec.getID(), 1355385522265l);

		azzert(contactEvents.size() == 2);
		azzert(contactEvents.get(0).getTimestamp() == 1355390676020l);
		azzert(contactEvents.get(1).getTimestamp() == 1355391229267l);

		/**
		 * Check the contact events with Aalborg. Notice that there is one LOST contact event first. The retrieval
		 * should NOT get this.
		 */
		contactEvents = dataApi.getNextLocationContactEventsForLocation(gsAalborg.getID(), 1355385522265l);

		azzert(contactEvents.size() == 2);
		azzert(contactEvents.get(0).getTimestamp() == 1355390809139l);
		azzert(contactEvents.get(1).getTimestamp() == 1355391373642l);

		/** See if we can get the metadata */
		List<Named> response = accessApi.getMetadata(parameters);
		azzert(response.size() == 1, "Expected to receive 1 piece of metadata. Received " + response.size());

		LOG.info("Finished");
	}

	protected void print(List<Named> elements) {

		TreeMap<Long, Named> sorted = new TreeMap<Long, Named>();

		for (Named element : elements) {
			if (element instanceof Named) {
				sorted.put(((Named)element).getTimestamp(), (Named)element);
			}
		}

		Iterator<Entry<Long, Named>> it = sorted.entrySet().iterator();
		while (it.hasNext()) {
			Named element = it.next().getValue();

			if (element instanceof TleOrbitalParameters) {
				continue;
			}

			if (element instanceof OrbitalState) {
				OrbitalState state = (OrbitalState) element;
				System.out.println(new Date(element.getTimestamp()) + "   Orbital State:  " + state.getPosition().p1 + ", " + state.getPosition().p2);
			}
			else if (element instanceof LocationContactEvent) {
				LocationContactEvent event = (LocationContactEvent) element;
				if (event.isVisible() == true) {
					System.out.println(new Date(element.getTimestamp()) + " Location Event: " + event.getTimestamp() + " " + event.getGroundStationId()
							+ " got contact to " + event.getSatelliteId());
				}
				else {
					System.out.println(new Date(element.getTimestamp()) + " Location Event: " + event.getTimestamp() + " " + event.getGroundStationId()
							+ " lost contact to " + event.getSatelliteId());
				}
			}
		}
	}
}
