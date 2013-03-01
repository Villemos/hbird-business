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

import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.tasking.SendCommand;
import org.hbird.exchange.tasking.SetParameter;
import org.hbird.exchange.tasking.Task;

public class TaskingTester extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(TaskingTester.class);

    @Handler
    public void process() throws InterruptedException {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        /** Start two task executors. */
        startTaskComponent("TaskExecutor1");
        startTaskComponent("TaskExecutor2");

        Thread.sleep(2000);

        /** Create simple task that sets a parameter. Is executed immediatly. */
        injection.sendBody(new SetParameter("SystemTest", "TASK_SET_PARA9", "A test parameter set by a task", 0, "PARA9", "", "A test parameter more", 9d,
                "Bananas"));

        Thread.sleep(2000);

        azzert(parameterListener.lastReceived.getName().equals("PARA9"), "The 'Set' parameter was received,");
        Parameter out1 = (Parameter) parameterListener.lastReceived;
        azzert(out1.getValue().doubleValue() == 9.0d, "Value was 9 as expected.");
        azzert(out1.getIssuedBy().equals("TaskExecutor1") || out1.getIssuedBy().equals("TaskExecutor2"), "It was issued by one of the two task executors.");

        parameterListener.elements.clear();

        /** Create a repeatable Set. */
        Task task = new SetParameter("SystemTest", "TASK_SET_PARA9", "A test parameter set by a task", 0, "PARA9", "", "A test parameter more", 9d, "Bananas");
        task.setRepeat(5);
        task.setExecutionDelay(1000);
        injection.sendBody(task);

        Thread.sleep(10000);

        azzert(parameterListener.elements.size() == 5, "Received 6 repetitions.");

        /** Create a task for issuing a command. */
        Command command = new Command("SystemTest", "Any", "COM1", "A test command used to test SendCommand task.", 0, 0);
        injection.sendBody(new SendCommand("SystemTest", "TASK_SET_PARA9", "A test parameter set by a task", 0, command));

        Thread.sleep(2000);

        azzert(commandingListener.lastReceived.getName().equals("COM1"), "Received command.");

        LOG.info("Finished");
    }
}
