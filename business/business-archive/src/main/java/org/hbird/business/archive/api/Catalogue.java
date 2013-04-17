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
 * API for receiving catalogue data, i.e. list of available data.
 * 
 * @author Gert Villemos
 *
 */
public class Catalogue extends HbirdApi implements ICatalogue {

	/**
	 * Constructor. 
	 * 
	 * @param issuedBy The name/ID of the component that is using the API to send requests.
	 */
    public Catalogue(String issuedBy) {
        super(issuedBy);
    }

    /**
     * @see org.hbird.business.api.ICatalogue#getGroundStations(g)
     */
    @Override
    public List<GroundStation> getGroundStations() {
        GroundStationRequest request = createGroundStationRequest(null);
        return executeRequest(request);
    }

    /**
     * @see org.hbird.business.api.ICatalogue#getGroundStationByName(java.lang.String)
     */
    @Override
    public GroundStation getGroundStationByName(String name) {
    	List<String> names = new ArrayList<String>();
		names.add(name);
    	GroundStationRequest request = createGroundStationRequest(names);
        List<GroundStation> stations = executeRequest(request);
        return getFirst(stations);
    }

    /**
     * @see org.hbird.business.api.ICatalogue#getGroundStationByName(java.lang.String)
     */
    @Override
    public List<GroundStation> getGroundStationsByName(List<String> names) {
        GroundStationRequest request = createGroundStationRequest(names);
        return executeRequest(request);
    }

    /**
     * @see org.hbird.business.api.ICatalogue#getSatellites()
     */
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

    /**
     * @see org.hbird.business.api.ICatalogue#getParameters()
     */
    @Override
    public List<Parameter> getParameters() {
        ParameterRequest request = createParameterRequest(); 
        return executeRequest(request);
    }

    /**
     * @see org.hbird.business.api.ICatalogue#getStates()
     */
    @Override
    public List<State> getStates() {
        StateRequest request = createStateRequest(); 
        return executeRequest(request);
    }

    protected void configureInit(DataRequest request, Class<?> clazz) {
        request.setIsInitialization(true);
        request.setClass(clazz.getSimpleName());
        request.setRows(1);    	
    }
    
    /**
     * Helper method to issued a ground station request to the archive 
     * 
     * @param names List of names of GroundStations
     * @return The GroundStation request to be issued
     */
    protected GroundStationRequest createGroundStationRequest(List<String> names) {
        GroundStationRequest request = new GroundStationRequest(issuedBy, names);
        configureInit(request, GroundStation.class);
        return request;
    }

    protected SatelliteRequest createSatelliteRequest(String issuedBy) {
        SatelliteRequest request = new SatelliteRequest(issuedBy);
        configureInit(request, Satellite.class);
        return request;
    }

    /**
     * @return
     */
    protected ParameterRequest createParameterRequest() {
        ParameterRequest request = new ParameterRequest(issuedBy);
        configureInit(request, Parameter.class);
        return request;
    }

    /**
     * @return
     */
    protected StateRequest createStateRequest() {
        StateRequest request = new StateRequest(issuedBy);
        configureInit(request, State.class);
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
