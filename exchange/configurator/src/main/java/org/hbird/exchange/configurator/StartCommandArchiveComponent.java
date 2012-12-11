package org.hbird.exchange.configurator;

public class StartCommandArchiveComponent extends StartComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4273598204575484126L;

	public StartCommandArchiveComponent() {
		super("CommandArchive", "StartCommandArchive", "Command to a configurator to start a command archive component.");
	}	
	
	public StartCommandArchiveComponent(String componentname) {
		super(componentname, "StartCommandArchive", "Command to a configurator to start a command archive component.");
	}	
	
	public StartCommandArchiveComponent(String issuedBy, String destination, String componentname) {
		super(issuedBy, destination, componentname, "StartCommandArchive", "Command to a configurator to start a command archive component.");
	}	
}
