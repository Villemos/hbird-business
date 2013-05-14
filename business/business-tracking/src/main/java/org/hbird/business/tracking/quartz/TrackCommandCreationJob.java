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
import org.hbird.exchange.groundstation.Track;
import org.hbird.exchange.interfaces.IStartableEntity;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class TrackCommandCreationJob implements Job {

    // value has to be EntityInstance.instanceId!
    public static final String JOB_DATA_KEY_CONTACT_INSTANCE_ID = "CONTACT_INSTANCE_ID";
    public static final String JOB_DATA_START_TIME = "CONTACT_START_TIME";
    public static final String JOB_DATA_EVENT_ID = "CONTACT_EVENT_ID";
    public static final String JOB_DATA_TLE_ID = "CONTACT_TLE_ID";

    private static final Logger LOG = LoggerFactory.getLogger(TrackCommandCreationJob.class);

    private IStartableEntity part;

    private IDataAccess dao;

    private ProducerTemplate producer;

    private String endPoint;

    private EntityCache<Satellite> satelliteCache;

    private EntityCache<TleOrbitalParameters> tleCache;

    public void setPart(IStartableEntity part) {
        this.part = part;
    }

    public void setDataAccess(IDataAccess dao) {
        this.dao = dao;
    }

    public void setProducerTemplate(ProducerTemplate producer) {
        this.producer = producer;
    }

    public void setEndpoint(String endPoint) {
        this.endPoint = endPoint;
    }

    /**
     * @param satelliteCache the satelliteCache to set
     */
    public void setSatelliteCache(EntityCache<Satellite> satelliteCache) {
        this.satelliteCache = satelliteCache;
    }

    /**
     * @param tleCache the tleCache to set
     */
    public void setTleCache(EntityCache<TleOrbitalParameters> tleCache) {
        this.tleCache = tleCache;
    }

    /**
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        String contactInstanceId = context.getMergedJobDataMap().getString(JOB_DATA_KEY_CONTACT_INSTANCE_ID);
        LocationContactEvent event = getEvent(dao, contactInstanceId);
        if (event != null) {
            String satId = event.getSatelliteId();
            Satellite sat = getSatellite(satelliteCache, satId);
            if (sat != null) {
                String tleId = event.getDerivedFrom();
                TleOrbitalParameters eventTle = getTle(tleCache, tleId);
                if (eventTle != null) {
                    TleOrbitalParameters latestTle = getLatestTle(dao, satId);
                    if (latestTle == null || areEqual(eventTle, latestTle)) {
                        Track command = createTrackCommand(part, event, sat);
                        LOG.info("Issuing Track command for the {}", event.prettyPrint());
                        producer.asyncSendBody(endPoint, command);
                    }
                    else {
                        LOG.info("LocationContactEvent is out dated; Track command is not issued for the {}", event.prettyPrint());
                    }
                }
                else {
                    LOG.error("Failed to resolve TLE for ID {}; Track command is not issued for the {}", tleId, event.prettyPrint());
                }
            }
            else {
                LOG.error("Failed to resolve Satellite for the ID {}; Track command is not issued for the {}", satId, event.prettyPrint());
            }
        }
        else {
            LOG.error("Failed to resolve LocationCOntatEvent for the ID {}; Track command is not issued", contactInstanceId);
        }
    }

    LocationContactEvent getEvent(IDataAccess dao, String eventInstanceId) {
        LocationContactEvent event = (LocationContactEvent) dao.resolve(eventInstanceId);
        return event;
    }

    Satellite getSatellite(EntityCache<Satellite> cache, String satelliteId) {
        return cache.getById(satelliteId);
    }

    TleOrbitalParameters getTle(EntityCache<TleOrbitalParameters> tleCache, String tleId) {
        return tleCache.getById(tleId);
    }

    TleOrbitalParameters getLatestTle(IDataAccess dao, String satelliteId) {
        return dao.getTleFor(satelliteId);
    }

    Track createTrackCommand(IStartableEntity part, LocationContactEvent event, Satellite satellite) {
        Track track = new Track(satellite.getID());
        track.setIssuedBy(part.getID());
        track.setDestination(event.getGroundStationId());
        track.setSatellite(satellite);
        track.setLocationContactEvent(event);
        return track;
    }

    boolean areEqual(TleOrbitalParameters tle1, TleOrbitalParameters tle2) {
        return tle1.getTimestamp() == tle2.getTimestamp();
    }
}
