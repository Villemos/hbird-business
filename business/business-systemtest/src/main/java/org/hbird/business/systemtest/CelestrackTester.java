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
import org.hbird.business.celestrack.CelestrackComponent;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;

/**
 * @author Admin
 * 
 */
public class CelestrackTester extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(CelestrackTester.class);

    @Handler
    public void process() throws Exception {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        startMonitoringArchive();

        try {
            accessApi.getTleFor("DTUSAT");
            azzert(false, "No TLE for DTUSAT");
        }
        catch (Exception e) {

        }

        try {
            accessApi.getTleFor("CUTE-1");
            azzert(false, "No TLE for CUTE-1");
        }
        catch (Exception e) {

        }

        try {
            accessApi.getTleFor("QUAKESAT");
            azzert(false, "No TLE for QUAKESAT");
        }
        catch (Exception e) {

        }

        startableEntityManager.start(new CelestrackComponent(IDGroundSegment + "TLEloader"));

        Thread.sleep(10000);

        // Object object = accessApi.resolve("DTUSAT");
        Satellite object = accessApi.getById("DTUSAT", Satellite.class);
        azzert(object != null, "Satellite DTUSAT created.");
        TleOrbitalParameters parameter = accessApi.getTleFor(object.getID());
        azzert(parameter != null, "Have TLE for DTUSAT");

        LOG.info("Finished");
    }
}
