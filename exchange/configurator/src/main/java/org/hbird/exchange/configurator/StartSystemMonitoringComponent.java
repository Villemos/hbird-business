package org.hbird.exchange.configurator;

import java.util.List;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.tasking.Task;

public class StartSystemMonitoringComponent extends StartComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3898965757266005926L;

	public StartSystemMonitoringComponent(String issuedBy, String destination, List<String> lockStates, List<Task> tasks, Command command) {
		super(issuedBy, "StartSystemMonitoringComponent", "Command to a configurator to start a system monitoring component.", lockStates, tasks, command);
	}	
	
	public StartSystemMonitoringComponent(String issuedBy, String componentName, String destination) {
		super(issuedBy, "StartSystemMonitoringComponent", "Command to a configurator to start a system monitoring component.", null, null, new Command(issuedBy, destination, "StartSystemMonitoringComponent", "Command to a configurator to start a system monitoring component."));
		addComponentName(componentName);
	}	

	public StartSystemMonitoringComponent(String issuedBy, String componentName) {
		super(issuedBy, "StartSystemMonitoringComponent", "Command to a configurator to start a system monitoring component.", null, null, new Command(issuedBy, "Configurator", "StartSystemMonitoringComponent", "Command to a configurator to start a system monitoring component."));
		addComponentName(componentName);
	}		
}
