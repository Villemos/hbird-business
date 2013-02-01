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
package org.hbird.exchange.businesscard;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.camel.Handler;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A BusinessCard presents a component to its environment. It defines the name
 * of the component, the host the component runs on and the Commands that the component
 * will accept. The component that the BusinessCard described is defined through the
 * 'issuedBy' attribute.
 * 
 * The business cards can be used to discover which components exist in the system and
 * which commands can be send.
 * 
 * @author Gert Villemos
 *
 */
public class BusinessCard extends Named {


	/** Serial version UID. */
	private static final long serialVersionUID = -1986397695903797884L;

	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(BusinessCard.class);

	/** The host name on which this component is running */
	protected String host;

	/** The list of commands that the component that sends this BusinessCard supports */
	protected List<Command> commands = null;

	/** The period between heartbeats. The time to-wait for the next expected beat,*/
	protected long period = 10000;


	/**
	 * @param issuedBy The name of the component that issues this BusinessCard
	 * @param period delay before next BusinessCard is published
	 */
	public BusinessCard(String issuedBy, long period) {
		this(issuedBy, period, null);
	}

	/**
	 * @param issuedBy The name of the component that issues this BusinessCard
	 * @param period delay before next BusinessCard is published
	 * @param commands List of commands that the component supports
	 */
	public BusinessCard(String issuedBy, long period, List<Command> commands) {
		super(issuedBy, issuedBy, "BusinessCard", "A description of a component.");
		this.period = period;
		try {
			this.host = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			LOG.warn("Failed to resolve hostname for the BusinessCard ", e);
		}
		this.commands = commands;
	}

	/**
	 * Method to add this business card to a route.
	 * 
	 * @return The BusinessCard, i.e. this object,
	 */
	@Handler
	public BusinessCard send() {
		this.timestamp = System.currentTimeMillis();
		return this;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the commands
	 */
	public List<Command> getCommands() {
		return commands;
	}

	/**
	 * @param commands the commands to set
	 */
	public void setCommands(List<Command> commands) {
		this.commands = commands;
	}

	/**
	 * @return the period
	 */
	public long getPeriod() {
		return period;
	}

	/**
	 * @param period the period to set
	 */
	public void setPeriod(long period) {
		this.period = period;
	}
}
