package org.hbird.exchange.configurator;

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.navigation.Location;
import org.hbird.exchange.navigation.Satellite;

public class StartNavigationComponent extends StartComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7639647113103082392L;

	public StartNavigationComponent() {
		super("Navigation", "StartNavigationComponent", "Command to a configurator to start a command component.");
		
		setPredictorMethod("keplian");
	}	

	public StartNavigationComponent(String componentname) {
		super(componentname, "StartNavigationComponent", "Command to a configurator to start a command component.");

		setPredictorMethod("keplian");
	}	

	public StartNavigationComponent(String issuedBy, String destination, String componentname) {
		super(issuedBy, destination, componentname, "StartNavigationComponent", "Command to a configurator to start a command component.");

		setPredictorMethod("keplian");
	}	

	public void setSatellite(Satellite satellite) {
		addArgument("satellite", satellite);
	}

	public Satellite getSatellite() {
		return (Satellite) getArguments().get("satellite");
	}

	public void setLocation(Location location) {
		addLocation(location);
	}

	public void addLocation(Location location) {
		List<Location> locations = new ArrayList<Location>();
		addArgument("locations", locations);
	}

	public Location getLocation() {
		return ((List<Location>) getArguments().get("locations")).get(0);
	}

	public void addLocations(List<Location> locations) {
		addArgument("locations", locations);
	}

	public List<Location> getLocations() {
		return (List<Location>) getArguments().get("locations");
	}
	
	public void setPredictorMethod(String method) {
		addArgument("predictortype", method);
	}
}
