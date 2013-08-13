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
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class TleResolver {

    private static Logger LOG = LoggerFactory.getLogger(TleResolver.class);

    @Autowired
    private final IDataAccess dao;

    public TleResolver(IDataAccess dao) {
        this.dao = dao;
    }

    @Handler
    public PredictionRequest<?> resolveTle(PredictionRequest<?> request) throws Exception {
        PredictionConfigurationBase config = request.getConfiguration();
        String satelliteId = config.getSatelliteId();
        LOG.debug("Resolving TLE for the satellite ID '{}'", satelliteId);
        TleOrbitalParameters tle;
        try {
            tle = dao.getTleFor(satelliteId);
        }
        catch (NotFoundException nfe) {
            LOG.error("No TLE found for the Satellite ID '{}'", satelliteId);
            LOG.info("   1. check config - is Satellite ID '{}' correct?", satelliteId);
            LOG.info("   2. check db - is the Satellite with the ID '{}' available?", satelliteId);
            LOG.info("   3. check db - is the TLE for the Satellite with the ID '{}' available?", satelliteId);
            throw new IllegalStateException("No TLE available for the satellite ID '" + satelliteId + "'");
        }
        LOG.debug("Found TLE '{}'", tle.getInstanceID());
        request.setTleParameters(tle);
        return request;
    }
}
