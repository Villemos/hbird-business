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
import java.util.List;
import java.util.Map;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.interfaces.IScheduled;
import org.hbird.exchange.interfaces.ITransferable;
import org.hbird.exchange.tasking.Task;

public class CommandRequest extends EntityInstance implements ITransferable {

    private static final long serialVersionUID = 5432372329519676996L;

    /**
     * The time at which the command should be released. Releasing the command means first verifying the lock
     * states, then scheduling the tasks and finally releasing the command itself. A value of 0 indicates immediate.
     */
    protected long transferTime = IScheduled.IMMEDIATE;

    /**
     * Destination of the {@link CommandRequest}.
     */
    protected String destination;

    /** The raw command. */
    protected Command command = null;

    /** Map keyed on the name of the lock state that is 'false' and holding as value the reason. */
    protected List<String> lockStates = new ArrayList<String>();

    /** List of tasks to be performed after the release of the command. */
    protected List<Task> tasks = new ArrayList<Task>();

    public CommandRequest(String ID, String name) {
        super(ID, name);
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

    /**
     * @see org.hbird.exchange.interfaces.ITransferable#getDestination()
     */
    @Override
    public String getDestination() {
        return destination;
    }

    /**
     * @see org.hbird.exchange.interfaces.ITransferable#getTransferTime()
     */
    @Override
    public long getTransferTime() {
        return transferTime;
    }

    /**
     * @param transferTime the transferTime to set
     */
    public void setTransferTime(long transferTime) {
        this.transferTime = transferTime;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

	/**
	 * @param command the command to set
	 */
	public void setCommand(Command command) {
		this.command = command;
	}

	/**
	 * @param lockStates the lockStates to set
	 */
	public void setLockStates(List<String> lockStates) {
		this.lockStates = lockStates;
	}

	/**
	 * @param tasks the tasks to set
	 */
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
    
    
}
