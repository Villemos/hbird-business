package org.hbird.queuemanagement;

import org.hbird.exchange.core.Named;

public class RemoveQueueElement extends Named {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4855448569441799079L;

	/** The element to be removed. */
	protected Object element;
	
	{
		this.name = "RemoveQueueElement";
		this.description = "A request to remove a specific element in an queue.";
	}
	
}
