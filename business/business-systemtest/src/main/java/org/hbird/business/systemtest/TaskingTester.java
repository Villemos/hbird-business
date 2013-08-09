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
import org.hbird.business.api.impl.Injector;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.Part;
import org.hbird.exchange.tasking.SendCommand;
import org.hbird.exchange.tasking.SetParameter;

public class TaskingTester extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(TaskingTester.class);

    @Handler
    public void process() throws Exception {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        String parameterName = "PARA1";

        Part taskExecutor = parts.get("Task Executor");

        /** Start two task executors. */
        startTaskComponent("TaskExecutor1");
        startTaskComponent("TaskExecutor2");

        Thread.sleep(2000);

        /** Create simple task that sets a parameter. Is executed immediatly. */
        
        SetParameter setparameter1 = new SetParameter("SET_PARA1", "SET_PARA1");
        setparameter1.setDescription("A test parameter set by a task");

        Parameter parameter1 = new Parameter(parameterName, parameterName);
        parameter1.setDescription("A test parameter more");
        parameter1.setValue(9d);
        parameter1.setUnit("Bananas");
        setparameter1.setParameter(parameter1);
        
        //publishApi.publish(setparameter1);
        publishApi.publish(setparameter1);
        
        Thread.sleep(2000);

        azzert(parameterListener.lastReceived.getName().equals(parameterName), "The 'Set' parameter was received,");
        Parameter out1 = (Parameter) parameterListener.lastReceived;
        azzert(out1.getValue().doubleValue() == 9.0d, "Value was 9 as expected.");
        azzert(out1.getIssuedBy().equals("TaskExecutor1")
                || out1.getIssuedBy().equals("TaskExecutor2"), "It was issued by one of the two task executors.");

        parameterListener.elements.clear();

        /** Create a repeatable Set. */
        SetParameter setparameter2 = new SetParameter("SET_PARA2", "SET_PARA2");
        setparameter2.setDescription("A test parameter set by a task");
        setparameter2.setParameter(parameter1);
        setparameter2.setRepeat(5);
        setparameter2.setExecutionDelay(1000);
        
        //publishApi.publish(setparameter2);
        publishApi.publish(setparameter2);

        Thread.sleep(10000);

        azzert(parameterListener.elements.size() == 5, "Received 5 repetitions.");

        /** Create a task for issuing a command. */
        String commandName = "COM1";
        String task2Name = "SEND_COM1";
        Command command = new Command(commandName, commandName);
        command.setDescription("A test command used to test SendCommand task.");
        
        SendCommand sendCommand = new SendCommand(task2Name, task2Name);
        sendCommand.setCommand(command);
        //publishApi.publish(sendCommand);
        publishApi.publish(sendCommand);
        
        Thread.sleep(2000);

        azzert(commandingListener.lastReceived.getName().equals(commandName), "Received command.");

        LOG.info("Finished");
    }
}
