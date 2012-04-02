package org.hbird.queuemanagement;

import org.hbird.exchange.core.Named;

public class DumpQueue extends Named {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3485417554095535572L;

	{
		this.name = "DumpQueue";
		this.description = "Returns a List of all elements in the queue.";
	}
}
