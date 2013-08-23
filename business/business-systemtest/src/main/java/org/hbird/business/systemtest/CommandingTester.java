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
package org.hbird.business.systemtest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.Part;
import org.hbird.exchange.tasking.SetParameter;
import org.hbird.exchange.tasking.Task;

public class CommandingTester extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(CommandingTester.class);

    @Handler
    public void process() throws Exception {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        startMonitoringArchive();
        startCommandingChain();

        Thread.sleep(2000);

        /** The command object. Destination is an unknown object. */
        Command command = new Command("COM2", "COM2");

        /** Send a simple command request. */
        publishCommandRequest("COMREQ1", "COMREQ1", "A simple command request container with no lock states and no tasks.", command, null, null);

        Thread.sleep(2000);

        azzert(commandingListener.lastReceived.getName().equals("COM2"),
                "Command request was executed, command 'COM2' was issued.");

        /** Create a command with a release time. */
        Date now = new Date();
        command.setTransferTime(now.getTime() + 2000);

        publishCommandRequest("COMREQ1", "COMREQ1", "A simple command request container with no lock states and no tasks.", command, null, null);

        Thread.sleep(2000);

        Part limits = parts.get("Limits");

        /** Add tasks to be done and lock states. */
        List<Task> tasks = new ArrayList<Task>();

        SetParameter setparameter1 = new SetParameter("TASK_SET_PARA10", "TASK_SET_PARA10");
        setparameter1.setDescription("A test parameter set by a task");

        Parameter parameter1 = new Parameter("PARA10", "PARA10");
        parameter1.setDescription("A test parameter more");
        parameter1.setValue(9d);
        parameter1.setUnit("Bananas");
        setparameter1.setParameter(parameter1);

        tasks.add(setparameter1);

        SetParameter setparameter2 = new SetParameter("TASK_SET_PARA11", "TASK_SET_PARA11");
        setparameter2.setDescription("A test parameter set by a task");

        Parameter parameter2 = new Parameter("PARA11", "PARA11");
        parameter2.setDescription("A test parameter more");
        parameter2.setValue(1d);
        parameter2.setUnit("Bananas");
        setparameter2.setParameter(parameter2);

        tasks.add(setparameter1);

        List<String> states = new ArrayList<String>();
        states.add("COM2/STATE1");
        states.add("COM2/STATE2");
        states.add("COM2/STATE3");
        states.add("COM2/STATE4");

        /** Set the values of the states. */

        publishState("COM2/STATE1", "COM2/STATE1", "A test description,", "COM2:Command:*", true);
        publishState("COM2/STATE2", "COM2/STATE2", "A test description,", "COM2:Command:*", true);
        publishState("COM2/STATE3", "COM2/STATE3", "A test description,", "COM2:Command:*", false);
        publishState("COM2/STATE4", "COM2/STATE4", "A test description,", "COM2:Command:*", true);

        /** The command should fail, as one of the states is 'false'. */
        publishCommandRequest("COMREQ1", "COMREQ1", "A simple command request container with no lock states and no tasks.", command, states, tasks);

        Thread.sleep(2000);

        azzert(failedCommandRequestListener.lastReceived.getName().equals("COMREQ1"));
        failedCommandRequestListener.lastReceived = null;

        /** Update state to make the command succeed. */
        publishState("COM2/STATE3", "COM2/STATE3", "A test description,", "COM2:Command:*", true);

        /** The command should fail, as one of the states is 'false'. */
        publishCommandRequest("COMREQ1", "COMREQ1", "A simple command request container with no lock states and no tasks.", command, states, tasks);

        Thread.sleep(2000);

        azzert(failedCommandRequestListener.lastReceived == null, "Command succeeded");

        /**
         * Add a state that is not inserted in the command, but which is a state of the command. See if the command
         * fails.
         */
        publishState("STATE_OTHER", "STATE_OTHER", "A test description", "COM2:Command:*", false);

        /** The command should fail, as one of the states is 'false'. */
        publishCommandRequest("COMREQ1", "COMREQ1", "A simple command request container with no lock states and no tasks.", command, states, tasks);

        Thread.sleep(2000);

        azzert(failedCommandRequestListener.lastReceived.getName().equals("COMREQ1"));

        LOG.info("Finished");
    }
}
