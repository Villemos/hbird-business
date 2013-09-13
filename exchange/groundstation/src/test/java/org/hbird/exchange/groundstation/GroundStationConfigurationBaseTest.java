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
package org.hbird.exchange.groundstation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class GroundStationConfigurationBaseTest {

    private GroundStationConfigurationBase config;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        config = new GroundStationConfigurationBase();
    }

    /**
     * Test method for
     * {@link org.hbird.exchange.groundstation.GroundStationConfigurationBase#GroundStationConfigurationBase(String, String, long, String)}
     * .
     */
    @Test
    public void testGroundStationConfigurationBase() {
        config = new GroundStationConfigurationBase("S-ID", "0.0.9", 4000L, "GS-ID");
        assertEquals("S-ID", config.getServiceId());
        assertEquals("0.0.9", config.getServiceVersion());
        assertEquals(4000L, config.getHeartBeatInterval());
        assertEquals("GS-ID", config.getGroundstationId());
    }

    /**
     * Test method for {@link org.hbird.exchange.groundstation.GroundStationConfigurationBase#getGroundstationId()}.
     */
    @Test
    public void testGetGroundstationId() {
        testSetGroundstationId();
    }

    /**
     * Test method for
     * {@link org.hbird.exchange.groundstation.GroundStationConfigurationBase#setGroundstationId(java.lang.String)}.
     */
    @Test
    public void testSetGroundstationId() {
        assertNull(config.getGroundstationId());
        config.setGroundstationId("A");
        assertEquals("A", config.getGroundstationId());
        config.setGroundstationId("V");
        assertEquals("V", config.getGroundstationId());
    }
}
