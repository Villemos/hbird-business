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
 * @author Gert Villemos
 * 
 */
public class BusinessCard extends EntityInstance {

    private static final long serialVersionUID = -4619331399009932893L;

    public static final String DESCRIPTION = "A businesscard from a startable component.";

    /** The host name on which this component is running. */
    protected String host;

    /** Interval between heart beats. */
    protected long period = -1L;

    /** Commands accepted by this {@link Part}. */
    protected Map<String, Command> commandsIn = new HashMap<String, Command>();
    protected Map<String, Command> commandsOut = new HashMap<String, Command>();

    protected Map<String, Event> eventsIn = new HashMap<String, Event>();
    protected Map<String, Event> eventsOut = new HashMap<String, Event>();

    protected Map<String, EntityInstance> dataIn = new HashMap<String, EntityInstance>();
    protected Map<String, EntityInstance> dataOut = new HashMap<String, EntityInstance>();

    public BusinessCard(String ID, String name) {
        super(ID, name);
        
        this.host = LocalHostNameResolver.getLocalHostName();
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

    /**
     * @return the commandsIn
     */
    public Map<String, Command> getCommandsIn() {
        return commandsIn;
    }

    /**
     * @param commandsIn the commandsIn to set
     */
    public void setCommandsIn(Map<String, Command> commandsIn) {
        this.commandsIn = commandsIn;
    }

    public void setCommandsIn(List<Command> commandsIn) {
        this.commandsIn = new HashMap<String, Command>();
        for (Command command : commandsIn) {
        	this.commandsIn.put(command.getName(), command);
        }
    }

    /**
     * @return the commandsOut
     */
    public Map<String, Command> getCommandsOut() {
        return commandsOut;
    }

    /**
     * @param commandsOut the commandsOut to set
     */
    public void setCommandsOut(Map<String, Command> commandsOut) {
        this.commandsOut = commandsOut;
    }

    /**
     * @return the eventsIn
     */
    public Map<String, Event> getEventsIn() {
        return eventsIn;
    }

    /**
     * @param eventsIn the eventsIn to set
     */
    public void setEventsIn(Map<String, Event> eventsIn) {
        this.eventsIn = eventsIn;
    }

    /**
     * @return the eventsOut
     */
    public Map<String, Event> getEventsOut() {
        return eventsOut;
    }

    /**
     * @param eventsOut the eventsOut to set
     */
    public void setEventsOut(Map<String, Event> eventsOut) {
        this.eventsOut = eventsOut;
    }

    /**
     * @return the dataIn
     */
    public Map<String, EntityInstance> getDataIn() {
        return dataIn;
    }

    /**
     * @param dataIn the dataIn to set
     */
    public void setDataIn(Map<String, EntityInstance> dataIn) {
        this.dataIn = dataIn;
    }

    /**
     * @return the dataOut
     */
    public Map<String, EntityInstance> getDataOut() {
        return dataOut;
    }

    /**
     * @param dataOut the dataOut to set
     */
    public void setDataOut(Map<String, EntityInstance> dataOut) {
        this.dataOut = dataOut;
    }
}
