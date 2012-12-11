package org.hbird.exchange.dataaccess;

import java.util.List;

import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.State;
import org.hbird.exchange.navigation.OrbitalState;

public interface IDataAccess {

	public List<Named> retrieveData(DataRequest request);
	
	public List<State> retrieveState(StateRequest request);
	
	public OrbitalState getOrbitalState(OrbitalStateRequest request);
}
