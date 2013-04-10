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
import org.hbird.exchange.configurator.StartComponent;

/**
 * @author Admin
 *
 */
public class CelestrackTester extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(CelestrackTester.class);

    @Handler
    public void process() throws InterruptedException {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        startMonitoringArchive();
        
        azzert(accessApi.retrieveTleFor("DTUSAT") == null, "No TLE for DTUSAT");
        azzert(accessApi.retrieveTleFor("CUTE-1 (CO-55)") == null, "No TLE for CUTE-1");
        azzert(accessApi.retrieveTleFor("QUAKESAT") == null, "No TLE for QUAKESAT");
        
        publishApi.publish(new StartComponent("Celestrack", new CelestrackComponent()));
        
        Thread.sleep(10000);
        
        azzert(accessApi.retrieveTleFor("DTUSAT") != null, "Have TLE for DTUSAT");
        azzert(accessApi.retrieveTleFor("CUTE-1 (CO-55)") != null, "Have TLE for CUTE-1");
        azzert(accessApi.retrieveTleFor("QUAKESAT") != null, "Have TLE for QUAKESAT");
        
		LOG.info("Finished");
    }	
}
