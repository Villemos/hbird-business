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

import org.apache.camel.model.RouteDefinition;
import org.hbird.business.api.ApiUtility;
import org.hbird.business.api.HbirdApi;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.TypeFilter;
import org.hbird.exchange.core.Issued;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.dataaccess.DataRequest;
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

public class DataAccess extends HbirdApi implements IDataAccess {

    protected TypeFilter<Parameter> parameterFilter = new TypeFilter<Parameter>(Parameter.class);
    protected TypeFilter<State> stateFilter = new TypeFilter<State>(State.class);
    protected TypeFilter<OrbitalState> orbitalStateFilter = new TypeFilter<OrbitalState>(OrbitalState.class);
    protected TypeFilter<TleOrbitalParameters> tleOrbitalParameterFilter = new TypeFilter<TleOrbitalParameters>(TleOrbitalParameters.class);

    public DataAccess(String issuedBy) {
        super(issuedBy);
    }

    @Override
    public List<Named> getData(DataRequest request) {
        return template.requestBody(inject, request, List.class);
    }

    
    
    /** INITIALIZATION */
    @Override
    public Parameter getParameter(String name) {
        List<Parameter> parameter = getParameterAt(null, name, (new Date()).getTime(), 1);
        return parameter.isEmpty() ? null : parameter.get(0);
    }

    @Override
    public Parameter getParameter(String source, String name) {
        List<Parameter> parameter = getParameterAt(source, name, (new Date()).getTime(), 1);
        return parameter.isEmpty() ? null : parameter.get(0);
    }

    @Override
    public Parameter getParameterAt(String name, long at) {
        List<Parameter> parameter = getParameterAt(null, name, at, 1);
        return parameter.isEmpty() ? null : parameter.get(0);
    }

    @Override
    public Parameter getParameterAt(String source, String name, long at) {
        List<Parameter> parameter = getParameterAt(source, name, at, 1);
        return parameter.isEmpty() ? null : parameter.get(0);
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
        ParameterRequest request = new ParameterRequest(issuedBy, name, rows);
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
        ParameterRequest request = new ParameterRequest(issuedBy, name, rows);
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
        ParameterRequest request = new ParameterRequest(issuedBy, name, rows);
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
        ParameterRequest request = new ParameterRequest(issuedBy, names, 1);
        request.setIsInitialization(true);
        request.setTo(at);
        return getParameterWithState(request);
    }

    /** STEPPING */

    @Override
    public List<Parameter> getParameter(String name, long from, long to) {
        ParameterRequest request = new ParameterRequest(issuedBy, name, from, to);
        return getParameterWithoutState(request);
    }

    @Override
    public List<Parameter> getParameter(String name, long from, long to, int rows) {
        ParameterRequest request = new ParameterRequest(issuedBy, name, from, to, rows);
        return getParameterWithoutState(request);
    }

    @Override
    public List<Parameter> getParameters(List<String> names, long from, long to) {
        ParameterRequest request = new ParameterRequest(issuedBy, names, from, to);
        return getParameterWithoutState(request);
    }

    @Override
    public List<Parameter> getParameters(List<String> names, long from, long to, int rows) {
        ParameterRequest request = new ParameterRequest(issuedBy, names, from, to, rows);
        return getParameterWithoutState(request);
    }

    @Override
    public Map<Parameter, List<State>> getParameterAndStates(String name, long from, long to) {
        ParameterRequest request = new ParameterRequest(issuedBy, name, from, to);
        return getParameterWithState(request);
    }

    @Override
    public Map<Parameter, List<State>> getParameterAndStates(String name, long from, long to, int rows) {
        ParameterRequest request = new ParameterRequest(issuedBy, name, from, to, rows);
        return getParameterWithState(request);
    }

    @Override
    public Map<Parameter, List<State>> getParametersAndStates(List<String> names, long from, long to) {
        ParameterRequest request = new ParameterRequest(issuedBy, names, from, to);
        return getParameterWithState(request);
    }

    @Override
    public Map<Parameter, List<State>> getParametersAndStates(List<String> names, long from, long to, int rows) {
        ParameterRequest request = new ParameterRequest(issuedBy, names, from, to, rows);
        return getParameterWithState(request);
    }

    
    
    
    
    @Override
    public List<State> getState(String isStateOf) {
        StateRequest request = new StateRequest(issuedBy, isStateOf);
        request.setIsInitialization(true);
        request.setRows(1);
        return stateFilter.getObjects(template.requestBody(inject, request, List.class));
    }

    @Override
    public List<State> getState(String isStateOf, long from, long to) {
        StateRequest request = new StateRequest(issuedBy, isStateOf, from, to);
        return stateFilter.getObjects(template.requestBody(inject, request, List.class));
    }

    @Override
    public List<State> getStates(List<String> states) {
        StateRequest request = new StateRequest(issuedBy, null, states);
        return stateFilter.getObjects(template.requestBody(inject, request, List.class));
    }

    @Override
    public List<OrbitalState> getOrbitalStatesFor(String satellite, long from, long to) {
        OrbitalStateRequest request = new OrbitalStateRequest(issuedBy, satellite, from, to);
        return orbitalStateFilter.getObjects(template.requestBody(inject, request, List.class));
    }

    @Override
    public List<OrbitalState> getOrbitalStatesFor(String satellite, long from, long to, String issuedBy, String derivedFromName, long derivedFromTimestamp) {
        OrbitalStateRequest request = new OrbitalStateRequest(issuedBy, satellite, from, to);
        request.setDerivedFrom(issuedBy, derivedFromName, derivedFromTimestamp);
        return orbitalStateFilter.getObjects(template.requestBody(inject, request, List.class));
    }

    @Override
    public OrbitalState getOrbitalStateFor(String satellite, String derivedIssuedBy, String derivedFromName, long derivedFromTimestamp) {
        OrbitalStateRequest request = new OrbitalStateRequest(issuedBy, satellite);
        request.setIsInitialization(true);
        request.setDerivedFrom(derivedIssuedBy, derivedFromName, derivedFromTimestamp);
        List<OrbitalState> states = orbitalStateFilter.getObjects(template.requestBody(inject, request, List.class));
        return states.isEmpty() == true ? null : states.get(0);
    }

    @Override
    public OrbitalState getOrbitalStateFor(String satellite) {
        TleOrbitalParameters parameter = getTleFor(satellite);

        return parameter == null ? null : getOrbitalStateFor(satellite, parameter.getIssuedBy(), parameter.getName(), parameter.getTimestamp());
    }

    @Override
    public List<TleOrbitalParameters> getTleFor(String satellite, long from, long to) {
        TleRequest request = new TleRequest(issuedBy, satellite, from, to);
        return tleOrbitalParameterFilter.getObjects(template.requestBody(inject, request, List.class));
    }

    @Override
    public TleOrbitalParameters getTleFor(String satellite) {
        TleRequest request = new TleRequest(issuedBy, satellite);
        request.setIsInitialization(true);
        List<TleOrbitalParameters> parameters = tleOrbitalParameterFilter.getObjects(template.requestBody(inject, request, List.class));

        return parameters.isEmpty() ? null : parameters.get(0);
    }

    public GroundStation retrieveLocation(String location) {
        GroundStationRequest request = new GroundStationRequest(issuedBy, location);
        List respond = template.requestBody(inject, request, List.class);
        return respond.isEmpty() ? null : (GroundStation) respond.get(0);
    }

    @Override
    public void configure() throws Exception {
        /** This configures a direct connection to SOLR. */
        RouteDefinition route = from(inject).to("solr:api");

        template = getContext().createProducerTemplate();
    }

    protected Map<Parameter, List<State>> getParameterWithState(ParameterRequest request) {
        request.setIncludeStates(true);
        List<Issued> respond = template.requestBody(inject, request, List.class);
        return ApiUtility.sortParametersAndState(respond);
    }

    protected List<Parameter> getParameterWithoutState(ParameterRequest request) {
        List<Named> respond = template.requestBody(inject, request, List.class);
        return parameterFilter.getObjects(respond);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hbird.business.api.IDataAccess#retrieveNextLocationContactEventsFor(java.lang.String)
     */
    @Override
    public List<LocationContactEvent> getNextLocationContactEventsFor(String location) {
        return getNextLocationContactEventsFor(location, null, (new Date()).getTime());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hbird.business.api.IDataAccess#retrieveNextLocationContactEventsFor(java.lang.String)
     */
    @Override
    public List<LocationContactEvent> getNextLocationContactEventsFor(String location, long from) {
        return getNextLocationContactEventsFor(location, null, from);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hbird.business.api.IDataAccess#retrieveNextLocationContactEventsFor(java.lang.String)
     */
    @Override
    public List<LocationContactEvent> getNextLocationContactEventsFor(String location, String satellite) {
        return getNextLocationContactEventsFor(location, satellite, (new Date()).getTime());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hbird.business.api.IDataAccess#retrieveNextLocationContactEventsFor(java.lang.String)
     */
    @Override
    public List<LocationContactEvent> getNextLocationContactEventsFor(String location, String satellite, long from) {

        List<LocationContactEvent> events = new ArrayList<LocationContactEvent>();

        /** Get next start event. */
        LocationContactEventRequest request = new LocationContactEventRequest(issuedBy, location, true);
        request.setRows(1);
        request.setSatelliteName(satellite);
        request.setFrom(from);
        List<Named> start = template.requestBody(inject, request, List.class);

        /** If we found one, find the following end event. */
        if (start.isEmpty() == false) {

            LocationContactEvent startEvent = (LocationContactEvent) start.get(0);

            request = new LocationContactEventRequest(issuedBy, location, false);
            request.setRows(1);
            request.setSatelliteName(startEvent.getSatelliteName());
            request.setLocation(startEvent.getGroundStationName());
            request.setFrom(startEvent.getTimestamp());
            List<Named> end = template.requestBody(inject, request, List.class);

            if (end.isEmpty() == false) {
                events.add(startEvent);
                events.add((LocationContactEvent) end.get(0));
            }
        }

        return events;
    }

    @Override
    public List<PointingData> getNextLocationPointingDataFor(String location) {
        return getNextLocationPointingDataFor(location, null);
    }

    @Override
    public List<PointingData> getNextLocationPointingDataFor(String location, String satellite) {
        List<LocationContactEvent> events = getNextLocationContactEventsFor(location);

        if (events.isEmpty() == false) {
            return getLocationPointingDataFor(location, events.get(0).getTimestamp(), events.get(1).getTimestamp());
        }

        return new ArrayList<PointingData>();
    }

    @Override
    public List<PointingData> getLocationPointingDataFor(String location, long from, long to) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Named> getMetadata(Issued subject) {
        /** Get next start event. */
        MetadataRequest request = new MetadataRequest(issuedBy, subject);
        List<Named> respond = template.requestBody(inject, request, List.class);
        return respond;
    }
}
