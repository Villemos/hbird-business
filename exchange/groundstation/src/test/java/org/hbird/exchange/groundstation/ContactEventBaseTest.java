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
public class ContactEventBaseTest {

    public static final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String GS_ID = "GS-ID";
    public static final String SAT_ID = "SAT-ID";

    private ContactEventBase base;

    /**
     * @throws java.lang.Exception
     */
    @SuppressWarnings("serial")
    @Before
    public void setUp() throws Exception {
        base = new ContactEventBase(ID, NAME) {
        };
    }

    @Test
    public void testGetGroundStationId() throws Exception {
        testSetGroundStationId();
    }

    @Test
    public void testSetGroundStationId() throws Exception {
        assertNull(base.getGroundStationId());
        base.setGroundStationId(GS_ID);
        assertEquals(GS_ID, base.getGroundStationId());
    }

    @Test
    public void testGetSatelliteId() throws Exception {
        testSetSatelliteId();
    }

    @Test
    public void testSetSatelliteId() throws Exception {
        assertNull(base.getSatelliteId());
        base.setSatelliteId(SAT_ID);
        assertEquals(SAT_ID, base.getSatelliteId());
    }
}
