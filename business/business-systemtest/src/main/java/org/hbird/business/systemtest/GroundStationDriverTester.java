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

        // // TODO Fix or delete
        // /** Issue request to start the websockets. */
        // startWebSockets();
        //
        // Antenna antenna = (Antenna) parts.get("Antenna1");
        //
        // /** Get the drivers (part of the system model) */
        // HamlibRotatorPart rotator = (HamlibRotatorPart) parts.get("Rotator");
        //
        // HamlibRadioPart radio = (HamlibRadioPart) parts.get("Radio");
        //
        // publishApi.publish(new StartComponent(rotator.getName(), rotator));
        // publishApi.publish(new StartComponent(radio.getName(), radio));
        //
        // /** Give the driver a second to start. */
        // Thread.sleep(5000);
        //
        // /**
        // * The following D3Vectors, orbital states and contact events have been extracted using the navigation module
        // * based on the
        // * test data of the system tests.
        // */
        // Vector3D startPos = new Vector3D(914048.2427495751d, -2486073.8818833837d, 6678705.0394312d);
        // Vector3D startVel = new Vector3D(-687.9592365995297d, -6985.94125990074d, -2501.1427144107d);
        // Vector3D startMom = new Vector3D(5.287506667482877E10, -2.3085117174275174E9, -8.095804822474546E9);
        //
        // Vector3D endPos = new Vector3D(671423.6805328343d, -4402265.16119629d, 5640483.384222631d);
        // Vector3D endVel = new Vector3D(-941.57653768571d, -5884.1166145430825d, -4471.744422210289d);
        // Vector3D endMom = new Vector3D(5.2875066674828766E10d, -2.308511717427518E9d, -8.095804822474544E9d);
        //
        // long now = System.currentTimeMillis();
        // long delta = 1363571566678l - 1363571275302l;
        //
        // OrbitalState startState = new OrbitalState("OrbitPredictor", "OrbitalState", "", now + 20000, "ESTCube-1",
        // startPos, startVel, startMom, null);
        //
        // LocationContactEvent contact = new LocationContactEvent("OrbitPredictor", now, "ES5EC", "ESTCube-1", "", now
        // + 20000, now + delta + 20000, 10203L);
        // contact.setSatelliteStateAtStart(startState);
        //
        // estcube1.setUplinkFrequency(10000L);
        //
        // /**
        // * Send a tracking request to the driver. Notice that on creation of the driver we set 'failOldRequests' to
        // * false
        // * i.e. even if the request we now send is old, it will still be processed.
        // */
        //
        // injection.sendBody(new Track("SystemTest", "ES5EC", estcube1, contact));
        //
        // while (true) {
        // Thread.sleep(20000);
        // }
        //
        // System.out.println("Received '" + groundStationPartListener.elements.size() + "' native commands:");
        // long startTime = ((NativeCommand) groundStationPartListener.elements.get(0)).getExecutionTime();
        // for (Named entry : groundStationPartListener.elements) {
        // System.out.print(((NativeCommand) entry).getExecutionTime() - startTime + " (" + ((NativeCommand)
        // entry).getExecutionTime() + ") " + ((NativeCommand) entry).getCommandToExecute());
        // }
        //
        // LOG.info("Finished");
    }
}
