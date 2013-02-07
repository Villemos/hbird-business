package org.hbird.queuemanagement;

import org.hbird.exchange.core.Command;

public class ListQueues extends Command {

	private static final long serialVersionUID = 1924299427283607320L;

	public ListQueues(String issuedBy, String destination) {
		super(issuedBy, destination, "ListQueues", "Command to list all queues of the system.");
	}
}
