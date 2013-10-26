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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.hbird.business.api.exceptions.DataAccessException;
import org.hbird.business.api.exceptions.NotFoundException;
import org.hbird.exchange.interfaces.IEntityInstance;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class MongoDAOBase {
    public static final String DEFAULT_DATABASE_NAME = "hbird";

    protected static final String FIELD_ID = "ID";
    protected static final String FIELD_VERSION = "version";
    protected static final String FIELD_TIMESTAMP = "timestamp";
    protected static final String FIELD_NAME = "name";
    protected static final String FIELD_APPLICABLE_TO = "applicableTo";
    protected static final String FIELD_SATELLITE_ID = "satelliteId";
    protected static final String FIELD_GROUND_STATION_ID = "groundStationId";
    protected static final String FIELD_START_TIME = "startTime";
    protected static final String FIELD_END_TIME = "endTime";
    protected static final String FIELD_DERIVED_FROM = "derivedFromId";
    protected static final String FIELD_ISSUED_BY = "issuedBy";

    public final Sort sortByVersionDesc = new Sort(new Sort.Order(Sort.Direction.DESC, FIELD_VERSION));
    public final Sort sortByTimestampAsc = new Sort(new Sort.Order(Sort.Direction.ASC, FIELD_TIMESTAMP));
    public final Sort sortByTimestampDesc = new Sort(new Sort.Order(Sort.Direction.DESC, FIELD_TIMESTAMP));
    public final Sort sortByStartTimeAsc = new Sort(new Sort.Order(Sort.Direction.ASC, FIELD_START_TIME));
    public final Sort sortByStartTimeDesc = new Sort(new Sort.Order(Sort.Direction.DESC, FIELD_START_TIME));
    public final Sort sortByIDAndVersion = new Sort(Sort.Direction.DESC, FIELD_ID, FIELD_VERSION);

    protected MongoOperations template = null;

    public MongoDAOBase(MongoOperations template) {
        this.template = template;
    }

    protected <T> T wrapReturn(T value) throws NotFoundException {
        if (value == null) {
            throw new NotFoundException();
        }

        return value;
    }

    protected <T> T findOne(Query query, Class<T> clazz) throws DataAccessException {
        try {
            return template.findOne(query, clazz);
        }
        catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    protected <T> T findById(String id, Class<T> clazz) throws DataAccessException {
        try {
            return template.findById(id, clazz);
        }
        catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    protected Query queryByFieldInRange(String field, String value, long from, long to) {
        return new Query(where(field).is(value).andOperator(
                where(FIELD_TIMESTAMP).gte(from),
                where(FIELD_TIMESTAMP).lte(to)))
                .with(sortByTimestampAsc);
    }

    protected <T> List<T> find(Query query, Class<T> clazz) throws DataAccessException {
        try {
            return template.find(query, clazz);
        }
        catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    protected <T extends IEntityInstance> List<T> getByFieldInRange(String field, String value, Class<T> clazz, long from, long to) throws DataAccessException {
        Query query = queryByFieldInRange(field, value, from, to);

        return find(query, clazz);
    }

    protected <T extends IEntityInstance> T getLastVersionByField(Class<T> clazz, String field, String value) throws DataAccessException {
        Query query = new Query(Criteria.where(field).is(value)).with(sortByVersionDesc).limit(1);
        return findOne(query, clazz);
    }

    protected <T extends IEntityInstance> List<T> getLastVersionOfEachTimestamp(List<T> l) {
        ListIterator<T> iter = l.listIterator();

        T lastEntry = null;

        while (iter.hasNext()) {
            T element = iter.next();
            iter.remove();

            if (lastEntry == null) {
                lastEntry = element;
            }
            else {
                if (lastEntry.getID().equals(element.getID()) && lastEntry.getTimestamp() == element.getTimestamp()) {
                    if (lastEntry.getVersion() < element.getVersion()) {
                        lastEntry = element;
                    }
                }
                else {
                    iter.add(lastEntry);
                    lastEntry = element;
                }
            }
        }

        if (lastEntry != null) {
            l.add(lastEntry);
        }

        return l;
    }

    protected <T extends IEntityInstance> List<T> getLastVersions(Query query, Class<T> clazz) throws DataAccessException {
        List<T> instances = find(query.with(sortByIDAndVersion), clazz);
        List<T> lastVersions = new ArrayList<T>();

        String currentID = "";
        for (T instance : instances) {
            if (!instance.getID().equals(currentID)) {
                lastVersions.add(instance);
                currentID = instance.getID();
            }
        }

        return lastVersions;
    }

}
