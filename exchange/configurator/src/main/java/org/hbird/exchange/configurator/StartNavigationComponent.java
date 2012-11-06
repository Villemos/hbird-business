package org.hbird.exchange.configurator;

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.navigation.Location;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.tasking.Task;

public class StartNavigationComponent extends StartComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7639647113103082392L;

	public StartNavigationComponent(String issuedBy, String destination, List<String> lockStates, List<Task> tasks, Command command) {
		super(issuedBy, "StartNavigationComponent", "Command to a configurator to start a command component.", lockStates, tasks, command);
	}	
	
	public StartNavigationComponent(String issuedBy, String componentName, String destination) {
		super(issuedBy, "StartNavigationComponent", "Command to a configurator to start a navigation component.", null, null, new Command(issuedBy, destination, "StartNavigationComponent", "Command to a configurator to start a command component."));
		addComponentName(componentName);
	}	

	public StartNavigationComponent(String issuedBy, String componentName) {
		super(issuedBy, "StartNavigationComponent", "Command to a configurator to start a navigation component.", null, null, new Command(issuedBy, "Configurator", "StartNavigationComponent", "Command to a configurator to start a command component."));
		addComponentName(componentName);
	}	

	public void setSatellite(Satellite satellite) {
		this.command.addArgument("satellite", satellite);
	}
	
	public Satellite getSatellite() {
		return (Satellite) this.command.getArguments().get("satellite");
	}
	
	public void addLocation(Location location) {
		List<Location> locations = new ArrayList<Location>();
		this.command.addArgument("locations", locations);
	}
	
	public Location getLocation() {
		return ((List<Location>) this.command.getArguments().get("locations")).get(0);
	}

	public void addLocations(List<Location> locations) {
		this.command.addArgument("locations", locations);
	}
	
	public List<Location> getLocations() {
		return (List<Location>) this.command.getArguments().get("locations");
	}
}
