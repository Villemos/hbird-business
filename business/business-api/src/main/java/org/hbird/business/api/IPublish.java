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
package org.hbird.business.api;

import java.util.List;

import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.core.Binary;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.core.Label;
import org.hbird.exchange.core.Metadata;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.navigation.D3Vector;
import org.hbird.exchange.navigation.GroundStation;
import org.hbird.exchange.navigation.Location;
import org.hbird.exchange.navigation.RadioChannel;
import org.hbird.exchange.navigation.RotatorProperties;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.hbird.exchange.tasking.Task;

/**
 * API Interface for publishing data to the system.
 * 
 * The API should be used by any element which needs to publish data that shall be distributed through
 * the hummingbird system. The API will create the data object and publish it to the underlying 
 * protocol, typically being activemq. The further distribution of the object depends on the
 * assembly of the system.
 * 
 * @author Gert Villemos
 *
 */
public interface IPublish {

	/**
	 * Method to force a commit of all data. Any process should flush buffers and cashes.
	 */
	public void commit();

	public Named publish(Named object);

	public Parameter publishParameter(String name, String type, String description, Number value, String unit);
	public Parameter publishParameter(String name, String type, String description, Number value, String unit, long timestamp);
	
	public State publishState(String name, String type, String description, String isStateOf, Boolean state);
	public State publishState(String name, String type, String description, String isStateOf, Boolean state, long timestamp);
	
	public Location publishLocation(String name, String type, String description, Double lon, Double lat, Double ele, Double frequency);

	public GroundStation publishGroundStation(String name, String type, String description, D3Vector geoLocation, RotatorProperties rotatorProperties, List<RadioChannel> radioChannels);
	
	public Satellite publishSatellite(String name, String type, String description);
	
	public Label publishLabel(String name, String type, String description, String value);

	public Binary publishBinary(String name, String type, String description, byte[] rawdata);

	public Command publishCommand(String name, String description, List<CommandArgument> arguments);
	public Command publishCommandTemplate(String name, String description, List<CommandArgument> arguments);
	public CommandRequest publishCommandRequest(String name, String description, Command command);
	public CommandRequest publishCommandRequest(String name, String description, Command command, List<String> lockStates, List<Task> tasks);
	
	/**
	 * Method to create and publish a piece of metadata associated to a Named object.
	 * 
	 * @param subject The subject of this metadata, i.e. the Named object being described.
	 * @param key The key of the metadata.
	 * @param metadata The value of the metadata
	 */
	public Metadata publishMetadata(Named subject, String key, String metadata);

	public TleOrbitalParameters publishTleParameters(String satellite, String type, String tle1, String tle2);
}
