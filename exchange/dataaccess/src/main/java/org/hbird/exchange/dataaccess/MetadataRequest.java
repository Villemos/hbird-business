package org.hbird.exchange.dataaccess;

import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.NamedInstanceIdentifier;

public class MetadataRequest extends DataRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -87368332296845820L;

	{
		arguments.put("applicableto", new CommandArgument("applicableto", "The Named object the metadata must be appliable to.", "String", "", null, false));
	}
	
	public MetadataRequest(String issuedBy, Named applicableTo) {
		super(issuedBy, "Archive");
		
		this.addArgument("applicableto", new NamedInstanceIdentifier(applicableTo.getName(), applicableTo.getTimestamp(), applicableTo.getType()));
		setType("Metadata");
	}
}
