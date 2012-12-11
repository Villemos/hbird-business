package org.hbird.exchange.dataaccess;

import org.hbird.exchange.core.Command;

public class OrbitalStateRequest extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3599199372427419649L;

	public OrbitalStateRequest(String satellite) {
		super("", "", "OrbitalStateRequest", "A request for the latest orbital state of a satellite");
		
		setSatellite(satellite);
		arguments.put("sort", "timestamp");
		arguments.put("sortorder", "DESC");
		arguments.put("rows", 1);
	}
	
	public OrbitalStateRequest(String satellite, Long from, Long to) {
		super("", "", "OrbitalStateRequest", "A request for the latest orbital state of a satellite");
		
		setSatellite(satellite);
		setFromTime(from);
		setToTime(to);
	}

	public void setSatellite(String satellite) {
		arguments.put("satellite", satellite);
	}
	
	public void setFromTime(Long from) {
		arguments.put("from", from);
	}
	
	public void setToTime(Long to) {
		arguments.put("to", to);
	}
}
