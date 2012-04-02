package org.hbird.queuemanagement;

import java.util.ArrayList;

import org.hbird.exchange.core.ServiceSpecification;

public class QueueManagementServiceSpecification extends ServiceSpecification {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5290598039194112375L;

	{
		this.name = "Queue Management Service";
		this.description = "The queue management supports changes to a queue.";
		
		this.logicalIn.put("clearQueue", new ClearQueue());
		this.logicalIn.put("dumpQueue", new DumpQueue());
		this.logicalIn.put("removeElement", new RemoveQueueElement());
		
		this.logicalOut.put("dump", new ArrayList<Object>());
	}
}
