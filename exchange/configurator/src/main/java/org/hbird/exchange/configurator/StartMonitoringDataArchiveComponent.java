package org.hbird.exchange.configurator;

public class StartMonitoringDataArchiveComponent extends StartComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 401570426742630847L;

	public StartMonitoringDataArchiveComponent() {
		super("MonitoringDataArchive", "StartParameterArchiveComponent", "Command to a configurator to start a parameter archive component.");
	}	
	
	public StartMonitoringDataArchiveComponent(String componentname) {
		super(componentname, "StartParameterArchiveComponent", "Command to a configurator to start a parameter archive component.");
	}	

	public StartMonitoringDataArchiveComponent(String issuedBy, String destination, String componentname) {
		super(issuedBy, destination, componentname, "StartParameterArchiveComponent", "Command to a configurator to start a parameter archive component.");
	}	
}
