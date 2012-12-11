package org.hbird.exchange.dataaccess;

import org.hbird.exchange.core.Command;

public class DeletionRequest extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7374733690642382839L;

	public DeletionRequest(String issuedBy, String destination, String deletionquery) {
		super(issuedBy, destination, "DeletionRequest", "A request to delete part or all data in an archive.");
		arguments.put("deletionquery", deletionquery);
	}	
}
