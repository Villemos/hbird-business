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

import java.util.List;

import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.exchange.core.Part;

/**
 * @author Admin
 * 
 */
public class CatalogueApiTester extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(CatalogueApiTester.class);

    @Handler
    public void process() throws InterruptedException {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        startMonitoringArchive();

        /** Publish all parts. */
        for (Part part : parts.values()) {
            try {
                publishApi.publish(part);
            }
            catch (Exception e) {
                LOG.error("Failed to publish part " + part);
            }
        }

        Thread.sleep(3000);
        forceCommit();

        /** Test retrieve all parts. */
        List<Part> results = catalogueApi.getParts();
        azzert(results.size() == parts.values().size(), "Expected to receive " + parts.values().size() + " entries.");

        LOG.info("Finished");
    }
}
