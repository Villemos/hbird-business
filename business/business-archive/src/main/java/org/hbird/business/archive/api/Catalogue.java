package org.hbird.business.archive.api;

import java.util.ArrayList;
import java.util.List;

import org.hbird.business.api.HbirdApi;
import org.hbird.business.api.ICatalogue;
import org.hbird.business.api.TypeFilter;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.dataaccess.GroundStationRequest;
import org.hbird.exchange.dataaccess.ParameterRequest;
import org.hbird.exchange.dataaccess.SatelliteRequest;
import org.hbird.exchange.dataaccess.StateRequest;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;

public class Catalogue extends HbirdApi implements ICatalogue {

    protected TypeFilter<Parameter> parameterFilter = new TypeFilter<Parameter>(Parameter.class);
    protected TypeFilter<State> stateFilter = new TypeFilter<State>(State.class);
    protected TypeFilter<OrbitalState> orbitalStateFilter = new TypeFilter<OrbitalState>(OrbitalState.class);
    protected TypeFilter<TleOrbitalParameters> tleOrbitalParameterFilter = new TypeFilter<TleOrbitalParameters>(TleOrbitalParameters.class);
    protected TypeFilter<GroundStation> groundStationFilter = new TypeFilter<GroundStation>(GroundStation.class);
    protected TypeFilter<Satellite> satelliteFilter = new TypeFilter<Satellite>(Satellite.class);

    public Catalogue(String issuedBy) {
        super(issuedBy);
    }

    @Override
    public List<GroundStation> getGroundStations() {
        GroundStationRequest request = createGroundStationRequest(issuedBy, null);
        return executeRequest(groundStationFilter, request);
    }

    /**
     * @see org.hbird.business.api.ICatalogue#getGroundStationByName(java.lang.String)
     */
    @Override
    public GroundStation getGroundStationByName(String name) {
    	List<String> names = new ArrayList<String>();
		names.add(name);
    	GroundStationRequest request = createGroundStationRequest(issuedBy, names);
        List<GroundStation> stations = executeRequest(groundStationFilter, request);
        return getFirst(stations);
    }

    /**
     * @see org.hbird.business.api.ICatalogue#getGroundStationByName(java.lang.String)
     */
    @Override
    public List<GroundStation> getGroundStationsByName(List<String> names) {
        GroundStationRequest request = createGroundStationRequest(issuedBy, names);
        return executeRequest(groundStationFilter, request);
    }

    @Override
    public List<Satellite> getSatellites() {
        SatelliteRequest request = createSatelliteRequest(issuedBy);
        return executeRequest(satelliteFilter, request);
    }

    /**
     * @see org.hbird.business.api.ICatalogue#getSatelliteByName(java.lang.String)
     */
    @Override
    public Satellite getSatelliteByName(String name) {
        SatelliteRequest request = createSatelliteRequest(issuedBy);
        request.addName(name);
        List<Satellite> satellites = executeRequest(satelliteFilter, request);
        return getFirst(satellites);
    }

    @Override
    public List<Parameter> getParameters() {
        ParameterRequest request = createParameterRequest(); 
        return executeRequest(parameterFilter, request);
    }

    @Override
    public List<State> getStates() {
        StateRequest request = createStateRequest(); 
        return executeRequest(stateFilter, request);
    }

    @Override
    public List<Command> getCommands() {
        // TODO Auto-generated method stub
        return null;
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
        ParameterRequest request = new ParameterRequest();
        request.setIsInitialization(true);
        request.setRows(1);
        return request;
    }

    /**
     * @return
     */
    protected StateRequest createStateRequest() {
        StateRequest request = new StateRequest();
        request.setIsInitialization(true);
        request.setRows(1);
        return request;
    }
}
