package org.hbird.exchange.tasking;

import org.hbird.exchange.core.ServiceSpecification;

public class TaskingServiceSpecification extends ServiceSpecification {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2214921576345413261L;

	{
		this.name = "Tasking Service";
		this.description = "The tasking receives Tasks as input. The tasks can have a release time set. The tasking service will delay the task until the given time. It will then execute the task. The task may output one or more objects.";
		
		this.logicalIn.put("input", new Task());
		
		this.logicalOut.put("output", new Object());
	}
}
