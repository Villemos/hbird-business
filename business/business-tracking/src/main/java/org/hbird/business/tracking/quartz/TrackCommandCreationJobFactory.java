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
public class TrackCommandCreationJobFactory extends SimpleJobFactory {

    private final IStartableEntity part;

    private final IDataAccess dao;

    private final ProducerTemplate producer;

    private final String endPoint;

    private final EntityCache<Satellite> satelliteCache;

    private final EntityCache<TleOrbitalParameters> tleCache;

    private final IdBuilder idBuilder;

    public TrackCommandCreationJobFactory(IStartableEntity part, IDataAccess dao, ProducerTemplate producer, String endPoint,
            EntityCache<Satellite> satelliteCache, EntityCache<TleOrbitalParameters> tleCache, IdBuilder idBuilder) {
        this.part = part;
        this.dao = dao;
        this.producer = producer;
        this.endPoint = endPoint;
        this.satelliteCache = satelliteCache;
        this.tleCache = tleCache;
        this.idBuilder = idBuilder;
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
        return job;
    }

    /**
     * @param job
     */
    void initialise(TrackCommandCreationJob commandCreator) {
        commandCreator.setPart(part);
        commandCreator.setDataAccess(dao);
        commandCreator.setProducerTemplate(producer);
        commandCreator.setEndpoint(endPoint);
        commandCreator.setSatelliteCache(satelliteCache);
        commandCreator.setTleCache(tleCache);
        commandCreator.setIdBuilder(idBuilder);
    }
}
