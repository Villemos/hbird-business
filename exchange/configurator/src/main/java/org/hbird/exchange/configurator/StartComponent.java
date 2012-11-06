package org.hbird.exchange.configurator;

import java.util.List;

import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.tasking.Task;

public abstract class StartComponent extends CommandRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5607028481851891556L;

	public StartComponent(String issuedBy, String name, String description, List<String> lockStates, List<Task> tasks, Command command) {
		super(issuedBy, name, description, lockStates, tasks, command);
	}
	
	protected void addComponentName(String componentname) {
		this.command.addArgument("componentname", componentname);
	}

	protected void addHeartbeat(long period) {
		this.command.addArgument("heartbeat", period);
	}

	public void setHeartbeat(long period) {
		addHeartbeat(period);
	}
	
	public long getHeartbeat() {
		return (Long) this.command.getArguments().get("heartbeat");
	}
}
