package org.hbird.exchange.dataaccess;

import java.util.HashMap;
import java.util.Map;

import org.hbird.exchange.core.CommandArgument;

public class LocationContactEventRequest extends DataRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9003134846725607506L;

	{
		arguments.put("satellite", new CommandArgument("satellite", "The satellite comming / leaving contact.", "String", "Name", null, false));
		arguments.put("location", new CommandArgument("location", "The location.", "String", "Name", null, true));
		arguments.put("visibility", new CommandArgument("contactevent", "Whether the contact event is a start of contact (true) or end of contact (false).", "boolean", "", true, true));
	}
	
	public LocationContactEventRequest(String issuedBy, String location) {
		super(issuedBy, "Archive");
		addArgument("type", "LocationContactEvent");
		addArgument("location", location);
	}

	public LocationContactEventRequest(String issuedBy, String location, boolean visibility) {
		super(issuedBy, "Archive");
		addArgument("type", "LocationContactEvent");
		addArgument("location", location);
		addArgument("visibility", visibility);
	}
	
	public void setSatellite(String satellite) {
		addArgument("satellite", satellite);		
	}
	
	public void setLocation(String location) {
		addArgument("location", location);		
	}
}
