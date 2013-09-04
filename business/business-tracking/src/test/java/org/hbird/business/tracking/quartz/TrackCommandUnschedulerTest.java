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

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TrackCommandUnschedulerTest {

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

    private TrackCommandUnscheduler contactUnscheduler;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        contactUnscheduler = new TrackCommandUnscheduler(scheduler, support);
        inOrder = inOrder(scheduler, exchange, in, out, event, support);
        when(exchange.getIn()).thenReturn(in);
        when(exchange.getOut()).thenReturn(out);
        when(in.getBody(LocationContactEvent.class)).thenReturn(event);
        when(support.createJobName(JobType.TRACK, event)).thenReturn(JOB_NAME);
        when(support.createGroupName(event)).thenReturn(GROUP_NAME);
    }

    @Test
    public void testProcessJobFound() throws Exception {
        when(scheduler.deleteJob(any(JobKey.class))).thenReturn(true);
        contactUnscheduler.process(exchange);
        inOrder.verify(exchange, times(1)).getIn();
        inOrder.verify(exchange, times(1)).getOut();
        inOrder.verify(out, times(1)).copyFrom(in);
        inOrder.verify(in, times(1)).getBody(LocationContactEvent.class);
        inOrder.verify(support, times(1)).createGroupName(event);
        inOrder.verify(support, times(1)).createJobName(JobType.TRACK, event);
        ArgumentCaptor<JobKey> captor = ArgumentCaptor.forClass(JobKey.class);
        inOrder.verify(scheduler, times(1)).deleteJob(captor.capture());
        assertNotNull(captor.getValue());
        assertEquals(GROUP_NAME, captor.getValue().getGroup());
        assertEquals(JOB_NAME, captor.getValue().getName());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testProcessJobNotFound() throws Exception {
        when(scheduler.deleteJob(any(JobKey.class))).thenReturn(false);
        contactUnscheduler.process(exchange);
        inOrder.verify(exchange, times(1)).getIn();
        inOrder.verify(exchange, times(1)).getOut();
        inOrder.verify(out, times(1)).copyFrom(in);
        inOrder.verify(in, times(1)).getBody(LocationContactEvent.class);
        inOrder.verify(support, times(1)).createGroupName(event);
        inOrder.verify(support, times(1)).createJobName(JobType.TRACK, event);
        ArgumentCaptor<JobKey> captor = ArgumentCaptor.forClass(JobKey.class);
        inOrder.verify(scheduler, times(1)).deleteJob(captor.capture());
        assertNotNull(captor.getValue());
        assertEquals(GROUP_NAME, captor.getValue().getGroup());
        assertEquals(JOB_NAME, captor.getValue().getName());
        inOrder.verifyNoMoreInteractions();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testProcessWithSchedulerException() throws Exception {
        when(scheduler.deleteJob(any(JobKey.class))).thenThrow(SchedulerException.class);
        contactUnscheduler.process(exchange);
        inOrder.verify(exchange, times(1)).getIn();
        inOrder.verify(exchange, times(1)).getOut();
        inOrder.verify(out, times(1)).copyFrom(in);
        inOrder.verify(in, times(1)).getBody(LocationContactEvent.class);
        inOrder.verify(support, times(1)).createGroupName(event);
        inOrder.verify(support, times(1)).createJobName(JobType.TRACK, event);
        ArgumentCaptor<JobKey> captor = ArgumentCaptor.forClass(JobKey.class);
        inOrder.verify(scheduler, times(1)).deleteJob(captor.capture());
        assertNotNull(captor.getValue());
        assertEquals(GROUP_NAME, captor.getValue().getGroup());
        assertEquals(JOB_NAME, captor.getValue().getName());
        inOrder.verify(exchange, times(1)).setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
        inOrder.verifyNoMoreInteractions();
    }
}
