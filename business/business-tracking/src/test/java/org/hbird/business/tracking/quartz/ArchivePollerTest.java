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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hbird.business.api.deprecated.IDataAccess;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.dataaccess.LocationContactEventRequest;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ArchivePollerTest {

    public static final String GS_ID = "GS-1";
    public static final String SAT_1 = "SAT-1";
    public static final String SAT_2 = "SAT-2";
    public static final String SAT_3 = "SAT-3";
    public static final long NOW = System.currentTimeMillis();

    @Mock
    private TrackingDriverConfiguration config;

    @Mock
    private IDataAccess dao;

    @Mock
    private LocationContactEvent event1;

    @Mock
    private LocationContactEvent event2;

    @Mock
    private LocationContactEventRequest request;

    @InjectMocks
    private ArchivePoller archivePoller;

    private InOrder inOrder;

    private List<String> satelliteIds;

    private List<EntityInstance> events;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        satelliteIds = Arrays.asList(SAT_1, SAT_2, SAT_3);
        events = new ArrayList<EntityInstance>(2);
        events.add(event1);
        events.add(event2);
        inOrder = inOrder(config, dao, event1, event2, request);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testPoll() {
        when(config.getGroundstationId()).thenReturn(GS_ID);
        when(config.getSatelliteIds()).thenReturn(satelliteIds);
        //when(dao.getData(any(LocationContactEventRequest.class))).thenReturn(events, events, Collections.<EntityInstance> emptyList());
        when(dao.getNextLocationContactEventFor(GS_ID, SAT_1)).thenReturn(event1);
        when(dao.getNextLocationContactEventFor(GS_ID, SAT_2)).thenReturn(event2);
        when(dao.getNextLocationContactEventFor(GS_ID, SAT_3)).thenThrow(Exception.class);
        //when(event1.getStartTime()).thenReturn(NOW + 1000L * 60 * 60, NOW + 1000L * 60 * 60, NOW - 1000L * 60 * 60);
        //when(event2.getStartTime()).thenReturn(NOW + 1000L * 60 * 60 * 2);

        List<LocationContactEvent> events = archivePoller.poll();
        assertNotNull(events);
        assertEquals(2, events.size());
        assertEquals(event1, events.get(0));
        assertEquals(event2, events.get(1));
        inOrder.verify(config, times(1)).getSatelliteIds();
        inOrder.verify(config, times(1)).getGroundstationId();
        inOrder.verify(dao, times(3)).getNextLocationContactEventFor(any(String.class), any(String.class));
        inOrder.verifyNoMoreInteractions();
        
        
        /* inOrder.verify(config, times(1)).getSatelliteIds();
        inOrder.verify(config, times(1)).getGroundstationId();
        inOrder.verify(dao, times(1)).getData(any(DataRequest.class));
        inOrder.verify(event1, times(1)).getStartTime();
        inOrder.verify(event2, times(1)).getStartTime();
        inOrder.verify(event1, times(1)).getStartTime();
        inOrder.verify(dao, times(1)).getData(any(DataRequest.class));
        inOrder.verify(event1, times(1)).getStartTime();
        inOrder.verify(event2, times(1)).getStartTime();
        inOrder.verify(dao, times(1)).getData(any(DataRequest.class));
        inOrder.verifyNoMoreInteractions(); */
    }

    /*@Test
    public void testCompare() {
        when(event1.getStartTime()).thenReturn(100L);
        when(event2.getStartTime()).thenReturn(110L);
        assertEquals(event1, archivePoller.compare(event1, event2, 90L));
        assertEquals(event2, archivePoller.compare(event1, event2, 105L));
        assertNull(archivePoller.compare(event1, event2, 115L));
        assertEquals(event2, archivePoller.compare(null, event2, 105L));
        assertNull(archivePoller.compare(null, event2, 115L));
        assertEquals(event2, archivePoller.compare(event2, event1, 105L));
        assertEquals(event1, archivePoller.compare(event2, event1, 90L));

        inOrder.verify(event2, times(1)).getStartTime();
        inOrder.verify(event1, times(1)).getStartTime();
        inOrder.verify(event2, times(1)).getStartTime();
        inOrder.verify(event1, times(1)).getStartTime();
        inOrder.verify(event2, times(1)).getStartTime();
        inOrder.verify(event1, times(1)).getStartTime();
        inOrder.verify(event2, times(2)).getStartTime();
        inOrder.verify(event1, times(1)).getStartTime();
        inOrder.verify(event2, times(1)).getStartTime();
        inOrder.verify(event1, times(1)).getStartTime();
        inOrder.verify(event2, times(1)).getStartTime();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCreateRequest() {
        LocationContactEventRequest request = archivePoller.createRequest(GS_ID, SAT_1);
        assertNotNull(request);
        assertEquals(GS_ID, request.getGroundStationID());
        assertEquals(SAT_1, request.getSatelliteID());
        assertNotNull(request.getID());
        UUID.fromString(request.getID());
        assertEquals(new Long(1L), request.getFrom());
    }

    @Test
    public void testGetEvents() {
        when(dao.getData(any(DataRequest.class))).thenReturn(events);
        assertEquals(events, archivePoller.getEvents(dao, request));
        inOrder.verify(dao, times(1)).getData(request);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetNext() {
        when(event1.getStartTime()).thenReturn(NOW + 1000L * 60 * 60);
        when(event2.getStartTime()).thenReturn(NOW + 1000L * 60 * 60 * 2);
        LocationContactEvent event = archivePoller.getNextEvent(events, NOW);
        assertNotNull(event);
        assertEquals(event1, event);
        inOrder.verify(event1, times(1)).getStartTime();
        inOrder.verify(event2, times(1)).getStartTime();
        inOrder.verify(event1, times(1)).getStartTime();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetNextInvalidObjects() {
        when(event2.getStartTime()).thenReturn(NOW + 1000L * 60 * 60 * 2);
        Parameter p = new Parameter(UUID.randomUUID().toString(), "Random Parameter");
        List<EntityInstance> list = new ArrayList<EntityInstance>(1);
        list.add(p);
        list.add(event2);
        LocationContactEvent event = archivePoller.getNextEvent(list, NOW);
        assertNotNull(event);
        assertEquals(event2, event);
        inOrder.verify(event2, times(1)).getStartTime();
        inOrder.verifyNoMoreInteractions();
    } */
}
