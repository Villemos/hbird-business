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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
public class Command extends CommandBase {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8080345987907627652L;

    /** List of arguments. The value is embedded in the header of the exchange. */
    protected Map<String, CommandArgument> arguments = null;

    /**
     * Constructor to create a command with the standard set of arguments. The arguments
     * will be created using the method {@link #getArgumentDefinitions()}
     * 
     * @param issuedBy The name of the entity issuing this command
     * @param destination The destination of the command
     * @param name The name of the command
     * @param description The description of the command
     */
    public Command(String ID, String name) {
        super(ID, name);
        this.arguments = createArgumentMap(getArgumentDefinitions(new ArrayList<CommandArgument>()));
    }

    /**
     * Creates {@link Map} of {@link CommandArgument}s.
     * 
     * Method {@link #getArgumentDefinitions()} is used to fill the {@link Map}.
     * 
     * @return {@link Map} of {@link CommandArgument}s
     * @see #getArgumentDefinitions()
     */
    protected Map<String, CommandArgument> createArgumentMap(List<CommandArgument> args) {

        Map<String, CommandArgument> map = new HashMap<String, CommandArgument>(args.size());

        for (CommandArgument arg : args) {
            map.put(arg.getName(), arg);
        }
        return map;
    }

    /**
     * Adds {@link CommandArgument}s to the {@link Command} argument list.
     * 
     * Result of this method is used to create {@link Command}'s argument map.
     * 
     * Override this method to add custom {@link CommandArgument}s to the {@link Command} object.
     * 
     * @param args {@link List} of {@link CommandArgument}s
     * @return List of {@link CommandArgument}s for the {@link Command}
     * @see #createArgumentMap()
     */
    protected List<CommandArgument> getArgumentDefinitions(List<CommandArgument> args) {
        return args;
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

    // public long getReleaseDelay() {
    // Date now = new Date();
    // return now.getTime() > transferTime ? 0 : transferTime - now.getTime();
    // }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        builder.append("ID", getInstanceID());
        builder.append("name", name);
        builder.append("issuedBy", issuedBy);
        builder.append("destination", destination);
        return builder.build();
    }

    /**
     * Sets value of the {@link CommandArgument}.
     * 
     * {@link CommandArgument} is selected by the key.
     * If {@link CommandArgument} for the key is not found {@link IllegalArgumentException} is thrown.
     * In case class of the value is not assignable for {@link CommandArgument}'s type {@link IllegalArgumentException}
     * is thrown
     * 
     * @param key {@link CommandArgument} identifier
     * @param value vale for the {@link CommandArgument}
     * @throws IllegalArgumentException if {@link CommandArgument} is not found for the key or value type is not
     *             accepted
     */
    public void setArgumentValue(String key, Object value) throws IllegalArgumentException {
        /** Ignore null values */
        if (value != null) {

            if (!arguments.containsKey(key)) {
                throw new IllegalArgumentException("Command " + getClass().getSimpleName() + "{name=" + name + "} doesn't have argument with name \"" + key
                        + "\"");
            }
            CommandArgument arg = arguments.get(key);
            Class<?> type = arg.getType();
            if (type.isAssignableFrom(value.getClass())) {
                arg.setValue(value);
            }
            else {
                throw new IllegalArgumentException("CommandArgument{command name=" + name + ", argument name=" + key + ", type=" + type.getName()
                        + "} is not accepting values with type " + value.getClass().getName());
            }
        }
    }

    public boolean hasArgument(String key) {
        return arguments.containsKey(key);
    }

    public boolean hasArgumentValue(String key) {
        return hasArgument(key) ? arguments.get(key).getValue() != null : false;
    }

    /**
     * Returns {@link CommandArgument} value for given key.
     * 
     * Return type of the method is defined by the parameter clazz.
     * Return null if {@link CommandArgument} is not found for the key.
     * Throws {@link IllegalArgumentException} if value type is not assignable for the clazz.
     * 
     * @param key {@link CommandArgument} identifier
     * @param clazz return type
     * @return {@link CommandArgument} value in type clazz
     * @throws IllegalArgumentException if value type is not assignable for the clazz
     */
    @SuppressWarnings("unchecked")
    public <T> T getArgumentValue(String key, Class<T> clazz) {
        T value = null;
        if (arguments.containsKey(key)) {
            CommandArgument arg = arguments.get(key);
            Class<?> type = arg.getType();
            if (clazz.isAssignableFrom(type)) {
                value = (T) arg.getValue();
            }
            else {
                throw new IllegalArgumentException("CommandArgument{commandname=" + name + ", argumentname=" + key + ", type=" + type.getName()
                        + "} is not assignable to argument type " + clazz.getName());
            }
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
        List<String> missingArguments = new ArrayList<String>(arguments.size());

        Iterator<Entry<String, CommandArgument>> it = arguments.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, CommandArgument> entry = it.next();
            if (entry.getValue().getMandatory() && entry.getValue().getValue() == null) {
                missingArguments.add(entry.getKey());
            }
        }

        return missingArguments;
    }

    public void setArgumentList(List<CommandArgument> arguments) {
        for (CommandArgument argument : arguments) {
            this.arguments.put(argument.getName(), argument);
        }
    }

    public List<CommandArgument> getArgumentList() {
        return new ArrayList<CommandArgument>(arguments.values());
    }

    public void addArgument(CommandArgument argument) {
        this.arguments.put(argument.getName(), argument);
    }

}
