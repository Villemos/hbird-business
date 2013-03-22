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
import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.core.Binary;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.core.D3Vector;
import org.hbird.exchange.core.Label;
import org.hbird.exchange.core.Metadata;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.dataaccess.CommitRequest;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.groundstation.Modem;
import org.hbird.exchange.groundstation.RadioDevice;
import org.hbird.exchange.groundstation.Rotator;
import org.hbird.exchange.groundstation.SoftwareDefinedRadio;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.hbird.exchange.tasking.Task;

public class Publish extends HbirdApi implements IPublish {

	public Publish(String issuedBy) {
		super(issuedBy);
	}

	public Named publish(Named object) {
		object.setIssuedBy(issuedBy);
		template.sendBody(inject, object);
		return object;
	}

	public Parameter publishParameter(String name, String type, String description, Number value, String unit) {
		return (Parameter) publish(new Parameter(issuedBy, name, type, description, value, unit));
	}

	public Parameter publishParameter(String name, String type, String description, Number value, String unit, long timestamp) {
		return (Parameter) publish(new Parameter(issuedBy, name, type, description, value, unit, timestamp));
	}

	
	public State publishState(String name, String type, String description, String isStateOf, Boolean state) {
		return (State) publish(new State(issuedBy, name, type, description, isStateOf, state));
	}

	public State publishState(String name, String type, String description, String isStateOf, Boolean state, long timestamp) {
		return (State) publish(new State(issuedBy, name, type, description, isStateOf, state, timestamp));
	}

	public GroundStation publishGroundStation(String name, D3Vector geoLocation, Rotator rotator, RadioDevice radioChannel, Modem modems, SoftwareDefinedRadio softwareDefinedRadios) {
		return (GroundStation) publish(new GroundStation(name, geoLocation, rotator, radioChannel, modems, softwareDefinedRadios));
	}

	public Satellite publishSatellite(String name, String type, String description) {
		return (Satellite) publish(new Satellite(name, description));
	}

	public void publishMeasuredOrbitalState(String name, String description, long timestamp, long generationTime, String satellite, double px, double py, double pz, double vx, double vy, double vz, double mx, double my, double mz, String derivedFromName, long derivedFromTimestamp, String derivedFromType) {
		// TODO
	}

	public Label publishLabel(String name, String type, String description, String value) {
		return (Label) publish(new Label(issuedBy, name, type, description, value));
	}

	public Binary publishBinary(String name, String type, String description, byte[] rawdata) {
		return (Binary) publish(new Binary(issuedBy, name, type, description, rawdata));
	}

	public Command publishCommand(String name, String description, List<CommandArgument> arguments) {
		return (Command) publish(new Command(issuedBy, name, "Command", description, arguments));
	}

	public Command publishCommandTemplate(String name, String description, List<CommandArgument> arguments) {
		return (Command) publish(new Command(issuedBy, name, "CommandTemplate", description, arguments));
	}

	public CommandRequest publishCommandRequest(String name, String description, Command command) {
		return (CommandRequest) publish(new CommandRequest(issuedBy, name, "Command", description, null, null, command));
	}

	public CommandRequest publishCommandRequest(String name, String description, Command command, List<String> lockStates, List<Task> tasks) {
		return (CommandRequest) publish(new CommandRequest(issuedBy, name, "CommandRequest", description, lockStates, tasks, command));
	}

	public void commit() {
		template.sendBody(inject, new CommitRequest(issuedBy));
	}
		
	/* (non-Javadoc)
	 * @see org.hbird.business.api.IPublish#publichMetadata(org.hbird.exchange.core.Named, java.lang.String, java.lang.String)
	 */
	public Metadata publishMetadata(Named subject, String key, String value) {
		Map<String, Object> metadata = new HashMap<String, Object>();
		metadata.put(key, value);
		
		return (Metadata) publish(new Metadata(issuedBy, "Metadata", subject, metadata));
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IPublish#publishTleParameters(java.lang.String, java.lang.String, java.lang.String)
	 */
	public TleOrbitalParameters publishTleParameters(String satellite, String type, String tle1, String tle2) {
		TleOrbitalParameters obj = new TleOrbitalParameters(issuedBy, type, satellite, tle1, tle2); 
		template.sendBody(inject, obj);
		return obj;
	}
}
