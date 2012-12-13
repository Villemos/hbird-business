package org.hbird.exchange.commandrelease;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.tasking.Task;

public class CommandRequest extends Named {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5923007317348738778L;

	/** The raw command. */
	protected Command command = null;

	/** Map keyed on the name of the lock state that is 'false' and holding as value the reason. */
	protected List<String> lockStates = new ArrayList<String>();

	/** List of tasks to be performed after the release of the command. */
	protected List<Task> tasks = new ArrayList<Task>();
	
	public CommandRequest(String issuedBy, String name, String description) {
		super(issuedBy, name, "ComponentRequest", description);
	}
	
	/**
	 * Basic constructor
	 * 
	 * @param name The name of the command.
	 * @param arguments The arguments of the command.
	 * @param lockStates The states of the command which must be true upon release.
	 * @param tasks The tasks to be performed as part of the command validation.
	 * @param releaseTime The time at which the command should be released by the MCS for transfer to the satellite.
	 * @param executionTime The time at which the command should be executed onboard.
	 */
	public CommandRequest(String issuedBy, String name, String description, List<String> lockStates, List<Task> tasks, Command command) {
		super(issuedBy, "CommandContainer" + name, "CommandContainer", "Command container holding lock states and tasks for the command '" + name + "'.");
		this.command = command;
		this.lockStates = lockStates;
		this.tasks = tasks;
	}
	
	
	/**
	 * Method to get the list of arguments of the command.
	 * 
	 * @return Map keyed on the argument name and with the value of the argument.
	 */
	public Map<String, CommandArgument> getArguments() {
		return command.getArguments();
	}

	/**
	 * Method to get the validation tasks of the command, i.e. a list of specific, timetagged tasks that
	 * will configure the system and perform the necesarry validation.
	 * 
	 * @return List of tasks.
	 */
	public List<Task> getTasks() {
		return tasks;
	}
	
	public Command getCommand() {
		return command;
	}

	public List<String> getLockStates() {
		return lockStates;
	}	
}
