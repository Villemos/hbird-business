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
package org.hbird.exchange.commandrelease;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.core.IScheduled;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.tasking.Task;

public class CommandRequest extends Named implements IScheduled {

	private static final long serialVersionUID = 5923007317348738778L;

	/** The time at which the command should be released. Releasing the command means first verifying the lock 
	 * states, then scheduling the tasks and finally releasing the command itself. A value of 0 indicates immediate. */
	protected long releaseTime = 0;	

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
	 * @param transferTime The time at which the command should be released by the MCS for transfer to the satellite.
	 * @param executionTime The time at which the command should be executed onboard.
	 */
	public CommandRequest(String issuedBy, String name, String type, String description, List<String> lockStates, List<Task> tasks, Command command) {
		super(issuedBy, "CommandContainer" + name, type, "Command container holding lock states and tasks for the command '" + name + "'.");
		this.command = command;
		this.lockStates = lockStates;
		this.tasks = tasks;
	}
	

	public CommandRequest(String issuedBy, String name, String type, String description, List<String> lockStates, List<Task> tasks, Command command, long releaseTime) {
		super(issuedBy, "CommandContainer" + name, type, "Command container holding lock states and tasks for the command '" + name + "'.");
		this.command = command;
		this.lockStates = lockStates;
		this.tasks = tasks;
		this.releaseTime = releaseTime;
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

	public long getDelay() {
		Date now = new Date();
		return now.getTime() < releaseTime ? now.getTime() - releaseTime : 0;
	}

	public long getDeliveryTime() {
		return releaseTime;
	}	
}
