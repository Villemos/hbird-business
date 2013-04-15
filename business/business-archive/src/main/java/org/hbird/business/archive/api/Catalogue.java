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
package org.hbird.business.archive.api;

import java.util.ArrayList;
import java.util.List;

import org.hbird.business.api.HbirdApi;
import org.hbird.business.api.ICatalogue;
import org.hbird.exchange.constants.StandardComponents;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.Part;
import org.hbird.exchange.core.State;
import org.hbird.exchange.dataaccess.DataRequest;
import org.hbird.exchange.dataaccess.GroundStationRequest;
import org.hbird.exchange.dataaccess.ParameterRequest;
import org.hbird.exchange.dataaccess.PartRequest;
import org.hbird.exchange.dataaccess.SatelliteRequest;
import org.hbird.exchange.dataaccess.StateRequest;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.navigation.Satellite;

/**
 * @author Gert Villemos
 *
 */
public class Catalogue extends HbirdApi implements ICatalogue {

    public Catalogue(String issuedBy) {
        super(issuedBy);
    }

    @Override
    public List<GroundStation> getGroundStations() {
        GroundStationRequest request = createGroundStationRequest(issuedBy, null);
        return executeRequest(request);
    }

    /**
     * @see org.hbird.business.api.ICatalogue#getGroundStationByName(java.lang.String)
     */
    @Override
    public GroundStation getGroundStationByName(String name) {
    	List<String> names = new ArrayList<String>();
		names.add(name);
    	GroundStationRequest request = createGroundStationRequest(issuedBy, names);
        List<GroundStation> stations = executeRequest(request);
        return getFirst(stations);
    }

    /**
     * @see org.hbird.business.api.ICatalogue#getGroundStationByName(java.lang.String)
     */
    @Override
    public List<GroundStation> getGroundStationsByName(List<String> names) {
        GroundStationRequest request = createGroundStationRequest(issuedBy, names);
        return executeRequest(request);
    }

    @Override
    public List<Satellite> getSatellites() {
        SatelliteRequest request = createSatelliteRequest(issuedBy);
        return executeRequest(request);
    }

    /**
     * @see org.hbird.business.api.ICatalogue#getSatelliteByName(java.lang.String)
     */
    @Override
    public Satellite getSatelliteByName(String name) {
        SatelliteRequest request = createSatelliteRequest(issuedBy);
        request.addName(name);
        List<Satellite> satellites = executeRequest(request);
        return getFirst(satellites);
    }

    @Override
    public List<Parameter> getParameters() {
        ParameterRequest request = createParameterRequest(); 
        return executeRequest(request);
    }

    @Override
    public List<State> getStates() {
        StateRequest request = createStateRequest(); 
        return executeRequest(request);
    }

    /**
     * @param n 
     * @return
     */
    protected GroundStationRequest createGroundStationRequest(String issuedBy, List<String> names) {
        GroundStationRequest request = new GroundStationRequest(issuedBy, names);
        request.setIsInitialization(true);
        request.setClass(GroundStation.class.getSimpleName());
        request.setRows(1);
        return request;
    }

    /**
     * @return
     */
    protected SatelliteRequest createSatelliteRequest(String issuedBy) {
        SatelliteRequest request = new SatelliteRequest(issuedBy);
        request.setIsInitialization(true);
        request.setClass(Satellite.class.getSimpleName());
        request.setRows(1);
        return request;
    }

    /**
     * @return
     */
    protected ParameterRequest createParameterRequest() {
        ParameterRequest request = new ParameterRequest(issuedBy);
        request.setIsInitialization(true);
        request.setRows(1);
        return request;
    }

    /**
     * @return
     */
    protected StateRequest createStateRequest() {
        StateRequest request = new StateRequest(issuedBy);
        request.setIsInitialization(true);
        request.setRows(1);
        return request;
    }

	/* (non-Javadoc)
	 * @see org.hbird.business.api.ICatalogue#getParts()
	 */
	@Override
	public List<Part> getParts() {
		PartRequest request = new PartRequest(issuedBy);		
		return executeRequest(request);
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.ICatalogue#getPart(java.lang.String)
	 */
	@Override
	public Part getPart(String name) {
		PartRequest request = new PartRequest(issuedBy);
		request.addName(name);
		List<Object> results = executeRequest(request);
		return results.isEmpty() ? null : (Part) results.get(0);
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.ICatalogue#getPartChildred(java.lang.String)
	 */
	@Override
	public List<Part> getPartChildren(String parentName) {
		PartRequest request = new PartRequest(issuedBy);
		request.setIsPartOf(parentName);
		return executeRequest(request);
	}
}
