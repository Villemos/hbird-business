package org.hbird.exchange.configurator;

import org.hbird.exchange.core.Command;

public abstract class StartComponent extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5607028481851891556L;

	public StartComponent(String issuedBy, String destination, String componentname, String requestname, String description) {
		super(issuedBy, destination, requestname, description);
		addComponentName(componentname);
	}

	public StartComponent(String componentname, String requestname, String description) {
		super("Assembly", "Configurator", requestname, description);
		addComponentName(componentname);
	}

	protected void addComponentName(String componentname) {
		addArgument("componentname", componentname);
	}

	protected void addHeartbeat(long period) {
		addArgument("heartbeat", period);
	}

	public void setHeartbeat(long period) {
		addHeartbeat(period);
	}
	
	public long getHeartbeat() {
		return (Long) getArguments().get("heartbeat");
	}
}
