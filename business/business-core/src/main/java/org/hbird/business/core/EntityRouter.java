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
package org.hbird.business.core;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.configurator.StandardEndpoints;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Event;
import org.hbird.exchange.tasking.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class EntityRouter implements Processor {

    static final String ROUTING_HEADER = "hbird.route";

    private static final Logger LOG = LoggerFactory.getLogger(EntityRouter.class);

    public String route(Object body) {
        String result = StandardEndpoints.MONITORING;
        if (body instanceof Task) {
            result = StandardEndpoints.TASKS;
        }
        if (body instanceof CommandRequest) {
            result = StandardEndpoints.REQUESTS;
        }
        if (body instanceof Command) {
            result = StandardEndpoints.COMMANDS;
        }
        if (body instanceof Event) {
            result = StandardEndpoints.EVENTS;
        }

        LOG.trace("Routing '{}' to '{}'", body.getClass().getSimpleName(), result);

        return result;
    }

    /**
     * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        Message in = exchange.getIn();
        Message out = exchange.getOut();
        out.copyFrom(in);
        out.setHeader(ROUTING_HEADER, route(out.getBody()));
    }
}
