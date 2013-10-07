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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.apache.camel.ProducerTemplate;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IdBuilder;
import org.hbird.business.core.cache.EntityCache;
import org.hbird.exchange.interfaces.IStartableEntity;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TrackComponentJobFactoryTest {

    private static final String END_POINT = "direct:inject";

    @Mock
    private IStartableEntity part;

    @Mock
    private IDataAccess dao;

    @Mock
    private ProducerTemplate producer;

    @Mock
    private EntityCache<Satellite> satelliteCache;

    @Mock
    private EntityCache<TleOrbitalParameters> tleCache;

    @Mock
    private TriggerFiredBundle bundle;

    @Mock
    private JobDetail jobDetail;

    @Mock
    private TrackCommandCreationJob trackingJob;

    @Mock
    private NotificationEventCreationJob notificationJob;

    @Mock
    private Scheduler scheduler;

    @Mock
    private IdBuilder idBuilder;

    @Mock
    private TrackingDriverConfiguration config;

    private TrackComponentJobFactory factory;

    private InOrder inOrder;

    private final Logger LOG = LoggerFactory.getLogger(TrackComponentJobFactory.class);

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        factory = new TrackComponentJobFactory(part, dao, producer, END_POINT, satelliteCache, tleCache, idBuilder, config);
        inOrder = inOrder(dao, producer, satelliteCache, tleCache, bundle, jobDetail, trackingJob, scheduler, part, idBuilder, config, notificationJob);
        when(bundle.getJobDetail()).thenReturn(jobDetail);
    }

    @Test
    public void testInitialiseTrackCommandCreationJob() {
        factory.initialise(trackingJob);
        inOrder.verify(trackingJob, times(1)).setDataAccess(dao);
        inOrder.verify(trackingJob, times(1)).setEndpoint(END_POINT);
        inOrder.verify(trackingJob, times(1)).setIdBuilder(idBuilder);
        inOrder.verify(trackingJob, times(1)).setIssuer(part);
        inOrder.verify(trackingJob, times(1)).setProducerTemplate(producer);
        inOrder.verify(trackingJob, times(1)).setSatelliteCache(satelliteCache);
        inOrder.verify(trackingJob, times(1)).setTleCache(tleCache);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testInitialiseNotificationEventCreationJob() {
        factory.initialise(notificationJob);
        inOrder.verify(notificationJob, times(1)).setConfig(config);
        inOrder.verify(notificationJob, times(1)).setEndPoint(END_POINT);
        inOrder.verify(notificationJob, times(1)).setIdBuilder(idBuilder);
        inOrder.verify(notificationJob, times(1)).setIssuer(part);
        inOrder.verify(notificationJob, times(1)).setProducer(producer);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCreateJobInstanceOtherType() throws Exception {
        doReturn(NoOpJob.class).when(jobDetail).getJobClass();
        Object o = factory.newJob(bundle, scheduler);
        assertNotNull(o);
        assertEquals(NoOpJob.class, o.getClass());
        inOrder.verify(bundle, times(1)).getJobDetail();
        inOrder.verify(jobDetail, times(1)).getJobClass();
        if (LOG.isDebugEnabled()) {
            inOrder.verify(jobDetail, times(1)).getKey();
        }
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCreateTrackingJobInstance() throws Exception {
        doReturn(TrackCommandCreationJob.class).when(jobDetail).getJobClass();
        Object o = factory.newJob(bundle, scheduler);
        assertNotNull(o);
        assertEquals(TrackCommandCreationJob.class, o.getClass());
        inOrder.verify(bundle, times(1)).getJobDetail();
        inOrder.verify(jobDetail, times(1)).getJobClass();
        if (LOG.isDebugEnabled()) {
            inOrder.verify(jobDetail, times(1)).getKey();
        }
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCreateNotificationJobInstance() throws Exception {
        doReturn(NotificationEventCreationJob.class).when(jobDetail).getJobClass();
        Object o = factory.newJob(bundle, scheduler);
        assertNotNull(o);
        assertEquals(NotificationEventCreationJob.class, o.getClass());
        inOrder.verify(bundle, times(1)).getJobDetail();
        inOrder.verify(jobDetail, times(1)).getJobClass();
        if (LOG.isDebugEnabled()) {
            inOrder.verify(jobDetail, times(1)).getKey();
        }
        inOrder.verifyNoMoreInteractions();
    }

    public static class NoOpJob implements Job {

        public NoOpJob() {
        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
        }
    }
}
