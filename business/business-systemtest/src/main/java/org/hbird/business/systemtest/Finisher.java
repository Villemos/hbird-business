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

import java.io.IOException;
import java.net.MalformedURLException;

import javax.management.MalformedObjectNameException;

import org.apache.camel.Exchange;
import org.apache.log4j.Logger;
import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.IQueueManagement;

import com.mongodb.Mongo;

public class Finisher extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(Finisher.class);

    public void process(Exchange exchange) throws MalformedObjectNameException, MalformedURLException, NullPointerException, IOException, Exception {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        LOG.info("System Test done.");

        LOG.info("Purging all activemq topics and queues.");

        /** Check that the antenna schedule has been filled. */
        IQueueManagement api = ApiFactory.getQueueManagementApi("SystemTest");

        for (String queueName : api.listQueues()) {
            LOG.info(" - Purging queue '" + queueName + "'.");
            api.clearQueue(queueName);
        }

        for (String topicName : api.listTopics()) {
            LOG.info(" - Purging topic '" + topicName + "'.");
            api.clearTopic(topicName);
        }

        Mongo mongo = getContext().getRegistry().lookup("mongo", Mongo.class);
        mongo.dropDatabase("hbird_test");

        Thread.sleep(2000);

        LOG.info("Ciao!");
        LOG.info("------------------------------------------------------------------------------------------------------------");

        System.exit(1);
    }
}
