package org.hbird.business.systemtest;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.camel.Handler;
import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.configurator.StartQueueManagerComponent;
import org.hbird.queuemanagement.ClearQueue;
import org.hbird.queuemanagement.ListQueues;
import org.hbird.queuemanagement.QueueHelper;
import org.hbird.queuemanagement.RemoveQueueElements;
import org.hbird.queuemanagement.ViewQueue;

public class QueueManagerTester extends SystemTest {

	@Handler
	public void process() throws InterruptedException {
	
		startQueueManager();
		
		Thread.sleep(2000);

		/** List all queues. */
		List<String> queues = (List<String>) injection.requestBody(new ListQueues("SystemTest", "CommandingQueueManager"));
				
		/** Purge all. */
		for (String canonicalName : queues) {
			injection.requestBody(new ClearQueue("SystemTest", "CommandingQueueManager", QueueHelper.getQueueName(canonicalName)));
		}
		
		Thread.sleep(2000);
		
		Date now = new Date();
		
		/** Send a few scheduled requests. */
		CommandRequest req1 = new CommandRequest("SystemTest", "REQ_1", "", null, null, null, now.getTime() + 15000);
		CommandRequest req2 = new CommandRequest("SystemTest", "REQ_1", "", null, null, null, now.getTime() + 20000);
		CommandRequest req3 = new CommandRequest("SystemTest", "REQ_1", "", null, null, null, now.getTime() + 25000);
		CommandRequest req4 = new CommandRequest("SystemTest", "REQ_1", "", null, null, null, now.getTime() + 30000);
		CommandRequest req5 = new CommandRequest("SystemTest", "REQ_1", "", null, null, null, now.getTime() + 35000);
		
		req1.setDatasetidentifier("systemtest.datasetidentifier");
		req2.setDatasetidentifier("systemtest.datasetidentifier");
		req4.setDatasetidentifier("test.datasetidentifier");
		
		injection.sendBody(req1);
		injection.sendBody(req2);
		injection.sendBody(req3);
		injection.sendBody(req4);
		injection.sendBody(req5);
		
		/** Retrieve the schedule. */
		Map<String, String> entries = (Map<String, String>) injection.requestBody(new ViewQueue("SystemTest", "CommandingQueueManager", "hbird.requests"));
		azzert(entries.size() == 5, "Expect 5 entries in the queue.");
		
		/** Delete an entry. */
		entries = (Map<String, String>) injection.requestBody(new RemoveQueueElements("SystemTest", "CommandingQueueManager", entries.entrySet().iterator().next().getKey()));
		azzert(entries.size() == 4, "Expect 4 entries in the queue.");

		entries = (Map<String, String>) injection.requestBody(new RemoveQueueElements("SystemTest", "CommandingQueueManager", "systemtest.", true));
		azzert(entries.size() == 2, "Expect 2 entries in the queue.");

	}
}
