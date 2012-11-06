package org.hbird.exchange.configurator;

import java.util.List;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.tasking.Task;

public class StartCommandArchiveComponent extends StartComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4273598204575484126L;

	public StartCommandArchiveComponent(String issuedBy, String destination, List<String> lockStates, List<Task> tasks, Command command) {
		super(issuedBy, "StartCommandArchive", "Command to a configurator to start a command archive component.", lockStates, tasks, command);
	}	
	
	public StartCommandArchiveComponent(String issuedBy, String componentName, String destination) {
		super(issuedBy, "StartCommandArchive", "Command to a configurator to start a command archive component.", null, null, new Command(issuedBy, destination, "StartCommandComponent", "Command to a configurator to start a command archive component."));
		addComponentName(componentName);
	}	

	public StartCommandArchiveComponent(String issuedBy, String componentName) {
		super(issuedBy, "StartCommandArchive", "Command to a configurator to start a command archive component.", null, null, new Command(issuedBy, "Configurator", "StartCommandComponent", "Command to a configurator to start a command archive component."));
		addComponentName(componentName);
	}		
}
