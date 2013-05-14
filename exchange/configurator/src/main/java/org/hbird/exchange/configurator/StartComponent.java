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
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.interfaces.IStartableEntity;

public class StartComponent extends Command {

    private static final long serialVersionUID = -8017279911954733945L;

    public static final String DESCRIPTION = "Request to start a component.";

    public static final String ARGUMENT_DESCRIPTION_ENTITY = "The entity that this command requests the start of.";

    public StartComponent(String ID) {
        super(ID, StartComponent.class.getSimpleName());
        setDescription(DESCRIPTION);
    }

    /**
     * @see org.hbird.exchange.core.Command#getArgumentDefinitions()
     */
    @Override
    protected List<CommandArgument> getArgumentDefinitions(List<CommandArgument> args) {
        args.add(new CommandArgument(StandardArguments.ENTITY, ARGUMENT_DESCRIPTION_ENTITY, IStartableEntity.class, true));
        return args;
    }

    public void setEntity(IStartableEntity entity) {
        setArgumentValue(StandardArguments.ENTITY, entity);
    }

    public IStartableEntity getEntity() {
        return getArgumentValue(StandardArguments.ENTITY, IStartableEntity.class);
    }
}
