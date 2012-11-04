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
package org.hbird.exchange.heartbeat;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.hbird.exchange.core.Named;

public class Heartbeat extends Named {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2792762576624569229L;

	{
		this.name = "Heartbeat";
		this.description = "A heartbeat signal. The component was alive as this signal was issued.";
		
		try {
			this.hostname = InetAddress.getLocalHost().getHostName();
			this.hostip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/** The time to the next beat. */
	protected long nextBeat = 10000;
	
	/** The name of the host upon which the component is running. */
	protected String hostname = "unknown";
	
	/** The IP of the host upon which the component is running. */
	protected String hostip = "unknown";	
		
	public Heartbeat() {};
	
	public Heartbeat(String issuedBy, long timestamp, long nextBeat) {
		super(issuedBy);
		
		this.timestamp = timestamp;
		this.nextBeat = nextBeat;
	}

	public long getNextBeat() {
		return nextBeat;
	}

	public void setNextBeat(long nextBeat) {
		this.nextBeat = nextBeat;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getHostip() {
		return hostip;
	}

	public void setHostip(String hostip) {
		this.hostip = hostip;
	}
	
	
}
