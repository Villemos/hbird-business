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
package org.hbird.business.groundstation.hamlib;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.hbird.exchange.constants.StandardArguments;

/**
 *
 */
public class SetHamlibNativeCommandHeaders implements Processor {

    public static final String HEADER_STAGE = "stage";
    public static final String HEADER_COMMAND_ID = "commandID";
    public static final String HEADER_EXECUTION_TIME = "executiontime";

    /**
     * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        Message in = exchange.getIn();
        Message out = exchange.getOut();
        out.copyFrom(in);

        HamlibNativeCommand command = in.getBody(HamlibNativeCommand.class);
        out.setHeader(HEADER_STAGE, command.getStage());
        out.setHeader(StandardArguments.DERIVED_FROM, command.getDerivedFrom());
        out.setHeader(HEADER_COMMAND_ID, command.getInstanceID());
        out.setHeader(HEADER_EXECUTION_TIME, command.getExecutionTime());
    }
}
