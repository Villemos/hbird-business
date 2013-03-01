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
package org.hbird.queuemanagement;

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;

public class ClearQueue extends Command {

    private static final long serialVersionUID = 9018116335238621646L;

    public ClearQueue(String issuedBy, String destination, String queuename) {
        super(issuedBy, destination, "ClearQueue", "Command to clear the queue.");

        setQueueName(queuename);
    }

    /**
     * @see org.hbird.exchange.core.Command#getArgumentDefinitions()
     */
    @Override
    protected List<CommandArgument> getArgumentDefinitions() {
        List<CommandArgument> args = new ArrayList<CommandArgument>(1);
        args.add(new CommandArgument(StandardArguments.QUEUE_NAME, "The name of the queue to be displayed.", "String (ID)",
                "", "hbird.requests", true));
        return args;
    }

    public String getQueueName() {
        return getArgumentValue(StandardArguments.QUEUE_NAME, String.class);
    }

    public void setQueueName(String queuename) {
        setArgumentValue(StandardArguments.QUEUE_NAME, queuename);
    }
}
