package org.hbird.exchange.configurator;

public class StartTaskExecutorComponent extends StartComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3982371669802672264L;

	public StartTaskExecutorComponent() {
		super("TaskExecutor", "StartTaskExecutorComponent", "Command to a configurator to start a task executor component.");
	}	
	
	public StartTaskExecutorComponent(String componentname) {
		super(componentname, "StartTaskExecutorComponent", "Command to a configurator to start a task executor component.");
	}	

	public StartTaskExecutorComponent(String issuedBy, String destination, String componentname) {
		super(issuedBy, destination, componentname, "StartTaskExecutorComponent", "Command to a configurator to start a task executor component.");
	}	
}
