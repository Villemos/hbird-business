package org.hbird.exchange.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @TITLE Command Definition
 * This object represents a command.
 * 
 * The lifecycle of a command is
 * 1) Creation.
 * 2) Release to the command queue. The command will wait in the queue until it is ready for release.
 * 3) Validation of the lock states of the command. If any of the lock states is 'false', then the command will not be released.
 * 4) Scheduling of all tasks needed to validate the command.
 * 5) Release, i.e. transfer to the system responsible for transfering the command to the satellite.
 *
 * h2. Arguments
 * 
 * A command can have zero or more arguments. An argument is simply a name / value pair.
 * 
 * h2. Lock States
 * 
 * A lock state is a parameter state, which must be true for the command to be released. This can be used to
 * define 
 * 1) Dependencies on limits (only release a command if a (set of) parameters is within a given limit).
 * 2) Define dependencies on other commands (interlock). The failure in a command should result in a command
 *    validation state parameter being set to false.
 * 3) Definition of general system locks (master mode) where no comamnds can be issued.
 *
 * h2. Tasks
 * 
 * The validation of the command is done through a number of validation tasks. Each task perform a specific
 * part of the validation at a given delta time compared to the release of the command. This can be used to
 * 1) Disable limit checking when it is known that it will change.
 * 2) Change the expected limit.
 * 3) Enable the limit again after the command propagation period has expired.
 * 4) Perform specific parameter checks at given points in time.
 * 
 * @CATEGORY Information Type
 * @END
 */
public class Command extends Named {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** The name of the component to which this command is destined. */
	protected String destination;
	
	/** List of arguments. The value is embedded in the header of the exchange. */
	protected Map<String, Object> arguments = new HashMap<String, Object>();
	
	/** The time at which this command should be released for transfer to the satellite. A value of
	 *  0 indicates immediate. */
	protected long releaseTime = 0;
	
	/** The time at which the command should be executed onboard the satellite. A value of 0 indicates
	 * immediate. */
	protected long executionTime = 0;
	
	public Command(String issuedBy, String destination, String name, String description) {
		super(issuedBy, name, "Command", description);
		this.destination = destination;
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
	public Command(String issuedBy, String destination, String name, String description, long releaseTime, long executionTime, Map<String, Object> arguments) {
		super(issuedBy, name, "Command", description);
		this.destination = destination;
		this.arguments = arguments;
		this.releaseTime = releaseTime;
		this.executionTime = executionTime;
	}
	
	public Map<String, Object> getArguments() {
		return arguments;
	}

	public void setArguments(Map<String, Object> arguments) {
		this.arguments = arguments;
	}

	/**
	 * Getter of the command release time.
	 * 
	 * @return The time (ms from 1970) at which the command should be released for transfer.
	 */
	public long getReleaseTime() {
		return releaseTime;
	}

	/**
	 * Getter of the command execution time.
	 * 
	 * @return The time (ms from 1970) at which the command should execute.
	 */
	public long getExecutionTime() {
		return executionTime;
	}
	
	public long getReleaseDelay() {
		Date now = new Date();
		return now.getTime() > releaseTime ? 0 : releaseTime - now.getTime();
	}

	public void setReleaseTime(long releaseTime) {
		this.releaseTime = releaseTime;
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}
	
	public String prettyPrint() {
		return "Command {name=" + name + ", timestamp=" + timestamp + "}";
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	public void addArgument(String key, Object value) {
		this.arguments.put(key, value);
	}
}
