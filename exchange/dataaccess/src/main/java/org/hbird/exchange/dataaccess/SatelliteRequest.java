package org.hbird.exchange.dataaccess;

import java.util.ArrayList;
import java.util.List;

public class SatelliteRequest extends DataRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1927868579577775670L;

	public SatelliteRequest(String issuedBy, List<String> satellites) {
		super(issuedBy, "Archive", "SatelliteRequest", "A request for the definition of satellites.");

		addNames(satellites);
		setType("Satellite");
		setIsInitialization(true);
	}
	
	public SatelliteRequest(String issuedBy, String satellite) {
		super(issuedBy, "Archive", "SatelliteRequest", "A request for the definition of satellites.");

		List<String> satellites = new ArrayList<String>();
		satellites.add(satellite);
		addNames(satellites);
		setType("Satellite");
		setIsInitialization(true);
	}

	public SatelliteRequest(String issuedBy) {
		super(issuedBy, "Archive", "SatelliteRequest", "A request for the definition of satellites.");
		
		setType("Satellite");
		setIsInitialization(true);
	}	
}
