package org.hbird.exchange.dataaccess;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;

public class OrbitalStateRequest extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3599199372427419649L;
 
	{
		arguments.put("satellite", new CommandArgument("satellite", "The name of the satellite for which to retrieve the states.", "String", "", null, true));
		arguments.put("sortorder", new CommandArgument("sortorder", "The order in which the returned data should be returned.", "String", "", "ASC", false));
		arguments.put("sort", new CommandArgument("sort", "The sort field. Default is timestamp.", "String", "", "timestamp", false));
		arguments.put("rows", new CommandArgument("rows", "The maximum number of rows to be retrieved.", "Long", "", null, false));
		arguments.put("from", new CommandArgument("from", "The start of a range search on timestamp. Default to '*'.", "Long", "Seconds", null, false));
		arguments.put("to", new CommandArgument("to", "The end of a range search on timestamp. Default to '*'.", "Long", "Seconds", null, false));
	}
	
	public OrbitalStateRequest(String satellite) {
		super("", "Archive", "OrbitalStateRequest", "A request for the latest orbital state of a satellite");
		
		setSatellite(satellite);
		addArgument("sort", "timestamp");
		addArgument("sortorder", "DESC");
		addArgument("rows", 1);
	}
	
	public OrbitalStateRequest(String satellite, Long from, Long to) {
		super("", "", "OrbitalStateRequest", "A request for the latest orbital state of a satellite");
		
		setSatellite(satellite);
		setFromTime(from);
		setToTime(to);
	}

	public void setSatellite(String satellite) {
		addArgument("satellite", satellite);
	}
	
	public void setFromTime(Long from) {
		addArgument("from", from);
	}
	
	public void setToTime(Long to) {
		addArgument("to", to);
	}
}
