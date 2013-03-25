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

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.hbird.business.core.InMemoryScheduler;
import org.hbird.business.core.SoftwareComponentDriver;
import org.hbird.exchange.configurator.StandardEndpoints;
import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.interfaces.IPart;
import org.hbird.exchange.interfaces.IStartablePart;
import org.springframework.beans.factory.annotation.Value;

import eu.estcube.domain.config.ConfigurationBase;
import eu.estcube.gs.hamlib.HamlibDriverConfiguration;
import eu.estcube.gs.hamlib.HamlibIO;

/**
 * Abstract base class for device drivers.
 * 
 * @author Gert Villemos
 *
 */
public class HamlibDriver extends SoftwareComponentDriver {

	@Value("#{config}")
	protected ConfigurationBase config;

	@Value("#{subdrivers}")
	protected List<HamlibDriver> partDrivers = new ArrayList<HamlibDriver>();

	@Value("#{context}")
	protected CamelContext context = null;

	protected String groundStationId;
	protected Verifier verifier = null;
	protected InMemoryScheduler inMemoryScheduler = null;
	
	protected boolean failOnOldCommand = true;

	
    public HamlibDriver(String groundstationId, IStartablePart part, Verifier verifier, InMemoryScheduler inMemoryScheduler) {
    	this.groundStationId = groundstationId;
        this.part = part;
        this.verifier = verifier;
        this.inMemoryScheduler = inMemoryScheduler;
    }

	/**
	 * Initialization method which can be used as part of the bean creation, for example
	 * 	<li><bean id="driver" class="eu.estcube.gs.GroundStationDriver" init-method="init"/></li>
	 */
	public void init() {
		try {
			/** Add the routes of this driver. */
			context.addRoutes(this);
			
			/** Add the routes of any sub drivers. */
			for (HamlibDriver subdriver : partDrivers) {
				subdriver.init();
			}
			
			context.start();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
    @Override
    public void doConfigure() {

    	String name = part.getQualifiedName();
    	String nameDot = part.getQualifiedName(".");
    	
        /** Setup route for the PART to receive the commands. The part will generate the 
         * NativeCommands. These will be routed to the INTERNAL schedule. 
         * 
         * The NativeCommands are at their execution time read through the EXECUTION below. */
        from(StandardEndpoints.commands + "?selector=name='Track'")
            .log("Received 'Track' command from '" + simple("${body.issuedBy}").getText() + "'with destination '" + groundStationId + "'. Will generate Hamlib commands for '" + name + "'")
            .split().method(part, "track")
                .setHeader("stage", simple("${body.stage}"))
                .setHeader("derivedfrom", simple("${body.derivedfrom}"))
                .setHeader("commandid", simple("${body.commandid}"))
                .setHeader("executiontime", simple("${body.executionTime}"))
                .bean(verifier, "register")
                .bean(inMemoryScheduler, "add")
         .routeId(name + ": Command injection");

        
        /** Setup route for the PART to receive the verifications. The part will verify the respond and if
         * it fails, or the verification stage is complete, issue a State. */
        from("direct:hbird" + nameDot + ".verification")
            .bean(verifier, "verify")
            .choice()
                .when(simple("${in.body} == null")).stop()
                .otherwise().to(StandardEndpoints.monitoring)
            .end()
            .routeId(name + ": Verification");
        
        
        /** Setup the EXECUTION route. The route reads the scheduled NativeCommands from the INTERNAL topic, send each to 
         * Hamlib through TCPIP and route the response back to the INTERNAL topic. */

        /** Read from the internal queue. The commands on the queue will be scheduled, i.e. be send to the
         * driver when they need to be executed. 
         * 
         * The execution path should be as short and efficient as possible. The verification is not done in 
         * this thread route but in a separate route. */
        from(inMemoryScheduler.getInjectUrl())
            .setHeader("stage", simple("${body.stage}"))
            .setHeader("derivedfrom", simple("${body.derivedfrom}"))
            .setHeader("commandid", simple("${body.commandid}"))
            .setHeader("executiontime", simple("${body.executionTime}"))
            .bean(new NativeCommandExtractor())
            .log(LoggingLevel.INFO, "Sending command '" + simple("${body}").getText() + "' to " + name)
            .inOut(HamlibIO.getDeviceDriverUrl((HamlibDriverConfiguration) config))
            .removeHeader("AMQ_SCHEDULED_DELAY")
            .to("direct:hbird" + nameDot + ".verification")
        .routeId(name + ": Command execution");
        
        
        
        /** Create route for cleanup of stages. */
        from("timer://cleanup?period=10000")
        .split().method(verifier, "cleanup")
            .to(StandardEndpoints.monitoring)
            .routeId(name + ": Cleanup");

        from("direct:parameters" + nameDot)
        	.bean(new OnChange())
        	.choice()
        	.when(header("hbird.haschanged").isEqualTo(false))
        		.log(LoggingLevel.INFO, "Not sending parameter. Not updated.")
        	.otherwise()
        		.setHeader(StandardArguments.NAME, simple("${in.body.name}"))
        		.setHeader(StandardArguments.ISSUED_BY, simple("${in.body.issuedBy}"))
        		.setHeader(StandardArguments.TYPE, simple("${in.body.type}"))
        		.setHeader(StandardArguments.DATA_SET_ID, simple("${in.body.datasetidentifier}"))
        		.setHeader("class", simple("${in.body.class.simpleName}"))
        		.to(StandardEndpoints.monitoring);

        // @formatter: on
    }

	public ConfigurationBase getConfig() {
		return config;
	}

	public void setConfig(ConfigurationBase config) {
		this.config = config;
	}

	public List<HamlibDriver> getPartDrivers() {
		return partDrivers;
	}

	public void setPartDrivers(List<HamlibDriver> partDrivers) {
		this.partDrivers = partDrivers;
	}
	
	public void addDriver(HamlibDriver driver) {
		this.partDrivers.add(driver);
	}

	public void setContext(CamelContext context) {
		this.context = context;
	}

	public boolean isFailOnOldCommand() {
		return failOnOldCommand;
	}

	public void setFailOnOldCommand(boolean failOnOldCommand) {
		this.failOnOldCommand = failOnOldCommand;
	}
}
