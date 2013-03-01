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

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.constants.StandardComponents;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;

public abstract class StartComponent extends Command {

    private static final long serialVersionUID = 3880066748415223278L;

    public StartComponent(String issuedBy, String destination, String componentname, String requestname, String description) {
        super(issuedBy, destination, requestname, description);
        addComponentName(componentname);
    }

    public StartComponent(String componentname, String requestname, String description) {
        super(StandardComponents.ASSEMBLY, "Configurator", requestname, description);
        addComponentName(componentname);
    }

    /**
     * @see org.hbird.exchange.core.Command#getArgumentDefinitions()
     */
    @Override
    protected List<CommandArgument> getArgumentDefinitions() {
        List<CommandArgument> args = new ArrayList<CommandArgument>();
        args.add(new CommandArgument(StandardArguments.COMPONENT_NAME,
                "The name of the component to be started. Is used to route messages to the component. All data will be 'issuedby' this name.", "String", "",
                null, true));
        args.add(new CommandArgument(StandardArguments.HEART_BEAT, "The period between heartbeat signals (BusinessCards) from this component.", "Long",
                "Milliseconds", 5000L, true));
        return args;
    }

    protected void addComponentName(String componentname) {
        setArgumentValue(StandardArguments.COMPONENT_NAME, componentname);
    }

    protected void addHeartbeat(long period) {
        setArgumentValue(StandardArguments.HEART_BEAT, period);
    }

    public void setHeartbeat(long period) {
        addHeartbeat(period);
    }

    public long getHeartbeat() {
        return getArgumentValue(StandardArguments.HEART_BEAT, Long.class);
    }
}
