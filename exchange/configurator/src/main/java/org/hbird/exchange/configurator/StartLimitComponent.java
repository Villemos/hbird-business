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

import java.util.List;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.validation.Limit;
import org.hbird.exchange.validation.Limit.eLimitType;

public class StartLimitComponent extends StartComponent {

    public static final String DESCRIPTION = "Command to a configurator to start a limit component.";

    private static final long serialVersionUID = 6948647097104421617L;

    public StartLimitComponent(String limitName, eLimitType type, String ofParameter, String limit, String stateName, String stateDescription) {
        super(limitName, StartLimitComponent.class.getSimpleName(), DESCRIPTION);
        addLimit(type, ofParameter, limit, stateName, stateDescription);
    }

    public StartLimitComponent(String issuedBy, String destination, String limitName, eLimitType type, String ofParameter, String limit, String stateName,
            String stateDescription) {
        super(issuedBy, destination, limitName, StartLimitComponent.class.getSimpleName(), DESCRIPTION);
        addLimit(type, ofParameter, limit, stateName, stateDescription);
    }

    /**
     * @see org.hbird.exchange.core.Command#getArgumentDefinitions()
     */
    @Override
    protected List<CommandArgument> getArgumentDefinitions() {
        List<CommandArgument> args = super.getArgumentDefinitions();
        args.add(new CommandArgument(StandardArguments.LIMIT, "The limit definition.", "Limit", "", null, true));
        return args;
    }

    public void addLimit(eLimitType type, String ofParameter, String limit, String stateName, String description) {

        if (limit != null && limit.equals("") == false) {
            Number value = null;
            if (limit.endsWith("i")) {
                value = Integer.parseInt(limit);
            }
            else if (limit.endsWith("f")) {
                value = Float.parseFloat(limit);
            }
            else if (limit.endsWith("s"))
            {
                value = Short.parseShort(limit);
            }
            else {
                value = Double.parseDouble(limit);
            }

            setArgumentValue(StandardArguments.LIMIT, new Limit(type, ofParameter, value, stateName, description));
        }
    }
}
