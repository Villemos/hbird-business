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
package org.hbird.business.commanding.bean;

import java.util.List;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.CamelContext;
import org.apache.camel.Handler;
import org.apache.camel.Headers;
import org.apache.log4j.Logger;
import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.IDataAccess;
import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.core.State;

/**
 * The command releaser validates whether a 'Command' within a 'CommandRequest' can be released.
 * 
 * The release of the command can be constrained by a set of 'lock states', being state parameters
 * (true / false). The set of applicable lock states are defined both as 'lockstates' in the 
 * command request, as well as 'State' parameters with the 'stateOf' attribute set of the name
 * of the command. In case any of these are false at the time of execution, the command wont
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
 */
public class CommandReleaser {

	private static org.apache.log4j.Logger LOG = Logger.getLogger(CommandReleaser.class);

	/** The header flag that indicates whether the command can be released. */
	protected String validityHeaderField = "Valid";

	protected IDataAccess api = ApiFactory.getDataAccessApi("CommandReleaser");
	
	/**
	 * Processor for the scheduling of validation task for a command as well as the
	 * release of the command.
	 * 
	 */
	@Handler
	public void process(@Body CommandRequest command, @Headers Map<String, Object> headers, CamelContext context) {

		if (command.getCommand() == null) {
			LOG.error("Command container '" + command.getName() + "' holds no command.");
			return;
		}
		
		LOG.info("Received command '" + command.getCommand().getName() + "' for release.");

		if (command.getCommand() == null) {
			headers.put(validityHeaderField, false);
			LOG.error("Command container '" + command.getName() + "' marked as invalid; it contains no command.");
			return;
		}

		/** Default we assume the command is valid. */
		headers.put("Valid", true);

		/** Request all states that are lock states of this command. */
		
		List<State> states = api.getState(command.getCommand().getName());
		if (command.getLockStates() != null) { 
			states.addAll(api.getStates(command.getLockStates()));
		}
		
		/** See if any of the states have the value FALSE*/
		for (State state :  states) {
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
}
