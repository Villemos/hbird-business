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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @TITLE Command Definition
 *        This object represents a command.
 * 
 *        The lifecycle of a command is
 *        1) Creation.
 *        2) Release to the command queue. The command will wait in the queue until it is ready for release.
 *        3) Validation of the lock states of the command. If any of the lock states is 'false', then the command will
 *        not be released.
 *        4) Scheduling of all tasks needed to validate the command.
 *        5) Release, i.e. transfer to the system responsible for transfering the command to the satellite.
 * 
 *        h2. Arguments
 * 
 *        A command can have zero or more arguments. An argument is simply a name / value pair.
 * 
 *        h2. Lock States
 * 
 *        A lock state is a parameter state, which must be true for the command to be released. This can be used to
 *        define
 *        1) Dependencies on limits (only release a command if a (set of) parameters is within a given limit).
 *        2) Define dependencies on other commands (interlock). The failure in a command should result in a command
 *        validation state parameter being set to false.
 *        3) Definition of general system locks (master mode) where no comamnds can be issued.
 * 
 *        h2. Tasks
 * 
 *        The validation of the command is done through a number of validation tasks. Each task perform a specific
 *        part of the validation at a given delta time compared to the release of the command. This can be used to
 *        1) Disable limit checking when it is known that it will change.
 *        2) Change the expected limit.
 *        3) Enable the limit again after the command propagation period has expired.
 *        4) Perform specific parameter checks at given points in time.
 * 
 * @CATEGORY Information Type
 * @END
 */
public class Command extends Named implements IScheduled {

    private static final long serialVersionUID = 1L;

    /** The name of the component to which this command is destined. */
    protected String destination;

    /**
     * The time at which this command should be transfered to its destination. A value of
     * 0 indicates immediate.
     */
    protected long transferTime = 0;

    /**
     * The time at which the command should be executed at the destination. A value of 0 indicates
     * immediate.
     */
    protected long executionTime = 0;

    /** List of arguments. The value is embedded in the header of the exchange. */
    private final Map<String, CommandArgument> arguments;

    public Command(String issuedBy, String destination, String name, String description) {
        super(issuedBy, name, Command.class.getSimpleName(), description);
        this.destination = destination;
        this.arguments = createArgumentMap();
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
    public Command(String issuedBy, String destination, String name, String description, long releaseTime, long executionTime) {
        this(issuedBy, destination, name, description);
        this.transferTime = releaseTime;
        this.executionTime = executionTime;
    }

    /**
     * Creates {@link Map} of {@link CommandArgument}s.
     * 
     * Method {@link #getArgumentDefinitions()} is used to fill the {@link Map}.
     * 
     * @return {@link Map} of {@link CommandArgument}s
     * @see #getArgumentDefinitions()
     */
    protected Map<String, CommandArgument> createArgumentMap() {
        List<CommandArgument> args = getArgumentDefinitions();
        Map<String, CommandArgument> map = new HashMap<String, CommandArgument>(args.size());
        for (CommandArgument arg : args) {
            map.put(arg.getName(), arg);
        }
        return map;
    }

    /**
     * Returns list of {@link CommandArgument}s for the {@link Command}.
     * 
     * Override this method to add custom {@link CommandArgument}s to the {@link Command} object.
     * 
     * @return List of {@link CommandArgument}s for the {@link Command}
     * @see #createArgumentMap()
     */
    protected List<CommandArgument> getArgumentDefinitions() {
        return Collections.emptyList();
    }

    public Map<String, CommandArgument> getArguments() {
        return arguments;
    }

    /**
     * Merge {@link Map} of {@link CommandArgument}s with existing {@link CommandArgument}s.
     * 
     * @param arguments {@link Map} of arguments to merge
     */
    public void addArguments(Map<String, CommandArgument> arguments) {
        this.arguments.putAll(arguments);
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

    @Override
    public String prettyPrint() {
        return String.format("%s{name=%s, timestamp=%s, issuedBy=%s, destination=%s}", getClass().getSimpleName(), name, timestamp, issuedBy, destination);
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setArgumentValue(String key, Object value) throws IllegalArgumentException {
        if (!arguments.containsKey(key)) {
            throw new IllegalArgumentException("Command " + getClass().getSimpleName() + "{name=" + name + "} doesn't have argument with name \"" + key + "\"");
        }
        else {
            arguments.get(key).setValue(value);
        }
    }

    public boolean hasArgument(String key) {
        return arguments.containsKey(key);
    }

    public boolean hasArgumentValue(String key) {
        return hasArgument(key) ? arguments.get(key).getValue() != null : false;
    }

    @SuppressWarnings("unchecked")
    public <T> T getArgumentValue(String key, Class<T> clazz) {
        T value = null;
        if (arguments.containsKey(key)) {
            value = (T) arguments.get(key).getValue();
        }
        return value;
    }

    @Deprecated
    public Object getArgumentValue(String key) {
        return arguments.containsKey(key) ? arguments.get(key).getValue() : null;
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
            if (entry.getValue().getMandatory() && entry.getValue().getValue() == null) {
                missingArguments.add(entry.getKey());
            }
        }

        return missingArguments;
    }

    // TODO - 01.03.2013, kimmell - check this!
    @Override
    public long getDelay() {
        long now = System.currentTimeMillis();
        return now < transferTime ? now - transferTime : 0;
    }

    @Override
    public long getDeliveryTime() {
        return transferTime;
    }
}
