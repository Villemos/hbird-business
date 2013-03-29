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

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hbird.exchange.util.LocalHostNameResolver;
import org.hbird.exchange.util.NamedHelper;

/**
 * @author Admin
 * 
 */
public class BusinessCard extends Named {

    private static final long serialVersionUID = -4619331399009932893L;

    public static final String DESCRIPTION = "A businesscard from a startable component.";

    /** The host name on which this component is running. */
    protected String host;

    /** Interval between heart beats. */
    protected long period = -1L;

    /** The list of commands that the component that sends this BusinessCard supports */
    protected List<Command> commands = null;

    public BusinessCard(String issuedBy, List<Command> commands, long period) {
        this(issuedBy, LocalHostNameResolver.getLocalHostName(), commands, period);
    }

    /**
     * @param hostName
     * @param commands2
     */
    public BusinessCard(String issuedBy, String hostName, List<Command> commands, long period) {
        super(issuedBy, issuedBy, BusinessCard.class.getSimpleName(), DESCRIPTION);
        this.host = hostName;
        this.commands = commands;
        this.period = period;
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
                .append("type", type)
                .append("commands", NamedHelper.toString(commands))
                .toString();
    }
}
