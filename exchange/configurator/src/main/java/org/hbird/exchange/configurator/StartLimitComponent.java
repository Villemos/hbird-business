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
package org.hbird.exchange.configurator;

import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.validation.Limit;
import org.hbird.exchange.validation.Limit.eLimitType;


public class StartLimitComponent extends StartComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8047192684233318282L;

	{
		arguments.put("limit", new CommandArgument("limit", "The limit definition.", "Limit", "", null, true));
	}

	public StartLimitComponent(String limitName, eLimitType type, String ofParameter, String limit, String stateName, String stateDescription) {
		super(limitName, "StartLimitComponent", "Command to a configurator to start a limit component.");
		addLimit(type, ofParameter, limit, stateName, stateDescription);
	}	

	public StartLimitComponent(String issuedBy, String destination, String limitName, eLimitType type, String ofParameter, String limit, String stateName, String stateDescription) {
		super(issuedBy, destination, limitName, "StartLimitComponent", "Command to a configurator to start a limit component.");
		addLimit(type, ofParameter, limit, stateName, stateDescription);
	}	

	public void addLimit(eLimitType type, String ofParameter, String limit, String stateName, String description) {

		if (limit != null && limit.equals("") == false) {
			Number value = null;
			if (limit.endsWith("i")) {
				value = Integer.parseInt(limit);
			}
			else if (limit.endsWith("f")) {
				value = Float.parseFloat(limit);
			}
			else if (limit.endsWith("s")) 
			{
				value = Short.parseShort(limit);
			}
			else {
				value = Double.parseDouble(limit);
			}
			
			addArgument("limit", new Limit(type, ofParameter, value, stateName, description));
		}
	}
}
