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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class EntityInstanceTest implements Serializable {

    private static final long serialVersionUID = -8085645472151545191L;

    private static final String ID = "ID";
    private static final String NAME = "NAME";

    private TestEntity entity;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        entity = new TestEntity(ID, NAME) {
            private static final long serialVersionUID = 630577884498187832L;
        };
    }

    @Test
    public void testEntityInstance() throws Exception {
        assertNotNull(entity);
        assertEquals(ID, entity.getID());
        assertEquals(NAME, entity.getName());
        long ts = entity.getTimestamp();
        long now = System.currentTimeMillis();
        assertEquals(ID + EntityInstance.INSTANCE_ID_SEPARATOR + ts, entity.getInstanceID());
        assertTrue(ts <= now);
        assertTrue(ts > now - 1000L * 30);
    }

    @Test
    public void testGetTimestamp() throws Exception {
        testSetTimestamp();
    }

    @Test
    public void testSetTimestamp() throws Exception {
        long ts = entity.getTimestamp();
        long now = System.currentTimeMillis();
        assertTrue(ts <= now);
        assertTrue(ts > now - 1000L * 30);
        entity.setTimestamp(-1L);
        assertEquals(-1L, entity.getTimestamp());
        entity.setTimestamp(0L);
        assertEquals(0L, entity.getTimestamp());
        entity.setTimestamp(1L);
        assertEquals(1L, entity.getTimestamp());
    }

    @Test
    public void testGetInstanceID() throws Exception {
        long ts = entity.getTimestamp();
        assertEquals(ID + EntityInstance.INSTANCE_ID_SEPARATOR + ts, entity.getInstanceID());
        entity.setTimestamp(-1010L);
        assertEquals(ID + EntityInstance.INSTANCE_ID_SEPARATOR + "-1010", entity.getInstanceID());
        entity.setTimestamp(-0L);
        assertEquals(ID + EntityInstance.INSTANCE_ID_SEPARATOR + "0", entity.getInstanceID());
    }

    @Test
    public void testCloneEntity() throws Exception {
        EntityInstance e1 = entity.cloneEntity();
        assertEquals(entity.getID(), e1.getID());
        assertEquals(entity.getName(), e1.getName());
        assertEquals(entity.getClass(), e1.getClass());
        assertEquals(entity.getDescription(), e1.getDescription());
        assertEquals(entity.getIssuedBy(), e1.getIssuedBy());
        assertNotSame(entity.getTimestamp(), e1.getTimestamp());
        assertNotSame(entity.getInstanceID(), e1.getInstanceID());
    }

    @Test
    public void testPrettyPrint() throws Exception {
        long ts = entity.getTimestamp();
        String s = entity.toString();
        assertNotNull(s);
        assertTrue(s.contains(ID + EntityInstance.INSTANCE_ID_SEPARATOR + ts));
        assertTrue(s.contains(entity.getClass().getSimpleName()));
        assertTrue(s.contains(NAME));
    }

    @Test
    public void testCompareTo() throws Exception {
        EntityInstance e1 = entity.cloneEntity();
        EntityInstance e2 = entity.cloneEntity();
        assertTrue(entity.equals(entity));
        assertFalse(entity.equals(e1));
        assertFalse(entity.equals(e2));
        assertFalse(e1.equals(entity));
        assertTrue(e1.equals(e1));
        assertFalse(e1.equals(e2));
        assertFalse(e2.equals(entity));
        assertFalse(e2.equals(e1));
        assertTrue(e2.equals(e2));
    }

    public static class TestEntity extends EntityInstance {

        private static final long serialVersionUID = 7498173703681828872L;

        public TestEntity(String ID, String name) {
            super(ID, name);
        }
    }
}
