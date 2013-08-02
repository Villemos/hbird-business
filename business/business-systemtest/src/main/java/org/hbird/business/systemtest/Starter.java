package org.hbird.business.systemtest;

import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.IQueueManagement;

public class Starter extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(Starter.class);

    @Handler
    public void process() throws Exception {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        LOG.info("Purging all activemq topics and queues.");

        IQueueManagement api = ApiFactory.getQueueManagementApi("SystemTest");

        for (String queueName : api.listQueues()) {
            LOG.info(" - Purging queue '" + queueName + "'.");
            api.clearQueue(queueName);
        }

        for (String topicName : api.listTopics()) {
            LOG.info(" - Purging topic '" + topicName + "'.");
            api.clearTopic(topicName);
        }

        LOG.info("Finished");
    }
}
