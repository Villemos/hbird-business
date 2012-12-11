package org.hbird.exchange.configurator;

import org.hbird.exchange.validation.Limit;
import org.hbird.exchange.validation.Limit.eLimitType;


public class StartLimitComponent extends StartComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8047192684233318282L;

	public StartLimitComponent(String limitName, eLimitType type, String ofParameter, String limit, String stateName, String stateDescription) {
		super(limitName, "StartLimitComponent", "Command to a configurator to start a limit component.");
		addLimit(type, ofParameter, limit, stateName, stateDescription);
	}	

	public StartLimitComponent(String issuedBy, String destination, String limitName, eLimitType type, String ofParameter, String limit, String stateName, String stateDescription) {
		super(issuedBy, destination, limitName, "StartLimitComponent", "Command to a configurator to start a limit component.");
		addLimit(type, ofParameter, limit, stateName, stateDescription);
	}	

	public void addLimit(eLimitType type, String ofParameter, String limit, String stateName, String description) {
		
		Number value = null;
		if (limit.endsWith("i")) {
			value = Integer.parseInt(limit);
		}
		else if (limit.endsWith("f")) {
			value = Float.parseFloat(limit);
		}
		else if (limit.endsWith("s")) 
		{
			value = Short.parseShort(limit);
		}
		else {
			value = Double.parseDouble(limit);
		}
		
		addArgument("limit", new Limit(type, ofParameter, value, stateName, description));
	}
}
