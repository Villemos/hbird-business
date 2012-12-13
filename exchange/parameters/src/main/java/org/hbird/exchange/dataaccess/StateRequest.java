package org.hbird.exchange.dataaccess;

import java.util.List;

import org.hbird.exchange.core.State;

public class StateRequest extends DataRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5274506775743830200L;

	public StateRequest(String issuedBy, String isStateOf) {
		super(issuedBy, "any", "StateRequest", "A request for the state(s) of a Named object");

		setClass(State.class.getSimpleName());
		addArgument("isStateOf", isStateOf);
	}	

	public StateRequest(String issuedBy, String isStateOf, List<String> names) {
		super(issuedBy, "any", "StateRequest", "A request for the state(s) of a Named object");

		setClass(State.class.getSimpleName());
		addArgument("isStateOf", isStateOf);
		addArgument("names", names);
	}	

	public StateRequest(String issuedBy, String destination, String isStateOf) {
		super(issuedBy, destination, "StateRequest", "A request for the state(s) of a Named object");

		setClass(State.class.getSimpleName());
		addArgument("isStateOf", isStateOf);
	}	
}
