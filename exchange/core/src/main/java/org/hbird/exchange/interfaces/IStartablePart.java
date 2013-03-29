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
package org.hbird.exchange.interfaces;

import java.util.List;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Event;
import org.hbird.exchange.core.Parameter;

/**
 * @author Gert Villemos
 *
 */
public interface IStartablePart extends IPart {
	
	public String getDriverName();

	public void setDriverName(String driverName);
	
	public String getConfigurator();

	public void setConfigurator(String configurator);
	
	public long getHeartbeat();
	
	public void setHeartbeat(long heartbeat);
	
	public List<Command> getCommandsIn();

	public void setCommandsIn(List<Command> commandsIn);

	public List<Command> getCommandsOut();

	public void setCommandsOut(List<Command> commandsOut);

	public List<Event> getEventsIn();

	public void setEventsIn(List<Event> eventsIn);

	public List<Event> getEventsOut();

	public void setEventsOut(List<Event> eventsOut);

	public List<Parameter> getParametersIn();
	
	public void setParametersIn(List<Parameter> parametersIn);

	public List<Parameter> getParametersOut();

	public void setParametersOut(List<Parameter> parametersOut);
}
