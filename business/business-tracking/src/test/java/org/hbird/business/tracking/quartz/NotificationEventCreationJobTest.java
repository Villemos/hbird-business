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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.apache.camel.ProducerTemplate;
import org.hbird.business.api.IdBuilder;
import org.hbird.exchange.core.Event;
import org.hbird.exchange.interfaces.IStartableEntity;
import org.hbird.exchange.util.Dates;
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
public class NotificationEventCreationJobTest {

    private static final long NOW = System.currentTimeMillis();
    private static final String END_POINT = "end-point";
    private static final String EVENT_INSTANCE_ID = "location-contact-event-instance-id";
    private static final String EVENT_ID = "notification-event-id";
    private static final String SAT = "sat-1";
    private static final String GS = "gs-hg";
    private static final String TYPE = "typo";
    private static final String ISSUER = "issuer";
    private static final String NAME_SPACE = "SPACE";

    @Mock
    private TrackingDriverConfiguration config;

    @Mock
    private IdBuilder idBuilder;

    @Mock
    private IStartableEntity issuer;

    @Mock
    private ProducerTemplate producer;

    @Mock
    private JobExecutionContext context;

    @Mock
    private JobDataMap jobDataMap;

    private NotificationEventCreationJob job;

    private InOrder inOrder;

    private RuntimeException exception;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        job = new NotificationEventCreationJob();
        job.setConfig(config);
        job.setEndPoint(END_POINT);
        job.setIdBuilder(idBuilder);
        job.setIssuer(issuer);
        job.setProducer(producer);
        exception = new RuntimeException("Nuts!");
        inOrder = inOrder(config, idBuilder, issuer, producer, context, jobDataMap);
        when(context.getMergedJobDataMap()).thenReturn(jobDataMap);
        when(jobDataMap.getString(NotificationEventCreationJob.JOB_DATA_KEY_CONTACT_INSTANCE_ID)).thenReturn(EVENT_INSTANCE_ID);
        when(jobDataMap.getLong(NotificationEventCreationJob.JOB_DATA_KEY_EVENT_TIME)).thenReturn(NOW);
        when(jobDataMap.getString(NotificationEventCreationJob.JOB_DATA_KEY_EVENT_TYPE)).thenReturn(TYPE);
        when(jobDataMap.getString(NotificationEventCreationJob.JOB_DATA_KEY_GROUND_STATION_NAME)).thenReturn(GS);
        when(jobDataMap.getString(NotificationEventCreationJob.JOB_DATA_KEY_SATELLITE_NAME)).thenReturn(SAT);
        when(config.getEventNameSpace()).thenReturn(NAME_SPACE);
        when(idBuilder.buildID(NAME_SPACE, TYPE)).thenReturn(EVENT_ID);
        when(issuer.getID()).thenReturn(ISSUER);
    }

    @Test
    public void testExecute() throws Exception {
        job.execute(context);

        inOrder.verify(context, times(1)).getMergedJobDataMap();
        inOrder.verify(jobDataMap, times(1)).getString(NotificationEventCreationJob.JOB_DATA_KEY_CONTACT_INSTANCE_ID);
        inOrder.verify(jobDataMap, times(1)).getString(NotificationEventCreationJob.JOB_DATA_KEY_GROUND_STATION_NAME);
        inOrder.verify(jobDataMap, times(1)).getString(NotificationEventCreationJob.JOB_DATA_KEY_SATELLITE_NAME);
        inOrder.verify(jobDataMap, times(1)).getString(NotificationEventCreationJob.JOB_DATA_KEY_EVENT_TYPE);
        inOrder.verify(jobDataMap, times(1)).getLong(NotificationEventCreationJob.JOB_DATA_KEY_EVENT_TIME);
        inOrder.verify(config, times(1)).getEventNameSpace();
        inOrder.verify(idBuilder, times(1)).buildID(NAME_SPACE, TYPE);
        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        inOrder.verify(producer, times(1)).asyncSendBody(eq(END_POINT), captor.capture());
        Event event = captor.getValue();
        assertNotNull(event);
        assertEquals(EVENT_ID, event.getID());
        assertEquals(ISSUER, event.getIssuedBy());
        assertEquals(EVENT_INSTANCE_ID, event.getApplicableTo());
        assertEquals(NOW, event.getTimestamp());
        assertTrue(NOW <= event.getVersion());
        assertTrue(event.getDescription().contains(SAT));
        assertTrue(event.getDescription().contains(GS));
        assertTrue(event.getDescription().contains(Dates.toDefaultDateFormat(NOW)));
        assertTrue(event.getDescription().contains(TYPE));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testExecuteWithExcpetion() throws Exception {
        when(producer.asyncSendBody(eq(END_POINT), any(Event.class))).thenThrow(exception);

        job.execute(context);

        inOrder.verify(context, times(1)).getMergedJobDataMap();
        inOrder.verify(jobDataMap, times(1)).getString(NotificationEventCreationJob.JOB_DATA_KEY_CONTACT_INSTANCE_ID);
        inOrder.verify(jobDataMap, times(1)).getString(NotificationEventCreationJob.JOB_DATA_KEY_GROUND_STATION_NAME);
        inOrder.verify(jobDataMap, times(1)).getString(NotificationEventCreationJob.JOB_DATA_KEY_SATELLITE_NAME);
        inOrder.verify(jobDataMap, times(1)).getString(NotificationEventCreationJob.JOB_DATA_KEY_EVENT_TYPE);
        inOrder.verify(jobDataMap, times(1)).getLong(NotificationEventCreationJob.JOB_DATA_KEY_EVENT_TIME);
        inOrder.verify(config, times(1)).getEventNameSpace();
        inOrder.verify(idBuilder, times(1)).buildID(NAME_SPACE, TYPE);
        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        inOrder.verify(producer, times(1)).asyncSendBody(eq(END_POINT), captor.capture());
        Event event = captor.getValue();
        assertNotNull(event);
        assertEquals(EVENT_ID, event.getID());
        assertEquals(ISSUER, event.getIssuedBy());
        assertEquals(EVENT_INSTANCE_ID, event.getApplicableTo());
        assertEquals(NOW, event.getTimestamp());
        assertTrue(NOW <= event.getVersion());
        assertTrue(event.getDescription().contains(SAT));
        assertTrue(event.getDescription().contains(GS));
        assertTrue(event.getDescription().contains(Dates.toDefaultDateFormat(NOW)));
        assertTrue(event.getDescription().contains(TYPE));
        inOrder.verifyNoMoreInteractions();
    }
}
