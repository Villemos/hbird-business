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
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.hbird.business.core.cache.EntityCache;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.groundstation.Track;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.Satellite;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class NotificationEventSchedulerTest {

    private static final long NOW = System.currentTimeMillis();
    private static final String GS_ID = "GS-A";
    private static final String SAT_ID = "SAT-1";
    private static final String GROUP_NAME = "group";
    private static final String TRIGGER_NAME = "teh trigga";
    private static final String JOB_NAME = "jobs";
    private static final String EVENT_ID = "EVENT-ID";
    private static final String SAT_NAME = "teh satellite";
    private static final String GS_NAME = "teh ground station";

    private static final Logger LOG = LoggerFactory.getLogger(NotificationEventSchedulerTest.class);

    @Mock
    private Scheduler scheduler;

    @Mock
    private SchedulingSupport support;

    @Mock
    private Track command;

    @Mock
    private LocationContactEvent event;

    @Mock
    private Trigger trigger;

    @Mock
    private JobDataMap jobDataMap;

    @Mock
    private JobDetail jobDetail;

    @Mock
    private Date date;

    @Mock
    private EntityCache<GroundStation> gsCache;

    @Mock
    private EntityCache<Satellite> satCache;

    @Mock
    private GroundStation gs;

    @Mock
    private Satellite sat;

    private SchedulerException schedulerException;

    private NotificationEventScheduler eventScheduler;

    private InOrder inOrder;

    private Exception cacheException;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        eventScheduler = new NotificationEventScheduler(scheduler, support, gsCache, satCache);
        schedulerException = new SchedulerException("Bananas!");
        cacheException = new Exception("The box is empty!");
        inOrder = inOrder(scheduler, support, command, event, trigger, jobDataMap, jobDetail, date, gsCache, satCache, gs, sat);
        when(command.getLocationContactEvent()).thenReturn(event);
        when(event.getGroundStationID()).thenReturn(GS_ID);
        when(event.getSatelliteID()).thenReturn(SAT_ID);
        when(event.getStartTime()).thenReturn(NOW - 1000L);
        when(event.getEndTime()).thenReturn(NOW + 1000L);
        when(event.getInstanceID()).thenReturn(EVENT_ID);
        when(gsCache.getById(GS_ID)).thenReturn(gs);
        when(satCache.getById(SAT_ID)).thenReturn(sat);
        when(gs.getName()).thenReturn(GS_NAME);
        when(sat.getName()).thenReturn(SAT_NAME);
    }

    @Test
    public void testHandle() throws Exception {
        when(support.createGroupName(event)).thenReturn(GROUP_NAME);
        when(support.createTriggerName(any(JobType.class), eq(event))).thenReturn(TRIGGER_NAME);
        when(support.createJobName(any(JobType.class), eq(event))).thenReturn(JOB_NAME);
        when(support.createTrigger(eq(GROUP_NAME), eq(TRIGGER_NAME), anyLong())).thenReturn(trigger);
        when(scheduler.scheduleJob(any(JobDetail.class), eq(trigger))).thenReturn(date);

        eventScheduler.handle(command);

        inOrder.verify(command, times(1)).getLocationContactEvent();
        inOrder.verify(event, times(1)).getStartTime();
        inOrder.verify(event, times(1)).getEndTime();

        inOrder.verify(support, times(1)).createGroupName(event);
        inOrder.verify(support, times(1)).createTriggerName(JobType.AOS, event);
        inOrder.verify(support, times(1)).createJobName(JobType.AOS, event);
        inOrder.verify(support, times(1)).createTrigger(GROUP_NAME, TRIGGER_NAME, NOW - 1000L);
        inOrder.verify(event, times(1)).getInstanceID();
        inOrder.verify(event, times(1)).getGroundStationID();
        inOrder.verify(gsCache, times(1)).getById(GS_ID);
        inOrder.verify(gs, times(1)).getName();
        inOrder.verify(event, times(1)).getSatelliteID();
        inOrder.verify(satCache, times(1)).getById(SAT_ID);
        inOrder.verify(sat, times(1)).getName();
        ArgumentCaptor<JobDetail> jobDetailCaptor = ArgumentCaptor.forClass(JobDetail.class);
        inOrder.verify(scheduler, times(1)).scheduleJob(jobDetailCaptor.capture(), eq(trigger));
        JobDetail job = jobDetailCaptor.getValue();
        assertNotNull(job);
        assertEquals(NotificationEventCreationJob.class, job.getJobClass());
        assertEquals(GROUP_NAME, job.getKey().getGroup());
        assertEquals(JOB_NAME, job.getKey().getName());
        JobDataMap map = job.getJobDataMap();
        assertNotNull(map);
        assertEquals(7, map.size());
        assertEquals(EVENT_ID, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_CONTACT_INSTANCE_ID));
        assertEquals(JobType.AOS.toString(), map.getString(NotificationEventCreationJob.JOB_DATA_KEY_EVENT_TYPE));
        assertEquals(NOW - 1000L, map.getLong(NotificationEventCreationJob.JOB_DATA_KEY_EVENT_TIME));
        assertEquals(GS_NAME, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_GROUND_STATION_NAME));
        assertEquals(GS_ID, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_GROUND_STATION_ID));
        assertEquals(SAT_ID, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_SATELLITE_ID));
        assertEquals(SAT_NAME, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_SATELLITE_NAME));
        if (LOG.isDebugEnabled()) {
            inOrder.verify(date, times(1)).getTime();
        }

        inOrder.verify(support, times(1)).createGroupName(event);
        inOrder.verify(support, times(1)).createTriggerName(JobType.LOS, event);
        inOrder.verify(support, times(1)).createJobName(JobType.LOS, event);
        inOrder.verify(support, times(1)).createTrigger(GROUP_NAME, TRIGGER_NAME, NOW + 1000L);
        inOrder.verify(event, times(1)).getInstanceID();
        inOrder.verify(gsCache, times(1)).getById(GS_ID);
        inOrder.verify(gs, times(1)).getName();
        inOrder.verify(event, times(1)).getSatelliteID();
        inOrder.verify(satCache, times(1)).getById(SAT_ID);
        inOrder.verify(sat, times(1)).getName();
        jobDetailCaptor = ArgumentCaptor.forClass(JobDetail.class);
        inOrder.verify(scheduler, times(1)).scheduleJob(jobDetailCaptor.capture(), eq(trigger));
        job = jobDetailCaptor.getValue();
        assertNotNull(job);
        assertEquals(NotificationEventCreationJob.class, job.getJobClass());
        assertEquals(GROUP_NAME, job.getKey().getGroup());
        assertEquals(JOB_NAME, job.getKey().getName());
        map = job.getJobDataMap();
        assertNotNull(map);
        assertEquals(7, map.size());
        assertEquals(EVENT_ID, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_CONTACT_INSTANCE_ID));
        assertEquals(JobType.LOS.toString(), map.getString(NotificationEventCreationJob.JOB_DATA_KEY_EVENT_TYPE));
        assertEquals(NOW + 1000L, map.getLong(NotificationEventCreationJob.JOB_DATA_KEY_EVENT_TIME));
        assertEquals(GS_ID, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_GROUND_STATION_ID));
        assertEquals(GS_NAME, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_GROUND_STATION_NAME));
        assertEquals(SAT_ID, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_SATELLITE_ID));
        assertEquals(SAT_NAME, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_SATELLITE_NAME));
        if (LOG.isDebugEnabled()) {
            inOrder.verify(date, times(1)).getTime();
        }
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testHandleWithExceptions() throws Exception {
        when(support.createGroupName(event)).thenReturn(GROUP_NAME);
        when(support.createTriggerName(any(JobType.class), eq(event))).thenReturn(TRIGGER_NAME);
        when(support.createJobName(any(JobType.class), eq(event))).thenReturn(JOB_NAME);
        when(support.createTrigger(eq(GROUP_NAME), eq(TRIGGER_NAME), anyLong())).thenReturn(trigger);
        when(scheduler.scheduleJob(any(JobDetail.class), eq(trigger))).thenReturn(date);
        when(gsCache.getById(GS_ID)).thenThrow(cacheException);

        eventScheduler.handle(command);

        inOrder.verify(command, times(1)).getLocationContactEvent();
        inOrder.verify(event, times(1)).getStartTime();
        inOrder.verify(event, times(1)).getEndTime();

        inOrder.verify(support, times(1)).createGroupName(event);
        inOrder.verify(support, times(1)).createTriggerName(JobType.AOS, event);
        inOrder.verify(support, times(1)).createJobName(JobType.AOS, event);
        inOrder.verify(support, times(1)).createTrigger(GROUP_NAME, TRIGGER_NAME, NOW - 1000L);
        inOrder.verify(event, times(1)).getInstanceID();
        inOrder.verify(event, times(1)).getGroundStationID();
        inOrder.verify(gsCache, times(1)).getById(GS_ID);

        inOrder.verify(support, times(1)).createGroupName(event);
        inOrder.verify(support, times(1)).createTriggerName(JobType.LOS, event);
        inOrder.verify(support, times(1)).createJobName(JobType.LOS, event);
        inOrder.verify(support, times(1)).createTrigger(GROUP_NAME, TRIGGER_NAME, NOW + 1000L);
        inOrder.verify(event, times(1)).getInstanceID();
        inOrder.verify(gsCache, times(1)).getById(GS_ID);

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testScheduleJob() throws Exception {
        when(support.createGroupName(event)).thenReturn(GROUP_NAME);
        when(support.createTriggerName(JobType.LOS, event)).thenReturn(TRIGGER_NAME);
        when(support.createJobName(JobType.LOS, event)).thenReturn(JOB_NAME);
        when(support.createTrigger(GROUP_NAME, TRIGGER_NAME, NOW)).thenReturn(trigger);
        when(scheduler.scheduleJob(any(JobDetail.class), eq(trigger))).thenReturn(date);
        eventScheduler.scheduleJob(scheduler, support, event, JobType.LOS, NOW);
        inOrder.verify(support, times(1)).createGroupName(event);
        inOrder.verify(support, times(1)).createTriggerName(JobType.LOS, event);
        inOrder.verify(support, times(1)).createJobName(JobType.LOS, event);
        inOrder.verify(support, times(1)).createTrigger(GROUP_NAME, TRIGGER_NAME, NOW);
        inOrder.verify(event, times(1)).getInstanceID();
        inOrder.verify(event, times(1)).getGroundStationID();
        inOrder.verify(event, times(1)).getSatelliteID();
        ArgumentCaptor<JobDetail> jobDetailCaptor = ArgumentCaptor.forClass(JobDetail.class);
        inOrder.verify(scheduler, times(1)).scheduleJob(jobDetailCaptor.capture(), eq(trigger));
        JobDetail job = jobDetailCaptor.getValue();
        assertNotNull(job);
        assertEquals(NotificationEventCreationJob.class, job.getJobClass());
        assertEquals(GROUP_NAME, job.getKey().getGroup());
        assertEquals(JOB_NAME, job.getKey().getName());
        JobDataMap map = job.getJobDataMap();
        assertNotNull(map);
        assertEquals(7, map.size());
        assertEquals(EVENT_ID, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_CONTACT_INSTANCE_ID));
        assertEquals(JobType.LOS.toString(), map.getString(NotificationEventCreationJob.JOB_DATA_KEY_EVENT_TYPE));
        assertEquals(NOW, map.getLong(NotificationEventCreationJob.JOB_DATA_KEY_EVENT_TIME));
        assertEquals(GS_ID, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_GROUND_STATION_ID));
        assertEquals(GS_NAME, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_GROUND_STATION_NAME));
        assertEquals(SAT_ID, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_SATELLITE_ID));
        assertEquals(SAT_NAME, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_SATELLITE_NAME));
        if (LOG.isDebugEnabled()) {
            inOrder.verify(date, times(1)).getTime();
        }
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testScheduleJobWithException() throws Exception {
        when(support.createGroupName(event)).thenReturn(GROUP_NAME);
        when(support.createTriggerName(JobType.LOS, event)).thenReturn(TRIGGER_NAME);
        when(support.createJobName(JobType.LOS, event)).thenReturn(JOB_NAME);
        when(support.createTrigger(GROUP_NAME, TRIGGER_NAME, NOW)).thenReturn(trigger);
        when(scheduler.scheduleJob(any(JobDetail.class), eq(trigger))).thenThrow(schedulerException);
        eventScheduler.scheduleJob(scheduler, support, event, JobType.LOS, NOW);
        inOrder.verify(support, times(1)).createGroupName(event);
        inOrder.verify(support, times(1)).createTriggerName(JobType.LOS, event);
        inOrder.verify(support, times(1)).createJobName(JobType.LOS, event);
        inOrder.verify(support, times(1)).createTrigger(GROUP_NAME, TRIGGER_NAME, NOW);
        inOrder.verify(event, times(1)).getInstanceID();
        inOrder.verify(event, times(1)).getGroundStationID();
        inOrder.verify(event, times(1)).getSatelliteID();
        ArgumentCaptor<JobDetail> jobDetailCaptor = ArgumentCaptor.forClass(JobDetail.class);
        inOrder.verify(scheduler, times(1)).scheduleJob(jobDetailCaptor.capture(), eq(trigger));
        JobDetail job = jobDetailCaptor.getValue();
        assertNotNull(job);
        assertEquals(NotificationEventCreationJob.class, job.getJobClass());
        assertEquals(GROUP_NAME, job.getKey().getGroup());
        assertEquals(JOB_NAME, job.getKey().getName());
        JobDataMap map = job.getJobDataMap();
        assertNotNull(map);
        assertEquals(7, map.size());
        assertEquals(EVENT_ID, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_CONTACT_INSTANCE_ID));
        assertEquals(JobType.LOS.toString(), map.getString(NotificationEventCreationJob.JOB_DATA_KEY_EVENT_TYPE));
        assertEquals(NOW, map.getLong(NotificationEventCreationJob.JOB_DATA_KEY_EVENT_TIME));
        assertEquals(GS_NAME, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_GROUND_STATION_NAME));
        assertEquals(GS_ID, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_GROUND_STATION_ID));
        assertEquals(SAT_NAME, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_SATELLITE_NAME));
        assertEquals(SAT_ID, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_SATELLITE_ID));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCreateJob() throws Exception {
        JobDataMap map = new JobDataMap();
        map.put(GROUP_NAME, SAT_ID);
        JobDetail job = eventScheduler.createJob(GROUP_NAME, JOB_NAME, map);
        assertNotNull(job);
        assertEquals(NotificationEventCreationJob.class, job.getJobClass());
        assertEquals(GROUP_NAME, job.getKey().getGroup());
        assertEquals(JOB_NAME, job.getKey().getName());
        assertNotNull(job.getJobDataMap());
        assertEquals(1, job.getJobDataMap().size());
        assertEquals(SAT_ID, job.getJobDataMap().get(GROUP_NAME));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCreateJobDataMap() throws Exception {
        JobDataMap map = eventScheduler.createJobDataMap(event, JobType.AOS, NOW);
        assertNotNull(map);
        assertEquals(7, map.size());
        assertEquals(EVENT_ID, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_CONTACT_INSTANCE_ID));
        assertEquals(JobType.AOS.toString(), map.getString(NotificationEventCreationJob.JOB_DATA_KEY_EVENT_TYPE));
        assertEquals(NOW, map.getLong(NotificationEventCreationJob.JOB_DATA_KEY_EVENT_TIME));
        assertEquals(GS_ID, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_GROUND_STATION_ID));
        assertEquals(GS_NAME, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_GROUND_STATION_NAME));
        assertEquals(SAT_ID, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_SATELLITE_ID));
        assertEquals(SAT_NAME, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_SATELLITE_NAME));
        inOrder.verify(event, times(1)).getInstanceID();
        inOrder.verify(event, times(1)).getGroundStationID();
        inOrder.verify(gsCache, times(1)).getById(GS_ID);
        inOrder.verify(gs, times(1)).getName();
        inOrder.verify(event, times(1)).getSatelliteID();
        inOrder.verify(satCache, times(1)).getById(SAT_ID);
        inOrder.verify(sat, times(1)).getName();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCreateJobDataMapNoGsFound() throws Exception {
        when(gsCache.getById(GS_ID)).thenReturn(null);
        JobDataMap map = eventScheduler.createJobDataMap(event, JobType.AOS, NOW);
        assertNotNull(map);
        assertEquals(7, map.size());
        assertEquals(EVENT_ID, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_CONTACT_INSTANCE_ID));
        assertEquals(JobType.AOS.toString(), map.getString(NotificationEventCreationJob.JOB_DATA_KEY_EVENT_TYPE));
        assertEquals(NOW, map.getLong(NotificationEventCreationJob.JOB_DATA_KEY_EVENT_TIME));
        assertEquals(GS_ID, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_GROUND_STATION_ID));
        assertEquals(GS_ID, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_GROUND_STATION_NAME));
        assertEquals(SAT_ID, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_SATELLITE_ID));
        assertEquals(SAT_NAME, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_SATELLITE_NAME));
        inOrder.verify(event, times(1)).getInstanceID();
        inOrder.verify(event, times(1)).getGroundStationID();
        inOrder.verify(gsCache, times(1)).getById(GS_ID);
        inOrder.verify(event, times(1)).getSatelliteID();
        inOrder.verify(satCache, times(1)).getById(SAT_ID);
        inOrder.verify(sat, times(1)).getName();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCreateJobDataMapNoSatFound() throws Exception {
        when(satCache.getById(SAT_ID)).thenReturn(null);
        JobDataMap map = eventScheduler.createJobDataMap(event, JobType.AOS, NOW);
        assertNotNull(map);
        assertEquals(7, map.size());
        assertEquals(EVENT_ID, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_CONTACT_INSTANCE_ID));
        assertEquals(JobType.AOS.toString(), map.getString(NotificationEventCreationJob.JOB_DATA_KEY_EVENT_TYPE));
        assertEquals(NOW, map.getLong(NotificationEventCreationJob.JOB_DATA_KEY_EVENT_TIME));
        assertEquals(GS_ID, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_GROUND_STATION_ID));
        assertEquals(GS_NAME, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_GROUND_STATION_NAME));
        assertEquals(SAT_ID, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_SATELLITE_ID));
        assertEquals(SAT_ID, map.getString(NotificationEventCreationJob.JOB_DATA_KEY_SATELLITE_NAME));
        inOrder.verify(event, times(1)).getInstanceID();
        inOrder.verify(event, times(1)).getGroundStationID();
        inOrder.verify(gsCache, times(1)).getById(GS_ID);
        inOrder.verify(gs, times(1)).getName();
        inOrder.verify(event, times(1)).getSatelliteID();
        inOrder.verify(satCache, times(1)).getById(SAT_ID);
        inOrder.verifyNoMoreInteractions();
    }
}
