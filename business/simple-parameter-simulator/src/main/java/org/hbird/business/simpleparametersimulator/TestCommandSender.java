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
package org.hbird.business.simpleparametersimulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.tasking.SetParameter;
import org.hbird.exchange.tasking.Task;

public class TestCommandSender {

    public CommandRequest send() {
        Map<String, CommandArgument> arguments = new HashMap<String, CommandArgument>();

        Command command = new Command("Simulator", "ESTcube/platform/battery1", "Turn Off", "Command to turn of the battery. NOTE: SIMULATED", 0, 0);
        command.addArguments(arguments);

        List<String> lockstates = new ArrayList<String>();
        lockstates.add("TestParameter3LowerLimit");

        List<Task> tasks = new ArrayList<Task>();
        tasks.add(new SetParameter("", "TestTask", "Task for testing", 0, new Parameter("", "Parameter90", "", "Parameter set by task", 9d, "Volt")));

        return new CommandRequest("Simulator", "TestCommandContainer", "", "A container for the test command used for validation", lockstates, tasks, command);
    }
}
