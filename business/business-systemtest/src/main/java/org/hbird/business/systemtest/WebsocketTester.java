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
package org.hbird.business.systemtest;

import java.util.Date;

import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.business.parts.WebsocketInterfaceComponent;
import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.core.Event;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;

/**
 * @author Admin
 *
 */
public class WebsocketTester extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(WebsocketTester.class);

    @Handler
    public void process() throws InterruptedException {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

    	/** Issue request to start the websockets. */
    	injection.sendBody(new StartComponent("SystemTest", new WebsocketInterfaceComponent("websockets")));
    	
    	Double value = 1d;
    	
    	boolean state = true;
    	
    	/** Circle and insert log messages and parameters... */
    	while (true) {
    		injection.sendBody(new Parameter("SystemTest", "/groundstation/TARTU/Rotator/Temperature", "Temperature", "A test parameter", value, "Celcius"));
    		injection.sendBody(new Parameter("SystemTest", "/groundstation/TARTU/Rotator/Longitude", "Longitude", "Another test parameter", value, "Radians"));
    		injection.sendBody(new Parameter("SystemTest", "/groundstation/TARTU/Radio/Frequency", "Frequency", "Another test parameter", value, "MHerz"));
    		
    		value++;
    		LOG.info("Publishing parameter 'PARA1' with value " + value);
    		
    		LOG.warn("A warning");
    		LOG.error("An error");
    		
    		publishApi.publish(new Event("SystemTest", "Event1", "TestEvent", "A test event", (new Date()).getTime()));

    		publishApi.publish(new State("SystemTest", "State2", "Lower Limit", "The lower limit of PARA1 (test)", "/groundstation/TARTU/Rotator/Temperature", true));
    		publishApi.publish(new State("SystemTest", "State1", "Upper Limit", "The upper limit of PARA1 (test)", "/groundstation/TARTU/Rotator/Temperature", state));
    		state = !state;    		
    		
    		Thread.sleep(1000);
    	}
    }
	
}