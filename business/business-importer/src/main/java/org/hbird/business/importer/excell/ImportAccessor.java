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
package org.hbird.business.importer.excell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.tasking.SetParameter;
import org.hbird.exchange.tasking.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImportAccessor {

    private static final transient Logger LOG = LoggerFactory.getLogger(ImportConsumer.class);

    protected ImportEndpoint endpoint = null;

    protected Map<String, Object> parameters = new HashMap<String, Object>();

    protected Map<String, Task> tasks = new HashMap<String, Task>();

    protected Map<String, CommandRequest> commands = new HashMap<String, CommandRequest>();

    public ImportAccessor(ImportEndpoint endpoint) {
        this.endpoint = endpoint;
    }

    public List<Object> getObjects() throws BiffException, IOException {
        List<Object> objects = new ArrayList<Object>();

        File input = new File(endpoint.getFilename());

        if (input.exists() == false) {
            LOG.warn("File '" + input.getAbsolutePath() + "' does not exist.");
            return null;
        }

        Workbook workbook = Workbook.getWorkbook(input);

        readParameters(workbook);
        readTasks(workbook);
        readCommands(workbook);

        objects.addAll(parameters.values());
        objects.addAll(tasks.values());
        objects.addAll(commands.values());

        return objects;
    }

    private void readCommands(Workbook workbook) {

        /** Get the commands. */
        Sheet bodySheet = workbook.getSheet("Commands");

        /** Iterate through the rows. */
        /** A starting row may have been set. */
        for (int row = 1; row < bodySheet.getRows(); row++) {

            if (bodySheet.getCell(1, row).getContents().equals("")) {
                continue;
            }

            /**
             * Format
             * 
             * String issuedBy, String name, String description, long releaseTime, long executionTime, List<Parameter>
             * arguments
             * 0. issuedBy
             * 1. Command Name
             * 2. Command Description.
             * 3. releaseTime
             * 4. executionTime
             * 5. argumentName
             * 6. argumentValue
             * 7. lockstates
             * 8. taskName
             * 9. taskParameterValue
             * */

            String issuedBy = bodySheet.getCell(0, row).getContents();
            String commandName = bodySheet.getCell(1, row).getContents();
            String commandDescription = bodySheet.getCell(2, row).getContents();
            String releaseTime = bodySheet.getCell(3, row).getContents();
            String executionTime = bodySheet.getCell(4, row).getContents();

            Map<String, CommandArgument> arguments = new HashMap<String, CommandArgument>();
            int tempRow = row;
            while (tempRow < bodySheet.getRows() && (bodySheet.getCell(1, tempRow).getContents().equals("") || tempRow == row)
                    && bodySheet.getCell(5, tempRow).getContents().equals("") == false) {
                String argumentName = bodySheet.getCell(5, tempRow).getContents();

                CommandArgument arg = null;
                arguments.put(argumentName, arg);
                tempRow++;
            }

            List<String> lockstates = new ArrayList<String>();
            tempRow = row;
            while ((bodySheet.getCell(1, tempRow).getContents().equals("") || tempRow == row)
                    && bodySheet.getCell(7, tempRow).getContents().equals("") == false) {
                lockstates.add(bodySheet.getCell(7, tempRow).getContents());
                tempRow++;
            }

            List<Task> commandTasks = new ArrayList<Task>();
            tempRow = row;
            while (tempRow < bodySheet.getRows() && (bodySheet.getCell(1, tempRow).getContents().equals("") || tempRow == row)
                    && bodySheet.getCell(8, tempRow).getContents().equals("") == false) {
                String taskName = bodySheet.getCell(8, tempRow).getContents();
                SetParameter task = new SetParameter((SetParameter) tasks.get(taskName));

                /** Deep copy the parameter. Else the task paramegter will be the same object as the actual parameter. */
                task.setParameter(new Parameter(((SetParameter) tasks.get(taskName)).getParameter()));

                Parameter para = task.getParameter();
                para.setValue(getValue(para.getValue().getClass().toString(), bodySheet.getCell(9, tempRow).getContents()));

                task.setParameter(para);
                commandTasks.add(task);

                tempRow++;
            }

            Command cmd = new Command("", "", commandName, commandDescription, Long.parseLong(releaseTime), Long.parseLong(executionTime));
            cmd.addArguments(arguments);

            commands.put(commandName, new CommandRequest(issuedBy, commandName, commandDescription, lockstates, commandTasks, cmd));
        }
    }

    private void readTasks(Workbook workbook) {
        Sheet bodySheet = workbook.getSheet("Tasks");

        /**
         * Format;
         * 0. Name
         * 1. Description
         * 2. ExecutionTime
         * 3. Type
         * 4. Parameter
         * 5. ParameterValue
         * */

        for (int row = 1; row < bodySheet.getRows(); row++) {
            String name = bodySheet.getCell(0, row).getContents();
            String description = bodySheet.getCell(1, row).getContents();
            String executionTime = bodySheet.getCell(2, row).getContents();
            String type = bodySheet.getCell(3, row).getContents();
            String parameter = bodySheet.getCell(4, row).getContents();
            String parameterValue = bodySheet.getCell(5, row).getContents();

            if (type.equals("set")) {
                Parameter para = new Parameter((Parameter) parameters.get(parameter));
                para.setValue(getValue(para.getValue().getClass().toString(), parameterValue));
                tasks.put(name, new SetParameter("", name, description, Long.parseLong(executionTime), para));
            }
        }
    }

    private void readParameters(Workbook workbook) {

        Sheet bodySheet = workbook.getSheet("Parameters");

        /**
         * Format;
         * 0. Name
         * 1. Description
         * 2. IsStateOf
         * 3. Type
         * 4. Value
         * 5. Unit.
         */

        for (int row = 1; row < bodySheet.getRows(); row++) {
            String name = bodySheet.getCell(0, row).getContents();
            String description = bodySheet.getCell(1, row).getContents();
            String isstateof = bodySheet.getCell(2, row).getContents();
            String type = bodySheet.getCell(3, row).getContents();
            String value = bodySheet.getCell(4, row).getContents();
            String unit = bodySheet.getCell(5, row).getContents();

            if (isstateof.equals("")) {
                parameters.put(name, new Parameter("", name, description, getValue(type, value), unit));
            }
            else {
                parameters.put(name, new State("", name, description, isstateof, Boolean.parseBoolean(value)));
            }
        }
    }

    protected Number getValue(String type, String value) {
        Number obj = null;

        if (type.equals("int") || type.contains("Integer")) {
            obj = Integer.parseInt(value);
        }
        else if (type.equals("double") || type.contains("Double")) {
            obj = Double.parseDouble(value);
        }
        else if (type.equals("float") || type.contains("Float")) {
            obj = Float.parseFloat(value);
        }
        else if (type.equals("long") || type.contains("Long")) {
            obj = Long.parseLong(value);
        }

        return obj;
    }

}
