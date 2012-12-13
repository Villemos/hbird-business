package org.hbird.exchange.dataaccess;

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.core.CommandArgument;

public class LocationRequest extends DataRequest {

	private static final long serialVersionUID = -3332566639747183418L;

	{
		arguments.put("sortorder", new CommandArgument("sortorder", "The order in which the returned data should be returned.", "String", "", "ASC", true));
		arguments.put("sort", new CommandArgument("sort", "The sort field. Default is timestamp.", "String", "", "timestamp", true));
		arguments.put("rows", new CommandArgument("rows", "The maximum number of rows to be retrieved.", "Long", "", 1000, true));
	}
	
	public LocationRequest(String issuedBy, List<String> locations) {
		super(issuedBy, "Archive", "LocationRequest", "A request for the definition of locations.");

		addNames(locations);
	}
	
	public LocationRequest(String issuedBy, String location) {
		super(issuedBy, "Archive", "LocationRequest", "A request for the definition of locations.");

		List<String> locations = new ArrayList<String>();
		locations.add(location);
		addNames(locations);
	}
}
