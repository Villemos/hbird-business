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
package org.hbird.business.archive.dao.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.UnknownHostException;
import java.util.List;

import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.Mongo;

public class MongoOrbitalDataAccessTest {
    private static final String TEST_DATABASE_NAME = "hbird_test";

    private MongoOrbitalDataAccess dao;
    private MongoDataAccess saver;
    private Mongo mongo;

    @Before
    public void setUp() throws UnknownHostException {
        mongo = new Mongo("localhost", 27017);

        MongoTemplate template = new MongoTemplate(mongo, TEST_DATABASE_NAME);
        dao = new MongoOrbitalDataAccess(template);
        saver = new MongoDataAccess(template);
    }

    @After
    public void tearDown() {
        mongo.dropDatabase(TEST_DATABASE_NAME);
        mongo.close();
    }

    @Test
    public void testGetTLEFor() throws Exception {
        String satID = "SAT";
        TleOrbitalParameters tle1 = new TleOrbitalParameters("foo", "foo");
        tle1.setSatelliteId(satID);
        tle1.setVersion(10);
        tle1.setTimestamp(10);

        TleOrbitalParameters tle2 = new TleOrbitalParameters("foo", "foo");
        tle2.setSatelliteId(satID);
        tle2.setVersion(20);
        tle2.setTimestamp(20);

        saver.save(tle1);
        saver.save(tle2);

        TleOrbitalParameters tle = dao.getTleFor(satID);
        assertEquals(satID, tle.getSatelliteID());
        assertEquals(tle2.getVersion(), tle.getVersion());

        try {
            tle = dao.getTleFor("foobad");
            fail("getTleFor must throw exception when TLE not found");
        }
        catch (Exception e) {
        }

        List<TleOrbitalParameters> tles = dao.getTleFor(satID, 15, 40);
        assertEquals(1, tles.size());
        assertEquals(satID, tles.get(0).getSatelliteID());
        assertEquals(tle2.getVersion(), tles.get(0).getVersion());
    }

    @Test
    public void testGetOrbitalStates() throws Exception {
        Satellite sat1 = new Satellite("SAT1", "SAT1");

        TleOrbitalParameters tle1 = new TleOrbitalParameters(sat1.getID(), "TLE");
        tle1.setSatelliteId(sat1.getID());
        tle1.setVersion(10);
        tle1.setTimestamp(10);

        TleOrbitalParameters tle2 = new TleOrbitalParameters(sat1.getID(), "TLE");
        tle2.setSatelliteId(sat1.getID());
        tle2.setVersion(20);
        tle2.setTimestamp(20);

        OrbitalState state1 = new OrbitalState("state1", "state1");
        state1.setSatelliteId(sat1.getID());
        state1.setDerivedFromId(tle1.getInstanceID());
        state1.setVersion(19);
        state1.setTimestamp(19);

        OrbitalState state2 = new OrbitalState("state2", "state2");
        state2.setSatelliteId(sat1.getID());
        state2.setDerivedFromId(tle1.getInstanceID());
        state2.setVersion(21);
        state2.setTimestamp(21);

        OrbitalState state3 = new OrbitalState("state3", "state3");
        state3.setSatelliteId(sat1.getID());
        state3.setDerivedFromId(tle2.getInstanceID());
        state3.setVersion(20);
        state3.setTimestamp(20);

        saver.save(tle1);
        saver.save(tle2);

        saver.save(state1);
        saver.save(state2);
        saver.save(state3);

        OrbitalState state = dao.getOrbitalStateFor(sat1.getID());
        assertEquals(tle2.getInstanceID(), state.getDerivedFromId());
        assertEquals(state3.getVersion(), state.getVersion());

        List<OrbitalState> states = dao.getOrbitalStatesFor(sat1.getID(), 0, 100);
        assertEquals(3, states.size());
        assertEquals(state1.getVersion(), states.get(0).getVersion());
        assertEquals(tle1.getInstanceID(), states.get(0).getDerivedFromId());
        assertEquals(state3.getVersion(), states.get(1).getVersion());
        assertEquals(tle2.getInstanceID(), states.get(1).getDerivedFromId());
        assertEquals(state2.getVersion(), states.get(2).getVersion());
        assertEquals(tle1.getInstanceID(), states.get(2).getDerivedFromId());
    }

    @Test
    public void testGetNextContact() throws Exception {
        GroundStation gs1 = new GroundStation("GS1", "GS1");
        Satellite sat1 = new Satellite("SAT1", "SAT1");
        Satellite sat2 = new Satellite("SAT2", "SAT2");

        long now = System.currentTimeMillis();

        LocationContactEvent contact0 = new LocationContactEvent(gs1.getID(), sat1.getID(), 4);
        contact0.setStartTime(now - 1000 * 60 * 5);
        contact0.setEndTime(now - 1000 * 60 * 2);

        LocationContactEvent contact1 = new LocationContactEvent(gs1.getID(), sat1.getID(), 10);
        contact1.setStartTime(now + 1000 * 60 * 2);
        contact1.setEndTime(now + 1000 * 60 * 5);

        LocationContactEvent contact2 = new LocationContactEvent(gs1.getID(), sat1.getID(), 11);
        contact2.setStartTime(now + 1000 * 60 * 12);
        contact2.setEndTime(now + 1000 * 60 * 14);

        LocationContactEvent contact3 = new LocationContactEvent(gs1.getID(), sat2.getID(), 5);
        contact3.setStartTime(now + 1000 * 60 * 4);
        contact3.setEndTime(now + 1000 * 60 * 14);

        LocationContactEvent contact4 = new LocationContactEvent(gs1.getID(), sat1.getID(), 50);
        contact4.setStartTime(now + 1000 * 60 * 30);
        contact4.setEndTime(now + 1000 * 60 * 34);

        saver.save(gs1);
        saver.save(sat1);
        saver.save(sat2);
        saver.save(contact0);
        saver.save(contact1);
        saver.save(contact2);
        saver.save(contact3);
        saver.save(contact4);

        // For GS
        LocationContactEvent contact = dao.getNextLocationContactEventForGroundStation(gs1.getID());
        assertEquals(gs1.getID(), contact.getGroundStationID());
        assertEquals(sat1.getID(), contact.getSatelliteID());
        assertEquals(10, contact.getOrbitNumber());
        assertEquals(contact1.getStartTime(), contact.getStartTime());

        // For GS and starting point
        List<LocationContactEvent> contacts = dao.getLocationContactEventsForGroundStation(gs1.getID(), now + 1000 * 60 * 13, now + 1000 * 60 * 15);
        assertEquals(2, contacts.size());

        contact = contacts.get(0);
        assertEquals(contact3.getInstanceID(), contact.getInstanceID());
        assertEquals(gs1.getID(), contact.getGroundStationID());
        assertEquals(sat2.getID(), contact.getSatelliteID());
        assertEquals(5, contact.getOrbitNumber());
        assertEquals(contact3.getStartTime(), contact.getStartTime());
        assertEquals(contact3.getEndTime(), contact.getEndTime());

        contact = contacts.get(1);
        assertEquals(contact2.getInstanceID(), contact.getInstanceID());
        assertEquals(gs1.getID(), contact.getGroundStationID());
        assertEquals(sat1.getID(), contact.getSatelliteID());
        assertEquals(11, contact.getOrbitNumber());
        assertEquals(contact2.getStartTime(), contact.getStartTime());
        assertEquals(contact2.getEndTime(), contact.getEndTime());

        contacts = dao.getLocationContactEventsForGroundStation(gs1.getID(), now + 1000 * 60 * 3, Long.MAX_VALUE);
        assertEquals(4, contacts.size());

        assertEquals(contact1.getInstanceID(), contacts.get(0).getInstanceID());
        assertEquals(contact3.getInstanceID(), contacts.get(1).getInstanceID());
        assertEquals(contact2.getInstanceID(), contacts.get(2).getInstanceID());
        assertEquals(contact4.getInstanceID(), contacts.get(3).getInstanceID());

        contacts = dao.getLocationContactEventsForGroundStation(gs1.getID(), now + 1000 * 1000 * 1000, Long.MAX_VALUE);
        assertEquals(0, contacts.size());

        // For GS and sat
        contact = dao.getNextLocationContactEventFor(gs1.getID(), sat1.getID());
        assertEquals(contact1.getInstanceID(), contact.getInstanceID());
        assertEquals(gs1.getID(), contact.getGroundStationID());
        assertEquals(sat1.getID(), contact.getSatelliteID());
        assertEquals(10, contact.getOrbitNumber());

        contact = dao.getNextLocationContactEventFor(gs1.getID(), sat2.getID());
        assertEquals(contact3.getInstanceID(), contact.getInstanceID());
        assertEquals(gs1.getID(), contact.getGroundStationID());
        assertEquals(sat2.getID(), contact.getSatelliteID());
        assertEquals(5, contact.getOrbitNumber());

        // For GS, sat and starting point
        contacts = dao.getLocationContactEventsFor(gs1.getID(), sat1.getID(), now + 1000 * 60 * 11, now + 1000 * 60 * 15);
        assertEquals(1, contacts.size());
        contact = contacts.get(0);
        assertEquals(contact2.getInstanceID(), contact.getInstanceID());
        assertEquals(gs1.getID(), contact.getGroundStationID());
        assertEquals(sat1.getID(), contact.getSatelliteID());
        assertEquals(11, contact.getOrbitNumber());

        contacts = dao.getLocationContactEventsFor(gs1.getID(), sat1.getID(), now + 1000 * 60 * 11, Long.MAX_VALUE);
        assertEquals(2, contacts.size());
        assertEquals(contact2.getInstanceID(), contacts.get(0).getInstanceID());
        assertEquals(contact4.getInstanceID(), contacts.get(1).getInstanceID());
    }

    @Test
    public void testTimestampClashInFromToMethods() throws Exception {
        Satellite sat1 = new Satellite("SAT1", "SAT1");

        OrbitalState state1_1 = new OrbitalState("SAT1/State1", "State1_1");
        state1_1.setSatelliteId(sat1.getID());
        state1_1.setTimestamp(10);
        state1_1.setVersion(1);

        OrbitalState state1_2 = new OrbitalState("SAT1/State1", "State1_2");
        state1_2.setSatelliteId(sat1.getID());
        state1_2.setTimestamp(10);
        state1_2.setVersion(2);

        OrbitalState state2_1 = new OrbitalState("SAT1/State2", "State2_1");
        state2_1.setSatelliteId(sat1.getID());
        state2_1.setTimestamp(20);
        state2_1.setVersion(2);

        OrbitalState state2_2 = new OrbitalState("SAT1/State2", "State2_2");
        state2_2.setSatelliteId(sat1.getID());
        state2_2.setTimestamp(20);
        state2_2.setVersion(1);

        saver.save(sat1);
        saver.save(state1_1);
        saver.save(state1_2);
        saver.save(state2_1);
        saver.save(state2_2);

        List<OrbitalState> states = dao.getOrbitalStatesFor(sat1.getID(), 0, 100);

        assertEquals(2, states.size());
        assertEquals(10, states.get(0).getTimestamp());
        assertEquals(2, states.get(0).getVersion());
        assertEquals(20, states.get(1).getTimestamp());
        assertEquals(2, states.get(1).getVersion());
    }
}
