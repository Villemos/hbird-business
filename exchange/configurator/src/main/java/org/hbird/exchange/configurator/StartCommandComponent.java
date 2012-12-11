package org.hbird.exchange.configurator;

public class StartCommandComponent extends StartComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8698880622780816407L;
	
	public StartCommandComponent() {
		super("CommandingChain", "StartCommandComponent", "Command to a configurator to start a command component.");
	}	
	
	public StartCommandComponent(String componentname) {
		super(componentname, "StartCommandComponent", "Command to a configurator to start a command component.");
	}	

	public StartCommandComponent(String issuedBy, String destination, String componentname) {
		super(issuedBy, destination, componentname, "StartCommandComponent", "Command to a configurator to start a command component.");
	}	
}
