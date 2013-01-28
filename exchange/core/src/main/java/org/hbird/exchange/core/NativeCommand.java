package org.hbird.exchange.core;

/**
 * Class for native commands to execute on target component.
 *
 * For example antenna rotator command to  rotate antenna would look something like:
 * <p>
 * <code>
 * NativeCommand rotateAntenna = new NativeCommand("MCS", "Rotator Identifier", "R 132 145");
 * </code>
 * </p>
 *
 * Where String "R 132 145" is native command for rotator controlling software.
 */
public class NativeCommand extends Command {

    /** Command argument for command to execute. */
    protected static final String ARGUMENT_COMMAND_TO_EXECUTE = "commandToExecute";

    /** Generated serial version UID. */
    private static final long serialVersionUID = 5909105582953442495L;


    /* Command arguments. */
    {
        arguments.put(ARGUMENT_COMMAND_TO_EXECUTE, new CommandArgument(ARGUMENT_COMMAND_TO_EXECUTE, "Command to execute in the destination", "String", "", null, true));
    }

    /**
     * Creates new {@link NativeCommand}.
     *
     * @param issuedBy issuer of the command
     * @param destination destination of the command
     * @param command command to execute on destination
     */
    public NativeCommand(String issuedBy, String destination, String command) {
        super(issuedBy, destination, "Native Command", "Native Command to execute in the destination");
        setCommandToExecute(command);

    }

    /**
     * Sets the command to execute.
     *
     * @param command command to execute on destination
     */
    public void setCommandToExecute(String command) {
        arguments.get(ARGUMENT_COMMAND_TO_EXECUTE).value = command;
    }

    /**
     * Returns the command to execute on destination.
     *
     * @return command to execute
     */
    public String getCommandToExecute() {
        return (String) arguments.get(ARGUMENT_COMMAND_TO_EXECUTE).value;
    }
}
