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

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Named;
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

	public void publish(Named object);
	
	public void publishParameter(String name, String type, String description, Number value, String unit);
	
	public void publishState(String name, String description, String isStateOf, Boolean state);
	
	public void publishLocation(String name, String description, Double lon, Double lat, Double ele, Double frequency);
	
	public void publishSatellite(String name, String description);
	
	public void publishLabel(String name, String type, String description, String value);
	
	public void publishBinary(String name, String type, String description, byte[] rawdata);
	
	public void publishCommand(String name, String description, Command command);
	public void publishCommand(String name, String description, Command command, List<String> lockStates, List<Task> tasks);
	
	/**
	 * Method to create and publish a piece of metadata associated to a Named object.
	 * 
	 * @param subject The subject of this metadata, i.e. the Named object being described.
	 * @param key The key of the metadata.
	 * @param metadata The value of the metadata
	 */
	public void publichMetadata(Named subject, String key, String metadata);

	public void publishTleParameters(String satellite, String tle1, String tle2);
}
