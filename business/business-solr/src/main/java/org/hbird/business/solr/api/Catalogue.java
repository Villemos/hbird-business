package org.hbird.business.solr.api;

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
import org.hbird.exchange.navigation.GroundStation;
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
        GroundStationRequest request = createGroundStationRequest(issuedBy);
        return executeRequest(groundStationFilter, request);
    }

    /**
     * @see org.hbird.business.api.ICatalogue#getGroundStationByName(java.lang.String)
     */
    @Override
    public GroundStation getGroundStationByName(String name) {
        GroundStationRequest request = createGroundStationRequest(issuedBy);
        request.addName(name);
        List<GroundStation> stations = executeRequest(groundStationFilter, request);
        return getFirst(stations);
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
        ParameterRequest request = createParameterRequest(1000); // TODO - 27.02.2013, kimmell - check the limit
        return executeRequest(parameterFilter, request);
    }

    @Override
    public List<State> getStates() {
        // TODO - 27.02.2013, kimmell - use StateRequest instead?
        ParameterRequest request = createStateRequest(1000); // TODO - 27.02.2013, kimmell - check the limit
        return executeRequest(stateFilter, request);
    }

    @Override
    public List<Command> getCommands() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return
     */
    GroundStationRequest createGroundStationRequest(String issuedBy) {
        GroundStationRequest request = new GroundStationRequest(issuedBy);
        request.setIsInitialization(true);
        request.setType(GroundStation.class.getSimpleName());
        request.setRows(1);
        return request;
    }

    /**
     * @return
     */
    SatelliteRequest createSatelliteRequest(String issuedBy) {
        SatelliteRequest request = new SatelliteRequest(issuedBy);
        request.setIsInitialization(true);
        request.setType(Satellite.class.getSimpleName());
        request.setRows(1);
        return request;
    }

    /**
     * @return
     */
    ParameterRequest createParameterRequest(int limit) {
        ParameterRequest request = new ParameterRequest(new ArrayList<String>(), limit);
        request.setIsInitialization(true);
        request.setType(Parameter.class.getSimpleName());
        request.setRows(1);
        return request;
    }

    /**
     * @return
     */
    ParameterRequest createStateRequest(int limit) {
        ParameterRequest request = createParameterRequest(limit);
        request.setType(State.class.getSimpleName());
        return request;
    }
}
