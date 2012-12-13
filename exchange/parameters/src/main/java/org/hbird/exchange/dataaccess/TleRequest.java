package org.hbird.exchange.dataaccess;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;

public class TleRequest extends Command {

	private static final long serialVersionUID = -4895555326865366387L;

	{
		arguments.put("satellite", new CommandArgument("satellite", "The name of the satellite for which to retrieve the states.", "String", "", null, true));
		arguments.put("from", new CommandArgument("from", "The start of a range search on timestamp. Default to '*'.", "Long", "Seconds", null, false));
		arguments.put("to", new CommandArgument("to", "The end of a range search on timestamp. Default to '*'.", "Long", "Seconds", null, false));
		arguments.put("sortorder", new CommandArgument("sortorder", "The order in which the returned data should be returned.", "String", "", "ASC", false));
		arguments.put("sort", new CommandArgument("sort", "The sort field. Default is timestamp.", "String", "", "timestamp", false));
		arguments.put("rows", new CommandArgument("rows", "The number of entries to retrieve. If set to 1 at from and to is empty, then the last available entry is retrieved.", "Long", "", 1l, false));
	}

	public TleRequest(String issuedBy, String satellite) {
		super(issuedBy, "Archive", "TleRequest", "A request for the TLE parameters of a satellite.");

		setSatellite(satellite);
		addArgument("sort", "timestamp");
		addArgument("sortorder", "DESC");
		addArgument("rows", 1);
	}

	public void setSatellite(String satellite) {
		addArgument("satellite", satellite);
	}
	
	public String getSatellite() {
		return (String) getArgument("Satellite");
	}
}
