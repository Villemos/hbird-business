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
package org.hbird.business.navigation.processors;

import org.apache.camel.Handler;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.exceptions.NotFoundException;
import org.hbird.business.navigation.configuration.PredictionConfigurationBase;
import org.hbird.business.navigation.request.PredictionRequest;
import org.hbird.exchange.navigation.Satellite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class SatelliteResolver {

    private static final Logger LOG = LoggerFactory.getLogger(SatelliteResolver.class);

    private final IDataAccess dao;

    public SatelliteResolver(IDataAccess dao) {
        this.dao = dao;
    }

    @Handler
    public <T extends PredictionConfigurationBase> PredictionRequest<T> resolve(PredictionRequest<T> request) {
        PredictionConfigurationBase config = request.getConfiguration();
        String satelliteId = config.getSatelliteId();
        try {
            Satellite satellite = dao.getById(satelliteId, Satellite.class);
            LOG.debug("Resolved Satellite for the ID '{}'", satelliteId);
            request.setSatellite(satellite);
        }
        catch (NotFoundException nfe) {
            LOG.warn("Satellite for the ID '{}' not found in DB", satelliteId);
            LOG.info("   1. check config - is the Satellite ID '{}' correct?", satelliteId);
            LOG.info("   2. check DB - is the Satellite with ID '{}' available?", satelliteId);
        }
        catch (Exception e) {
            LOG.error("Failed to resolve Satellite for the ID '{}'", satelliteId, e);
        }

        return request;
    }
}
