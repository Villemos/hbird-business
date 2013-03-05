package org.hbird.business.systemtest;

import java.util.Date;
import java.util.Map;


import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.IQueueManagement;
import org.hbird.exchange.commandrelease.CommandRequest;

public class QueueManagerTester extends SystemTest {

	private static org.apache.log4j.Logger LOG = Logger.getLogger(QueueManagerTester.class);
	
	@Handler
	public void process() throws Exception {
	
		LOG.info("------------------------------------------------------------------------------------------------------------");
		LOG.info("Starting");
		
		startQueueManager();
		
		Thread.sleep(2000);

		/** List all queues. */
		IQueueManagement api = ApiFactory.getQueueManagementApi("SystemTest");
				
		/** Purge all. */
		api.clearAll();
		
		Thread.sleep(2000);
		
		Date now = new Date();
		
		/** Send a few scheduled requests. */
		CommandRequest req1 = new CommandRequest("SystemTest", "REQ_1", "", "", null, null, null, now.getTime() + 15000);
		CommandRequest req2 = new CommandRequest("SystemTest", "REQ_1", "", "", null, null, null, now.getTime() + 20000);
		CommandRequest req3 = new CommandRequest("SystemTest", "REQ_1", "", "", null, null, null, now.getTime() + 25000);
		CommandRequest req4 = new CommandRequest("SystemTest", "REQ_1", "", "", null, null, null, now.getTime() + 30000);
		CommandRequest req5 = new CommandRequest("SystemTest", "REQ_1", "", "", null, null, null, now.getTime() + 35000);
		
		req1.setDatasetidentifier("systemtest.datasetidentifier");
		req2.setDatasetidentifier("systemtest.datasetidentifier");
		req4.setDatasetidentifier("test.datasetidentifier");
		
		injection.sendBody(req1);
		injection.sendBody(req2);
		injection.sendBody(req3);
		injection.sendBody(req4);
		injection.sendBody(req5);
		
		/** Retrieve the schedule. */
		Map<String, String> entries = api.viewQueue("hbird.requests");
		// azzert(entries.size() == 5, "Expect 5 entries in the queue.");
		
		/** Delete an entry. */
		entries = api.removeQueueElements("hbird.requests", entries.entrySet().iterator().next().getKey());
		// azzert(entries.size() == 4, "Expect 4 entries in the queue.");

		LOG.info("Finished");
	}
}
