package org.hbird.exchange.configurator;

import java.util.List;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.tasking.Task;

public class StartTaskExecutorComponent extends StartComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3982371669802672264L;

	public StartTaskExecutorComponent(String issuedBy, String destination, List<String> lockStates, List<Task> tasks, Command command) {
		super(issuedBy, "StartTaskExecutorComponent", "Command to a configurator to start a task executor component.", lockStates, tasks, command);
	}	
	
	public StartTaskExecutorComponent(String issuedBy, String destination) {
		super(issuedBy, "StartTaskExecutorComponent", "Command to a configurator to start a task executor component.", null, null, new Command(issuedBy, destination, "StartTaskExecutorComponent", "Command to a configurator to start a task executor component."));
	}	

	public StartTaskExecutorComponent(String issuedBy) {
		super(issuedBy, "StartTaskExecutorComponent", "Command to a configurator to start a task executor component.", null, null, new Command(issuedBy, "Configurator", "StartTaskExecutorComponent", "Command to a configurator to start a task executor component."));
	}		
}
