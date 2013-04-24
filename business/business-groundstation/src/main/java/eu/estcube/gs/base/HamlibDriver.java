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
package eu.estcube.gs.base;

import org.apache.camel.LoggingLevel;
import org.hbird.business.core.InMemoryScheduler;
import org.hbird.business.core.SoftwareComponentDriver;
import org.hbird.exchange.configurator.StandardEndpoints;
import org.hbird.exchange.constants.StandardArguments;

/**
 * Abstract base class for device drivers.
 * 
 * @author Gert Villemos
 * 
 */
public abstract class HamlibDriver extends SoftwareComponentDriver {

    protected Verifier verifier = new Verifier();

    protected InMemoryScheduler inMemoryScheduler = new InMemoryScheduler(getContext().createProducerTemplate());

    protected boolean failOnOldCommand = true;

    @Override
    public void doConfigure() {

        String name = part.getName();

        inMemoryScheduler.setInjectUrl("direct://inject." + name);

        /**
         * Setup route for the PART to receive the commands. The part will generate the
         * NativeCommands. These will be routed to the INTERNAL schedule.
         * 
         * The NativeCommands are at their execution time read through the EXECUTION below.
         */
        from(StandardEndpoints.COMMANDS + "?selector=name='Track'")
                .log("Received 'Track' command from '" + simple("${body.issuedBy}").getText() + "'. Will generate Hamlib commands for '" + name + "'")
                .split().method(part, "track")
                .setHeader("stage", simple("${body.stage}"))
                .setHeader("derivedfrom", simple("${body.derivedfrom}"))
                .setHeader("commandid", simple("${body.commandid}"))
                .setHeader("executiontime", simple("${body.executionTime}"))
                .bean(verifier, "register")
                .bean(inMemoryScheduler, "add")
                .routeId(name + ": Command injection");

        /**
         * Setup route for the PART to receive the verifications. The part will verify the respond and if
         * it fails, or the verification stage is complete, issue a State.
         */
        from("direct:hbird" + name + ".verification")
                .bean(verifier, "verify")
                .choice()
                .when(simple("${in.body} == null")).stop()
                .otherwise().to(StandardEndpoints.MONITORING)
                .end()
                .routeId(name + ": Verification");

        /**
         * Setup the EXECUTION route. The route reads the scheduled NativeCommands from the INTERNAL topic, send each to
         * Hamlib through TCPIP and route the response back to the INTERNAL topic.
         */

        /**
         * Read from the internal queue. The commands on the queue will be scheduled, i.e. be send to the
         * driver when they need to be executed.
         * 
         * The execution path should be as short and efficient as possible. The verification is not done in
         * this thread route but in a separate route.
         */
        from(inMemoryScheduler.getInjectUrl())
                .setHeader("stage", simple("${body.stage}"))
                .setHeader("derivedfrom", simple("${body.derivedfrom}"))
                .setHeader("commandid", simple("${body.commandid}"))
                .setHeader("executiontime", simple("${body.executionTime}"))
                .bean(new NativeCommandExtractor())
                .log(LoggingLevel.INFO, "Sending command '" + simple("${body}").getText() + "' to " + name)
                .inOut("netty:tcp://" + getAddress())
                .removeHeader("AMQ_SCHEDULED_DELAY")
                .to("direct:hbird" + name + ".verification")
                .routeId(name + ": Command execution");

        /** Create route for cleanup of stages. */
        from("timer://cleanup?period=10000")
                .split().method(verifier, "cleanup")
                .to(StandardEndpoints.MONITORING)
                .routeId(name + ": Cleanup");

        from("direct:parameters" + name)
                .bean(new OnChange())
                .choice()
                .when(header("hbird.haschanged").isEqualTo(false))
                .log(LoggingLevel.INFO, "Not sending parameter. Not updated.")
                .otherwise()
                .setHeader(StandardArguments.NAME, simple("${in.body.name}"))
                .setHeader(StandardArguments.ISSUED_BY, simple("${in.body.issuedBy}"))
                .setHeader(StandardArguments.TYPE, simple("${in.body.type}"))
                .setHeader(StandardArguments.CLASS, simple("${in.body.class.simpleName}"))
                .to(StandardEndpoints.MONITORING);

        // @formatter: on
    }

    public boolean isFailOnOldCommand() {
        return failOnOldCommand;
    }

    public void setFailOnOldCommand(boolean failOnOldCommand) {
        this.failOnOldCommand = failOnOldCommand;
    }

    public abstract String getAddress();
}
