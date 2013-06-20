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

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Handler;
import org.hbird.business.api.ICatalogue;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.navigation.configuration.ContactPredictionConfiguration;
import org.hbird.business.navigation.request.ContactPredictionRequest;
import org.hbird.exchange.groundstation.GroundStation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class GroundStationResolver {

    private static final Logger LOG = LoggerFactory.getLogger(GroundStationResolver.class);

    private final IDataAccess dao;
    private final ICatalogue catalogue;

    public GroundStationResolver(IDataAccess dao, ICatalogue catalogue) {
        this.dao = dao;
        this.catalogue = catalogue;
    }

    @Handler
    public <T> ContactPredictionRequest<T> resolve(ContactPredictionRequest<T> request) {
        ContactPredictionConfiguration config = request.getConfiguration();
        List<String> ids = config.getGroundStationsIds();
        List<GroundStation> gsList;
        if (ids == null || ids.isEmpty()) {
            LOG.debug("No GroundStations configured; using all availables");
            gsList = getAllGroundStations(catalogue);
        }
        else {
            LOG.debug("Resolving {} GroundStations", ids.size());
            gsList = resolveGroundStations(dao, ids);
        }
        LOG.debug("Resolved {} GroundStations", gsList.size());
        request.setGroundStations(gsList);
        return request;
    }

    List<GroundStation> getAllGroundStations(ICatalogue catalogue) {
        return catalogue.getGroundStations();
    }

    List<GroundStation> resolveGroundStations(IDataAccess dao, List<String> ids) {
        List<GroundStation> result = new ArrayList<GroundStation>(ids.size());
        for (String id : ids) {
            try {
                GroundStation gs = (GroundStation) dao.resolve(id);
                if (gs == null) {
                    LOG.error("No GroundStation found for the ID '{}'", id);
                }
                else {
                    LOG.debug("Resolved GroundStation for the ID '{}'", id);
                    result.add(gs);
                }
            }
            catch (Exception e) {
                LOG.error("Failed to resolve GroundStation for the ID '{}'", id, e);
            }
        }
        return result;
    }
}
