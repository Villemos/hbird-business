package org.hbird.business.command.releaser;

import java.util.List;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.Headers;
import org.apache.camel.impl.DefaultExchange;
import org.apache.log4j.Logger;
import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.core.State;
import org.hbird.exchange.dataaccess.IDataAccess;
import org.hbird.exchange.dataaccess.StateRequest;

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

	protected String validityHeaderField = "Valid";

	protected IDataAccess store = null;

	/**
	 * Processor for the scheduling of validation task for a command as well as the
	 * release of the command.
	 * 
	 */
	@Handler
	public void process(@Body CommandRequest command, @Headers Map<String, Object> headers, CamelContext context) {

		LOG.info("Received command '" + command.getCommand().getName() + "' for release.");

		if (command.getCommand() == null) {
			headers.put(validityHeaderField, false);
			LOG.error("Command container '" + command.getName() + "' marked as invalid; it contains no command.");
			return;
		}

		/** Default we assume the command is valid. */
		headers.put("Valid", true);

		StateRequest request = new StateRequest("CommandReleaser", command.getCommand().getName(), command.getLockStates());

		Exchange arg1 = new DefaultExchange(context);
		arg1.getIn().setBody(request);
		context.createProducerTemplate().send("direct:statestore", arg1);
		List<State> states = (List<State>) arg1.getOut().getBody();

		// for (State state : (List<State>) store.retrieveState(request)) {
		for (State state : states) {
			if (state.getValue() == false) {
				LOG.error("Validation state '" + state.getName() + "' is FALSE. Command '" + command.getCommand().getName() + "' release state marked as invalid.");
				headers.put(validityHeaderField, false);	
			}				
			else {
				LOG.debug("State '" + state.getName() + "' is TRUE.");
			}
		}

		LOG.info("Forwarding command with validity='" + headers.get(validityHeaderField) + "'.");
	}

	public State reportState(@Body CommandRequest request, @Headers Map<String, Object> header) {
		return new State("CommandReleaser", "Command Release Verification", "The state of the command release.", request.getCommand().getName(), (Boolean) header.get(validityHeaderField));
	}

	public IDataAccess getStore() {
		return store;
	}

	public void setStore(IDataAccess store) {
		this.store = store;
	}
}
