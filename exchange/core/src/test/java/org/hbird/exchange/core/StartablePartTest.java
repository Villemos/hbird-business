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
/**
 * 
 */
package org.hbird.exchange.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class StartablePartTest {

    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String DRIVER_NAME = "driver name";
    public static final String CONFIGURATOR = "configurator";

    private StartablePart part;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        part = new StartablePart(NAME, DESCRIPTION, DRIVER_NAME);
    }

    /**
     * Test method for
     * {@link org.hbird.exchange.core.StartablePart#StartablePart(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testStartablePart() {
        assertEquals(NAME, part.getName());
        assertNull(part.getConfigurator());
        assertEquals(DESCRIPTION, part.getDescription());
        assertEquals(DRIVER_NAME, part.getDriverName());
        assertEquals(StartablePart.DEFAULT_HEARTBEAT, part.getHeartbeat());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.StartablePart#getDriverName()}.
     */
    @Test
    public void testGetDriverName() {
        testSetDriverName();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.StartablePart#setDriverName(java.lang.String)}.
     */
    @Test
    public void testSetDriverName() {
        assertEquals(DRIVER_NAME, part.getDriverName());
        part.setDriverName(null);
        assertNull(part.getDriverName());
        part.setDriverName(DRIVER_NAME + DRIVER_NAME);
        assertEquals(DRIVER_NAME + DRIVER_NAME, part.getDriverName());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.StartablePart#getConfigurator()}.
     */
    @Test
    public void testGetConfigurator() {
        testSetConfigurator();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.StartablePart#setConfigurator(java.lang.String)}.
     */
    @Test
    public void testSetConfigurator() {
        assertNull(part.getConfigurator());
        part.setConfigurator(CONFIGURATOR);
        assertEquals(CONFIGURATOR, part.getConfigurator());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.StartablePart#getHeartbeat()}.
     */
    @Test
    public void testGetHeartbeat() {
        testSetHeartbeat();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.StartablePart#setHeartbeat(long)}.
     */
    @Test
    public void testSetHeartbeat() {
        assertEquals(StartablePart.DEFAULT_HEARTBEAT, part.getHeartbeat());
        part.setHeartbeat(0);
        assertEquals(0, part.getHeartbeat());
        part.setHeartbeat(-1 * StartablePart.DEFAULT_HEARTBEAT);
        assertEquals(-1 * StartablePart.DEFAULT_HEARTBEAT, part.getHeartbeat());
    }
}
