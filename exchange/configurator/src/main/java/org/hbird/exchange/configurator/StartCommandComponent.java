package org.hbird.exchange.configurator;

import java.util.List;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.tasking.Task;

public class StartCommandComponent extends StartComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8698880622780816407L;
	
	public StartCommandComponent(String issuedBy, String destination, List<String> lockStates, List<Task> tasks, Command command) {
		super(issuedBy, "StartCommandComponent", "Command to a configurator to start a command component.", lockStates, tasks, command);
	}	
	
	public StartCommandComponent(String issuedBy, String componentName, String destination) {
		super(issuedBy, "StartCommandComponent", "Command to a configurator to start a command component.", null, null, new Command(issuedBy, destination, "StartCommandComponent", "Command to a configurator to start a command component."));
		addComponentName(componentName);
	}	

	public StartCommandComponent(String issuedBy, String componentName) {
		super(issuedBy, "StartCommandComponent", "Command to a configurator to start a command component.", null, null, new Command(issuedBy, "Configurator", "StartCommandComponent", "Command to a configurator to start a command component."));
		addComponentName(componentName);
	}	
}
