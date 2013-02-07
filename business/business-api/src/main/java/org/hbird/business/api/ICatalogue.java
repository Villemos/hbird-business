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
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.navigation.Location;
import org.hbird.exchange.navigation.Satellite;

public interface ICatalogue {

	/**
	 * Method to return a 'catalogue' (a list) of all parameters in the archive.
	 * The list contains the last sample of each parameter. 
	 *  
	 * @return List containing one sample of each parameter in the archive.
	 */
	public List<Parameter> getParameterMetadata();
	
	
	/**
	 * Method to return a 'catalogue' (a list) of all states in the archive.
	 * The list contains the last sample received value of each state. 
	 * 
	 * @return List containing one sample of each parameter in the archive.
	 */
	public List<State> getStateMetadata();
	
	
	/**
	 * TODO
	 * 
	 * @return
	 */
	public List<Command> getCommandMetadata();

	/**
	 * TODO
	 * 
	 * @return
	 */
	public List<Location> getLocationMetadata();
	
	/**
	 * TODO
	 * 
	 * @return
	 */
	public List<Satellite> getSatelliteMetadata();
}
