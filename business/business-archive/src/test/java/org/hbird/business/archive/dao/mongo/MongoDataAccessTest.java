package org.hbird.business.archive.dao.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.core.Metadata;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
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

class TestEntityInstance extends EntityInstance {
    private static final long serialVersionUID = 5791358392868382015L;
    private String testField;

    public TestEntityInstance(String ID, String name, String testField) {
        super(ID, name);

        this.testField = testField;
    }

    public String getTestField() {
        return testField;
    }
}

public class MongoDataAccessTest {
    private static final String TEST_DATABASE_NAME = "hbird_test";

    private MongoDataAccess dao;
    private Mongo mongo;

    @Before
    public void setUp() throws UnknownHostException {
        mongo = new Mongo("localhost", 27017);

        // MongoTemplate template = new MongoTemplate(mongo, TEST_DATABASE_NAME);
        MongoTemplate template = new SubclassAwareMongoTemplate(mongo, TEST_DATABASE_NAME);
        dao = new MongoDataAccess(template);
    }

    @After
    public void tearDown() {
        mongo.dropDatabase(TEST_DATABASE_NAME);
        mongo.close();
    }

    @Test
    public void testSavingAndRetrievingOneByID() throws Exception {
        String id = "foo";
        String name = "an entity";

        TestEntityInstance entity = new TestEntityInstance(id, name, name);

        dao.save(entity);
        TestEntityInstance retrieved = dao.getById(id, TestEntityInstance.class);

        assertEquals(id, retrieved.getID());
        assertEquals(name, retrieved.getName());
    }

    @Test
    public void testOverwritingByInstanceID() throws Exception {
        String id = "foo";
        String name1 = "first entity";
        String name2 = "second entity";

        TestEntityInstance first = new TestEntityInstance(id, name1, name1);
        first.setVersion(0);
        TestEntityInstance second = new TestEntityInstance(id, name2, name2);
        second.setVersion(0);

        assertEquals(first.getInstanceID(), second.getInstanceID());

        TestEntityInstance retrieved;

        dao.save(first);

        retrieved = dao.getByInstanceId(first.getInstanceID(), TestEntityInstance.class);
        assertEquals(first.getName(), retrieved.getName());

        dao.save(second);

        retrieved = dao.getByInstanceId(second.getInstanceID(), TestEntityInstance.class);
        assertEquals(second.getName(), retrieved.getName());

        List<TestEntityInstance> all = dao.getAll(TestEntityInstance.class);

        assertEquals(1, all.size());
    }

    @Test
    public void testGetAllReturnsLastInstances() throws Exception {
        Satellite sat1_1 = new Satellite("SAT1", "SAT1");
        sat1_1.setTimestamp(10);
        sat1_1.setVersion(10);

        Satellite sat1_2 = new Satellite("SAT1", "SAT1");
        sat1_2.setTimestamp(15);
        sat1_2.setVersion(15);

        Satellite sat2_1 = new Satellite("SAT2", "SAT2");
        sat2_1.setTimestamp(3);
        sat2_1.setVersion(3);

        dao.save(sat1_1);
        dao.save(sat1_2);
        dao.save(sat2_1);

        List<Satellite> satellites = dao.getAll(Satellite.class);
        assertEquals(2, satellites.size());

        Satellite sat1, sat2;

        if ("SAT1".equals(satellites.get(0))) {
            sat1 = satellites.get(0);
            sat2 = satellites.get(1);
        }
        else {
            sat1 = satellites.get(1);
            sat2 = satellites.get(0);
        }

        assertEquals("SAT1", sat1.getID());
        assertEquals(15, sat1.getVersion());
        assertEquals("SAT2", sat2.getID());
        assertEquals(3, sat2.getVersion());
    }

    @Test
    public void testSavingAndRetrievingOneByInstanceID() throws Exception {
        String id = "foo";
        String name = "an entity instance";
        String testField = "bar";

        TestEntityInstance entity = new TestEntityInstance(id, name, testField);

        dao.save(entity);
        TestEntityInstance retrieved = dao.getByInstanceId(entity.getInstanceID(), TestEntityInstance.class);

        assertEquals(id, retrieved.getID());
        assertEquals(name, retrieved.getName());
        assertEquals(testField, retrieved.getTestField());

        assertEquals(entity.getTimestamp(), retrieved.getTimestamp());
        assertEquals(entity.getVersion(), retrieved.getVersion());
    }

    @Test
    public void testSavingManyInstancesAndRetrievingLatestByID() throws Exception {
        String id = "foo";

        TestEntityInstance first = new TestEntityInstance(id, id, id);
        first.setVersion(0);
        TestEntityInstance second = new TestEntityInstance(id, id, id);
        second.setVersion(1);

        assertTrue("Versions of two successively created objects are different", first.getVersion() < second.getVersion());

        dao.save(second);
        dao.save(first);

        TestEntityInstance retrieved = dao.getById(id, TestEntityInstance.class);

        assertEquals(id, retrieved.getID());
        assertEquals(second.getVersion(), retrieved.getVersion());
        assertEquals(second.getInstanceID(), retrieved.getInstanceID());
    }

    @Test
    public void testSavingManyAndRetrievingAll() throws Exception {
        TestEntityInstance ent1 = new TestEntityInstance("foo", "foo", "foo");
        TestEntityInstance ent2 = new TestEntityInstance("bar", "bar", "bar");

        dao.save(ent1);
        dao.save(ent2);

        List<TestEntityInstance> retrieved = dao.getAll(TestEntityInstance.class);

        assertEquals(2, retrieved.size());
        assertTrue(retrieved.get(0).getID().equals(ent1.getID()) && retrieved.get(1).getID().equals(ent2.getID()) ||
                retrieved.get(1).getID().equals(ent1.getID()) && retrieved.get(0).getID().equals(ent2.getID()));
    }

    @Test
    public void testGetParameter() throws Exception {
        String names[] = { "PAR1", "PAR2", "PAR1", "PAR3" };
        int values[] = { 5, 100, 10, 16 };
        int versions[] = { 10, 20, 30, 25 };

        for (int i = 0; i < names.length; i++) {
            Parameter param = new Parameter(names[i], names[i]);
            param.setValue(values[i]);
            param.setTimestamp(versions[i]);
            param.setVersion(versions[i]);

            dao.save(param);
        }

        // Basic retrieval by name
        Parameter par = dao.getParameter("PAR1");

        assertEquals("PAR1", par.getName());
        assertEquals(values[2], par.getValue());
        assertEquals(versions[2], par.getVersion());

        par = dao.getParameter("PAR2");
        assertEquals("PAR2", par.getName());
        assertEquals(values[1], par.getValue());
        assertEquals(versions[1], par.getVersion());

        // Exception if no sample found
        try { // TODO: Ugly, need to split the test to use juni4 expected exception assertions
            par = dao.getParameter("PAR0");
            System.out.println("DAO return value when no object available: " + par);
            fail("getParameter must fail when no samples available");
        }
        catch (Exception e) {
        }

        // Retrieving history
        List<Parameter> params = dao.getParameter("PAR1", 0, 1000);
        assertEquals(2, params.size());
        assertEquals(values[0], params.get(0).getValue());
        assertEquals(values[2], params.get(1).getValue());

        params = dao.getParameter("PAR1", 15, 1000);
        assertEquals(1, params.size());
        assertEquals(values[2], params.get(0).getValue());

        params = dao.getParameter("PAR1", 0, 10);
        assertEquals(1, params.size());
        assertEquals(values[0], params.get(0).getValue());

        params = dao.getParameter("PAR1", 40, 1000);
        assertEquals(0, params.size());
    }

    @Test
    public void testGetState() throws Exception {
        GroundStation gs1 = new GroundStation("GS1", "GS1");
        Satellite sat1 = new Satellite("SAT1", "SAT1");

        State state1 = new State("foo", "foo");
        state1.setApplicableTo(gs1.getID());
        state1.setVersion(20);
        state1.setTimestamp(20);

        State state2 = new State("bar", "bar");
        state2.setApplicableTo(sat1.getID());
        state2.setVersion(30);
        state2.setTimestamp(30);

        State state3 = new State("baz", "baz");
        state3.setApplicableTo(gs1.getID());
        state3.setVersion(10);
        state3.setTimestamp(10);

        dao.save(state1);
        dao.save(state2);
        dao.save(state3);

        // getState(applicableTo)
        List<State> states = dao.getState(gs1.getID());
        assertEquals(2, states.size());
        assertEquals(gs1.getID(), states.get(0).getApplicableTo());
        assertEquals(gs1.getID(), states.get(1).getApplicableTo());
        assertEquals(state1.getVersion(), states.get(0).getVersion());
        assertEquals(state3.getVersion(), states.get(1).getVersion());

        states = dao.getState(sat1.getID());
        assertEquals(1, states.size());
        assertEquals(sat1.getID(), states.get(0).getApplicableTo());
        assertEquals(state2.getVersion(), states.get(0).getVersion());

        states = dao.getState("wrongid");
        assertEquals(0, states.size());

        // getState(applicableTo, from, to)
        states = dao.getState(gs1.getID(), 0, 100);
        assertEquals(2, states.size());
        assertEquals(gs1.getID(), states.get(0).getApplicableTo());
        assertEquals(gs1.getID(), states.get(1).getApplicableTo());
        assertEquals(state3.getVersion(), states.get(0).getVersion());
        assertEquals(state1.getVersion(), states.get(1).getVersion());

        // getState(names)
        List<String> names = new ArrayList<String>();
        names.add("foo");
        names.add("bar");
        names.add("foobad");

        states = dao.getStates(names);
        assertEquals(2, states.size());
        assertTrue(state1.getVersion() == states.get(0).getVersion() || state1.getVersion() == states.get(1).getVersion());
        assertTrue(state2.getVersion() == states.get(0).getVersion() || state2.getVersion() == states.get(1).getVersion());
    }

    @Test
    public void testGetStateWhenMultipleInstances() throws Exception {
        GroundStation gs1 = new GroundStation("GS1", "GS1");

        State state1 = new State("State1", "State1");
        state1.setApplicableTo(gs1.getID());
        state1.setTimestamp(10);
        state1.setVersion(10);

        State state2 = new State("State2", "State2");
        state2.setApplicableTo(gs1.getID());
        state2.setTimestamp(11);
        state2.setVersion(11);

        State state3 = new State("State1", "State1");
        state3.setApplicableTo(gs1.getID());
        state3.setTimestamp(5);
        state3.setVersion(5);

        dao.save(state1);
        dao.save(state2);
        dao.save(state3);

        List<State> states = dao.getState(gs1.getID());

        assertEquals(2, states.size());

        State s1 = states.get(0);
        State s2 = states.get(1);

        assertTrue(s1.getID().equals("State1") || s1.getID().equals("State2"));

        if (s1.getID().equals("State2")) {
            State temp = s1;
            s1 = s2;
            s2 = temp;
        }

        assertEquals(state1.getID(), s1.getID());
        assertEquals(gs1.getID(), s1.getApplicableTo());
        assertEquals(state1.getVersion(), s1.getVersion());

        assertEquals(state2.getID(), s2.getID());
        assertEquals(gs1.getID(), s2.getApplicableTo());
        assertEquals(state2.getVersion(), s2.getVersion());

        //

        List<String> names = new ArrayList<String>();
        names.add(state1.getName());

        states = dao.getStates(names);

        assertEquals(1, states.size());
        assertEquals(state1.getName(), states.get(0).getName());
        assertEquals(state1.getVersion(), states.get(0).getVersion());
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

        dao.save(tle1);
        dao.save(tle2);

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

        dao.save(tle1);
        dao.save(tle2);

        dao.save(state1);
        dao.save(state2);
        dao.save(state3);

        OrbitalState state = dao.getOrbitalStateFor(sat1.getID());
        assertEquals(tle2.getInstanceID(), state.getDerivedFromId());
        assertEquals(state3.getVersion(), state.getVersion());

        // TODO: Should it ignore state2 in this case?
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

        LocationContactEvent contact1 = new LocationContactEvent(gs1.getID(), sat1.getID(), 10);
        contact1.setStartTime(now + 1000 * 60 * 2);
        contact1.setEndTime(now + 1000 * 60 * 10);

        LocationContactEvent contact2 = new LocationContactEvent(gs1.getID(), sat1.getID(), 11);
        contact2.setStartTime(now + 1000 * 60 * 12);
        contact2.setEndTime(now + 1000 * 60 * 14);

        LocationContactEvent contact3 = new LocationContactEvent(gs1.getID(), sat2.getID(), 5);
        contact3.setStartTime(now + 1000 * 60 * 4);
        contact3.setEndTime(now + 1000 * 60 * 14);

        dao.save(gs1);
        dao.save(sat1);
        dao.save(sat2);
        dao.save(contact1);
        dao.save(contact2);
        dao.save(contact3);

        // For GS
        LocationContactEvent contact = dao.getNextLocationContactEventForGroundStation(gs1.getID());
        assertEquals(gs1.getID(), contact.getGroundStationID());
        assertEquals(sat1.getID(), contact.getSatelliteID());
        assertEquals(10, contact.getOrbitNumber());
        assertEquals(contact1.getStartTime(), contact.getStartTime());

        // For GS and starting point
        contact = dao.getNextLocationContactEventForGroundStation(gs1.getID(), now + 1000 * 60 * 3);
        assertEquals(gs1.getID(), contact.getGroundStationID());
        assertEquals(sat2.getID(), contact.getSatelliteID());
        assertEquals(5, contact.getOrbitNumber());
        assertEquals(contact3.getStartTime(), contact.getStartTime());

        try {
            contact = dao.getNextLocationContactEventForGroundStation(gs1.getID(), now + 1000 * 1000 * 1000);
            fail("getNextLocationContactEventForGroundStation must throw exception when there are no contacts in database");
        }
        catch (Exception e) {
        }

        // For GS and sat
        contact = dao.getNextLocationContactEventFor(gs1.getID(), sat1.getID());
        assertEquals(gs1.getID(), contact.getGroundStationID());
        assertEquals(sat1.getID(), contact.getSatelliteID());
        assertEquals(10, contact.getOrbitNumber());

        contact = dao.getNextLocationContactEventFor(gs1.getID(), sat2.getID());
        assertEquals(gs1.getID(), contact.getGroundStationID());
        assertEquals(sat2.getID(), contact.getSatelliteID());
        assertEquals(5, contact.getOrbitNumber());

        // For GS, sat and starting point
        contact = dao.getNextLocationContactEventFor(gs1.getID(), sat1.getID(), now + 1000 * 60 * 3);
        assertEquals(gs1.getID(), contact.getGroundStationID());
        assertEquals(sat1.getID(), contact.getSatelliteID());
        assertEquals(11, contact.getOrbitNumber());
    }

    @Test
    public void testGetMetadata() throws Exception {
        String gsId = "GS1";
        String satId = "SAT1";
        GroundStation gs = new GroundStation(gsId, gsId);
        Satellite sat = new Satellite(satId, satId);

        Map<String, Object> entries1 = new HashMap<String, Object>();
        entries1.put("par1", 1);
        entries1.put("par2", 2);

        Metadata data1 = new Metadata("data1", "data1");
        data1.setMetadata(entries1);
        data1.setApplicableTo(gs.getID());

        Metadata data2 = new Metadata("data2", "data2");
        data2.setMetadata(Collections.<String, Object> singletonMap("par3", 5));
        data2.setApplicableTo("foo");

        dao.save(gs);
        dao.save(sat);
        dao.save(data1);
        dao.save(data2);

        List<Metadata> data = dao.getMetadata(gs.getID());
        assertEquals(1, data.size());
        assertEquals(1, data.get(0).getMetadata().get("par1"));
        assertEquals(2, data.get(0).getMetadata().get("par2"));

        data = dao.getMetadata(sat.getID());
        assertEquals(0, data.size());
    }

    @Test
    public void testGetMetadataWhenMultipleInstances() throws Exception {
        String gsId = "GS1";
        String satId = "SAT1";
        GroundStation gs = new GroundStation(gsId, gsId);
        Satellite sat = new Satellite(satId, satId);

        Map<String, Object> entries1 = new HashMap<String, Object>();
        entries1.put("par1", 10);

        Metadata data1 = new Metadata("data1", "data1");
        data1.setMetadata(Collections.<String, Object> singletonMap("par1", 1));
        data1.setVersion(10);
        data1.setTimestamp(10);
        data1.setApplicableTo(sat.getID());

        Metadata data1_new = new Metadata("data1", "data1");
        data1_new.setMetadata(Collections.<String, Object> singletonMap("par1", 2));
        data1_new.setVersion(15);
        data1_new.setTimestamp(15);
        data1_new.setApplicableTo(sat.getID());

        Metadata data2 = new Metadata("data2", "data2");
        data2.setMetadata(Collections.<String, Object> singletonMap("par1", 3));
        data2.setVersion(1);
        data2.setTimestamp(1);
        data2.setApplicableTo(sat.getID());

        dao.save(gs);
        dao.save(sat);
        dao.save(data1);
        dao.save(data1_new);
        dao.save(data2);

        List<Metadata> data = dao.getMetadata(sat.getID());

        assertEquals(2, data.size());

        Metadata d1, d2;

        if (data.get(0).getID().equals("data1")) {
            d1 = data.get(0);
            d2 = data.get(1);
        }
        else {
            d1 = data.get(1);
            d2 = data.get(0);
        }

        assertEquals("data1", d1.getID());
        assertEquals(sat.getID(), d1.getApplicableTo());
        assertEquals(2, d1.getMetadata().get("par1"));
        assertEquals(15, d1.getVersion());

        assertEquals("data2", d2.getID());
        assertEquals(sat.getID(), d2.getApplicableTo());
        assertEquals(3, d2.getMetadata().get("par1"));
        assertEquals(1, d2.getVersion());
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

        dao.save(sat1);
        dao.save(state1_1);
        dao.save(state1_2);
        dao.save(state2_1);
        dao.save(state2_2);

        List<OrbitalState> states = dao.getOrbitalStatesFor(sat1.getID(), 0, 100);

        assertEquals(2, states.size());
        assertEquals(10, states.get(0).getTimestamp());
        assertEquals(2, states.get(0).getVersion());
        assertEquals(20, states.get(1).getTimestamp());
        assertEquals(2, states.get(1).getVersion());
    }

}
