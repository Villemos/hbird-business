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

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.core.CommandArgument;

public class LocationRequest extends DataRequest {

	private static final long serialVersionUID = -3332566639747183418L;

	{
		arguments.put("sortorder", new CommandArgument("sortorder", "The order in which the returned data should be returned.", "String", "", "ASC", true));
		arguments.put("sort", new CommandArgument("sort", "The sort field. Default is timestamp.", "String", "", "timestamp", true));
		arguments.put("rows", new CommandArgument("rows", "The maximum number of rows to be retrieved.", "Long", "", 1000, true));
	}
	
	public LocationRequest(String issuedBy, List<String> locations) {
		super(issuedBy, "Archive", "LocationRequest", "A request for the definition of locations.");

		addNames(locations);
		setType("Location");
		setIsInitialization(true);
	}
	
	public LocationRequest(String issuedBy, String location) {
		super(issuedBy, "Archive", "LocationRequest", "A request for the definition of locations.");

		List<String> locations = new ArrayList<String>();
		locations.add(location);
		addNames(locations);
		setType("Location");
		setIsInitialization(true);
	}
}
