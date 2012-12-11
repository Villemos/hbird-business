package org.hbird.exchange.configurator;

public class StartSystemMonitoringComponent extends StartComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3898965757266005926L;

	public StartSystemMonitoringComponent() {
		super("SystemMonitoring", "StartSystemMonitoringComponent", "Command to a configurator to start a system monitoring component.");
	}	
	
	public StartSystemMonitoringComponent(String componentname) {
		super(componentname, "StartSystemMonitoringComponent", "Command to a configurator to start a system monitoring component.");
	}	

	public StartSystemMonitoringComponent(String issuedBy, String destination, String componentname) {
		super(issuedBy, destination, componentname, "StartSystemMonitoringComponent", "Command to a configurator to start a system monitoring component.");
	}	
}
