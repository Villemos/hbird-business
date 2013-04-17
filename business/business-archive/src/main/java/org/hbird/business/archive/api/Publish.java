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
package org.hbird.business.archive.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hbird.business.api.HbirdApi;
import org.hbird.business.api.IPublish;
import org.hbird.business.archive.ArchiveComponent;
import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.core.Binary;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.core.Label;
import org.hbird.exchange.core.Metadata;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.dataaccess.CommitRequest;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.hbird.exchange.tasking.Task;

/**
 * API for publishing different kinds of objects to the system.
 * 
 * @author Gert Villemos
 *
 */
public class Publish extends HbirdApi implements IPublish {

	/**
	 * Constructor. 
	 * 
	 * @param issuedBy The name/ID of the component that is using the API to send requests.
	 */
	public Publish(String issuedBy) {
		super(issuedBy, ArchiveComponent.ARCHIVE_NAME);
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IPublish#publish(org.hbird.exchange.core.Named)
	 */
	@Override
	public Named publish(Named object) {
		object.setIssuedBy(issuedBy);
		if (object instanceof Command && ((Command) object).getDestination() == null) {
			((Command) object).setDestination(destination);
		}

		template.sendBody(inject, object);
		return object;
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IPublish#publishParameter(java.lang.String, java.lang.String, java.lang.Number, java.lang.String)
	 */
	@Override
	public Parameter publishParameter(String name, String description, Number value, String unit) {
		return (Parameter) publish(new Parameter(issuedBy, name, description, value, unit));
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IPublish#publishParameter(java.lang.String, java.lang.String, java.lang.Number, java.lang.String, long)
	 */
	@Override
	public Parameter publishParameter(String name, String description, Number value, String unit, long timestamp) {
		return (Parameter) publish(new Parameter(issuedBy, name, description, value, unit, timestamp));
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IPublish#publishState(java.lang.String, java.lang.String, java.lang.String, java.lang.Boolean)
	 */
	@Override
	public State publishState(String name, String description, String isStateOf, Boolean state) {
		return (State) publish(new State(issuedBy, name, description, isStateOf, state));
	}

	public State publishState(String name, String description, String isStateOf, Boolean state, long timestamp) {
		return (State) publish(new State(issuedBy, name, description, isStateOf, state, timestamp));
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IPublish#publishLabel(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Label publishLabel(String name, String description, String value) {
		return (Label) publish(new Label(issuedBy, name, description, value));
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IPublish#publishBinary(java.lang.String, java.lang.String, byte[])
	 */
	@Override
	public Binary publishBinary(String name, String description, byte[] rawdata) {
		return (Binary) publish(new Binary(issuedBy, name, description, rawdata));
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IPublish#publishCommand(java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public Command publishCommand(String name, String description, List<CommandArgument> arguments) {
		return (Command) publish(new Command(issuedBy, name, "Command", description, arguments));
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IPublish#publishCommandTemplate(java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public Command publishCommandTemplate(String name, String description, List<CommandArgument> arguments) {
		return (Command) publish(new Command(issuedBy, name, "CommandTemplate", description, arguments));
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IPublish#publishCommandRequest(java.lang.String, java.lang.String, org.hbird.exchange.core.Command)
	 */
	@Override
	public CommandRequest publishCommandRequest(String name, String description, Command command) {
		return (CommandRequest) publish(new CommandRequest(issuedBy, name, description, null, null, command));
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IPublish#publishCommandRequest(java.lang.String, java.lang.String, org.hbird.exchange.core.Command, java.util.List, java.util.List)
	 */
	@Override
	public CommandRequest publishCommandRequest(String name, String description, Command command, List<String> lockStates, List<Task> tasks) {
		return (CommandRequest) publish(new CommandRequest(issuedBy, name, description, lockStates, tasks, command));
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IPublish#commit()
	 */
	@Override
	public void commit() {
		template.sendBody(inject, new CommitRequest(issuedBy));
	}
		
	/* (non-Javadoc)
	 * @see org.hbird.business.api.IPublish#publichMetadata(org.hbird.exchange.core.Named, java.lang.String, java.lang.String)
	 */
	@Override
	public Metadata publishMetadata(Named subject, String key, String value) {
		Map<String, Object> metadata = new HashMap<String, Object>();
		metadata.put(key, value);
		
		return (Metadata) publish(new Metadata(issuedBy, key, subject, metadata));
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IPublish#publishTleParameters(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public TleOrbitalParameters publishTleParameters(String name, String satellite, String tle1, String tle2) {
		TleOrbitalParameters obj = new TleOrbitalParameters(issuedBy, name, satellite, tle1, tle2); 
		template.sendBody(inject, obj);
		return obj;
	}
}
