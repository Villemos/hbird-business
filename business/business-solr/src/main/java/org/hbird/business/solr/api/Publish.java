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
package org.hbird.business.solr.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hbird.business.api.HbirdApi;
import org.hbird.business.api.IPublish;
import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.core.Binary;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Label;
import org.hbird.exchange.core.Metadata;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.navigation.Location;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.hbird.exchange.tasking.Task;

public class Publish extends HbirdApi implements IPublish {

	public Publish(String issuedBy) {
		super(issuedBy);
	}
	
	public void publish(Named object) {
		object.setIssuedBy(issuedBy);
		template.sendBody(inject, object);
	}
	
	public void publishParameter(String name, String type, String description, Number value, String unit) {
		template.sendBody(inject, new Parameter(issuedBy, name, type, description, value, unit));		
	}

	public void publishState(String name, String description, String isStateOf, Boolean state) {
		template.sendBody(inject, new State(issuedBy, name, description, isStateOf, state));
	}

	public void publishLocation(String name, String description, Double lon, Double lat, Double ele, Double frequency) {
		template.sendBody(inject, new Location(issuedBy, name, description, lon, lat, ele, frequency));
	}

	public void publishSatellite(String name, String description) {
		template.sendBody(inject, new Satellite(issuedBy, name, description));
	}

	public void publishMeasuredOrbitalState(String name, String description, long timestamp, long generationTime, String satellite, double px, double py, double pz, double vx, double vy, double vz, double mx, double my, double mz, String derivedFromName, long derivedFromTimestamp, String derivedFromType) {
		// TODO
	}
		
	public void publishLabel(String name, String type, String description, String value) {
		template.sendBody(inject, new Label(issuedBy, name, type, description, value));
	}

	public void publishBinary(String name, String type, String description, byte[] rawdata) {
		template.sendBody(inject, new Binary(issuedBy, name, type, description, rawdata));
	}

	public void publishCommand(String name, String description, Command command) {
		template.sendBody(inject, new CommandRequest(issuedBy, name, description, null, null, command));
	}

	public void publishCommand(String name, String description, Command command, List<String> lockStates, List<Task> tasks) {
		template.sendBody(inject, new CommandRequest(issuedBy, name, description, lockStates, tasks, command));
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IPublish#publichMetadata(org.hbird.exchange.core.Named, java.lang.String, java.lang.String)
	 */
	public void publichMetadata(Named subject, String key, String value) {
		Map<String, Object> metadata = new HashMap<String, Object>();
		metadata.put(key, value);
		
		template.sendBody(inject, new Metadata(issuedBy, "Metadata", subject, metadata));
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IPublish#publishTleParameters(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void publishTleParameters(String satellite, String tle1, String tle2) {
		template.sendBody(inject, new TleOrbitalParameters(issuedBy, satellite, tle1, tle2));
	}
}
