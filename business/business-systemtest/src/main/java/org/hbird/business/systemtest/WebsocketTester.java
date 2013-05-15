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

import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.business.websockets.WebsocketInterfaceComponent;
import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.constants.StandardMissionEvents;

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
        StartComponent request = new StartComponent("SystemTest");
        request.setEntity(new WebsocketInterfaceComponent("WEBSOCKET"));
        publishApi.publish(request);

        boolean state = true;

        /** Circle and insert log messages and parameters... */
        for (double value = 0; value < 20; value++) {
            publishApi.publishParameter("PARA1", "PARA1", "A test parameter", value, "Celcius");
            publishApi.publishParameter("PARA2", "PARA2", "Another test parameter", value, "Radians");
            publishApi.publishParameter("PARA3", "PARA3", "Another test parameter", value, "MHerz");

            LOG.info("Publishing parameter 'PARA1' with value " + value);

            LOG.warn("A warning");
            LOG.error("An error");

            publishApi.publish(StandardMissionEvents.CONTROL_REESTABLISHED.cloneEntity());

            publishApi.publishState("STATE2", "STATE2", "The lower limit of PARA1 (test)", "/groundstation/TARTU/Rotator/Temperature", true);
            publishApi.publishState("STATE1", "STATE1", "The upper limit of PARA1 (test)", "/groundstation/TARTU/Rotator/Temperature", state);
            state = !state;

            Thread.sleep(1000);
        }
    }
}
