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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class EventAnalyzerTest {

    private static final long NOW = System.currentTimeMillis();
    private static final String TRIGGER_NAME = "TRIGGER-NAME";
    private static final String JOB_NAME = "JOB-NAME";
    private static final String GROUP_NAME = "GROUP-NAME";

    @Mock
    private Scheduler scheduler;

    @Mock
    private SchedulingSupport support;

    @Mock
    private Exchange exchange;

    @Mock
    private Message in;

    @Mock
    private Message out;

    @Mock
    private LocationContactEvent event;

    @Mock
    private Trigger trigger;

    @Mock
    private JobDetail job;

    @Mock
    private JobDataMap jobData;

    private InOrder inOrder;

    private EventAnalyzer eventAnalyzer;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        eventAnalyzer = new EventAnalyzer(scheduler, support);
        inOrder = Mockito.inOrder(scheduler, exchange, in, out, event, job, jobData, support);
        when(exchange.getIn()).thenReturn(in);
        when(exchange.getOut()).thenReturn(out);
        when(in.getBody(LocationContactEvent.class)).thenReturn(event);
        when(event.getStartTime()).thenReturn(NOW);
        when(support.createGroupName(event)).thenReturn(GROUP_NAME);
        when(support.createJobName(JobType.TRACK, event)).thenReturn(JOB_NAME);
        when(support.createTriggerName(JobType.TRACK, event)).thenReturn(TRIGGER_NAME);
    }

    @Test
    public void testProcessNewEvent() throws Exception {
        when(scheduler.getTrigger(any(TriggerKey.class))).thenReturn(null);
        eventAnalyzer.process(exchange);
        inOrder.verify(exchange, times(1)).getIn();
        inOrder.verify(exchange, times(1)).getOut();
        inOrder.verify(out, times(1)).copyFrom(in);
        inOrder.verify(in, times(1)).getBody(LocationContactEvent.class);
        inOrder.verify(support, times(1)).createTriggerName(JobType.TRACK, event);
        inOrder.verify(support, times(1)).createGroupName(event);
        ArgumentCaptor<TriggerKey> triggerKeyCaptor = ArgumentCaptor.forClass(TriggerKey.class);
        inOrder.verify(scheduler, times(1)).getTrigger(triggerKeyCaptor.capture());
        TriggerKey triggerKey = triggerKeyCaptor.getValue();
        assertNotNull(triggerKey);
        assertEquals(GROUP_NAME, triggerKey.getGroup());
        assertEquals(TRIGGER_NAME, triggerKey.getName());
        inOrder.verify(out, times(1)).setHeader(EventAnalyzer.HEADER_KEY_EVENT_TYPE, EventAnalyzer.HEADER_VALUE_NEW_EVENT);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testProcessKnownEvent() throws Exception {
        when(scheduler.getTrigger(any(TriggerKey.class))).thenReturn(trigger);
        when(scheduler.getJobDetail(any(JobKey.class))).thenReturn(job);
        when(job.getJobDataMap()).thenReturn(jobData);
        when(jobData.getLong(TrackCommandCreationJob.JOB_DATA_START_TIME)).thenReturn(NOW);
        eventAnalyzer.process(exchange);
        inOrder.verify(exchange, times(1)).getIn();
        inOrder.verify(exchange, times(1)).getOut();
        inOrder.verify(out, times(1)).copyFrom(in);
        inOrder.verify(in, times(1)).getBody(LocationContactEvent.class);
        inOrder.verify(support, times(1)).createTriggerName(JobType.TRACK, event);
        inOrder.verify(support, times(1)).createGroupName(event);
        ArgumentCaptor<TriggerKey> triggerKeyCaptor = ArgumentCaptor.forClass(TriggerKey.class);
        inOrder.verify(scheduler, times(1)).getTrigger(triggerKeyCaptor.capture());
        TriggerKey triggerKey = triggerKeyCaptor.getValue();
        assertNotNull(triggerKey);
        assertEquals(GROUP_NAME, triggerKey.getGroup());
        assertEquals(TRIGGER_NAME, triggerKey.getName());
        inOrder.verify(support, times(1)).createJobName(JobType.TRACK, event);
        ArgumentCaptor<JobKey> jobKeyCaptor = ArgumentCaptor.forClass(JobKey.class);
        inOrder.verify(scheduler, times(1)).getJobDetail(jobKeyCaptor.capture());
        JobKey jobKey = jobKeyCaptor.getValue();
        assertNotNull(jobKey);
        assertEquals(JOB_NAME, jobKey.getName());
        assertEquals(GROUP_NAME, jobKey.getGroup());
        inOrder.verify(job, times(1)).getJobDataMap();
        inOrder.verify(jobData, times(1)).getLong(TrackCommandCreationJob.JOB_DATA_START_TIME);
        inOrder.verify(event, times(1)).getStartTime();
        inOrder.verify(out, times(1)).setHeader(EventAnalyzer.HEADER_KEY_EVENT_TYPE, EventAnalyzer.HEADER_VALUE_KNOWN_EVENT);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testProcessUpdatedEvent() throws Exception {
        when(scheduler.getTrigger(any(TriggerKey.class))).thenReturn(trigger);
        when(scheduler.getJobDetail(any(JobKey.class))).thenReturn(job);
        when(job.getJobDataMap()).thenReturn(jobData);
        when(jobData.getLong(TrackCommandCreationJob.JOB_DATA_START_TIME)).thenReturn(NOW + 1);
        eventAnalyzer.process(exchange);
        inOrder.verify(exchange, times(1)).getIn();
        inOrder.verify(exchange, times(1)).getOut();
        inOrder.verify(out, times(1)).copyFrom(in);
        inOrder.verify(in, times(1)).getBody(LocationContactEvent.class);
        inOrder.verify(support, times(1)).createTriggerName(JobType.TRACK, event);
        inOrder.verify(support, times(1)).createGroupName(event);
        ArgumentCaptor<TriggerKey> triggerKeyCaptor = ArgumentCaptor.forClass(TriggerKey.class);
        inOrder.verify(scheduler, times(1)).getTrigger(triggerKeyCaptor.capture());
        TriggerKey triggerKey = triggerKeyCaptor.getValue();
        assertNotNull(triggerKey);
        assertEquals(GROUP_NAME, triggerKey.getGroup());
        assertEquals(TRIGGER_NAME, triggerKey.getName());
        inOrder.verify(support, times(1)).createJobName(JobType.TRACK, event);
        ArgumentCaptor<JobKey> jobKeyCaptor = ArgumentCaptor.forClass(JobKey.class);
        inOrder.verify(scheduler, times(1)).getJobDetail(jobKeyCaptor.capture());
        JobKey jobKey = jobKeyCaptor.getValue();
        assertNotNull(jobKey);
        assertEquals(JOB_NAME, jobKey.getName());
        assertEquals(GROUP_NAME, jobKey.getGroup());
        inOrder.verify(job, times(1)).getJobDataMap();
        inOrder.verify(jobData, times(1)).getLong(TrackCommandCreationJob.JOB_DATA_START_TIME);
        inOrder.verify(event, times(1)).getStartTime();
        inOrder.verify(out, times(1)).setHeader(EventAnalyzer.HEADER_KEY_EVENT_TYPE, EventAnalyzer.HEADER_VALUE_UPDATED_EVENT);
        inOrder.verifyNoMoreInteractions();
    }
}
