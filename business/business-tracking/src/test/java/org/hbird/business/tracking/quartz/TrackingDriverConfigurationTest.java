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
package org.hbird.business.tracking.quartz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TrackingDriverConfigurationTest {

    @Mock
    private List<String> satellites;

    private TrackingDriverConfiguration config;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        config = new TrackingDriverConfiguration();
        inOrder = Mockito.inOrder(satellites);
    }

    @After
    public void tearDown() {
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testTrackingDriverConfig() {
        assertEquals(TrackingDriverConfiguration.DEFAULT_ARCHIVE_POLL_INTERVAL, config.getArchivePollInterval());
        assertEquals(TrackingDriverConfiguration.DEFAULT_SCHEDULE_DELTA, config.getScheduleDelta());
        assertEquals(TrackingDriverConfiguration.DEFAULT_HEART_BEAT_INTERVAL, config.getHeartBeatInterval());
        assertNull(config.getServiceId());
        assertNull(config.getServiceVersion());
        assertNull(config.getGroundstationId());
        assertNotNull(config.getSatelliteIds());
        assertEquals(0, config.getSatelliteIds().size());
    }

    @Test
    public void testGetScheduleDelta() {
        testSetScheduleDelta();
    }

    @Test
    public void testSetScheduleDelta() {
        assertEquals(TrackingDriverConfiguration.DEFAULT_SCHEDULE_DELTA, config.getScheduleDelta());
        config.setScheduleDelta(123);
        assertEquals(123, config.getScheduleDelta());
        config.setScheduleDelta(-10000000332L);
        assertEquals(-10000000332L, config.getScheduleDelta());
    }

    @Test
    public void testSetArchivePollInterval() {
        assertEquals(TrackingDriverConfiguration.DEFAULT_ARCHIVE_POLL_INTERVAL, config.getArchivePollInterval());
        config.setArchivePollInterval(5542);
        assertEquals(5542, config.getArchivePollInterval());
        config.setArchivePollInterval(-9985745);
        assertEquals(-9985745, config.getArchivePollInterval());
    }

    @Test
    public void testGetSatelliteIds() {
        testSetSatelliteIds();
    }

    @Test
    public void testSetSatelliteIds() {
        assertNotNull(config.getSatelliteIds());
        assertEquals(0, config.getSatelliteIds().size());
        config.setSatelliteIds(satellites);
        assertEquals(satellites, config.getSatelliteIds());
    }
}
