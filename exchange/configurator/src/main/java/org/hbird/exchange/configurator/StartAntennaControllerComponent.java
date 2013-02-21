package org.hbird.exchange.configurator;

import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.navigation.Location;

public class StartAntennaControllerComponent extends StartComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1971324667157956566L;

	{
		arguments.put("location", new CommandArgument("location", "Name of the location. The location must be defined and exist as an object in the system.", "String", "", null, true));
		arguments.put("satellite", new CommandArgument("satellite", "Name of the satellite. The satellite must be defined and exist as an object in the system.", "String", "", null, true));
		arguments.put("queuename", new CommandArgument("queuename", "The name of the queue into which the schedule shall be send.", "String", "", null, true));
	}

	public StartAntennaControllerComponent(String componentname, Location location, String satellite) {
		super(componentname, "StartAntennaController", "Command to a configurator to start an antenna control component.");
		
		addArgument("location", location);
		addArgument("satellite", satellite);
		addArgument("queuename", "hbird.antennaschedule." + location.getName());
	}

	public Location getLocation() {
		return (Location) getArgument("location");
	}	
	
	public String getSatellite() {
		return (String) getArgument("satellite");
	}	

	public String getQueueName() {
		return (String) getArgument("queuename");
	}	
}
