package org.hbird.exchange.dataaccess;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;

public class DeletionRequest extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7374733690642382839L;

	{
		arguments.put("deletionquery", new CommandArgument("deletionquery", "The query based on which data will be deleted.", "String", "", null, true));
	}
	
	public DeletionRequest(String issuedBy, String destination, String deletionquery) {
		super(issuedBy, destination, "DeletionRequest", "A request to delete part or all data in an archive.");
		addArgument("deletionquery", deletionquery);
	}	
}
