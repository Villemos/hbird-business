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
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.business.navigation.ContactEventComponent;
import org.hbird.business.navigation.OrbitPropagationComponent;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.core.Metadata;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.hbird.exchange.util.Dates;

public class NavigationTester extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(NavigationTester.class);

    @Handler
    public void process() throws Exception {

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

        orbitalStateListener.elements.clear();

        /** Send a TLE request for a satellite and a subset of locations */
        OrbitPropagationComponent orbCom = new OrbitPropagationComponent("ESTCUBE_ORBIT_PROPAGATOR");
        orbCom.setSatelliteId(estcube1.getID());
        orbCom.setFrom(1355385448149l);
        orbCom.setTo(1355385448149l + 2 * 60 * 60 * 1000);
        orbCom.setExecutionDelay(0l);

        ContactEventComponent conCom = new ContactEventComponent("ESTCUBE_ES5EC_CONTACT_PREDICTOR");
        estcubeLocationComponent.setSatelliteId(estcube1.getID());
        conCom.setLocations(locations);
        conCom.setSatelliteId(estcube1.getID());
        orbCom.setFrom(1355385448149l);
        orbCom.setTo(1355385448149l + 2 * 60 * 60 * 1000);
        orbCom.setExecutionDelay(0l);

        startableEntityManager.start(orbCom);
        startableEntityManager.start(conCom);

        int totalSleep = 0;
        while (totalSleep < 120000 && orbitalStateListener.elements.size() != 121) {
            Thread.sleep(2000);
            totalSleep += 2000;
        }

        azzert(orbitalStateListener.elements.size() == 121, "Expect to receive 121 orbital states. Received " + orbitalStateListener.elements.size());
        print(orbitalStateListener.elements);

        azzert(locationEventListener.elements.size() == 2, "Expect to receive 2 location events. Received " + locationEventListener.elements.size());
        print(locationEventListener.elements);

        /** Retrieve the next set of TARTU events and check them. */
        LocationContactEvent contactEvent = accessApi.getNextLocationContactEventForGroundStation(es5ec.getID(), 1355385522265l);

        azzert(contactEvent != null);
        azzert(contactEvent.getStartTime() == 1355390676020l);
        azzert(contactEvent.getEndTime() == 1355391229267l);

        /**
         * Check the contact events with Aalborg. Notice that there is one LOST contact event first. The retrieval
         * should NOT get this.
         */
        contactEvent = accessApi.getNextLocationContactEventForGroundStation(gsAalborg.getID(), 1355385522265l);

        azzert(contactEvent != null);
        azzert(contactEvent.getStartTime() == 1355390809139l);
        azzert(contactEvent.getEndTime() == 1355391373642l);

        /** See if we can get the metadata */
        List<Metadata> response = accessApi.getMetadata(parameters.getID());
        azzert(response.size() == 1, "Expected to receive 1 piece of metadata. Received " + response.size());

        LOG.info("Finished");
    }

    protected void print(List<EntityInstance> elements) {

        TreeMap<Long, EntityInstance> sorted = new TreeMap<Long, EntityInstance>();

        for (EntityInstance element : elements) {
            if (element instanceof EntityInstance) {
                sorted.put(element.getTimestamp(), element);
            }
        }

        Iterator<Entry<Long, EntityInstance>> it = sorted.entrySet().iterator();
        while (it.hasNext()) {
            EntityInstance element = it.next().getValue();

            if (element instanceof TleOrbitalParameters) {
                continue;
            }

            if (element instanceof OrbitalState) {
                OrbitalState state = (OrbitalState) element;
                System.out.printf("%s Orbital state: %s, %s; %s%n", Dates.toDefaultDateFormat(state.getTimestamp()), state.getPosition().getX(),
                        state.getPosition().getY(), state.getGeoLocation());
            }
            else if (element instanceof LocationContactEvent) {
                LocationContactEvent event = (LocationContactEvent) element;
                System.out.println(event.toString());
            }
        }
    }
}
