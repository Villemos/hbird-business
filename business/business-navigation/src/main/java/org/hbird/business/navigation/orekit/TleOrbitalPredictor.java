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
package org.hbird.business.navigation.orekit;

import java.util.Date;
import java.util.List;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.hbird.business.api.ICatalogue;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IPublish;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.dataaccess.TlePropagationRequest;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.hbird.exchange.util.NamedHelper;
import org.orekit.errors.OrekitException;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.tle.TLE;
import org.orekit.tle.TLEPropagator;
import org.orekit.utils.PVCoordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TleOrbitalPredictor {

    private static Logger LOG = LoggerFactory.getLogger(TleOrbitalPredictor.class);

    protected final KeplerianOrbitPredictor predictor;

    protected final IDataAccess dao;

    protected final ICatalogue catalogue;

    protected final IPublish publisher;

    /**
     * @param dao
     * @param catalogue
     * @param publisher
     */
    public TleOrbitalPredictor(IDataAccess dao, ICatalogue catalogue, IPublish publisher, KeplerianOrbitPredictor predictor) {
        this.dao = dao;
        this.catalogue = catalogue;
        this.publisher = publisher;
        this.predictor = predictor;
    }

    /**
     * Method to predict the orbit.
     * 
     * @throws OrekitException
     */
    @Handler
    public List<EntityInstance> predictOrbit(@Body TlePropagationRequest request) throws OrekitException {
        LOG.info("Received TLE propagation request from '{}'.", request.getIssuedBy());

        String satelliteId = request.getSatelliteId();

        /* Create the TLE element */
        TleOrbitalParameters parameters = request.getTleParameters();
        if (parameters == null) {
            parameters = dao.getTleFor(satelliteId);
        }

        List<EntityInstance> results = null;

        if (parameters == null) {
            /* If no TLE found, then we can't propagate. */
            LOG.error("Request holds no TLE object and Failed to find TLE in archive for satellite '{}'", satelliteId);
        }
        else {
            TLE tle = new TLE(parameters.getTleLine1(), parameters.getTleLine2());

            LOG.info("Propagating based on TLE '{}'", parameters.getInstanceID());

            /* Get the initial orbital state at the requested start time. */
            AbsoluteDate startDate = new AbsoluteDate(new Date(request.getStartTime()), TimeScalesFactory.getUTC());
            PVCoordinates initialOrbitalState = TLEPropagator.selectExtrapolator(tle).getPVCoordinates(startDate);

            /* Get the definition of the Locations. */
            List<GroundStation> locations = catalogue.getGroundStationsByName(request.getLocations());

            LOG.info("Propagating for groundstation(s) '{}' and satellite '{}' ...", NamedHelper.toString(locations), satelliteId);

            /** Calculate the orbital states and the contact events from the initial time forwards */

            boolean publish = request.getPublish();

            results = predictor.predictOrbit(request, locations, initialOrbitalState, startDate, parameters, publish ? publisher : null);
        }

        /** Return the data. */
        LOG.info("Propagating done. Have injected states and events.");
        return results;
    }
}
