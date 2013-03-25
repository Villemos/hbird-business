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
import org.hbird.business.core.InMemoryScheduler;
import org.hbird.business.navigation.controller.OrbitPropagationController;
import org.hbird.exchange.core.Part;
import org.hbird.exchange.groundstation.Antenna;

import eu.estcube.gs.base.HamlibDriver;
import eu.estcube.gs.base.Verifier;
import eu.estcube.gs.hamlib.HamlibDriverConfiguration;
import eu.estcube.gs.radio.HamlibRadioPart;
import eu.estcube.gs.rotator.HamlibRotatorPart;

/**
 * @author Admin
 *
 */
public class StrandTrackingTester extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(StrandTrackingTester.class);

    @Handler
    public void process() throws InterruptedException {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        startMonitoringArchive();
        startOrbitPredictor();
        startTaskComponent("TaskExecutor");
        startCommandingChain();
        
    	/** Issue request to start the websockets. */;
    	startWebSockets();
        
		/** Create an antenna controller task and inject it. */
    	startStrandAntennaController();

		publishTleParameters();
		publishGroundStationsAndSatellites();

        forceCommit();        

        Antenna antenna = (Antenna) Part.getAllParts().get("Antenna1");

    	/** Get the drivers (part of the system model)*/
        HamlibRotatorPart rotator = (HamlibRotatorPart) Part.getAllParts().get("Rotator");
        rotator.setIsPartOf(antenna);
        rotator.setFailOldRequests(false);
        
        HamlibRadioPart radio = (HamlibRadioPart) Part.getAllParts().get("Radio");        
        radio.setIsPartOf(antenna);
        radio.setFailOldRequests(false);
        
        /** Create a verifier, which we need for the Drivers*/
        Verifier verifier = new Verifier();

        /** ./rigctld -m 1 -t 4532 */
        HamlibDriver radioDriver = new HamlibDriver("ES5EC", radio, verifier, new InMemoryScheduler(context.createProducerTemplate(), "direct:injection.radio"));
        radioDriver.setConfig(new HamlibDriverConfiguration("eu.estcube.gs.radio", "0.0.1-SNAPSHOT", 3000, "ES5EC", "dummy", "RADIO", 4532, "localhost",  60000l, 1000, 2));
        radioDriver.setContext(context);
        radioDriver.setFailOnOldCommand(false);
        radioDriver.init();
        
        /** ./rotctld -m 1 -t 4533 */
        HamlibDriver rotatorDriver = new HamlibDriver("ES5EC", rotator, verifier, new InMemoryScheduler(context.createProducerTemplate(), "direct:injection.rotator"));
        rotatorDriver.setConfig(new HamlibDriverConfiguration("eu.estcube.gs.rotator", "0.0.1-SNAPSHOT", 3000, "ES5EC", "dummy", "ROTATOR", 4533, "localhost",  60000l, 1000, 2));        
        rotatorDriver.setContext(context);
        rotatorDriver.setFailOnOldCommand(false);
        rotatorDriver.init();


        /** Give the driver a second to start. */
        Thread.sleep(5000);
            
        List<String> locations = new ArrayList<String>();
        locations.add(gsDarmstadt.getQualifiedName());

		/** Create a controller task and inject it. 
		 * 
		 * We set
		 *  Lead Time (frequency of check whether orbit needs to be propagated) to 60 minutes.
		 *  Interval (the time for which orbit data must as a minimum be available) to 6es hours.
		 * */
		OrbitPropagationController task = new OrbitPropagationController("SystemTest", "ESTcubeNavigation", "", 60 * 60 * 1000, 6 * 60 * 60 * 1000, strand.getQualifiedName(), locations);
		task.setRepeat(0);
		injection.sendBody(task);
        
        /** Send a TLE request for a satellite and a subset of locations */
        // IOrbitPrediction api = ApiFactory.getOrbitPredictionApi("SystemTest");
        // api.requestOrbitPropagationStream("ESTCube-1", locations, 1355385448149l, 1355385448149l + 2 * 60 * 60 * 1000);

        boolean cont = true;
        while (cont) {
        	Thread.sleep(2000);
        }
        
		LOG.info("Finished");
    }
}
