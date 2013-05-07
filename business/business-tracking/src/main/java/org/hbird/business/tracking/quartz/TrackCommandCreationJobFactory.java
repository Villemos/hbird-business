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
import org.hbird.business.core.cache.EntityCache;
import org.hbird.exchange.interfaces.IStartablePart;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

/**
 *
 */
public class TrackCommandCreationJobFactory extends AdaptableJobFactory {

    private final IDataAccess dao;

    private final ProducerTemplate producer;

    private final String endPoint;

    private final EntityCache<Satellite> satelliteCache;

    private final EntityCache<TleOrbitalParameters> tleCache;

    public TrackCommandCreationJobFactory(IDataAccess dao, ProducerTemplate producer, String endPoint, EntityCache<Satellite> satelliteCache,
            EntityCache<TleOrbitalParameters> tleCache, IStartablePart part) {
        this.dao = dao;
        this.producer = producer;
        this.endPoint = endPoint;
        this.satelliteCache = satelliteCache;
        this.tleCache = tleCache;
    }

    /**
     * @see org.springframework.scheduling.quartz.AdaptableJobFactory#createJobInstance(org.quartz.spi.TriggerFiredBundle)
     */
    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object job = bundle.getJobDetail().getJobClass().newInstance();
        if (job instanceof TrackCommandCreationJob) {
            TrackCommandCreationJob commandCreator = (TrackCommandCreationJob) job;
            commandCreator.setDataAccess(dao);
            commandCreator.setProducerTemplate(producer);
            commandCreator.setEndpoint(endPoint);
            commandCreator.setSatelliteCache(satelliteCache);
            commandCreator.setTleCache(tleCache);
        }
        return job;
    }

}
