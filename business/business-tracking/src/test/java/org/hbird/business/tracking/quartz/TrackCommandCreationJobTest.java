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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.apache.camel.ProducerTemplate;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IdBuilder;
import org.hbird.business.api.exceptions.ArchiveException;
import org.hbird.business.core.cache.EntityCache;
import org.hbird.exchange.groundstation.Track;
import org.hbird.exchange.interfaces.IStartableEntity;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TrackCommandCreationJobTest {

    private static final String ENDPOINT = "direct:inject";
    private static final String CONTACT_INSTANCE_ID = "contact:1";
    private static final String SATELLITE_ID = "sat-1";
    private static final String TLE_ID = "TLE:1";
    private static final String PART_ID = "Tracking";
    private static final String GS_ID = "gs-0";
    private static final long NOW = System.currentTimeMillis();
    private static final String TRACK_COMMAND_ID = "/Command/Track";

    @Mock
    private IStartableEntity part;

    @Mock
    private IDataAccess dao;

    @Mock
    private ProducerTemplate producerTemplate;

    @Mock
    private EntityCache<Satellite> satelliteCache;

    @Mock
    private EntityCache<TleOrbitalParameters> tleCache;

    @Mock
    private JobExecutionContext quartzContext;

    @Mock
    private LocationContactEvent event;

    @Mock
    private Satellite satellite;

    @Mock
    private TleOrbitalParameters tle1;

    @Mock
    private TleOrbitalParameters tle2;

    @Mock
    private IdBuilder idBuilder;

    @Mock
    private JobDataMap jobData;

    private ArchiveException archiveException;

    private TrackCommandCreationJob job;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        job = new TrackCommandCreationJob();
        job.setIssuer(part);
        job.setDataAccess(dao);
        job.setEndpoint(ENDPOINT);
        job.setProducerTemplate(producerTemplate);
        job.setSatelliteCache(satelliteCache);
        job.setTleCache(tleCache);
        job.setIdBuilder(idBuilder);
        archiveException = new ArchiveException("This is bad!");
        inOrder = inOrder(dao, producerTemplate, satelliteCache, tleCache, quartzContext, event, satellite, tle1, tle2, jobData, part, idBuilder);
        when(part.getID()).thenReturn(PART_ID);
        when(quartzContext.getMergedJobDataMap()).thenReturn(jobData);
        when(jobData.getString(TrackCommandCreationJob.JOB_DATA_KEY_CONTACT_INSTANCE_ID)).thenReturn(CONTACT_INSTANCE_ID);
        when(event.getSatelliteID()).thenReturn(SATELLITE_ID);
        when(event.getDerivedFromId()).thenReturn(TLE_ID);
        when(event.getGroundStationID()).thenReturn(GS_ID);
        when(tle1.getVersion()).thenReturn(NOW - 2);
        when(tle2.getVersion()).thenReturn(NOW - 1);
        when(satellite.getID()).thenReturn(SATELLITE_ID);
        when(idBuilder.buildID(SATELLITE_ID, Track.class.getSimpleName())).thenReturn(TRACK_COMMAND_ID);
    }

    @After
    public void tearDown() {
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testExecuteEventNotFound() throws Exception {
        when(dao.getByInstanceId(CONTACT_INSTANCE_ID, LocationContactEvent.class)).thenReturn(null);
        job.execute(quartzContext);
        inOrder.verify(quartzContext, times(1)).getMergedJobDataMap();
        inOrder.verify(jobData, times(1)).getString(TrackCommandCreationJob.JOB_DATA_KEY_CONTACT_INSTANCE_ID);
        inOrder.verify(dao, times(1)).getByInstanceId(CONTACT_INSTANCE_ID, LocationContactEvent.class);
    }

    @Test
    public void testExecuteSatNotFound() throws Exception {
        when(dao.getByInstanceId(CONTACT_INSTANCE_ID, LocationContactEvent.class)).thenReturn(event);
        when(event.getSatelliteID()).thenReturn(SATELLITE_ID);
        when(satelliteCache.getById(SATELLITE_ID)).thenReturn(null);
        job.execute(quartzContext);
        inOrder.verify(quartzContext, times(1)).getMergedJobDataMap();
        inOrder.verify(jobData, times(1)).getString(TrackCommandCreationJob.JOB_DATA_KEY_CONTACT_INSTANCE_ID);
        inOrder.verify(dao, times(1)).getByInstanceId(CONTACT_INSTANCE_ID, LocationContactEvent.class);
        inOrder.verify(event, times(1)).getSatelliteID();
        inOrder.verify(satelliteCache, times(1)).getById(SATELLITE_ID);
    }

    @Test
    public void testExecuteTleNotFound() throws Exception {
        when(dao.getByInstanceId(CONTACT_INSTANCE_ID, LocationContactEvent.class)).thenReturn(event);
        when(event.getSatelliteID()).thenReturn(SATELLITE_ID);
        when(satelliteCache.getById(SATELLITE_ID)).thenReturn(satellite);
        when(event.getDerivedFromId()).thenReturn(TLE_ID);
        when(tleCache.getById(TLE_ID)).thenReturn(null);
        job.execute(quartzContext);
        inOrder.verify(quartzContext, times(1)).getMergedJobDataMap();
        inOrder.verify(jobData, times(1)).getString(TrackCommandCreationJob.JOB_DATA_KEY_CONTACT_INSTANCE_ID);
        inOrder.verify(dao, times(1)).getByInstanceId(CONTACT_INSTANCE_ID, LocationContactEvent.class);
        inOrder.verify(event, times(1)).getSatelliteID();
        inOrder.verify(satelliteCache, times(1)).getById(SATELLITE_ID);
        inOrder.verify(event, times(1)).getDerivedFromId();
        inOrder.verify(tleCache, times(1)).getById(TLE_ID);
    }

    @Test
    public void testExecuteTlesDontMatch() throws Exception {
        when(dao.getByInstanceId(CONTACT_INSTANCE_ID, LocationContactEvent.class)).thenReturn(event);
        when(event.getSatelliteID()).thenReturn(SATELLITE_ID);
        when(satelliteCache.getById(SATELLITE_ID)).thenReturn(satellite);
        when(event.getDerivedFromId()).thenReturn(TLE_ID);
        when(tleCache.getById(TLE_ID)).thenReturn(tle1);
        when(dao.getTleFor(SATELLITE_ID)).thenReturn(tle2);
        job.execute(quartzContext);
        inOrder.verify(quartzContext, times(1)).getMergedJobDataMap();
        inOrder.verify(jobData, times(1)).getString(TrackCommandCreationJob.JOB_DATA_KEY_CONTACT_INSTANCE_ID);
        inOrder.verify(dao, times(1)).getByInstanceId(CONTACT_INSTANCE_ID, LocationContactEvent.class);
        inOrder.verify(event, times(1)).getSatelliteID();
        inOrder.verify(satelliteCache, times(1)).getById(SATELLITE_ID);
        inOrder.verify(event, times(1)).getDerivedFromId();
        inOrder.verify(tleCache, times(1)).getById(TLE_ID);
        inOrder.verify(dao, times(1)).getTleFor(SATELLITE_ID);
        inOrder.verify(tle1, times(1)).getVersion();
        inOrder.verify(tle2, times(1)).getVersion();
    }

    @Test
    public void testExecuteLatestTlesNotFound() throws Exception {
        when(dao.getByInstanceId(CONTACT_INSTANCE_ID, LocationContactEvent.class)).thenReturn(event);
        when(event.getSatelliteID()).thenReturn(SATELLITE_ID);
        when(satelliteCache.getById(SATELLITE_ID)).thenReturn(satellite);
        when(event.getDerivedFromId()).thenReturn(TLE_ID);
        when(tleCache.getById(TLE_ID)).thenReturn(tle1);
        when(dao.getTleFor(SATELLITE_ID)).thenReturn(null);
        job.execute(quartzContext);
        inOrder.verify(quartzContext, times(1)).getMergedJobDataMap();
        inOrder.verify(jobData, times(1)).getString(TrackCommandCreationJob.JOB_DATA_KEY_CONTACT_INSTANCE_ID);
        inOrder.verify(dao, times(1)).getByInstanceId(CONTACT_INSTANCE_ID, LocationContactEvent.class);
        inOrder.verify(event, times(1)).getSatelliteID();
        inOrder.verify(satelliteCache, times(1)).getById(SATELLITE_ID);
        inOrder.verify(event, times(1)).getDerivedFromId();
        inOrder.verify(tleCache, times(1)).getById(TLE_ID);
        inOrder.verify(dao, times(1)).getTleFor(SATELLITE_ID);
        inOrder.verify(event, times(1)).getGroundStationID();
        ArgumentCaptor<Track> captor = ArgumentCaptor.forClass(Track.class);
        inOrder.verify(producerTemplate, times(1)).asyncSendBody(eq(ENDPOINT), captor.capture());
        Track t = captor.getValue();
        assertEquals(PART_ID, t.getIssuedBy());
        assertEquals(GS_ID, t.getDestination());
        assertEquals(satellite, t.getSatellite());
        assertEquals(event, t.getLocationContactEvent());
    }

    @Test
    public void testExecute() throws Exception {
        when(dao.getByInstanceId(CONTACT_INSTANCE_ID, LocationContactEvent.class)).thenReturn(event);
        when(event.getSatelliteID()).thenReturn(SATELLITE_ID);
        when(satelliteCache.getById(SATELLITE_ID)).thenReturn(satellite);
        when(event.getDerivedFromId()).thenReturn(TLE_ID);
        when(tleCache.getById(TLE_ID)).thenReturn(tle1);
        when(dao.getTleFor(SATELLITE_ID)).thenReturn(tle2);
        when(tle1.getVersion()).thenReturn(NOW - 1);
        when(tle2.getVersion()).thenReturn(NOW - 1);
        job.execute(quartzContext);
        inOrder.verify(quartzContext, times(1)).getMergedJobDataMap();
        inOrder.verify(jobData, times(1)).getString(TrackCommandCreationJob.JOB_DATA_KEY_CONTACT_INSTANCE_ID);
        inOrder.verify(dao, times(1)).getByInstanceId(CONTACT_INSTANCE_ID, LocationContactEvent.class);
        inOrder.verify(event, times(1)).getSatelliteID();
        inOrder.verify(satelliteCache, times(1)).getById(SATELLITE_ID);
        inOrder.verify(event, times(1)).getDerivedFromId();
        inOrder.verify(tleCache, times(1)).getById(TLE_ID);
        inOrder.verify(dao, times(1)).getTleFor(SATELLITE_ID);
        inOrder.verify(tle1, times(1)).getVersion();
        inOrder.verify(tle2, times(1)).getVersion();
        inOrder.verify(satellite, times(1)).getID();
        inOrder.verify(idBuilder, times(1)).buildID(SATELLITE_ID, Track.class.getSimpleName());
        inOrder.verify(part, times(1)).getID();
        inOrder.verify(event, times(1)).getGroundStationID();
        ArgumentCaptor<Track> captor = ArgumentCaptor.forClass(Track.class);
        inOrder.verify(producerTemplate, times(1)).asyncSendBody(eq(ENDPOINT), captor.capture());
        Track t = captor.getValue();
        assertEquals(TRACK_COMMAND_ID, t.getID());
        assertEquals(PART_ID, t.getIssuedBy());
        assertEquals(GS_ID, t.getDestination());
        assertEquals(satellite, t.getSatellite());
        assertEquals(event, t.getLocationContactEvent());
    }

    @Test
    public void testGetEvent() throws Exception {
        when(dao.getByInstanceId(CONTACT_INSTANCE_ID, LocationContactEvent.class)).thenReturn(event);
        assertEquals(event, job.getEvent(dao, CONTACT_INSTANCE_ID));
        inOrder.verify(dao, times(1)).getByInstanceId(CONTACT_INSTANCE_ID, LocationContactEvent.class);
    }

    @Test
    public void testGetEventWithException() throws Exception {
        when(dao.getByInstanceId(CONTACT_INSTANCE_ID, LocationContactEvent.class)).thenThrow(archiveException);
        assertNull(job.getEvent(dao, CONTACT_INSTANCE_ID));
        inOrder.verify(dao, times(1)).getByInstanceId(CONTACT_INSTANCE_ID, LocationContactEvent.class);
    }

    @Test
    public void testGetSatellite() throws Exception {
        when(satelliteCache.getById(SATELLITE_ID)).thenReturn(satellite);
        assertEquals(satellite, job.getSatellite(satelliteCache, SATELLITE_ID));
        inOrder.verify(satelliteCache, times(1)).getById(SATELLITE_ID);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetSatelliteWithException() throws Exception {
        when(satelliteCache.getById(SATELLITE_ID)).thenThrow(Exception.class);
        assertNull(job.getSatellite(satelliteCache, SATELLITE_ID));
        inOrder.verify(satelliteCache, times(1)).getById(SATELLITE_ID);
    }

    @Test
    public void testGetTle() throws Exception {
        when(tleCache.getById(TLE_ID)).thenReturn(tle1);
        assertEquals(tle1, job.getTle(tleCache, TLE_ID));
        inOrder.verify(tleCache, times(1)).getById(TLE_ID);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetTleWithException() throws Exception {
        when(tleCache.getById(TLE_ID)).thenThrow(Exception.class);
        assertNull(job.getTle(tleCache, TLE_ID));
        inOrder.verify(tleCache, times(1)).getById(TLE_ID);
    }

    @Test
    public void testGetLatestTle() throws Exception {
        when(dao.getTleFor(SATELLITE_ID)).thenReturn(tle1);
        assertEquals(tle1, job.getLatestTle(dao, SATELLITE_ID));
        inOrder.verify(dao, times(1)).getTleFor(SATELLITE_ID);
    }

    @Test
    public void testGetLatestTleWithException() throws Exception {
        when(dao.getTleFor(SATELLITE_ID)).thenThrow(archiveException);
        assertNull(job.getLatestTle(dao, SATELLITE_ID));
        inOrder.verify(dao, times(1)).getTleFor(SATELLITE_ID);
    }

    @Test
    public void testAreEqual() {
        assertTrue(job.areEqual(tle1, tle1));
        assertTrue(job.areEqual(tle2, tle2));
        assertFalse(job.areEqual(tle1, tle2));
        assertFalse(job.areEqual(tle2, tle1));
        inOrder.verify(tle1, times(2)).getVersion();
        inOrder.verify(tle2, times(2)).getVersion();
        inOrder.verify(tle1, times(1)).getVersion();
        inOrder.verify(tle2, times(2)).getVersion();
        inOrder.verify(tle1, times(1)).getVersion();
    }

    @Test
    public void createTrackCommand() {
        Track track = job.createTrackCommand(idBuilder, part, event, satellite);
        assertNotNull(track);
        assertEquals(TRACK_COMMAND_ID, track.getID());
        assertEquals(PART_ID, track.getIssuedBy());
        assertEquals(GS_ID, track.getDestination());
        assertEquals(satellite, track.getSatellite());
        assertEquals(event, track.getLocationContactEvent());
        inOrder.verify(satellite, times(1)).getID();
        inOrder.verify(idBuilder, times(1)).buildID(SATELLITE_ID, Track.class.getSimpleName());
        inOrder.verify(part, times(1)).getID();
        inOrder.verify(event, times(1)).getGroundStationID();
        inOrder.verifyNoMoreInteractions();
    }
}
