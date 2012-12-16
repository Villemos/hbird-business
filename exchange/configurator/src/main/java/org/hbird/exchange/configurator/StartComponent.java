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

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;

public abstract class StartComponent extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5607028481851891556L;

	{
		arguments.put("componentname", new CommandArgument("componentname", "The name of the component to be started. Is used to route messages to the component. All data will be 'issuedby' this name.", "String", "", null, true));
		arguments.put("heartbeat", new CommandArgument("heartbeat", "The period between heartbeat signals (BusinessCards) from this component.", "Long", "Milliseconds", 5000l, true));
	}
	
	public StartComponent(String issuedBy, String destination, String componentname, String requestname, String description) {
		super(issuedBy, destination, requestname, description);
		addComponentName(componentname);
	}

	public StartComponent(String componentname, String requestname, String description) {
		super("Assembly", "Configurator", requestname, description);
		addComponentName(componentname);
	}

	protected void addComponentName(String componentname) {
		addArgument("componentname", componentname);
	}

	protected void addHeartbeat(long period) {
		addArgument("heartbeat", period);
	}

	public void setHeartbeat(long period) {
		addHeartbeat(period);
	}
	
	public long getHeartbeat() {
		return (Long) getArgument("heartbeat");
	}
}
