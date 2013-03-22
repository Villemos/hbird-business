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
package org.hbird.business.taskexecutor;

import org.apache.camel.model.ProcessorDefinition;
import org.hbird.business.core.SoftwareComponentDriver;
import org.hbird.exchange.configurator.StandardEndpoints;
import org.hbird.exchange.constants.StandardArguments;

/**
 * Component builder to create a Task Executor component
 * 
 * @author Gert Villemos
 * 
 */
public class TaskExecutorComponentDriver extends SoftwareComponentDriver {

    @Override
    public void doConfigure() {

        String componentname = command.getArgumentValue(StandardArguments.COMPONENT_NAME, String.class);
        ProcessorDefinition<?> route = from(StandardEndpoints.tasks).split().method(new TaskExecutor(componentname), "receive");
        addInjectionRoute(route);

        addCommandHandler();
    }
}
