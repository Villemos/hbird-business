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

import org.hbird.business.api.exceptions.ArchiveException;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.core.Metadata;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.Part;
import org.hbird.exchange.core.State;
import org.hbird.exchange.groundstation.GroundStation;
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
    private final String testField;

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

        MongoTemplate template = new MongoTemplate(mongo, TEST_DATABASE_NAME);
        // MongoTemplate template = new SubclassAwareMongoTemplate(mongo, TEST_DATABASE_NAME);
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
        String ids[] = { "PAR1", "PAR2", "PAR1", "PAR3" };
        int values[] = { 5, 100, 10, 16 };
        int versions[] = { 10, 20, 30, 25 };

        for (int i = 0; i < ids.length; i++) {
            Parameter param = new Parameter(ids[i], ids[i]);
            param.setValue(values[i]);
            param.setTimestamp(versions[i]);
            param.setVersion(versions[i]);

            dao.save(param);
        }

        // Basic retrieval by name
        Parameter par = dao.getById("PAR1", Parameter.class);

        assertEquals("PAR1", par.getName());
        assertEquals(values[2], par.getValue());
        assertEquals(versions[2], par.getVersion());

        par = dao.getById("PAR2", Parameter.class);
        assertEquals("PAR2", par.getName());
        assertEquals(values[1], par.getValue());
        assertEquals(versions[1], par.getVersion());

        // Exception if no sample found
        try { // TODO: Ugly, need to split the test to use juni4 expected exception assertions
            par = dao.getById("PAR0", Parameter.class);
            fail("getParameter must fail when no samples available");
        }
        catch (Exception e) {
        }

        // Retrieving history
        List<Parameter> params = dao.getById("PAR1", 0, 1000, Parameter.class);
        assertEquals(2, params.size());
        assertEquals(values[0], params.get(0).getValue());
        assertEquals(values[2], params.get(1).getValue());

        params = dao.getById("PAR1", 15, 1000, Parameter.class);
        assertEquals(1, params.size());
        assertEquals(values[2], params.get(0).getValue());

        params = dao.getById("PAR1", 0, 10, Parameter.class);
        assertEquals(1, params.size());
        assertEquals(values[0], params.get(0).getValue());

        params = dao.getById("PAR1", 40, 1000, Parameter.class);
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

        List<State> states = dao.getApplicableTo(gs1.getID(), State.class);
        assertEquals(2, states.size());
        assertEquals(gs1.getID(), states.get(0).getApplicableTo());
        assertEquals(gs1.getID(), states.get(1).getApplicableTo());
        if (state1.getVersion() == states.get(0).getVersion()) {
            assertEquals(state3.getVersion(), states.get(1).getVersion());
        }
        else {
            assertEquals(state3.getVersion(), states.get(0).getVersion());
            assertEquals(state1.getVersion(), states.get(1).getVersion());
        }

        states = dao.getApplicableTo(sat1.getID(), State.class);
        assertEquals(1, states.size());
        assertEquals(sat1.getID(), states.get(0).getApplicableTo());
        assertEquals(state2.getVersion(), states.get(0).getVersion());

        states = dao.getApplicableTo("wrongid", State.class);
        assertEquals(0, states.size());

        states = dao.getApplicableTo(gs1.getID(), State.class, 0, 100);
        assertEquals(2, states.size());
        assertEquals(gs1.getID(), states.get(0).getApplicableTo());
        assertEquals(gs1.getID(), states.get(1).getApplicableTo());
        if (state3.getVersion() == states.get(1).getVersion()) {
            assertEquals(state1.getVersion(), states.get(0).getVersion());
        }
        else {
            assertEquals(state3.getVersion(), states.get(0).getVersion());
            assertEquals(state1.getVersion(), states.get(1).getVersion());
        }

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

        List<State> states = dao.getApplicableTo(gs1.getID(), State.class);

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

        List<Metadata> data = dao.getApplicableTo(gs.getID(), Metadata.class);
        assertEquals(1, data.size());
        assertEquals(1, data.get(0).getMetadata().get("par1"));
        assertEquals(2, data.get(0).getMetadata().get("par2"));

        data = dao.getApplicableTo(sat.getID(), Metadata.class);
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

        List<Metadata> data = dao.getApplicableTo(sat.getID(), Metadata.class);

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
    public void testGetAllBySupertype() throws Exception {
        Satellite sat = new Satellite("SAT1", "SAT1");
        GroundStation gs = new GroundStation("GS1", "GS1");

        dao.save(sat);
        dao.save(gs);

        List<Part> parts = dao.getAllBySupertype(Part.class);

        assertEquals(2, parts.size());

        Part p1, p2;

        if (parts.get(0).getID().equals("SAT1")) {
            p1 = parts.get(0);
            p2 = parts.get(1);
        }
        else {
            p1 = parts.get(1);
            p2 = parts.get(0);
        }

        assertEquals("SAT1", p1.getID());
        assertEquals(Satellite.class, p1.getClass());

        assertEquals("GS1", p2.getID());
        assertEquals(GroundStation.class, p2.getClass());
    }

    @Test
    public void testGetDerivedFrom() throws Exception {
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

        dao.save(sat1);
        dao.save(tle1);
        dao.save(tle2);
        dao.save(state1);
        dao.save(state2);
        dao.save(state3);

        List<OrbitalState> states = dao.getDerivedFrom(tle1.getInstanceID(), OrbitalState.class);
        assertEquals(2, states.size());
        assertEquals(tle1.getInstanceID(), states.get(0).getDerivedFromId());
        assertEquals(tle1.getInstanceID(), states.get(1).getDerivedFromId());

        OrbitalState s1 = states.get(0);
        OrbitalState s2 = states.get(1);
        if (s1.getVersion() != state1.getVersion()) {
            s2 = s1;
            s1 = states.get(1);
        }
        assertEquals(state1.getVersion(), s1.getVersion());
        assertEquals(state2.getVersion(), s2.getVersion());

        states = dao.getDerivedFrom(tle1.getInstanceID(), OrbitalState.class, 100, 200);
        assertEquals(0, states.size());

        states = dao.getDerivedFrom(tle1.getInstanceID(), OrbitalState.class, 0, 19);
        assertEquals(1, states.size());
        assertEquals(tle1.getInstanceID(), states.get(0).getDerivedFromId());
        assertEquals(state1.getInstanceID(), states.get(0).getInstanceID());
    }

    @Test
    public void testGetIssuedBy() throws ArchiveException {
        String issuer1 = "op1";
        String issuer2 = "op2";

        Satellite sat1 = new Satellite("SAT1", "SAT1");
        sat1.setIssuedBy(issuer1);
        sat1.setTimestamp(10);

        Satellite sat2 = new Satellite("SAT2", "SAT2");
        sat2.setIssuedBy(issuer2);
        sat2.setTimestamp(20);

        Satellite sat3 = new Satellite("SAT3", "SAT3");
        sat3.setIssuedBy(issuer1);
        sat3.setTimestamp(30);

        dao.save(sat1);
        dao.save(sat2);
        dao.save(sat3);

        List<Satellite> sats = dao.getIssuedBy(issuer1, Satellite.class);
        assertEquals(2, sats.size());
        assertTrue(sats.get(0).getID().equals(sat1.getID()) || sats.get(1).getID().equals(sat1.getID()));
        assertTrue(sats.get(0).getID().equals(sat3.getID()) || sats.get(1).getID().equals(sat3.getID()));

        sats = dao.getIssuedBy(issuer2, Satellite.class);
        assertEquals(1, sats.size());
        assertEquals(sat2.getID(), sats.get(0).getID());

        sats = dao.getIssuedBy(issuer1, Satellite.class, 20, 30);
        assertEquals(1, sats.size());
        assertEquals(sat3.getID(), sats.get(0).getID());
    }
}
