package org.hbird.exchange.configurator;

import java.util.List;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.tasking.Task;

public class StartParameterArchiveComponent extends StartComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 401570426742630847L;

	public StartParameterArchiveComponent(String issuedBy, String destination, List<String> lockStates, List<Task> tasks, Command command) {
		super(issuedBy, "StartParameterArchiveComponent", "Command to a configurator to start a parameter archive component.", lockStates, tasks, command);
	}	
	
	public StartParameterArchiveComponent(String issuedBy, String componentName, String destination) {
		super(issuedBy, "StartParameterArchiveComponent", "Command to a configurator to start a parameter archive component.", null, null, new Command(issuedBy, destination, "StartParameterArchiveComponent", "Command to a configurator to start a parameter archive component."));
		addComponentName(componentName);
	}	

	public StartParameterArchiveComponent(String issuedBy, String componentName) {
		super(issuedBy, "StartParameterArchiveComponent", "Command to a configurator to start a parameter archive component.", null, null, new Command(issuedBy, "Configurator", "StartParameterArchiveComponent", "Command to a configurator to start a parameter archive component."));
		addComponentName(componentName);
	}		
}
