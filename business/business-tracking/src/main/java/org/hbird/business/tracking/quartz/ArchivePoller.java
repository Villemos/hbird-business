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
import java.util.UUID;

import org.apache.camel.Handler;
import org.hbird.business.api.deprecated.IDataAccess;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.dataaccess.LocationContactEventRequest;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ArchivePoller {

    private static final Logger LOG = LoggerFactory.getLogger(ArchivePoller.class);

    private final TrackingDriverConfiguration config;

    private final IDataAccess dao;

    public ArchivePoller(TrackingDriverConfiguration config, IDataAccess dao) {
        this.config = config;
        this.dao = dao;
    }

    @Handler
    public List<LocationContactEvent> poll() {
        List<String> satellites = config.getSatelliteIds();
        List<LocationContactEvent> result = new ArrayList<LocationContactEvent>(satellites.size());
        String groundStationId = config.getGroundstationId();
        //long now = System.currentTimeMillis();

        for (String satelliteId : satellites) {
            // XXX - 25.06.2013, kimmell - this is more complicated than it should be
            // waits for IDataAccess and ICatalogue refactoring / update
            // should work with single request to data access layer instead of this
            //LocationContactEventRequest request = createRequest(groundStationId, satelliteId);
            
        	try {
        		LocationContactEvent event = dao.getNextLocationContactEventFor(groundStationId, satelliteId);
            
            	LOG.trace("Found {}", event.toString());
                result.add(event);
        	} catch(Exception e) {
        		LOG.warn("Couldn't find next contact event for groundstation " + groundStationId + " and satellite " + satelliteId , e);
            }
        }
        return result;
    }

    /*LocationContactEventRequest createRequest(String groundStationId, String satelliteId) {
        LocationContactEventRequest request = new LocationContactEventRequest(UUID.randomUUID().toString());
        request.setGroundStationID(groundStationId);
        request.setSatelliteID(satelliteId);
        request.setFrom(1L);
        return request;
    }

    List<EntityInstance> getEvents(IDataAccess dao, LocationContactEventRequest request) {
        return dao.getData(request);
    }
    
    LocationContactEvent getNextEvent(List<EntityInstance> list, long now) {
        LocationContactEvent next = null;
        for (EntityInstance entity : list) {
            if (entity instanceof LocationContactEvent) {
                LocationContactEvent event = (LocationContactEvent) entity;
                // LOG.debug("  now: {} vs {}", Dates.toDefaultDateFormat(now), event);
                next = compare(next, event, now);
            }
            else {
                LOG.warn("Not a LocationContactEvent {}; there is something wrong with the dao; skipping the value ...", entity);
            }
        }
        return next;
    }

    LocationContactEvent compare(LocationContactEvent oldValue, LocationContactEvent newValue, long now) {
        long newStart = newValue.getStartTime();
        if (oldValue == null) {
            return newStart > now ? newValue : null;
        }
        long oldStart = oldValue.getStartTime();

        if (oldStart <= now && newStart <= now) {
            return null;
        }
        if (oldStart > now && newStart <= now) {
            return oldValue;
        }
        if (oldStart <= now && newStart > now) {
            return newValue;
        }
        if (oldStart > now && newStart > now) {
            return newStart < oldStart ? newValue : oldValue;
        }
        return null;
    } */
}
