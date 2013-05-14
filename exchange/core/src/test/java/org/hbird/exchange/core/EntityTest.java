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
package org.hbird.exchange.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class EntityTest {

    private static final String ID = "ENTITY-ID";
    private static final String NAME = "ENTITY-NAME";

    private Entity entity;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        entity = new Entity(ID, NAME);
    }

    @Test
    public void testEntity() throws Exception {
        assertNotNull(entity);
        assertEquals(ID, entity.getID());
        assertEquals(NAME, entity.getName());
        assertNull(entity.getDescription());
        assertNull(entity.getIssuedBy());
    }

    @Test
    public void testGetName() throws Exception {
        testSetName();
    }

    @Test
    public void testSetName() throws Exception {
        assertEquals(NAME, entity.getName());
        entity.setName(null);
        assertNull(entity.getName());
        entity.setName("");
        assertEquals("", entity.getName());
        entity.setName(" ");
        assertEquals(" ", entity.getName());
        entity.setName("\n");
        assertEquals("\n", entity.getName());
        entity.setName("HBird");
        assertEquals("HBird", entity.getName());
    }

    @Test
    public void testGetDescription() throws Exception {
        testSetDescription();
    }

    @Test
    public void testSetDescription() throws Exception {
        assertNull(entity.getDescription());
        entity.setDescription("");
        assertEquals("", entity.getDescription());
        entity.setDescription(" ");
        assertEquals(" ", entity.getDescription());
        entity.setDescription("\n");
        assertEquals("\n", entity.getDescription());
        entity.setDescription("Description");
        assertEquals("Description", entity.getDescription());
        entity.setDescription(null);
        assertNull(entity.getDescription());
    }

    @Test
    public void testGetID() throws Exception {
        testSetID();
    }

    @Test
    public void testGetIssuedBy() throws Exception {
        testSetIssuedBy();
    }

    @Test
    public void testSetIssuedBy() throws Exception {
        assertNull(entity.getIssuedBy());
        entity.setIssuedBy("");
        assertEquals("", entity.getIssuedBy());
        entity.setIssuedBy(" ");
        assertEquals(" ", entity.getIssuedBy());
        entity.setIssuedBy("\n");
        assertEquals("\n", entity.getIssuedBy());
        entity.setIssuedBy(null);
        assertNull(entity.getIssuedBy());
    }

    @Test
    public void testSetID() throws Exception {
        assertEquals(ID, entity.getID());
        entity.setID("");
        assertEquals("", entity.getID());
        entity.setID(" ");
        assertEquals(" ", entity.getID());
        entity.setID("\n");
        assertEquals("\n", entity.getID());
        entity.setID("HBird");
        assertEquals("HBird", entity.getID());
        entity.setID(null);
        assertEquals(null, entity.getID());
    }
}
