/**
 * Licensed to the Hummingbird Foundation (HF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The HF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hbird.exchange.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
public class Command extends Named implements IScheduled {
		
	private static final long serialVersionUID = 1L;
	
	/** The name of the component to which this command is destined. */
	protected String destination;
		
	/** The time at which this command should be transfered to its destination. A value of
	 *  0 indicates immediate. */
	protected long transferTime = 0;
	
	/** The time at which the command should be executed at the destination. A value of 0 indicates
	 * immediate. */
	protected long executionTime = 0;
	
	/** List of arguments. The value is embedded in the header of the exchange. */
	protected Map<String, CommandArgument> arguments = new HashMap<String, CommandArgument>();

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
	public Command(String issuedBy, String destination, String name, String description, long releaseTime, long executionTime, Map<String, CommandArgument> arguments) {
		super(issuedBy, name, "Command", description);
		this.destination = destination;
		this.arguments = arguments;
		this.transferTime = releaseTime;
		this.executionTime = executionTime;
	}
	
	public Map<String, CommandArgument> getArguments() {
		return arguments;
	}

	public void setArguments(Map<String, CommandArgument> arguments) {
		this.arguments = arguments;
	}

	/**
	 * Getter of the command release time.
	 * 
	 * @return The time (ms from 1970) at which the command should be released for transfer.
	 */
	public long getReleaseTime() {
		return transferTime;
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
		return now.getTime() > transferTime ? 0 : transferTime - now.getTime();
	}

	public void setReleaseTime(long releaseTime) {
		this.transferTime = releaseTime;
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
		
		if (arguments.containsKey(key) == false) {
			System.out.println("ERROR: Attempt to set non-standard command argument '" + key + "'.");
		}
		else {
			arguments.get(key).value = value;
		}
	}

	public Object getArgument(String key) {
		Object value = null;
		if (arguments.containsKey(key)) {
			value = arguments.get(key).value;
		}
		
		return value;
	}

	/**
	 * Validates whether the command has values for all the required arguments.
	 * 
	 * @return A list of the names of the missing arguments
	 */
	public List<String> checkArguments() {
		List<String> missingArguments = new ArrayList<String>();
		
		Iterator<Entry<String, CommandArgument>> it = arguments.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, CommandArgument> entry = it.next();
			if (entry.getValue().mandatory && entry.getValue().value == null) {
				missingArguments.add(entry.getKey());
			}
		}
		
		return missingArguments;
	}

	public long getDelay() {
		Date now = new Date();
		return now.getTime() < transferTime ? now.getTime() - transferTime : 0;
	}

	public long getDeliveryTime() {
		return transferTime;
	}
}
