package org.hbird.business.command.releaser;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.apache.camel.Headers;
import org.apache.log4j.Logger;
import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.core.StateParameter;

/**
 * The command releaser will validate whether a 'Command' can be send.
 * 
 * The command releaser do not use the 'releaseTime' scheduling for command release. This
 * is expected done prior to reception by this bean. A command received by this class will
 * thus be processed immediately.
 * 
 * A Command can contains any number of 'lock states', being state parameters
 * (true / false). In case any of these are false at the time of execution, the command wont
 * be released, i.e. the route will be stopped. 
 * 
 * A command will affect the satellite in some way. Thats means that the ground system(s) most likely
 * also have to be reconfigured; limits may need to be disabled, changed and at some point enabled again and
 * specific checks may need to be done to validate that the command was send, executed and succeeded. To
 * release tasks, the command releaser uses the route entry point 'begin:Tasks'. This must be available
 * in the route configuration.
 * 
 * The command releaser thereafter parses the command on in the route.
 * 
 * Example of Usage:
 * 
 * <bean id="commandReleaser" class="org.hbird.business.command.releaser.CommandReleaser"/>
 * 
 * <route>
 *   <from uri="activemq:queue:Commands"/>
 *   <to uri="bean:commandReleaser"/>
 *   <IF Header(Command.Release) == FALSE>
 *     <to uri="activemq:queue:FailedCommands"/>
 *   <ELSE>
 *     <to SCHEDULE ALL TASKS in Header(Tasks)>
 *     <to SCHEDULE COMMAND>
 *     <to uri="activemq:queue:ReleasedCommands>
 *   <DONE>
 * </route>
 * 
 */
public class CommandReleaser {

	private static org.apache.log4j.Logger LOG = Logger.getLogger(CommandReleaser.class);

	protected Map<String, StateParameter> stateParameters = new HashMap<String, StateParameter>();

	/**
	 * Processor for the scheduling of validation task for a command as well as the
	 * release of the command.
	 * 
	 */
	@Handler
	public void process(@Body CommandRequest command, @Headers Map<String, Object> headers) {

		LOG.info("Received command '" + command.getName() + "' for release.");

		if (command.getCommand() == null) {
			headers.put("Valid", false);
			LOG.error("Command container '" + command.getName() + "' marked as invalid; it contains no command.");
			return;
		}

		/** Default we assume the command is valid. */
		headers.put("Valid", true);

		/** Check states. */
		synchronized (stateParameters) {
			/** TODO The state parameters that point to the command should also be checked. */
			
			for (String state : command.getLockStates()) {
				if (stateParameters.containsKey(state) == false || stateParameters.get(state).getStateValue() == false) {
					LOG.error("Command '" + command.getCommand().getName() + "' marked as invalid.");
					headers.put("Valid", false);	
				}				
			}
		}

		LOG.info("Forwarding command with validity='" + headers.get("Valid") + "'.");
	}


	/**
	 * Method to receive a state parameter.
	 * 
	 * @param state
	 */
	public void state(@Body StateParameter state) {
		synchronized (stateParameters) {
			stateParameters.put(state.getName(), state);
		}
	}
}
