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
import org.hbird.exchange.configurator.ReportStatus;
import org.hbird.exchange.configurator.StopComponent;
import org.hbird.exchange.constants.StandardComponents;
import org.hbird.exchange.core.Named;

public class BusinessCardTester extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(BusinessCardTester.class);

    @Handler
    public void process() throws InterruptedException {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        /** See if we have the businesscards of the Configurator. */
        Thread.sleep(8000);

        synchronized (businessCardListener.elements) {
            Boolean didArrive = false;
            for (Named obj : businessCardListener.elements) {
                /** Notice that the name given to the configurator in the assembly is 'Main Configurator' */
                if (obj.getIssuedBy().equals("Configurator")) {
                    didArrive = true;
                    break;
                }
            }

            azzert(didArrive, "Business card messages arrive from Configurator");
            businessCardListener.elements.clear();
        }

        startMonitoringArchive();

        Thread.sleep(3000);

        synchronized (businessCardListener.elements) {
            Boolean archiveDidArrive = false;
            for (Named obj : businessCardListener.elements) {
                if (obj.getIssuedBy().equals(StandardComponents.ARCHIVE)) {
                    archiveDidArrive = true;
                    break;
                }
            }

            azzert(archiveDidArrive, "Business card messages arrive from Archive");
            businessCardListener.elements.clear();
        }

        Thread.sleep(3000);

        Object data = injection.requestBody(new ReportStatus("SystemTest", "Configurator"));
        azzert(data != null, "Status received from Configurator.");

        /** Stop the archive and check that it is actually stopped. */
        injection.sendBody(new StopComponent("SystemTest", "Configurator", StandardComponents.ARCHIVE));

        businessCardListener.elements.clear();

        Thread.sleep(3000);

        synchronized (businessCardListener.elements) {
            Boolean archiveDidArrive = false;
            for (Named obj : businessCardListener.elements) {
                if (obj.getIssuedBy().equals(StandardComponents.ARCHIVE)) {
                    archiveDidArrive = true;
                    break;
                }
            }

            azzert(!archiveDidArrive, "Business card messages not arriving from Archive");
        }

        this.monitoringArchiveStarted = false;
        
        LOG.info("Finished");
    }
}
