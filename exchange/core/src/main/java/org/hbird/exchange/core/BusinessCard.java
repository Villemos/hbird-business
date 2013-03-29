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
package org.hbird.exchange.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @author Admin
 *
 */
public class BusinessCard extends Issued {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4845057459222972255L;

	/** The host name on which this component is running */
	protected String host;
	{
		try {
			host = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			//
		}		
	}
		
	/** The list of commands that the component that sends this BusinessCard supports */
	protected List<Command> commands = null;

	public BusinessCard(String issuedBy, List<Command> commands) {
		super(issuedBy, issuedBy, "A businesscard from a startable component.");
		this.commands = commands;
		this.issuedBy = issuedBy;
	}

	/**
	 * @param hostName
	 * @param commands2
	 */
	public BusinessCard(String issuedBy, String hostName, List<Command> commands) {
		super(issuedBy, issuedBy, "A businesscard from a startable component.");
		this.host = hostName;
		this.commands = commands;
		this.issuedBy = issuedBy;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public List<Command> getCommands() {
		return commands;
	}

	public void setCommands(List<Command> commands) {
		this.commands = commands;
	}
}
