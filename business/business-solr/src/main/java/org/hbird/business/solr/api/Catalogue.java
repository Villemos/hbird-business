package org.hbird.business.solr.api;

import java.util.ArrayList;
import java.util.List;

import org.hbird.business.api.HbirdApi;
import org.hbird.business.api.ICatalogue;
import org.hbird.business.api.TypeFilter;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.dataaccess.LocationRequest;
import org.hbird.exchange.dataaccess.ParameterRequest;
import org.hbird.exchange.dataaccess.SatelliteRequest;
import org.hbird.exchange.navigation.Location;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;

public class Catalogue extends HbirdApi implements ICatalogue {

	protected TypeFilter<Parameter> parameterFilter = new TypeFilter<Parameter>(Parameter.class);
	protected TypeFilter<State> stateFilter = new TypeFilter<State>(State.class);
	protected TypeFilter<OrbitalState> orbitalStateFilter = new TypeFilter<OrbitalState>(OrbitalState.class);
	protected TypeFilter<TleOrbitalParameters> tleOrbitalParameterFilter = new TypeFilter<TleOrbitalParameters>(TleOrbitalParameters.class);
	protected TypeFilter<Location> locationFilter = new TypeFilter<Location>(Location.class);
	protected TypeFilter<Satellite> satelliteFilter = new TypeFilter<Satellite>(Satellite.class);
	
	public Catalogue(String issuedBy) {
		super(issuedBy);
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
		return parameterFilter.getObjects(template.requestBody(inject, request, List.class));
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
}
