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

    private IStartableEntity issuer;

    private IDataAccess dao;

    private ProducerTemplate producer;

    private String endPoint;

    private EntityCache<Satellite> satelliteCache;

    private EntityCache<TleOrbitalParameters> tleCache;

    private IdBuilder idBuilder;

    public void setIssuer(IStartableEntity issuer) {
        this.issuer = issuer;
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
     * @param idBuilder the idBuilder to set
     */
    public void setIdBuilder(IdBuilder idBuilder) {
        this.idBuilder = idBuilder;
    }

    /**
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String contactInstanceId = context.getMergedJobDataMap().getString(JOB_DATA_KEY_CONTACT_INSTANCE_ID);
        LocationContactEvent event = getEvent(dao, contactInstanceId);
        if (event != null) {
            String satId = event.getSatelliteID();
            Satellite sat = getSatellite(satelliteCache, satId);
            if (sat != null) {
                String tleId = event.getDerivedFromId();
                TleOrbitalParameters eventTle = getTle(tleCache, tleId);
                if (eventTle != null) {
                    TleOrbitalParameters latestTle = getLatestTle(dao, satId);
                    if (latestTle == null || areEqual(eventTle, latestTle)) {
                        Track command = createTrackCommand(idBuilder, issuer, event, sat);
                        LOG.info("Issuing Track command for the '{}'", event.toString());
                        producer.asyncSendBody(endPoint, command);
                    }
                    else {
                        LOG.info("LocationContactEvent is out dated; Track command is not issued for the '{}'", event.toString());
                    }
                }
                else {
                    LOG.error("Failed to resolve TLE for ID '{}'; Track command is not issued for the '{}'", tleId, event.toString());
                }
            }
            else {
                LOG.error("Failed to resolve Satellite for the ID '{}'; Track command is not issued for the '{}'", satId, event.toString());
            }
        }
        else {
            LOG.error("Failed to resolve LocationCOntatEvent for the ID '{}'; Track command is not issued", contactInstanceId);
        }
    }

    // TODO: Maybe refactor the code to handle exceptions directly, but then there
    // is a mismatch between dao and cache contracts
    LocationContactEvent getEvent(IDataAccess dao, String eventInstanceId) {
        try {
            return dao.getByInstanceId(eventInstanceId, LocationContactEvent.class);
        }
        catch (Exception e) {
            LOG.info("Failed to resolve LocationContactEvent for the ID '{}' ", eventInstanceId, e);
            return null;
        }
    }

    Satellite getSatellite(EntityCache<Satellite> cache, String satelliteId) {
        try {
            return cache.getById(satelliteId);
        }
        catch (Exception e) {
            LOG.error("Failed to resolve Satellite for the ID '{}'", satelliteId, e);
            return null;
        }
    }

    TleOrbitalParameters getTle(EntityCache<TleOrbitalParameters> tleCache, String tleId) {
        try {
            return tleCache.getById(tleId);
        }
        catch (Exception e) {
            LOG.error("Failed to resolve TleOrbitalParameters for the ID '{}'", tleId, e);
            return null;
        }
    }

    TleOrbitalParameters getLatestTle(IDataAccess dao, String satelliteId) {
        try {
            return dao.getTleFor(satelliteId);
        }
        catch (Exception e) {
            LOG.info("Failed to retrieve latest TLE for the Satellite ID '{}'", satelliteId, e);
            return null;
        }
    }

    Track createTrackCommand(IdBuilder idBuilder, IStartableEntity part, LocationContactEvent event, Satellite satellite) {
        String id = idBuilder.buildID(satellite.getID(), Track.class.getSimpleName());
        Track track = new Track(id);
        track.setIssuedBy(part.getID());
        track.setDestination(event.getGroundStationID());
        track.setSatellite(satellite);
        track.setLocationContactEvent(event);
        return track;
    }

    boolean areEqual(TleOrbitalParameters tle1, TleOrbitalParameters tle2) {
        return tle1.getVersion() == tle2.getVersion();
    }
}
