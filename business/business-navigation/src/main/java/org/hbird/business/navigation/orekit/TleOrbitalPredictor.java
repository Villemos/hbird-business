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
import org.apache.log4j.Logger;
import org.hbird.business.api.ApiFactory;
import org.hbird.exchange.constants.StandardComponents;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.dataaccess.TlePropagationRequest;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.orekit.errors.OrekitException;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.tle.TLE;
import org.orekit.tle.TLEPropagator;
import org.orekit.utils.PVCoordinates;

public class TleOrbitalPredictor {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(TleOrbitalPredictor.class);

    protected KeplianOrbitPredictor keplianOrbitPredictor = null;

    public String name = StandardComponents.ORBIT_PREDICTOR;

    /**
     * Method used to receive a TLE and generate a propagation request based on the TLE.
     * 
     * @param tleParameters
     * @param context
     * @return
     * @throws OrekitException
     */
    // public List<DataSet> receiveTleParameters(@Body TleOrbitalParameters tleParameters, CamelContext context) throws
    // OrekitException {
    // TlePropagationRequest request = new TlePropagationRequest("", tleParameters.getSatellite(), tleParameters, null);
    // return predictOrbit(request, context);
    // }

    /**
     * Method to predict the orbit.
     * 
     * @throws OrekitException
     */
    @Handler
    public List<Named> predictOrbit(@Body TlePropagationRequest request) throws OrekitException {

    	LOG.info("Received TLE propagation request from '" + request.getIssuedBy() + "'.");
    	
        /** Create the TLE element */
        TleOrbitalParameters parameters = request.getTleParameters();
        if (parameters == null) {
            parameters = getTleParameters(request.getSatellite());
        }

        List<Named> results = null;

        /** If no TLE found, then we cant propagate. */
        if (parameters == null) {
            LOG.error("Request holds no TLE object and Failed to find TLE in archive for satellite '" + request.getSatellite() + "'");
        }
        else {
            TLE tle = new TLE(parameters.getTleLine1(), parameters.getTleLine2());

        	LOG.info("Propagating based on TLE timestamped '" + parameters.getTimestamp() + "'");
            
            /** Get the definition of the Locations. */
            List<GroundStation> locations = getLocations(request.getLocations());
            
            /** Get the initial orbital state at the requested start time. */
            AbsoluteDate startDate = new AbsoluteDate(new Date(request.getStartTime()), TimeScalesFactory.getUTC());
            PVCoordinates initialOrbitalState = TLEPropagator.selectExtrapolator(tle).getPVCoordinates(startDate);

            String gsNames = "all";
            for (GroundStation entry : locations) {
            	gsNames = gsNames.equals("all") ? entry.getName() : gsNames + ", " + entry.getName();
            }            
        	LOG.info("Propagating for groundstation(s) '" + gsNames + "' and satellite '" + request.getSatellite() + "'...");
            
            /** Calculate the orbital states and the contact events from the initial time forwards */
            results = keplianOrbitPredictor.predictOrbit(locations, initialOrbitalState, request.getStartTime(), request.getSatellite(), request.getStepSize(),
                    request.getDeltaPropagation(), request.getContactDataStepSize(), parameters, request.getPublish());
        }

        /** Return the data. */
    	LOG.info("Propagating done. Have injected states and events.");
        return results;
    }

    private TleOrbitalParameters getTleParameters(String satellite) {    	
    	return ApiFactory.getDataAccessApi(name).retrieveTleFor(satellite);
    }

    private List<GroundStation> getLocations(List<String> list) {
    	return ApiFactory.getCatalogueApi(name).getGroundStationsByName(list);
    }

    public void setKeplianOrbitPredictor(KeplianOrbitPredictor keplianOrbitPredictor) {
        this.keplianOrbitPredictor = keplianOrbitPredictor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
