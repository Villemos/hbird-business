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
package org.hbird.exchange.navigation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class SatelliteTest {

    private static final String SAT_ID = "SAT-1";
    private static final String SAT_NAME = "SAT-NAME";

    private Satellite sat;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        sat = new Satellite(SAT_ID, SAT_NAME);
    }

    @Test
    public void testGetDesignator() throws Exception {
        testSetDesignator();
    }

    @Test
    public void testSetDesignator() throws Exception {
        assertNull(sat.getDesignator());
        sat.setDesignator("designator");
        assertEquals("designator", sat.getDesignator());
    }

    @Test
    public void testGetSatelliteNumber() throws Exception {
        testSetSatelliteNumber();
    }

    @Test
    public void testSetSatelliteNumber() throws Exception {
        assertNull(sat.getSatelliteNumber());
        sat.setSatelliteNumber("sat-nr");
        assertEquals("sat-nr", sat.getSatelliteNumber());
    }

    @Test
    public void testGetSatelliteID() throws Exception {
        assertEquals(SAT_ID, sat.getID());
    }

    @Test
    public void testGetUplinkFrequency() throws Exception {
        testSetUplinkFrequency();
    }

    @Test
    public void testSetUplinkFrequency() throws Exception {
        assertEquals(Satellite.DEFAULT_RADIO_UPLINK_FREQUENCY, sat.getUplinkFrequency());
        sat.setUplinkFrequency(123L);
        assertEquals(123L, sat.getUplinkFrequency());
    }

    @Test
    public void testGetDownlinkFrequency() throws Exception {
        testSetDownlinkFrequency();
    }

    @Test
    public void testSetDownlinkFrequency() throws Exception {
        assertEquals(Satellite.DEFAULT_RADIO_DOWNLINK_FREQUENCY, sat.getDownlinkFrequency());
        sat.setDownlinkFrequency(345L);
        assertEquals(345L, sat.getDownlinkFrequency());
    }

    @Test
    public void testGetSatelliteMass() throws Exception {
        testSetSatelliteMass();
    }

    @Test
    public void testSetSatelliteMass() throws Exception {
        assertEquals(Satellite.DEFAULT_SATELLITE_MASS, sat.getSatelliteMass());
        sat.setSatelliteMass(59430L);
        assertEquals(59430L, sat.getSatelliteMass());
    }
}
