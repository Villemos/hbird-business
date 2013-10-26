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
package org.hbird.business.archive.dao.mongo;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.List;
import java.util.ListIterator;

import org.hbird.business.api.IOrbitalDataAccess;
import org.hbird.business.api.exceptions.DataAccessException;
import org.hbird.business.api.exceptions.NotFoundException;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

public class MongoOrbitalDataAccess extends MongoDAOBase implements IOrbitalDataAccess {
    protected MongoOperations template = null;

    public MongoOrbitalDataAccess(MongoOperations template) {
        super(template);
    }

    @Override
    public OrbitalState getOrbitalStateFor(String satelliteID) throws DataAccessException, NotFoundException {
        List<TleOrbitalParameters> tles = getTleFor(satelliteID, Long.MIN_VALUE, Long.MAX_VALUE);

        ListIterator<TleOrbitalParameters> iter = tles.listIterator(tles.size());

        while (iter.hasPrevious()) {
            TleOrbitalParameters tle = iter.previous();
            OrbitalState state = findOne(query(where(FIELD_SATELLITE_ID).is(satelliteID)
                    .and(FIELD_DERIVED_FROM).is(tle.getInstanceID()))
                    .with(sortByTimestampDesc), OrbitalState.class);

            if (state != null) {
                return state;
            }
        }

        throw new NotFoundException("Could not find any orbital state for satellite " + satelliteID);
    }

    @Override
    public List<OrbitalState> getOrbitalStatesFor(String satelliteID, long from, long to) throws DataAccessException {
        return getLastVersionOfEachTimestamp(getByFieldInRange(FIELD_SATELLITE_ID, satelliteID, OrbitalState.class, from, to));
    }

    @Override
    public TleOrbitalParameters getTleFor(String satelliteID) throws NotFoundException, DataAccessException {
        return wrapReturn(getLastVersionByField(TleOrbitalParameters.class, FIELD_SATELLITE_ID, satelliteID));
    }

    @Override
    public List<TleOrbitalParameters> getTleFor(String satelliteID, long from, long to) throws DataAccessException {
        return getLastVersionOfEachTimestamp(getByFieldInRange(FIELD_SATELLITE_ID, satelliteID, TleOrbitalParameters.class, from, to));
    }

    @Override
    public LocationContactEvent getNextLocationContactEventForGroundStation(String groundStationID) throws NotFoundException, DataAccessException {
        Query query = query(where(FIELD_GROUND_STATION_ID).is(groundStationID).and(FIELD_END_TIME).gte(System.currentTimeMillis()))
                .with(sortByStartTimeAsc).limit(1);

        return wrapReturn(findOne(query, LocationContactEvent.class));
    }

    @Override
    public List<LocationContactEvent> getLocationContactEventsForGroundStation(String groundStationID, long from, long to)
            throws DataAccessException {
        Query query = query(where(FIELD_GROUND_STATION_ID).is(groundStationID)
                .and(FIELD_END_TIME).gte(from)
                .and(FIELD_START_TIME).lte(to))
                .with(sortByStartTimeAsc);

        return find(query, LocationContactEvent.class);
    }

    @Override
    public LocationContactEvent getNextLocationContactEventFor(String groundStationID, String satelliteID) throws NotFoundException, DataAccessException {
        long now = System.currentTimeMillis();
        Query query = query(where(FIELD_GROUND_STATION_ID).is(groundStationID)
                .and(FIELD_SATELLITE_ID).is(satelliteID)
                .and(FIELD_START_TIME).gte(now))
                .with(sortByStartTimeAsc).limit(1);

        return wrapReturn(findOne(query, LocationContactEvent.class));
    }

    @Override
    public List<LocationContactEvent> getLocationContactEventsFor(String groundStationID, String satelliteID, long from, long to) throws NotFoundException,
            DataAccessException {
        Query query = query(where(FIELD_GROUND_STATION_ID).is(groundStationID)
                .and(FIELD_SATELLITE_ID).is(satelliteID)
                .and(FIELD_END_TIME).gte(from)
                .and(FIELD_START_TIME).lte(to))
                .with(sortByStartTimeAsc);

        return find(query, LocationContactEvent.class);
    }
}
