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

import org.hbird.exchange.core.Named;

public class Heartbeat extends Named {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2792762576624569229L;

	{
		this.name = "Heartbeat";
		this.description = "A heartbeat signal. The component was alive as this signal was issued.";
	}

	protected long nextBeat = 10000;
	
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
}
