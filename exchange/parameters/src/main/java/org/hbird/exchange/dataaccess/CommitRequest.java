package org.hbird.exchange.dataaccess;

import org.hbird.exchange.core.Command;

public class CommitRequest extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2865838932564359124L;

	public CommitRequest(String issuedBy, String destination) {
		super(issuedBy, destination, "CommitRequest", "Request to force the commitment of all stored data, i.e. flush cashes and buffers.");
	}
}
