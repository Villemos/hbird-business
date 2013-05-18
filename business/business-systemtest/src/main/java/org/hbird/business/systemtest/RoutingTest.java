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

import org.apache.camel.Handler;
import org.apache.camel.Message;
import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class RoutingTest extends SystemTest {

    private static final String GS_ID = "/MISSON/GroundStations/GS-X";
    private static final String SAT_ID = "/MISSON/Satellites/SAT-1";
    private static final long ORBIT_NUMBER = (long) (1000000L * Math.random());
    private static final String ISSUER = "/MISSION/MCS/ContactPredictor";
    private static final long NOW = System.currentTimeMillis();
    private static final String APPLICABLE_TO = "/MISSION/Satellites/SAT-1/Orbit";
    private static final String DERIVED_FROM = "/MISSION/Satellites/SAT-1/TLE:123";

    private static Logger LOG = LoggerFactory.getLogger(EventTester.class);

    @Handler
    public void process() throws InterruptedException {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        eventListener.elements.clear();
        azzert(eventListener.elements.size() == 0, "There should be no events published");

        messageListener.start();
        azzert(messageListener.getMessages().size() == 0, "There should be no events published");

        LocationContactEvent event = new LocationContactEvent(GS_ID, SAT_ID, ORBIT_NUMBER);
        event.setIssuedBy(ISSUER);
        event.setTimestamp(NOW);
        event.setApplicableTo(APPLICABLE_TO);
        event.setDerivedFromId(DERIVED_FROM);

        publishApi.publish(event);
        Thread.sleep(100);

        azzert(eventListener.elements.size() == 1, "There should be 1 event published " + eventListener.elements.size());
        LocationContactEvent e = (LocationContactEvent) eventListener.elements.get(0);
        azzert(e.getInstanceID().equals(event.getInstanceID()), "InstanceId are same");
        azzert(e.getInstanceID().equals(GS_ID + ":" + SAT_ID + ":" + ORBIT_NUMBER), "InstanceId has expected value " + e.getInstanceID());

        azzert(messageListener.getMessages().size() == 1, "There should be 1 message published");
        Message msg = messageListener.getMessages().get(0);
        azzert(msg.getHeader(StandardArguments.CLASS).equals(LocationContactEvent.class.getSimpleName()), "Header 'class' should be set");
        azzert(msg.getHeader(StandardArguments.NAME).equals(LocationContactEvent.class.getSimpleName()), "Header 'name' should be set");
        azzert(msg.getHeader(StandardArguments.ENTITY_ID).equals(GS_ID + ":" + SAT_ID), "Header 'entityId' should be set");
        azzert(msg.getHeader(StandardArguments.ISSUED_BY).equals(ISSUER),
                "Header 'issuedBy' should be set to '" + ISSUER + "'; was '" + msg.getHeader(StandardArguments.ISSUED_BY) + "'");
        azzert(msg.getHeader(StandardArguments.ENTITY_INSTANCE_ID).equals(GS_ID + ":" + SAT_ID + ":" + ORBIT_NUMBER), "Header 'instanceId' should be set");
        azzert(msg.getHeader(StandardArguments.TIMESTAMP).equals(NOW), "Header 'timestamp' should be set to");
        azzert(msg.getHeader(StandardArguments.APPLICABLE_TO).equals(APPLICABLE_TO), "Header 'applicableTo' should be set");
        azzert(msg.getHeader(StandardArguments.DERIVED_FROM).equals(DERIVED_FROM), "Header 'derivedFrom' should be set");
        azzert(msg.getHeader(StandardArguments.GROUND_STATION_ID).equals(GS_ID), "Header 'groundStationId' should be set");
        azzert(msg.getHeader(StandardArguments.SATELLITE_ID).equals(SAT_ID), "Header 'satelliteId' should be set");

        messageListener.stop();

        LOG.info("Finished");
    }
}
