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
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;

public class StopComponent extends Command {

    public static final String DESCRIPTION = "A command to stop a component in the configurator.";

    private static final long serialVersionUID = 2311720104274270225L;

    public StopComponent(String issuedBy, String destination, String componentToStop) {
        super(issuedBy, destination, StopComponent.class.getSimpleName(), DESCRIPTION);
        setComponent(componentToStop);
    }

    /**
     * @see org.hbird.exchange.core.Command#getArgumentDefinitions()
     */
    @Override
    protected List<CommandArgument> getArgumentDefinitions() {
        List<CommandArgument> args = new ArrayList<CommandArgument>(1);
        args.add(new CommandArgument(StandardArguments.COMPONENT_NAME, "The name of the component to be stopped.", "String", "", null, true));
        return args;
    }

    public String getComponent() {
        return getArgumentValue(StandardArguments.COMPONENT_NAME, String.class);
    }

    public void setComponent(String componentname) {
        setArgumentValue(StandardArguments.COMPONENT_NAME, componentname);
    }
}
