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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.hbird.exchange.navigation.LocationContactEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

/**
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class TrackCommandSchedulerTest {

    private static final long NOW = System.currentTimeMillis();
    private static final String JOB_NAME = "JOB-NAME";
    private static final String TRIGGER_NAME = "TRIGGER-NAME";
    private static final String GROUP_NAME = "GROUP-NAME";
    private static final String EVENT_ID = "GS-X:SAT-1";
    private static final String EVENT_INSTANCE_ID = EVENT_ID + ":1234567890";
    private static final String TLE_ID = "TLE:8912";

    @Mock
    private TrackingDriverConfiguration config;

    @Mock
    private LocationContactEvent event;

    @Mock
    private Scheduler scheduler;

    @Mock
    private SchedulingSupport support;

    @Mock
    private Trigger trigger;

    @Mock
    private Date date;

    private TrackCommandScheduler contactScheduler;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        contactScheduler = new TrackCommandScheduler(config, scheduler, support);
        inOrder = inOrder(config, event, scheduler, support, trigger, date);
        when(support.createGroupName(event)).thenReturn(GROUP_NAME);
        when(support.createTriggerName(JobType.TRACK, event)).thenReturn(TRIGGER_NAME);
        when(support.createTrigger(eq(GROUP_NAME), eq(TRIGGER_NAME), anyLong())).thenReturn(trigger);
        when(support.createJobName(JobType.TRACK, event)).thenReturn(JOB_NAME);
        when(event.getInstanceID()).thenReturn(EVENT_INSTANCE_ID);
        when(event.getID()).thenReturn(EVENT_ID);
        when(event.getDerivedFromId()).thenReturn(TLE_ID);
        when(event.getStartTime()).thenReturn(NOW);
        // when(support.createJob(event, GROUP_NAME)).thenReturn(jobDetail);
    }

    @Test
    public void testHandleTooLate() throws Exception {
        when(support.getScheduleTime(eq(event), eq(config), anyLong())).thenReturn(SchedulingSupport.TOO_LATE);
        try {
            contactScheduler.handle(event);
            fail("Exception expected");
        }
        catch (Exception e) {
            assertEquals(IllegalStateException.class, e.getClass());
        }
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        inOrder.verify(support, times(1)).getScheduleTime(eq(event), eq(config), captor.capture());
        assertNotNull(captor.getValue());
        long timestamp = captor.getValue();
        assertTrue(timestamp >= NOW);
        assertTrue(timestamp <= System.currentTimeMillis());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testHandleWithSchedulerException() throws Exception {
        when(support.getScheduleTime(eq(event), eq(config), anyLong())).thenReturn(500L);
        doThrow(SchedulerException.class).when(scheduler).scheduleJob(any(JobDetail.class), eq(trigger));
        contactScheduler.handle(event);
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        inOrder.verify(support, times(1)).getScheduleTime(eq(event), eq(config), captor.capture());
        assertNotNull(captor.getValue());
        long timestamp = captor.getValue();
        assertTrue(timestamp >= NOW);
        assertTrue(timestamp <= System.currentTimeMillis());
        inOrder.verify(support, times(1)).createGroupName(event);
        inOrder.verify(support, times(1)).createTriggerName(JobType.TRACK, event);
        // inOrder.verify(support, times(1)).createJob(event, GROUP_NAME);
        inOrder.verify(event, times(1)).getInstanceID();
        inOrder.verify(event, times(1)).getID();
        inOrder.verify(event, times(1)).getDerivedFromId();
        inOrder.verify(event, times(1)).getStartTime();
        inOrder.verify(support, times(1)).createTrigger(GROUP_NAME, TRIGGER_NAME, 500L);
        ArgumentCaptor<JobDetail> jobDetailCaptor = ArgumentCaptor.forClass(JobDetail.class);
        inOrder.verify(scheduler, times(1)).scheduleJob(jobDetailCaptor.capture(), eq(trigger));
        JobDetail jobDetail = jobDetailCaptor.getValue();
        assertNotNull(jobDetail);
        assertEquals(TrackCommandCreationJob.class, jobDetail.getJobClass());
        assertEquals(GROUP_NAME, jobDetail.getKey().getGroup());
        assertEquals(JOB_NAME, jobDetail.getKey().getName());
        assertEquals(EVENT_INSTANCE_ID, jobDetail.getJobDataMap().getString(TrackCommandCreationJob.JOB_DATA_KEY_CONTACT_INSTANCE_ID));
        assertEquals(EVENT_ID, jobDetail.getJobDataMap().getString(TrackCommandCreationJob.JOB_DATA_EVENT_ID));
        assertEquals(TLE_ID, jobDetail.getJobDataMap().getString(TrackCommandCreationJob.JOB_DATA_TLE_ID));
        assertEquals(NOW, jobDetail.getJobDataMap().getLong(TrackCommandCreationJob.JOB_DATA_START_TIME));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testHandle() throws Exception {
        when(support.getScheduleTime(eq(event), eq(config), anyLong())).thenReturn(500L);
        when(scheduler.scheduleJob(any(JobDetail.class), eq(trigger))).thenReturn(date);
        contactScheduler.handle(event);
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        inOrder.verify(support, times(1)).getScheduleTime(eq(event), eq(config), captor.capture());
        assertNotNull(captor.getValue());
        long timestamp = captor.getValue();
        assertTrue(timestamp >= NOW);
        assertTrue(timestamp <= System.currentTimeMillis());
        inOrder.verify(support, times(1)).createGroupName(event);
        inOrder.verify(support, times(1)).createTriggerName(JobType.TRACK, event);
        // inOrder.verify(support, times(1)).createJob(event, GROUP_NAME);
        inOrder.verify(event, times(1)).getInstanceID();
        inOrder.verify(event, times(1)).getID();
        inOrder.verify(event, times(1)).getDerivedFromId();
        inOrder.verify(event, times(1)).getStartTime();

        inOrder.verify(support, times(1)).createTrigger(GROUP_NAME, TRIGGER_NAME, 500L);
        ArgumentCaptor<JobDetail> jobDetailCaptor = ArgumentCaptor.forClass(JobDetail.class);
        inOrder.verify(scheduler, times(1)).scheduleJob(jobDetailCaptor.capture(), eq(trigger));
        JobDetail jobDetail = jobDetailCaptor.getValue();
        assertNotNull(jobDetail);
        assertEquals(TrackCommandCreationJob.class, jobDetail.getJobClass());
        assertEquals(GROUP_NAME, jobDetail.getKey().getGroup());
        assertEquals(JOB_NAME, jobDetail.getKey().getName());
        assertEquals(EVENT_INSTANCE_ID, jobDetail.getJobDataMap().getString(TrackCommandCreationJob.JOB_DATA_KEY_CONTACT_INSTANCE_ID));
        assertEquals(EVENT_ID, jobDetail.getJobDataMap().getString(TrackCommandCreationJob.JOB_DATA_EVENT_ID));
        assertEquals(TLE_ID, jobDetail.getJobDataMap().getString(TrackCommandCreationJob.JOB_DATA_TLE_ID));
        assertEquals(NOW, jobDetail.getJobDataMap().getLong(TrackCommandCreationJob.JOB_DATA_START_TIME));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCreateJob() throws Exception {
        JobDetail job = contactScheduler.createJob(GROUP_NAME, JOB_NAME, event);
        assertNotNull(job);
        assertEquals(TrackCommandCreationJob.class, job.getJobClass());
        assertEquals(GROUP_NAME, job.getKey().getGroup());
        assertEquals(JOB_NAME, job.getKey().getName());
        assertEquals(EVENT_INSTANCE_ID, job.getJobDataMap().getString(TrackCommandCreationJob.JOB_DATA_KEY_CONTACT_INSTANCE_ID));
        assertEquals(EVENT_ID, job.getJobDataMap().getString(TrackCommandCreationJob.JOB_DATA_EVENT_ID));
        assertEquals(TLE_ID, job.getJobDataMap().getString(TrackCommandCreationJob.JOB_DATA_TLE_ID));
        assertEquals(NOW, job.getJobDataMap().getLong(TrackCommandCreationJob.JOB_DATA_START_TIME));
        inOrder.verify(event, times(1)).getInstanceID();
        inOrder.verify(event, times(1)).getID();
        inOrder.verify(event, times(1)).getDerivedFromId();
        inOrder.verify(event, times(1)).getStartTime();
        inOrder.verifyNoMoreInteractions();
    }
}
