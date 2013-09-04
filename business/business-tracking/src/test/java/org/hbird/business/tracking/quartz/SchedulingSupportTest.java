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
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.hbird.exchange.navigation.LocationContactEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.Scheduler;
import org.quartz.Trigger;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SchedulingSupportTest {

    private static final long NOW = System.currentTimeMillis();
    private static final String SAT_ID = "SAT-1";
    private static final String GS_ID = "GS-X";
    private static final String EVENT_ID = "GS-X:SAT-1";
    private static final String EVENT_INSTANCE_ID = EVENT_ID + ":1234567890";
    private static final String GROUP_NAME = "GS-X/SAT-1";
    private static final String TRIGGER_NAME = "Teh trigga";

    @Mock
    private TrackingDriverConfiguration config;

    @Mock
    private Scheduler scheduler;

    @Mock
    private LocationContactEvent event;

    private SchedulingSupport support;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        support = new SchedulingSupport();
        inOrder = inOrder(config, scheduler, event);
    }

    @Test
    public void testCreateTrigger() throws Exception {
        Trigger trigger = support.createTrigger(GROUP_NAME, TRIGGER_NAME, NOW);
        assertNotNull(trigger);
        assertEquals(NOW, trigger.getStartTime().getTime());
        assertEquals(TRIGGER_NAME, trigger.getKey().getName());
        assertEquals(GROUP_NAME, trigger.getKey().getGroup());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCreateJobName() throws Exception {
        when(event.getInstanceID()).thenReturn(EVENT_INSTANCE_ID);
        assertEquals(JobType.TRACK.toString() + SchedulingSupport.JOB + "-" + EVENT_INSTANCE_ID, support.createJobName(JobType.TRACK, event));
        inOrder.verify(event, times(1)).getInstanceID();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCreateTriggerName() throws Exception {
        when(event.getInstanceID()).thenReturn(EVENT_INSTANCE_ID);
        assertEquals(JobType.TRACK.toString() + SchedulingSupport.TRIGGER + "-" + EVENT_INSTANCE_ID, support.createTriggerName(JobType.TRACK, event));
        inOrder.verify(event, times(1)).getInstanceID();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCreateGroupName() throws Exception {
        when(event.getGroundStationID()).thenReturn(GS_ID);
        when(event.getSatelliteID()).thenReturn(SAT_ID);
        assertEquals(GS_ID + "/" + SAT_ID, support.createGroupName(event));
        inOrder.verify(event, times(1)).getGroundStationID();
        inOrder.verify(event, times(1)).getSatelliteID();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetScheduleTime() throws Exception {
        when(event.getStartTime()).thenReturn(NOW + 101L);
        when(config.getScheduleDelta()).thenReturn(100L);
        assertEquals(NOW + 1L, support.getScheduleTime(event, config, NOW));
        inOrder.verify(event, times(1)).getStartTime();
        inOrder.verify(config, times(1)).getScheduleDelta();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetScheduleTimeTooLate() throws Exception {
        when(event.getStartTime()).thenReturn(NOW + 100);
        when(config.getScheduleDelta()).thenReturn(100L);
        assertEquals(SchedulingSupport.TOO_LATE, support.getScheduleTime(event, config, NOW));
        inOrder.verify(event, times(1)).getStartTime();
        inOrder.verify(config, times(1)).getScheduleDelta();
        inOrder.verifyNoMoreInteractions();
    }

}
