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
import org.hbird.business.core.cache.EntityCache;
import org.hbird.exchange.interfaces.IStartablePart;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.jobs.NoOpJob;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TrackCommandCreationJobFactoryTest {

    private static final String END_POINT = "direct:inject";

    @Mock
    private IStartablePart part;

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
    private TrackCommandCreationJob job;

    @Mock
    private Scheduler scheduler;

    private TrackCommandCreationJobFactory factory;

    private InOrder inOrder;

    private final Logger LOG = LoggerFactory.getLogger(TrackCommandCreationJobFactory.class);

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        factory = new TrackCommandCreationJobFactory(part, dao, producer, END_POINT, satelliteCache, tleCache);
        inOrder = inOrder(dao, producer, satelliteCache, tleCache, bundle, jobDetail, job, scheduler, part);
        when(bundle.getJobDetail()).thenReturn(jobDetail);
    }

    @After
    public void tearDown() {
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testInitialize() {
        factory.initialise(job);
        inOrder.verify(job, times(1)).setDataAccess(dao);
        inOrder.verify(job, times(1)).setProducerTemplate(producer);
        inOrder.verify(job, times(1)).setEndpoint(END_POINT);
        inOrder.verify(job, times(1)).setSatelliteCache(satelliteCache);
        inOrder.verify(job, times(1)).setTleCache(tleCache);
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
    }

    @Test
    public void testCreateJobInstance() throws Exception {
        doReturn(TrackCommandCreationJob.class).when(jobDetail).getJobClass();
        Object o = factory.newJob(bundle, scheduler);
        assertNotNull(o);
        assertEquals(TrackCommandCreationJob.class, o.getClass());
        inOrder.verify(bundle, times(1)).getJobDetail();
        inOrder.verify(jobDetail, times(1)).getJobClass();
        if (LOG.isDebugEnabled()) {
            inOrder.verify(jobDetail, times(1)).getKey();
        }
    }
}
