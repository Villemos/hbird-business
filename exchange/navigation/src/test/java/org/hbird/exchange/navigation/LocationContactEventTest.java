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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;

import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.util.Dates;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class LocationContactEventTest {

    private static final String GS_ID = "GS/ID";
    private static final String SAT_ID = "SAT/ID";
    private static final String TLE_ID = "SAT/ID/TLE:2";
    private static final long NOW = System.currentTimeMillis();
    private static final long ORBIT_NUMBER = 10000023L;

    @Mock
    private ContactParameterRange range;

    @Mock
    private ExtendedContactParameterRange extendedRange;

    @Mock
    private OrbitalState satelliteState;

    private LocationContactEvent event;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        event = new LocationContactEvent(GS_ID, SAT_ID, ORBIT_NUMBER);
        inOrder = inOrder(range, extendedRange, satelliteState);
    }

    @After
    public void tearDown() {
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testLocationContactEvent() {
        assertEquals(GS_ID + EntityInstance.INSTANCE_ID_SEPARATOR + SAT_ID, event.getID());
        assertEquals(GS_ID, event.getGroundStationID());
        assertEquals(SAT_ID, event.getSatelliteID());
        assertEquals(ORBIT_NUMBER, event.getOrbitNumber());
        assertTrue(NOW <= event.getTimestamp());
        assertTrue(event.getTimestamp() <= System.currentTimeMillis());
        assertEquals(LocationContactEvent.DESCRIPTION, event.getDescription());
        assertEquals(LocationContactEvent.class.getSimpleName(), event.getName());
    }

    @Test
    public void testGetInstanceId() {
        assertEquals(GS_ID + EntityInstance.INSTANCE_ID_SEPARATOR + SAT_ID + EntityInstance.INSTANCE_ID_SEPARATOR + ORBIT_NUMBER, event.getInstanceID());
        event.setTimestamp(NOW + 10101L);
        assertEquals(GS_ID + EntityInstance.INSTANCE_ID_SEPARATOR + SAT_ID + EntityInstance.INSTANCE_ID_SEPARATOR + ORBIT_NUMBER, event.getInstanceID());
        event.setID(SAT_ID);
        assertEquals(SAT_ID + EntityInstance.INSTANCE_ID_SEPARATOR + ORBIT_NUMBER, event.getInstanceID());
        event.setID(GS_ID);
        assertEquals(GS_ID + EntityInstance.INSTANCE_ID_SEPARATOR + ORBIT_NUMBER, event.getInstanceID());
        event.setID(null);
        assertEquals("null" + EntityInstance.INSTANCE_ID_SEPARATOR + ORBIT_NUMBER, event.getInstanceID());
    }

    @Test
    public void testGetGroundStationId() {
        assertEquals(GS_ID, event.getGroundStationID());
    }

    @Test
    public void testGetSatelliteId() {
        assertEquals(SAT_ID, event.getSatelliteID());
    }

    @Test
    public void testOrbitNumber() {
        assertEquals(ORBIT_NUMBER, event.getOrbitNumber());
    }

    @Test
    public void testGetId() {
        testSetId();
    }

    @Test
    public void testSetId() {
        assertEquals(GS_ID + EntityInstance.INSTANCE_ID_SEPARATOR + SAT_ID, event.getID());
        event.setID(null);
        assertNull(event.getID());
        event.setID(GS_ID);
        assertEquals(GS_ID, event.getID());
    }

    @Test
    public void testIsInSunlight() {
        testSetInSunlight();
    }

    @Test
    public void testSetInSunlight() {
        assertFalse(event.isInSunLigth());
        event.setInSunLigth(true);
        assertTrue(event.isInSunLigth());
        event.setInSunLigth(false);
        assertFalse(event.isInSunLigth());
    }

    @Test
    public void testGetAzimuth() {
        testSetAzimuth();
    }

    @Test
    public void testSetAzimuth() {
        assertNull(event.getAzimuth());
        event.setAzimuth(range);
        assertEquals(range, event.getAzimuth());
    }

    @Test
    public void testGetElevation() {
        testSetElevation();
    }

    @Test
    public void testSetElevation() {
        assertNull(event.getElevation());
        event.setElevation(extendedRange);
        assertEquals(extendedRange, event.getElevation());
    }

    @Test
    public void testGetUplinkDoppler() {
        testSetUplinkDoppler();
    }

    @Test
    public void testSetUplinkDoppler() {
        assertNull(event.getUplinkDoppler());
        event.setUplinkDoppler(range);
        assertEquals(range, event.getUplinkDoppler());
    }

    @Test
    public void testGetDownlinkDoppler() {
        testSetDownlinkDoppler();
    }

    @Test
    public void testSetDownlinkDoppler() {
        assertNull(event.getDownlinkDoppler());
        event.setDownlinkDoppler(range);
        assertEquals(range, event.getDownlinkDoppler());
    }

    @Test
    public void testGetUplinkSignalLoss() {
        testSetUplinkSignalLoss();
    }

    @Test
    public void testSetUplinkSignalLoss() {
        assertNull(event.getUplinkSignalLoss());
        event.setUplinkSignalLoss(extendedRange);
        assertEquals(extendedRange, event.getUplinkSignalLoss());
    }

    @Test
    public void testGetDownlinkSignalLoss() {
        testSetDownlinkSignalLoss();
    }

    @Test
    public void testSetDownlinkSignalLoss() {
        assertNull(event.getDownlinkSignalLoss());
        event.setDownlinkSignalLoss(extendedRange);
        assertEquals(extendedRange, event.getDownlinkSignalLoss());
    }

    @Test
    public void testGetRange() {
        testSetRange();
    }

    @Test
    public void testSetRange() {
        assertNull(event.getRange());
        event.setRange(extendedRange);
        assertEquals(extendedRange, event.getRange());
    }

    @Test
    public void testGetSignalDelay() {
        testSetSignalDelay();
    }

    @Test
    public void testSetSignalDelay() {
        assertNull(event.getSignalDelay());
        event.setSignalDelay(extendedRange);
        assertEquals(extendedRange, event.getSignalDelay());
    }

    @Test
    public void testGetSatelliteStateAtStart() {
        testSetSatelliteStateAtStart();
    }

    @Test
    public void testSetSatelliteStateAtStart() {
        assertNull(event.getSatelliteStateAtStart());
        event.setSatelliteStateAtStart(satelliteState);
        assertEquals(satelliteState, event.getSatelliteStateAtStart());
    }

    @Test
    public void testGetDerivedFromId() {
        testSetDerivedFromId();
    }

    @Test
    public void testGetDerivedFrom() {
        testSetDerivedFromId();
    }

    @Test
    public void testSetDerivedFromId() {
        assertNull(event.getDerivedFromId());
        event.setDerivedFromId(TLE_ID);
        assertEquals(TLE_ID, event.getDerivedFromId());
    }

    @Test
    public void testGetStartTime() {
        testSetStartTime();
    }

    @Test
    public void testSetStartTime() {
        assertEquals(0L, event.getStartTime());
        event.setStartTime(NOW - 1);
        assertEquals(NOW - 1, event.getStartTime());
    }

    @Test
    public void testGetEndTime() {
        testSetEndTime();
    }

    @Test
    public void testSetEndTime() {
        assertEquals(0L, event.getEndTime());
        event.setEndTime(NOW + 1);
        assertEquals(NOW + 1, event.getEndTime());
    }

    @Test
    public void testToString() {
        event.setStartTime(NOW - 1);
        event.setEndTime(NOW + 1);
        String s = event.toString();
        assertTrue(s.contains(event.getInstanceID()));
        assertTrue(s.contains(GS_ID));
        assertTrue(s.contains(SAT_ID));
        assertTrue(s.contains(String.valueOf(ORBIT_NUMBER)));
        assertTrue(s.contains(Dates.toDefaultDateFormat(NOW - 1)));
        assertTrue(s.contains(Dates.toDefaultDateFormat(NOW + 1)));
    }
}
