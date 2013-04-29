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

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.business.groundstation.hamlib.radio.HamlibRadioPart;
import org.hbird.business.groundstation.hamlib.rotator.HamlibRotatorPart;
import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.groundstation.Antenna;

/**
 * @author Admin
 * 
 */
public class EndToEndTrackingTester extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(EndToEndTrackingTester.class);

    @Handler
    public void process() throws InterruptedException {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        startMonitoringArchive();
        startOrbitPredictor();
        startTaskComponent("TaskExecutor");
        startCommandingChain();

        /** Issue request to start the websockets. */
        ;
        startWebSockets();

        /** Create an antenna controller task and inject it. */
        startAntennaController();

        publishTleParameters();
        publishGroundStationsAndSatellites();

        forceCommit();

        Antenna antenna = (Antenna) parts.get("Antenna1");

        /** Get the drivers (part of the system model) */
        HamlibRotatorPart rotator = (HamlibRotatorPart) parts.get("Rotator");
        rotator.setIsPartOf(antenna);

        HamlibRadioPart radio = (HamlibRadioPart) parts.get("Radio");

        radio.setIsPartOf(antenna);

        publishApi.publish(new StartComponent(rotator.getName(), rotator));
        publishApi.publish(new StartComponent(radio.getName(), radio));

        /** Give the driver a second to start. */
        Thread.sleep(5000);

        List<String> locations = new ArrayList<String>();
        locations.add(es5ec.getName());
        // locations.add(gsAalborg.getName());

        /**
         * Create a controller task and inject it.
         * 
         * We set
         * Lead Time (frequency of check whether orbit needs to be propagated) to 60 minutes.
         * Interval (the time for which orbit data must as a minimum be available) to 6es hours.
         * */
        startEstcubeOrbitPropagator();

        /** Send a TLE request for a satellite and a subset of locations */
        // IOrbitPrediction api = ApiFactory.getOrbitPredictionApi("SystemTest");
        // api.requestOrbitPropagationStream("ESTCube-1", locations, 1355385448149l, 1355385448149l + 2 * 60 * 60 *
        // 1000);

        boolean cont = true;
        while (cont) {
            Thread.sleep(2000);
        }

        LOG.info("Finished");
    }
}
