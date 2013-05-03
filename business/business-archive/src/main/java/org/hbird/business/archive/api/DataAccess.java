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
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.hbird.business.api.ApiUtility;
import org.hbird.business.api.HbirdApi;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.archive.ArchiveComponent;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.core.Event;
import org.hbird.exchange.core.Metadata;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.dataaccess.DataRequest;
import org.hbird.exchange.dataaccess.EventRequest;
import org.hbird.exchange.dataaccess.GroundStationRequest;
import org.hbird.exchange.dataaccess.LocationContactEventRequest;
import org.hbird.exchange.dataaccess.MetadataRequest;
import org.hbird.exchange.dataaccess.OrbitalStateRequest;
import org.hbird.exchange.dataaccess.ParameterRequest;
import org.hbird.exchange.dataaccess.StateRequest;
import org.hbird.exchange.dataaccess.TleRequest;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.PointingData;
import org.hbird.exchange.navigation.TleOrbitalParameters;

/**
 * API for accessing (retrieving) data from the system.
 * 
 * @author Gert Villemos
 * 
 */
public class DataAccess extends HbirdApi implements IDataAccess {

	/**
	 * Constructor.
	 * 
	 * @param issuedBy The name/ID of the component that is using the API to send requests.
	 */
	public DataAccess(String issuedBy) {
		super(issuedBy, ArchiveComponent.ARCHIVE_NAME);
	}

	public DataAccess(String issuedBy, CamelContext context) {
		super(issuedBy, ArchiveComponent.ARCHIVE_NAME, context);
	}

	@Override
	public void configure() throws Exception {
		/** This configures a direct connection to SOLR. */
		if (containsRoute("seda://injectToSolr") == false) {
			from("seda://injectToSolr").to("solr:api");
		}

		template = getContext().createProducerTemplate();
	}

	@Override
	public List<EntityInstance> getData(DataRequest request) {
		return executeRequestRespond(request);
	}

	@Override
	public Parameter getParameter(String name) {
		return getFirst(getParameterAt(null, name, (new Date()).getTime(), 1));
	}

	@Override
	public Parameter getParameter(String source, String name) {
		return getFirst(getParameterAt(source, name, (new Date()).getTime(), 1));
	}

	@Override
	public Parameter getParameterAt(String name, long at) {
		return getFirst(getParameterAt(null, name, at, 1));
	}

	@Override
	public Parameter getParameterAt(String source, String name, long at) {
		return getFirst(getParameterAt(source, name, at, 1));
	}

	@Override
	public List<Parameter> getParameter(String name, int rows) {
		return getParameterAt(null, name, (new Date()).getTime(), rows);
	}

	@Override
	public List<Parameter> getParameter(String source, String name, int rows) {
		return getParameterAt(source, name, (new Date()).getTime(), rows);
	}

	@Override
	public List<Parameter> getParameterAt(String name, long at, int rows) {
		return getParameterAt(null, name, at, rows);
	}

	@Override
	public List<Parameter> getParameterAt(String source, String name, long at, int rows) {
		ParameterRequest request = new ParameterRequest(getID());
		request.setIssuedBy(issuedBy);
		request.setRows(rows);
		request.addName(name);
		request.setIsInitialization(true);
		request.setTo(at);
		return getParameterWithoutState(request);
	}

	@Override
	public List<Parameter> getParameters(List<String> names) {
		return getParametersAt(names, (new Date()).getTime(), 1);
	}

	@Override
	public List<Parameter> getParameters(List<String> names, int rows) {
		return getParametersAt(names, (new Date()).getTime(), rows);
	}

	@Override
	public List<Parameter> getParametersAt(List<String> names, long at) {
		return getParametersAt(names, at, 1);
	}

	@Override
	public List<Parameter> getParametersAt(List<String> name, long at, int rows) {
		ParameterRequest request = new ParameterRequest(getID());
		request.setIssuedBy(issuedBy);
		request.setRows(rows);
		request.addNames(name);
		request.setIsInitialization(true);
		request.setTo(at);
		return getParameterWithoutState(request);

	}

	@Override
	public Map<Parameter, List<State>> getParameterAndStates(String name) {
		return getParameterAndStatesAt(name, (new Date().getTime()), 1);
	}

	@Override
	public Map<Parameter, List<State>> getParameterAndStates(String name, int rows) {
		return getParameterAndStatesAt(name, (new Date().getTime()), rows);
	}

	@Override
	public Map<Parameter, List<State>> getParameterAndStatesAt(String name, long at) {
		return getParameterAndStatesAt(name, at, 1);
	}

	@Override
	public Map<Parameter, List<State>> getParameterAndStatesAt(String name, long at, int rows) {
		ParameterRequest request = new ParameterRequest(getID());
		request.setIssuedBy(issuedBy);
		request.setRows(rows);
		request.addName(name);
		request.setIsInitialization(true);
		request.setTo(at);
		return getParameterWithState(request);
	}

	@Override
	public Map<Parameter, List<State>> getParametersAndStates(List<String> names) {
		return getParametersAndStateAt(names, (new Date()).getTime(), 1);
	}

	@Override
	public Map<Parameter, List<State>> getParametersAndStates(List<String> names, int rows) {
		return getParametersAndStateAt(names, (new Date()).getTime(), rows);
	}

	@Override
	public Map<Parameter, List<State>> getParametersAndStateAt(List<String> names, long at) {
		return getParametersAndStateAt(names, at, 1);
	}

	@Override
	public Map<Parameter, List<State>> getParametersAndStateAt(List<String> names, long at, int rows) {
		ParameterRequest request = new ParameterRequest(getID());
		request.setIssuedBy(issuedBy);
		request.setRows(rows);
		request.addNames(names);
		request.setIsInitialization(true);
		request.setTo(at);
		return getParameterWithState(request);
	}

	/** STEPPING */

	@Override
	public List<Parameter> getParameter(String name, long from, long to) {
		ParameterRequest request = new ParameterRequest(getID());
		request.setIssuedBy(issuedBy);
		request.addName(name);
		request.setFrom(from);
		request.setTo(to);
		return getParameterWithoutState(request);
	}

	@Override
	public List<Parameter> getParameter(String name, long from, long to, int rows) {
		ParameterRequest request = new ParameterRequest(getID());
		request.setIssuedBy(issuedBy);
		request.addName(name);
		request.setFrom(from);
		request.setTo(to);
		request.setRows(rows);
		return getParameterWithoutState(request);
	}

	@Override
	public List<Parameter> getParameters(List<String> names, long from, long to) {
		ParameterRequest request = new ParameterRequest(getID());
		request.setIssuedBy(issuedBy);
		request.addNames(names);
		request.setFrom(from);
		request.setTo(to);
		return getParameterWithoutState(request);
	}

	@Override
	public List<Parameter> getParameters(List<String> names, long from, long to, int rows) {
		ParameterRequest request = new ParameterRequest(getID());
		request.setIssuedBy(issuedBy);
		request.addNames(names);
		request.setFrom(from);
		request.setTo(to);
		request.setRows(rows);
		return getParameterWithoutState(request);
	}

	@Override
	public Map<Parameter, List<State>> getParameterAndStates(String name, long from, long to) {
		ParameterRequest request = new ParameterRequest(getID());
		request.setIssuedBy(issuedBy);
		request.addName(name);
		request.setFrom(from);
		request.setTo(to);
		return getParameterWithState(request);
	}

	@Override
	public Map<Parameter, List<State>> getParameterAndStates(String name, long from, long to, int rows) {
		ParameterRequest request = new ParameterRequest(getID());
		request.setIssuedBy(issuedBy);
		request.addName(name);
		request.setFrom(from);
		request.setTo(to);
		request.setRows(rows);
		return getParameterWithState(request);
	}

	@Override
	public Map<Parameter, List<State>> getParametersAndStates(List<String> names, long from, long to) {
		ParameterRequest request = new ParameterRequest(getID());
		request.setIssuedBy(issuedBy);
		request.addNames(names);
		request.setFrom(from);
		request.setTo(to);
		return getParameterWithState(request);
	}

	@Override
	public Map<Parameter, List<State>> getParametersAndStates(List<String> names, long from, long to, int rows) {
		ParameterRequest request = new ParameterRequest(getID());
		request.setIssuedBy(issuedBy);
		request.addNames(names);
		request.setFrom(from);
		request.setTo(to);
		request.setRows(rows);
		return getParameterWithState(request);
	}

	@Override
	public List<State> getState(String isStateOf) {
		StateRequest request = new StateRequest(getID());
		request.setIssuedBy(issuedBy);
		request.setIsStateOf(isStateOf);
		request.setIsInitialization(true);
		request.setRows(1);
		return executeRequestRespond(request);
	}

	@Override
	public List<State> getState(String isStateOf, long from, long to) {
		StateRequest request = new StateRequest(getID());
		request.setIssuedBy(issuedBy);
		request.setIsStateOf(isStateOf);
		request.setFrom(from);
		request.setTo(to);
		return executeRequestRespond(request);
	}

	@Override
	public List<State> getStates(List<String> states) {
		StateRequest request = new StateRequest(getID());
		request.setIssuedBy(issuedBy);
		request.addNames(states);
		return executeRequestRespond(request);
	}

	@Override
	public List<OrbitalState> getOrbitalStatesFor(String satellite, long from, long to) {
		OrbitalStateRequest request = new OrbitalStateRequest(getID());
		request.setIssuedBy(issuedBy);
		request.setSatelliteName(satellite);
		request.setFrom(from);
		request.setTo(to);    	
		return executeRequestRespond(request);
	}

	@Override
	public OrbitalState getOrbitalStateFor(String satellite) {
		OrbitalStateRequest request = new OrbitalStateRequest(getID());
		request.setIssuedBy(issuedBy);
		request.setSatelliteName(satellite);
		request.setIsInitialization(true);
		List<OrbitalState> states = executeRequestRespond(request);
		return getFirst(states);
	}

	@Override
	public List<TleOrbitalParameters> getTleFor(String satellite, long from, long to) {
		TleRequest request = new TleRequest(getID());
		request.setIssuedBy(issuedBy);
		request.setSatelliteName(satellite);
		request.setFrom(from);
		request.setTo(to);

		return executeRequestRespond(request);
	}

	@Override
	public TleOrbitalParameters getTleFor(String satellite) {
		TleRequest request = new TleRequest(getID());
		request.setIssuedBy(issuedBy);
		request.setSatelliteName(satellite);		
		request.setIsInitialization(true);
		List<TleOrbitalParameters> parameters = executeRequestRespond(request);
		return getFirst(parameters);
	}

	public GroundStation retrieveLocation(String location) {
		GroundStationRequest request = new GroundStationRequest(getID());
		request.setIssuedBy(issuedBy);
		request.setLocation(location);
		List<GroundStation> respond = executeRequestRespond(request);
		return getFirst(respond);
	}

	protected Map<Parameter, List<State>> getParameterWithState(ParameterRequest request) {
		request.setIncludeStates(true);
		List<EntityInstance> respond = executeRequestRespond(request);
		return ApiUtility.sortParametersAndState(respond);
	}

	protected List<Parameter> getParameterWithoutState(ParameterRequest request) {
		return executeRequestRespond(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hbird.business.api.IDataAccess#retrieveNextLocationContactEventsFor(java.lang.String)
	 */
	@Override
	public LocationContactEvent getNextLocationContactEventForLocation(String location) {
		return getNextLocationContactEventFor(location, null, (new Date()).getTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hbird.business.api.IDataAccess#retrieveNextLocationContactEventsFor(java.lang.String)
	 */
	@Override
	public LocationContactEvent getNextLocationContactEventForLocation(String location, long from) {
		return getNextLocationContactEventFor(location, null, from);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hbird.business.api.IDataAccess#retrieveNextLocationContactEventsFor(java.lang.String)
	 */
	@Override
	public LocationContactEvent getNextLocationContactEventFor(String location, String satellite) {
		return getNextLocationContactEventFor(location, satellite, System.currentTimeMillis());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hbird.business.api.IDataAccess#retrieveNextLocationContactEventsFor(java.lang.String)
	 */
	@Override
	public LocationContactEvent getNextLocationContactEventFor(String location, String satellite, long from) {
		/* Get next start event. */
		LocationContactEventRequest request = new LocationContactEventRequest(getID());
		request.setIssuedBy(issuedBy);
		request.setLocation(location);

		request.setRows(1);
		request.setSatelliteName(satellite);
		request.setFrom(from);
		List<LocationContactEvent> events = executeRequestRespond(request);
		return getFirst(events);
	}

	@Override
	public List<PointingData> getNextLocationPointingDataForLocation(String location) {
		return getNextLocationPointingDataFor(location, null);
	}

	@Override
	public List<PointingData> getNextLocationPointingDataForSatellite(String satellite) {
		return getNextLocationPointingDataFor(null, satellite);
	}

	@Override
	public List<PointingData> getNextLocationPointingDataFor(String location, String satellite) {
		LocationContactEvent event = getNextLocationContactEventForLocation(location);

		if (event != null) {
			return getLocationPointingDataFor(location, event.getStartTime(), event.getEndTime());
		}

		return new ArrayList<PointingData>();
	}

	@Override
	public List<PointingData> getLocationPointingDataFor(String location, long from, long to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Metadata> getMetadata(EntityInstance subject) {
		MetadataRequest request = new MetadataRequest(getID());
		request.setIssuedBy(issuedBy);
		request.setIsApplicableTo(subject.getID());

		return executeRequestRespond(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hbird.business.api.IDataAccess#getEvents(long, long)
	 */
	@Override
	public List<Event> getEvents(Long from, Long to) {
		return executeRequestRespond(new EventRequest(issuedBy, from, to));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hbird.business.api.IDataAccess#resolveNamed(java.lang.String)
	 */
	@Override
	public EntityInstance resolve(String ID) {
		DataRequest request = new DataRequest(issuedBy);
		request.setID(ID);
		request.setIsInitialization(true);
		List<EntityInstance> results = executeRequestRespond(request);
		return getFirst(results);
	}	
}
