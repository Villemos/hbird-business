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

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.core.BusinessCard;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Event;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.Part;
import org.hbird.exchange.interfaces.IStartablePart;

/**
 * @author Admin
 *
 */
public class StartablePart extends Part implements IStartablePart {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8396148214309147407L;

	/** The class name of the driver that can 'execute' this part. */
	protected String driverName = null;
	
	/** The location (name of a Configurator) where the component should be started. */
	protected String configurator = null;
	
	protected long heartbeat = 5000;
		
	protected BusinessCard card = null;

	
	protected List<Command> commandsIn = new ArrayList<Command>();
	protected List<Command> commandsOut = new ArrayList<Command>();

	protected List<Event> eventsIn = new ArrayList<Event>();
	protected List<Event> eventsOut = new ArrayList<Event>();

	protected List<Parameter> parametersIn = new ArrayList<Parameter>();
	protected List<Parameter> parametersOut = new ArrayList<Parameter>();

	{
		commandsIn.add(new StartComponent("", null));
		commandsIn.add(new StopComponent("", ""));
	}
	
	
	/**
	 * @param name
	 * @param description
	 */
	public StartablePart(String ID, String name, String description, String driverName) {
		super(ID, name, description);
		this.driverName = driverName;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getConfigurator() {
		return configurator;
	}

	public void setConfigurator(String configurator) {
		this.configurator = configurator;
	}

	public long getHeartbeat() {
		return heartbeat;
	}

	public void setHeartbeat(long heartbeat) {
		this.heartbeat = heartbeat;
	}

	public List<Command> getCommandsIn() {
		return commandsIn;
	}

	public void setCommandsIn(List<Command> commandsIn) {
		this.commandsIn = commandsIn;
	}

	public List<Command> getCommandsOut() {
		return commandsOut;
	}

	public void setCommandsOut(List<Command> commandsOut) {
		this.commandsOut = commandsOut;
	}

	public List<Event> getEventsIn() {
		return eventsIn;
	}

	public void setEventsIn(List<Event> eventsIn) {
		this.eventsIn = eventsIn;
	}

	public List<Event> getEventsOut() {
		return eventsOut;
	}

	public void setEventsOut(List<Event> eventsOut) {
		this.eventsOut = eventsOut;
	}

	public List<Parameter> getParametersIn() {
		return parametersIn;
	}

	public void setParametersIn(List<Parameter> parametersIn) {
		this.parametersIn = parametersIn;
	}

	public List<Parameter> getParametersOut() {
		return parametersOut;
	}

	public void setParametersOut(List<Parameter> parametersOut) {
		this.parametersOut = parametersOut;
	}
}
