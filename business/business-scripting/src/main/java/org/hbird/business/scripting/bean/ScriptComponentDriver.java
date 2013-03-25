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
package org.hbird.business.scripting.bean;

import org.apache.camel.model.ProcessorDefinition;
import org.hbird.business.core.SoftwareComponentDriver;
import org.hbird.business.scripting.ScriptComponent;
import org.hbird.exchange.configurator.StandardEndpoints;
import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.scripting.ScriptExecutionRequest;

/**
 * Component builder to create a script engine component.
 * 
 * @author Gert Villemos
 * 
 */
public class ScriptComponentDriver extends SoftwareComponentDriver {

    @Override
    protected void doConfigure() {

        ScriptExecutor executor = new ScriptExecutor(((ScriptComponent) command.getPart()).getRequest());

        /** Iterate over each dependency needed by this script. */
        for (String dependency : executor.getDependencies()) {

            /** Create the routes for receiving the data needed by the script. */
            ProcessorDefinition<?> route = from(StandardEndpoints.monitoring + "?" + addNameSelector(dependency)).bean(executor, "calculate");
            addInjectionRoute(route);
        }
        
        addCommandHandler();
    }
}
