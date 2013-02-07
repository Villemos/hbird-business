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
package org.hbird.business.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.camel.model.RouteDefinition;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.dataaccess.DataRequest;
import org.hbird.exchange.dataaccess.LocationRequest;
import org.hbird.exchange.dataaccess.OrbitalStateRequest;
import org.hbird.exchange.dataaccess.ParameterRequest;
import org.hbird.exchange.dataaccess.SatelliteRequest;
import org.hbird.exchange.dataaccess.StateRequest;
import org.hbird.exchange.navigation.Location;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;

public class DataAccess extends HbirdApi implements IDataAccess, ICatalogue {

	protected TypeFilter<Parameter> parameterFilter = new TypeFilter<Parameter>(Parameter.class);
	protected TypeFilter<State> stateFilter = new TypeFilter<State>(State.class);
	protected TypeFilter<OrbitalState> orbitalStateFilter = new TypeFilter<OrbitalState>(OrbitalState.class);
	protected TypeFilter<TleOrbitalParameters> tleOrbitalParameterFilter = new TypeFilter<TleOrbitalParameters>(TleOrbitalParameters.class);
	protected TypeFilter<Location> locationFilter = new TypeFilter<Location>(Location.class);
	protected TypeFilter<Satellite> satelliteFilter = new TypeFilter<Satellite>(Satellite.class);

	public DataAccess(String issuedBy) {
		super(issuedBy);
	}


	public List<Named> retrieveData(DataRequest request) {
		return template.requestBody(inject, request, List.class);
	}


	
	/** INITIALIZATION */
	public Parameter getParameter(String name) {
		List<Parameter> parameter = getParameter(name, 1);
		return parameter.size() > 0 ? parameter.get(0) : null;
	}

	public Parameter getParameterAt(String name, long at) {
		List<Parameter> parameter = getParameterAt(name, at, 1);
		return parameter.size() > 0 ? parameter.get(0) : null;
	}

	public List<Parameter> getParameter(String name, int rows) {
		return getParameterAt(name, (new Date()).getTime(), rows);
	}

	public List<Parameter> getParameterAt(String name, long at, int rows) {
		ParameterRequest request = new ParameterRequest(name, rows);
		request.setIsInitialization(true);
		request.setTo(at);
		return getParameterWithoutState(request);
	}
	

	

	public List<Parameter> getParameters(List<String> names) {
		return getParametersAt(names, (new Date()).getTime(), 1);
	}

	public List<Parameter> getParameters(List<String> names, int rows) {
		return getParametersAt(names, (new Date()).getTime(), rows);
	}
	
	public List<Parameter> getParametersAt(List<String> names, long at) {
		return getParametersAt(names, at, 1);
	}

	public List<Parameter> getParametersAt(List<String> name, long at, int rows) {
		ParameterRequest request = new ParameterRequest(name, rows);
		request.setIsInitialization(true);
		request.setTo(at);
		return getParameterWithoutState(request);

	}
	
	

	public Map<Parameter, List<State>> getParameterAndStates(String name) {
		return getParameterAndStatesAt(name, (new Date().getTime()), 1);
	}

	public Map<Parameter, List<State>> getParameterAndStates(String name, int rows) {
		return getParameterAndStatesAt(name, (new Date().getTime()), rows);
	}

	public Map<Parameter, List<State>> getParameterAndStatesAt(String name, long at) {
		return getParameterAndStatesAt(name, at, 1);
	}

	public Map<Parameter, List<State>> getParameterAndStatesAt(String name, long at, int rows) {
		ParameterRequest request = new ParameterRequest(name, rows);
		request.setIsInitialization(true);
		request.setTo(at);
		return getParameterWithState(request);
	}

	
	
	
	
	
	
	public Map<Parameter, List<State>> getParametersAndStates(List<String> names) {
		return getParametersAndStateAt(names, (new Date()).getTime(), 1);
	}

	public Map<Parameter, List<State>> getParametersAndStates(List<String> names, int rows) {
		return getParametersAndStateAt(names, (new Date()).getTime(), rows);
	}

	public Map<Parameter, List<State>> getParametersAndStateAt(List<String> names, long at) {
		return getParametersAndStateAt(names, at, 1);
	}

	public Map<Parameter, List<State>> getParametersAndStateAt(List<String> names, long at, int rows) {
		ParameterRequest request = new ParameterRequest(names, 1);
		request.setIsInitialization(true);
		request.setTo(at);
		return getParameterWithState(request);
	}

	
	
	
	/** STEPPING */
	
	public List<Parameter> retrieveParameter(String name, long from, long to) {
		ParameterRequest request =  new ParameterRequest(name, from, to);
		return getParameterWithoutState(request);
	}

	public List<Parameter> retrieveParameter(String name, long from, long to, int rows) {
		ParameterRequest request = new ParameterRequest(name, from, to, rows);
		return getParameterWithoutState(request);
	}

	public List<Parameter> retrieveParameters(List<String> names, long from, long to) {
		ParameterRequest request = new ParameterRequest(names, from, to);
		return getParameterWithoutState(request);
	}

	public List<Parameter> retrieveParameters(List<String> names, long from, long to, int rows) {
		ParameterRequest request = new ParameterRequest(names, from, to, rows);
		return getParameterWithoutState(request);
	}

	public Map<Parameter, List<State>> retrieveParameterAndStates(String name, long from, long to) {
		ParameterRequest request = new ParameterRequest(name, from, to);
		return getParameterWithState(request);
	}

	public Map<Parameter, List<State>> retrieveParameterAndStates(String name, long from, long to, int rows) {
		ParameterRequest request = new ParameterRequest(name, from, to, rows);
		return getParameterWithState(request);
	}


	public Map<Parameter, List<State>> retrieveParametersAndStates(List<String> names, long from, long to) {
		ParameterRequest request = new ParameterRequest(names, from, to);
		return getParameterWithState(request);
	}

	public Map<Parameter, List<State>> retrieveParametersAndStates(List<String> names, long from, long to, int rows) {
		ParameterRequest request = new ParameterRequest(names, from, to, rows);
		return getParameterWithState(request);
	}

	public List<State> retrieveState(String isStateOf) {
		StateRequest request = new StateRequest(issuedBy, isStateOf);
		return stateFilter.getObjects(template.requestBody(inject, request, List.class));
	}

	public List<State> retrieveState(String isStateOf, long from, long to) {
		StateRequest request = new StateRequest(issuedBy, isStateOf, from, to);
		return stateFilter.getObjects(template.requestBody(inject, request, List.class));
	}





	public List<OrbitalState> retrieveOrbitalStatesFor(String satellite, long from, long to) {
		OrbitalStateRequest request = new OrbitalStateRequest(satellite, from, to);
		return orbitalStateFilter.getObjects(template.requestBody(inject, request, List.class));
	}

	public List<TleOrbitalParameters> retrieveTleFor(String satellite, long from, long to) {
		OrbitalStateRequest request = new OrbitalStateRequest(satellite, from, to);
		return tleOrbitalParameterFilter.getObjects(template.requestBody(inject, request, List.class));
	}

	
	
	
	public Location retrieveLocation(String location) {
		LocationRequest request = new LocationRequest(issuedBy, location);
		List respond = template.requestBody(inject, request, List.class);
		if (respond.isEmpty()) {
			return null;
		}		
		
		return (Location) respond.get(0);
	}
	
	
	

	
	
	
	public List<Location> getLocationMetadata() {
		LocationRequest request = new LocationRequest(issuedBy);
		request.setIsInitialization(true);
		request.setType("Location");
		request.setRows(1);
		return locationFilter.getObjects(template.requestBody(inject, request, List.class));
	}
	
	public List<Satellite> getSatelliteMetadata() {
		SatelliteRequest request = new SatelliteRequest(issuedBy);
		request.setIsInitialization(true);
		request.setType("Satellite");
		request.setRows(1);
		return satelliteFilter.getObjects(template.requestBody(inject, request, List.class));
	}

	public List<Parameter> getParameterMetadata() {
		ParameterRequest request = new ParameterRequest(new ArrayList<String>(), 1000);
		request.setIsInitialization(true);
		request.setType("Parameter");
		request.setRows(1);
		return getParameterWithoutState(request);
	}

	public List<State> getStateMetadata() {
		ParameterRequest request = new ParameterRequest(new ArrayList<String>(), 1000);
		request.setIsInitialization(true);
		request.setType("State");
		request.setRows(1);
		return stateFilter.getObjects(template.requestBody(inject, request, List.class));
	}

	public List<Command> getCommandMetadata() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void configure() throws Exception {
		/** This configures a direct connection to SOLR. */
		RouteDefinition route = from(inject).to("solr:api");
		
		template = getContext().createProducerTemplate();
	}
	
	protected Map<Parameter, List<State>> getParameterWithState(ParameterRequest request) {
		request.setIncludeStates(true);
		List<Named> respond = template.requestBody(inject, request, List.class);
		return ApiUtility.sortParametersAndState(respond);
	}

	protected List<Parameter> getParameterWithoutState(ParameterRequest request) {
		List<Named> respond = template.requestBody(inject, request, List.class);
		return parameterFilter.getObjects(respond);
	}
		

}