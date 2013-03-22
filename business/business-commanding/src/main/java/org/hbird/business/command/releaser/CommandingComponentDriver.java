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
package org.hbird.business.command.releaser;

import org.apache.camel.model.ProcessorDefinition;
import org.hbird.business.core.FieldBasedSplitter;
import org.hbird.business.core.SoftwareComponentDriver;
import org.hbird.exchange.configurator.StandardEndpoints;
import org.hbird.exchange.constants.StandardArguments;

/**
 * Route builder to create a commanding chain.
 * 
 * @author Gert Villemos
 */
public class CommandingComponentDriver extends SoftwareComponentDriver {

    @Override
    public void doConfigure() {

        /** Create the route and the interface for getting the command states. */
        from("direct:statestore").to("solr:statestore");

        CommandReleaser releaser = new CommandReleaser();

        /**
         * Read from the scheduled queue and release the command. The release consists of the validation
         * that all lock states are valid and the ejection of the command itself.
         * */
        from(StandardEndpoints.requests)
                .bean(releaser)
                .wireTap("seda:reportCommandRequestState")
                .choice()
                .when(header("Valid").isEqualTo(true)).to("seda:validCommandRequests")
                .otherwise().to(StandardEndpoints.failedRequests);

        /** Extract the tasks from the command request and schedule them. Extract the command and release it. */
        ProcessorDefinition<?> route = from("seda:validCommandRequests")
                .wireTap("seda:taskExtractor")
                .setBody(simple("${in.body.command}"))
                .setHeader(StandardArguments.DESTINATION, simple("${in.body.destination}"));
        addInjectionRoute(route);

        FieldBasedSplitter splitter = new FieldBasedSplitter();

        route = from("seda:taskExtractor")
                .split().method(splitter);
        addInjectionRoute(route);

        addCommandHandler();
    }
}
