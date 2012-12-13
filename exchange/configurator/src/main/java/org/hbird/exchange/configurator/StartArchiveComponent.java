package org.hbird.exchange.configurator;

public class StartArchiveComponent extends StartComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 401570426742630847L;

	public StartArchiveComponent() {
		super("Archive", "StartArchiveComponent", "Command to a configurator to start an archive component.");
	}	
	
	public StartArchiveComponent(String componentname) {
		super(componentname, "StartParameterArchiveComponent", "Command to a configurator to start a parameter archive component.");
	}	

	public StartArchiveComponent(String issuedBy, String destination, String componentname) {
		super(issuedBy, destination, componentname, "StartParameterArchiveComponent", "Command to a configurator to start a parameter archive component.");
	}	
}
