package org.hbird.exchange.dataaccess;

import java.util.List;

public class StateRequest extends DataRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5274506775743830200L;

	public StateRequest(String issuedBy, String isStateOf) {
		super(issuedBy, "any", "StateRequest", "A request for the state(s) of a Named object");

		arguments.put("isStateOf", isStateOf);
	}	

	public StateRequest(String issuedBy, String isStateOf, List<String> names) {
		super(issuedBy, "any", "StateRequest", "A request for the state(s) of a Named object");

		arguments.put("isStateOf", isStateOf);
		arguments.put("names", names);
	}	

	public StateRequest(String issuedBy, String destination, String isStateOf) {
		super(issuedBy, destination, "StateRequest", "A request for the state(s) of a Named object");

		arguments.put("isStateOf", isStateOf);
	}	
}
