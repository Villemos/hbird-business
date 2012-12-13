package org.hbird.exchange.configurator;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;

public abstract class StartComponent extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5607028481851891556L;

	{
		arguments.put("componentname", new CommandArgument("componentname", "The name of the component to be started. Is used to route messages to the component. All data will be 'issuedby' this name.", "String", "", null, true));
		arguments.put("heartbeat", new CommandArgument("heartbeat", "The period between heartbeat signals from this component.", "Long", "Milliseconds", 0l, false));
	}
	
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
		return (Long) getArgument("heartbeat");
	}
}
