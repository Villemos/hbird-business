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
import org.hbird.exchange.core.Part;
import org.hbird.exchange.tasking.SetParameter;
import org.hbird.exchange.tasking.Task;

public class CommandingTester extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(CommandingTester.class);

    @Handler
    public void process() throws InterruptedException {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        startMonitoringArchive();
        startCommandingChain();

        Thread.sleep(2000);

        /** The command object. Destination is an unknown object. */
        Command command = new Command(estcube1.getID(), "GroundStation1", "COM2", "A test command");

        /** Send a simple command request. */
        publishApi.publishCommandRequest("COMREQ1", "A simple command request container with no lock states and no tasks.", command, null, null);

        Thread.sleep(2000);

        azzert(commandingListener.lastReceived.getQualifiedName().equals(estcube1.getID() + "/COM2"),
                "Command request was executed, command 'COM2' was issued.");

        /** Create a command with a release time. */
        Date now = new Date();
        command.setReleaseTime(now.getTime() + 2000);

        publishApi.publishCommandRequest("COMREQ1", "A simple command request container with no lock states and no tasks.", command, null, null);

        Thread.sleep(2000);

        Part limits = parts.get("Limits");

        /** Add tasks to be done and lock states. */
        List<Task> tasks = new ArrayList<Task>();
        tasks.add(new SetParameter("SystemTest", "TASK_SET_PARA10", "A test parameter set by a task", 0, limits.getQualifiedName() + "/PARA10",
                "A test parameter more", 9d, "Bananas"));
        tasks.add(new SetParameter("SystemTest", "TASK_SET_PARA11", "A test parameter set by a task", 0, limits.getQualifiedName() + "/PARA11",
                "A test parameter more", 1d, "Bananas"));

        List<String> states = new ArrayList<String>();
        states.add(estcube1.getID() + "/COM2/STATE1");
        states.add(estcube1.getID() + "/COM2/STATE2");
        states.add(estcube1.getID() + "/COM2/STATE3");
        states.add(estcube1.getID() + "/COM2/STATE4");

        /** Set the values of the states. */

        publishApi.publishState("STATE1", "A test description,", estcube1.getID() + "/COM2", true);
        publishApi.publishState("STATE2", "A test description,", estcube1.getID() + "/COM2", true);
        publishApi.publishState("STATE3", "A test description,", estcube1.getID() + "/COM2", false);
        publishApi.publishState("STATE4", "A test description,", estcube1.getID() + "/COM2", true);

        /** Send command to commit all changes. */
        forceCommit();

        /** The command should fail, as one of the states is 'false'. */
        publishApi.publishCommandRequest("COMREQ1", "A simple command request container with no lock states and no tasks.", command, states, tasks);

        Thread.sleep(2000);

        azzert(failedCommandRequestListener.lastReceived.getName().equals("COMREQ1"));
        failedCommandRequestListener.lastReceived = null;

        /** Update state to make the command succeed. */
        publishApi.publishState("STATE3", "A test description,", estcube1.getID() + "/COM2", true);

        /** Send command to commit all changes. */
        forceCommit();

        /** The command should fail, as one of the states is 'false'. */
        publishApi.publishCommandRequest("COMREQ1", "A simple command request container with no lock states and no tasks.", command, states, tasks);

        Thread.sleep(2000);

        azzert(failedCommandRequestListener.lastReceived.getName().equals("COMREQ1"));

        /**
         * Add a state that is not inserted in the command, but which is a state of the command. See if the command
         * fails.
         */
        publishApi.publishState("STATE_OTHER", "A test description", estcube1.getID() + "/COM2", false);

        /** Send command to commit all changes. */
        forceCommit();

        /** The command should fail, as one of the states is 'false'. */
        publishApi.publishCommandRequest("COMREQ1", "A simple command request container with no lock states and no tasks.", command, states, tasks);

        Thread.sleep(2000);

        azzert(failedCommandRequestListener.lastReceived.getName().equals("COMREQ1"));

        LOG.info("Finished");
    }
}
