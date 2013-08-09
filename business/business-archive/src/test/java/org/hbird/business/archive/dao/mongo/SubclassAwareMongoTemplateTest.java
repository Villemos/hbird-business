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
import static org.junit.Assert.assertNotNull;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.net.UnknownHostException;
import java.util.List;

import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.core.Part;
import org.hbird.exchange.core.State;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.Satellite;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.Mongo;

// NB: Don't forget to exclude from build
public class SubclassAwareMongoTemplateTest {
    private SubclassAwareMongoTemplate template1;
    private Mongo mongo;

    private static final String DATABASE_NAME = "hbird_subclass_test";

    @Before
    public void setUp() throws UnknownHostException {
        mongo = new Mongo("localhost");
        mongo.dropDatabase(DATABASE_NAME);

        template1 = new SubclassAwareMongoTemplate(mongo, DATABASE_NAME);
    }

    public void tearDown() {
        mongo.dropDatabase(DATABASE_NAME);
        mongo.close();
    }

    @Test
    public void testStraightSaveRetrieve() {
        Satellite sat = new Satellite("SAT1", "SAT1");
        sat.setVersion(1);

        template1.save(sat);

        Satellite retrieved = template1.findOne(query(where("ID").is("SAT1")), Satellite.class);

        assertNotNull(retrieved);
        assertEquals("SAT1", retrieved.getID());
        assertEquals(1, retrieved.getVersion());
    }

    @Test
    public void testRetrievingBySuperclass() {
        Satellite sat = new Satellite("SAT1", "SAT1");
        sat.setVersion(1);

        template1.save(sat);

        Part retrieved = template1.findOne(query(where("ID").is("SAT1")), Part.class);

        assertNotNull(retrieved);
        assertEquals("SAT1", retrieved.getID());
        assertEquals(1, retrieved.getVersion());

        assertEquals(Satellite.class, retrieved.getClass());
    }

    @Test
    public void testFind() {
        Satellite sat = new Satellite("SAT1", "SAT1");
        GroundStation gs = new GroundStation("GS1", "GS1");

        State s1 = new State("SAT1/State1", "State1");
        s1.setApplicableTo(sat.getID());

        State s2 = new State("SAT1/State2", "State2");
        s2.setApplicableTo(sat.getID());

        State s3 = new State("GS1/State1", "State1");
        s3.setApplicableTo(gs.getID());

        template1.save(sat);
        template1.save(gs);
        template1.save(s1);
        template1.save(s2);
        template1.save(s3);

        List<EntityInstance> retrieved = template1.find(query(where("applicableTo").is(sat.getID())), EntityInstance.class);

        assertEquals(2, retrieved.size());
        assertEquals(State.class, retrieved.get(0).getClass());
        assertEquals(State.class, retrieved.get(1).getClass());
    }

    @Test
    public void testGetAll() {
        Satellite sat = new Satellite("SAT1", "SAT1");
        GroundStation gs = new GroundStation("GS1", "GS1");
        OrbitalState state = new OrbitalState("SAT1/State", "State");

        template1.save(sat);
        template1.save(gs);
        template1.save(state);

        List<Part> parts = template1.findAll(Part.class);

        assertEquals(2, parts.size());

        Part part1, part2;

        if (parts.get(0).getID().equals(sat.getID())) {
            part1 = parts.get(0);
            part2 = parts.get(1);
        }
        else {
            part1 = parts.get(1);
            part2 = parts.get(0);
        }

        assertEquals(sat.getID(), part1.getID());
        assertEquals(gs.getID(), part2.getID());

        List<EntityInstance> entities = template1.findAll(EntityInstance.class);

        assertEquals(3, entities.size());
    }

    @Test
    public void testCacheConsistency() throws UnknownHostException {
        Mongo mongo2 = new Mongo("localhost");
        SubclassAwareMongoTemplate template2 = new SubclassAwareMongoTemplate(mongo2, DATABASE_NAME);

        Satellite sat = new Satellite("SAT1", "SAT1");
        template1.save(sat);

        List<Part> parts = template2.findAll(Part.class);

        assertEquals(1, parts.size());
        assertEquals(sat.getID(), parts.get(0).getID());
        assertEquals(Satellite.class, parts.get(0).getClass());
    }
}
