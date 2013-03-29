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

import org.apache.camel.CamelContext;
import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.core.D3Vector;
import org.hbird.exchange.groundstation.Antenna;
import org.hbird.exchange.groundstation.Track;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.OrbitalState;

import eu.estcube.gs.radio.HamlibRadioPart;
import eu.estcube.gs.rotator.HamlibRotatorPart;

/**
 * @author Admin
 * 
 */
public class GroundStationDriverTester extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(GroundStationDriverTester.class);

    @Handler
    public void process(CamelContext context) throws InterruptedException {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        /** Issue request to start the websockets. */
        startWebSockets();

        Antenna antenna = (Antenna) parts.get("Antenna1");

        /** Get the drivers (part of the system model) */
        HamlibRotatorPart rotator = (HamlibRotatorPart) parts.get("Rotator");
        rotator.setIsPartOf(antenna);
        rotator.setFailOldRequests(false);

        HamlibRadioPart radio = (HamlibRadioPart) parts.get("Radio");
        radio.setIsPartOf(antenna);
        radio.setFailOldRequests(false);

        publishApi.publish(new StartComponent(rotator.getName(), rotator));
        publishApi.publish(new StartComponent(radio.getName(), radio));

        /** Give the driver a second to start. */
        Thread.sleep(5000);

        /**
         * The following D3Vectors, orbital states and contact events have been extracted using the navigation module
         * based on the
         * test data of the system tests.
         */
        D3Vector startPos = new D3Vector("SystemTest", "Position", "Position", "", 914048.2427495751d, -2486073.8818833837d, 6678705.0394312d);
        D3Vector startVel = new D3Vector("SystemTest", "Velocity", "Position", "", -687.9592365995297d, -6985.94125990074d, -2501.1427144107d);
        D3Vector startMom = new D3Vector("SystemTest", "Velocity", "Position", "", 5.287506667482877E10, -2.3085117174275174E9, -8.095804822474546E9);

        D3Vector endPos = new D3Vector("SystemTest", "Position", "Position", "", 671423.6805328343d, -4402265.16119629d, 5640483.384222631d);
        D3Vector endVel = new D3Vector("SystemTest", "Velocity", "Position", "", -941.57653768571d, -5884.1166145430825d, -4471.744422210289d);
        D3Vector endMom = new D3Vector("SystemTest", "Velocity", "Position", "", 5.2875066674828766E10d, -2.308511717427518E9d, -8.095804822474544E9d);

        long now = System.currentTimeMillis();
        long delta = 1363571566678l - 1363571275302l;

        OrbitalState startState = new OrbitalState("OrbitPredictor", "OrbitalState", "Predicted", "", now + 20000, "ESTCube-1", startPos, startVel, startMom,
                "", 0l, "");
        OrbitalState endState = new OrbitalState("OrbitPredictor", "OrbitalState", "Predicted", "", now + delta + 20000, "ESTCube-1", endPos, endVel, endMom,
                "", 0l, "");

        LocationContactEvent start = new LocationContactEvent("OrbitPredictor", "Predicted", now + 20000, "ES5EC", "", "ESTCube-1", true, startState, "", 0l,
                "");
        start.setVisible(true);
        LocationContactEvent end = new LocationContactEvent("OrbitPredictor", "Predicted", now + delta + 20000, "ES5EC", "", "ESTCube-1", false, endState, "",
                0l, "");
        end.setVisible(false);

        estcube1.setFrequency(10000D);

        /**
         * Send a tracking request to the driver. Notice that on creation of the driver we set 'failOldRequests' to
         * false
         * i.e. even if the request we now send is old, it will still be processed.
         */

        injection.sendBody(new Track("SystemTest", "ES5EC", estcube1, start, end));

        while (true) {
            Thread.sleep(20000);
        }

        // System.out.println("Received '" + groundStationPartListener.elements.size() + "' native commands:");
        // long startTime = ((NativeCommand) groundStationPartListener.elements.get(0)).getExecutionTime();
        // for (Named entry : groundStationPartListener.elements) {
        // System.out.print(((NativeCommand) entry).getExecutionTime() - startTime + " (" + ((NativeCommand)
        // entry).getExecutionTime() + ") " + ((NativeCommand) entry).getCommandToExecute());
        // }

        // LOG.info("Finished");
    }
}
