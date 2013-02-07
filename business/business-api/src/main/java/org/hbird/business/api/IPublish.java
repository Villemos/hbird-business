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

public interface IPublish {

	public void publish(Named object);
	
	public void publishParameter(String name, String type, String description, Number value, String unit);
	
	public void publishState(String name, String description, String isStateOf, Boolean state);
	
	public void publishLocation(String name, String description, Double lon, Double lat, Double ele, Double frequency);
	
	public void publishLabel(String name, String type, String description, String value);
	
	public void publishBinary(String name, String type, String description, byte[] rawdata);
	
	public void publishCommand(String name, String description, Command command);
	public void publishCommand(String name, String description, Command command, List<String> lockStates, List<Task> tasks);
}
