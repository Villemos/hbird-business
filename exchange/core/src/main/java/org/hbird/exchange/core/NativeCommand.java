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
import java.util.List;

/**
 * Class for native commands to execute on target component.
 * 
 * For example antenna rotator command to rotate antenna would look something like:
 * <p>
 * <code>
 * NativeCommand rotateAntenna = new NativeCommand("MCS", "Rotator Identifier", "R 132 145");
 * </code>
 * </p>
 * 
 * Where String "R 132 145" is native command for rotator controlling software.
 */
public class NativeCommand extends Command {

    public static final String DESCRIPTION = "Command to execute in the destination";

    /** Command argument for command to execute. */
    protected static final String ARGUMENT_COMMAND_TO_EXECUTE = "commandToExecute";

    /** Generated serial version UID. */
    private static final long serialVersionUID = 5909105582953442495L;

    /**
     * Creates new {@link NativeCommand}.
     * 
     * @param issuedBy issuer of the command
     * @param destination destination of the command
     * @param command command to execute on destination
     */
    public NativeCommand(String issuedBy, String destination, String command) {
        super(issuedBy, destination, NativeCommand.class.getSimpleName(), DESCRIPTION);
        setCommandToExecute(command);
    }

    /**
     * @see org.hbird.exchange.core.Command#getArgumentDefinitions()
     */
    @Override
    protected List<CommandArgument> getArgumentDefinitions() {
        List<CommandArgument> args = new ArrayList<CommandArgument>(1);
        args.add(new CommandArgument(ARGUMENT_COMMAND_TO_EXECUTE, DESCRIPTION, "String", "", null, true));
        return args;
    }

    /**
     * Sets the command to execute.
     * 
     * @param command command to execute on destination
     */
    public void setCommandToExecute(String command) {
        setArgumentValue(ARGUMENT_COMMAND_TO_EXECUTE, command);
    }

    /**
     * Returns the command to execute on destination.
     * 
     * @return command to execute
     */
    public String getCommandToExecute() {
        return getArgumentValue(ARGUMENT_COMMAND_TO_EXECUTE, String.class);
    }
}
