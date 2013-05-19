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
package org.hbird.business.navigation.orekit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.hbird.exchange.navigation.GeoLocation;
import org.junit.Before;
import org.junit.Test;
import org.orekit.bodies.GeodeticPoint;

/**
 *
 */
public class NavigationUtilitiesTest {

    private static final double LATITUDE_DEG = 58.37D;
    private static final double LONGITUDE_DEG = 26.73D;

    private static final double LATITUDE_RAD = Math.toRadians(LATITUDE_DEG);
    private static final double LONGITUDE_RAD = Math.toRadians(LONGITUDE_DEG);

    private static final double ALTITUDE = 59.0D;

    private GeoLocation gl;
    private GeodeticPoint gp;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        gl = new GeoLocation(LATITUDE_DEG, LONGITUDE_DEG, ALTITUDE);
        gp = new GeodeticPoint(LATITUDE_RAD, LONGITUDE_RAD, ALTITUDE);
    }

    @Test
    public void testToGeodeticPoint() {
        GeodeticPoint p = NavigationUtilities.toGeodeticPoint(gl);
        assertNotNull(p);
        assertEquals(gp.getLatitude(), p.getLatitude(), 0.0D);
        assertEquals(gp.getLongitude(), p.getLongitude(), 0.0D);
        assertEquals(gp.getAltitude(), p.getAltitude(), 0.0D);
    }

    @Test
    public void testToGeodeticLocation() {
        GeoLocation g = NavigationUtilities.toGeoLocation(gp);
        assertNotNull(g);
        assertEquals(gl.getLatitude(), g.getLatitude(), 0.0D);
        assertEquals(gl.getLongitude(), g.getLongitude(), 0.0D);
        assertEquals(gl.getAltitude(), g.getAltitude(), 0.0D);
    }
}
