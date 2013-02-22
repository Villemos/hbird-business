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
package org.hbird.business.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.camel.model.RouteDefinition;
import org.hbird.business.api.ICatalogue;
import org.hbird.business.api.IDataAccess;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.dataaccess.DataRequest;
import org.hbird.exchange.dataaccess.LocationContactEventRequest;
import org.hbird.exchange.dataaccess.LocationRequest;
import org.hbird.exchange.dataaccess.OrbitalStateRequest;
import org.hbird.exchange.dataaccess.ParameterRequest;
import org.hbird.exchange.dataaccess.SatelliteRequest;
import org.hbird.exchange.dataaccess.StateRequest;
import org.hbird.exchange.dataaccess.TleRequest;
import org.hbird.exchange.navigation.Location;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.PointingData;
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
		return parameter.isEmpty() ? null : parameter.get(0);
	}

	public Parameter getParameterAt(String name, long at) {
		List<Parameter> parameter = getParameterAt(name, at, 1);
		return parameter.isEmpty() ? null : parameter.get(0);
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
		request.setIsInitialization(true);
		request.setRows(1);
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

	public List<OrbitalState> retrieveOrbitalStatesFor(String satellite, long from, long to, String derivedFromName, long derivedFromTimestamp) {
		OrbitalStateRequest request = new OrbitalStateRequest(satellite, from, to);
		request.setDerivedFrom(derivedFromName, derivedFromTimestamp, "TleOrbitalParameters");
		return orbitalStateFilter.getObjects(template.requestBody(inject, request, List.class));
	}

	public OrbitalState retrieveOrbitalStateFor(String satellite, String derivedFromName, long derivedFromTimestamp) {
		OrbitalStateRequest request = new OrbitalStateRequest(satellite);
		request.setIsInitialization(true);
		request.setDerivedFrom(derivedFromName, derivedFromTimestamp, "TleOrbitalParameters");
		List<OrbitalState> states = orbitalStateFilter.getObjects(template.requestBody(inject, request, List.class));
		return states.isEmpty() == true ? null : states.get(0);
	}


	public OrbitalState retrieveOrbitalStateFor(String satellite) {
		TleOrbitalParameters parameter = retrieveTleFor(satellite);

		return parameter == null ? null : retrieveOrbitalStateFor(satellite, parameter.getName(), parameter.getTimestamp());
	}



	public List<TleOrbitalParameters> retrieveTleFor(String satellite, long from, long to) {
		TleRequest request = new TleRequest(issuedBy, satellite, from, to);
		return tleOrbitalParameterFilter.getObjects(template.requestBody(inject, request, List.class));
	}

	public TleOrbitalParameters retrieveTleFor(String satellite) {
		TleRequest request = new TleRequest(issuedBy, satellite);
		request.setIsInitialization(true);
		List<TleOrbitalParameters> parameters = tleOrbitalParameterFilter.getObjects(template.requestBody(inject, request, List.class));

		return parameters.isEmpty() ? null : parameters.get(0);
	}




	public Location retrieveLocation(String location) {
		LocationRequest request = new LocationRequest(issuedBy, location);
		List respond = template.requestBody(inject, request, List.class);
		return respond.isEmpty() ? null : (Location) respond.get(0);
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


	/* (non-Javadoc)
	 * @see org.hbird.business.api.IDataAccess#retrieveNextLocationContactEventsFor(java.lang.String)
	 */
	public List<LocationContactEvent> retrieveNextLocationContactEventsFor(String location) {
		return retrieveNextLocationContactEventsFor(location, null, (new Date()).getTime());
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IDataAccess#retrieveNextLocationContactEventsFor(java.lang.String)
	 */
	public List<LocationContactEvent> retrieveNextLocationContactEventsFor(String location, long from) {
		return retrieveNextLocationContactEventsFor(location, null, from);
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IDataAccess#retrieveNextLocationContactEventsFor(java.lang.String)
	 */
	public List<LocationContactEvent> retrieveNextLocationContactEventsFor(String location, String satellite) {
		return retrieveNextLocationContactEventsFor(location, satellite, (new Date()).getTime());
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IDataAccess#retrieveNextLocationContactEventsFor(java.lang.String)
	 */
	public List<LocationContactEvent> retrieveNextLocationContactEventsFor(String location, String satellite, long from) {

		List<LocationContactEvent> events = new ArrayList<LocationContactEvent>();

		/** Get next start event. */
		LocationContactEventRequest request = new LocationContactEventRequest(issuedBy, location, true);
		request.setRows(1);
		request.setSatellite(satellite);
		request.setFrom(from);
		List<Named> start = template.requestBody(inject, request, List.class);

		/** If we found one, find the following end event. */
		if (start.isEmpty() == false) {

			LocationContactEvent startEvent = (LocationContactEvent) start.get(0);

			request = new LocationContactEventRequest(issuedBy, location, false);
			request.setRows(1);
			request.setSatellite(startEvent.getSatellite());
			request.setLocation(startEvent.getLocation());
			request.setFrom(startEvent.getTimestamp());
			List<Named> end = template.requestBody(inject, request, List.class);

			if (end.isEmpty() == false) {
				events.add(startEvent);
				events.add((LocationContactEvent) end.get(0));
			}
		}

		return events;
	}

	public List<PointingData> retrieveNextLocationContactDataFor(String location) {
		return retrieveNextLocationContactDataFor(location, null);
	}

	public List<PointingData> retrieveNextLocationContactDataFor(String location, String satellite) {
		List<LocationContactEvent> events = retrieveNextLocationContactEventsFor(location);

		if (events.isEmpty() == false) {
			return retrieveLocationContactDataFor(location, events.get(0).getTimestamp(), events.get(1).getTimestamp());
		}

		return new ArrayList<PointingData>();
	}

	public List<PointingData> retrieveLocationContactDataFor(String location, long from, long to) {
		// TODO Auto-generated method stub
		return null;
	}
}
