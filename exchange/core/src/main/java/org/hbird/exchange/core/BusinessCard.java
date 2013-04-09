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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Handler;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hbird.exchange.util.LocalHostNameResolver;

/**
 * @author Admin
 * 
 */
public class BusinessCard extends Issued {

    private static final long serialVersionUID = -4619331399009932893L;

    public static final String DESCRIPTION = "A businesscard from a startable component.";

    /** The host name on which this component is running. */
    protected String host;

    /** Interval between heart beats. */
    protected long period = -1L;

    /** Commands accepted by this {@link Part}. */
    public Map<String, Command> commandsIn = new HashMap<String, Command>();
    public Map<String, Command> commandsOut = new HashMap<String, Command>();

    public Map<String, Event> eventsIn = new HashMap<String, Event>();
    public Map<String, Event> eventsOut = new HashMap<String, Event>();

    public Map<String, Issued> dataIn = new HashMap<String, Issued>();
    public Map<String, Issued> dataOut = new HashMap<String, Issued>();

    public BusinessCard(String issuedBy, long period) {
        // XXX - 03.04.2013, kimmell - name and issuedBy are set to same value here
        super(issuedBy, issuedBy, DESCRIPTION);
        this.period = period;
        this.host = LocalHostNameResolver.getLocalHostName();
    }

    /**
	 * @param name
	 * @param heartbeat
	 * @param commands
	 */
	public BusinessCard(String name, long heartbeat, List<Command> commands) {
		this(name, heartbeat);
		for (Command command : commands) {
			this.commandsIn.put(command.getName(), command);
		}
	}

	public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("issuedBy", issuedBy)
                .append("host", host)
                .append("timestamp", timestamp)
                .append("period", period)
                .toString();
    }

    /**
     * Returns this {@link BusinessCard} with updated timestamp.
     * 
     * @return this {@link BusinessCard} with updated timestamp
     */
    @Handler
    public BusinessCard touch() {
        timestamp = System.currentTimeMillis();
        return this;
    }
}
