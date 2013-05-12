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
package org.hbird.business.core;

import java.util.List;

import org.apache.camel.CamelContext;
import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.configurator.StopComponent;
import org.hbird.exchange.core.BusinessCard;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.interfaces.IStartablePart;

/**
 * @author Gert Villemos
 * 
 */
public class StartableEntity extends CommandableEntity implements IStartablePart {

    /** Default heart beat interval in millis. */
    public static final long DEFAULT_HEARTBEAT = 5000L;

    /**
	 * 
	 */
    private static final long serialVersionUID = 8396148214309147407L;

    /** The class name of the driver that can 'execute' this part. */
    protected String driverName = null;

    /** The location (name of a Configurator) where the component should be started. */
    protected String configurator = null;

    protected long heartbeat = DEFAULT_HEARTBEAT;

    protected BusinessCard card;

    protected CamelContext context = null;

    /**
     * @return the context
     */
    @Override
    public CamelContext getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    @Override
    public void setContext(CamelContext context) {
        this.context = context;
    }

    /**
     * @param name
     * @param description
     */
    public StartableEntity(String ID, String name) {
        super(ID, name);
    }

    @Override
    public BusinessCard getBusinessCard() {
        if (card == null) {
            card = createBusinessCard(getID(), getName(), getHeartbeat(), getCommands(), getDescription());
        }
        return card.touch();
    }

    @Override
    public String getDriverName() {
        return driverName;
    }

    @Override
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    @Override
    public String getConfigurator() {
        return configurator;
    }

    @Override
    public void setConfigurator(String configurator) {
        this.configurator = configurator;
    }

    @Override
    public long getHeartbeat() {
        return heartbeat;
    }

    @Override
    public void setHeartbeat(long heartbeat) {
        this.heartbeat = heartbeat;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hbird.business.core.CommandablePart#createCommandList()
     */
    @Override
    protected List<Command> createCommandList(List<Command> commands) {
        commands.add(new StartComponent(""));
        commands.add(new StopComponent(""));
        return commands;
    }

    protected BusinessCard createBusinessCard(String id, String name, long heartBeat, List<Command> commands, String description) {
        BusinessCard card = new BusinessCard(name, name);
        card.setPeriod(heartBeat);
        card.setCommandsIn(commands);
        card.setDescription(description);
        card.setIssuedBy(id);
        return card;
    }
}
