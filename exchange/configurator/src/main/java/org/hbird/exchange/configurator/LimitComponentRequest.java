package org.hbird.exchange.configurator;

import org.hbird.exchange.validation.Limit;
import org.hbird.exchange.validation.Limit.eLimitType;

public class LimitComponentRequest extends ComponentConfigurationRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8047192684233318282L;

	public LimitComponentRequest(eLimitType type, String ofParameter, String limit, String stateName, String description) {
		
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
		
		this.limit = new Limit(type, ofParameter, value, stateName, description);
	}
	
	public Limit limit;
}
