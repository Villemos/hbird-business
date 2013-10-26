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

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Handler;
import org.hbird.business.api.IOrbitalDataAccess;
import org.hbird.business.api.exceptions.NotFoundException;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ArchivePoller {

    private static final Logger LOG = LoggerFactory.getLogger(ArchivePoller.class);

    private final TrackingDriverConfiguration config;

    private final IOrbitalDataAccess dao;

    public ArchivePoller(TrackingDriverConfiguration config, IOrbitalDataAccess dao) {
        this.config = config;
        this.dao = dao;
    }

    @Handler
    public List<LocationContactEvent> poll() {
        List<String> satellites = config.getSatelliteIds();
        List<LocationContactEvent> result = new ArrayList<LocationContactEvent>(satellites.size());
        String groundStationId = config.getGroundstationId();

        for (String satelliteId : satellites) {

            try {
                LocationContactEvent event = dao.getNextLocationContactEventFor(groundStationId, satelliteId);
                LOG.trace("Found {}", event.toString());
                result.add(event);
            }
            catch (NotFoundException nfe) {
                LOG.warn("Couldn't find next contact event for GroundStation '{}' and Satellite '{}' from DB.", groundStationId, satelliteId);
                LOG.info("   1. check the logs - is prediction done for the GroundStation '{}' and Satellite '{}'?", groundStationId, satelliteId);
                LOG.info("   2. check config - is GroundStation ID '{}' correct?", groundStationId);
                LOG.info("   3. check config - is Satellite ID '{}' correct?", satelliteId);
            }
            catch (Exception e) {
                LOG.warn("Failed to find next contact event for groundstation '{}' and satellite '{}'", new Object[] { groundStationId, satelliteId, e });
            }
        }
        return result;
    }
}
