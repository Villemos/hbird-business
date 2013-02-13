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
package org.hbird.exchange.dataaccess;

import org.hbird.exchange.core.CommandArgument;

public class OrbitalStateRequest extends DataRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3599199372427419649L;
 
	{
		arguments.put("satellite", new CommandArgument("satellite", "The name of the satellite for which to retrieve the states.", "String", "", null, true));
	}
	
	public OrbitalStateRequest(String satellite) {
		super("", "Archive", "OrbitalStateRequest", "A request for the latest orbital state of a satellite");
		
		setSatellite(satellite);
		addArgument("sort", "timestamp");
		addArgument("sortorder", "DESC");
		addArgument("rows", 1);
		addArgument("type", "OrbitalState");
		setIsInitialization(true);
	}
	
	public OrbitalStateRequest(String satellite, Long from, Long to) {
		super("", "", "OrbitalStateRequest", "A request for the latest orbital state of a satellite");
		
		addArgument("type", "OrbitalState");
		setSatellite(satellite);
		setFromTime(from);
		setToTime(to);
	}

	public void setSatellite(String satellite) {
		addArgument("satellite", satellite);
	}
	
	public void setFromTime(Long from) {
		addArgument("from", from);
	}
	
	public void setToTime(Long to) {
		addArgument("to", to);
	}
}
