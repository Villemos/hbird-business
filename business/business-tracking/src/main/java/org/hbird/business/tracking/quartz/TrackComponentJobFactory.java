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

import org.apache.camel.ProducerTemplate;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IdBuilder;
import org.hbird.business.core.cache.EntityCache;
import org.hbird.exchange.interfaces.IStartableEntity;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.simpl.SimpleJobFactory;
import org.quartz.spi.TriggerFiredBundle;

/**
 *
 */
public class TrackComponentJobFactory extends SimpleJobFactory {

    private final IStartableEntity part;

    private final IDataAccess dao;

    private final ProducerTemplate producer;

    private final String endPoint;

    private final EntityCache<Satellite> satelliteCache;

    private final EntityCache<TleOrbitalParameters> tleCache;

    private final IdBuilder idBuilder;

    private final TrackingDriverConfiguration config;

    public TrackComponentJobFactory(IStartableEntity part, IDataAccess dao, ProducerTemplate producer, String endPoint,
            EntityCache<Satellite> satelliteCache, EntityCache<TleOrbitalParameters> tleCache, IdBuilder idBuilder, TrackingDriverConfiguration config) {
        this.part = part;
        this.dao = dao;
        this.producer = producer;
        this.endPoint = endPoint;
        this.satelliteCache = satelliteCache;
        this.tleCache = tleCache;
        this.idBuilder = idBuilder;
        this.config = config;
    }

    /**
     * @see org.quartz.simpl.SimpleJobFactory#newJob(org.quartz.spi.TriggerFiredBundle, org.quartz.Scheduler)
     */
    @Override
    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
        Job job = super.newJob(bundle, scheduler);
        if (job instanceof TrackCommandCreationJob) {
            initialise((TrackCommandCreationJob) job);
        }
        else if (job instanceof NotificationEventCreationJob) {
            initialise((NotificationEventCreationJob) job);
        }
        return job;
    }

    /**
     * @param job
     */
    void initialise(TrackCommandCreationJob job) {
        job.setDataAccess(dao);
        job.setEndpoint(endPoint);
        job.setIdBuilder(idBuilder);
        job.setIssuer(part);
        job.setProducerTemplate(producer);
        job.setSatelliteCache(satelliteCache);
        job.setTleCache(tleCache);
    }

    void initialise(NotificationEventCreationJob job) {
        job.setConfig(config);
        job.setEndPoint(endPoint);
        job.setIdBuilder(idBuilder);
        job.setIssuer(part);
        job.setProducer(producer);
    }
}
