package org.hbird.exchange.configurator;

import org.hbird.exchange.validation.Limit;
import org.hbird.exchange.validation.Limit.eLimitType;
import org.hbird.exchange.validation.Limit.eValueType;

public class LimitComponentRequest extends ComponentConfigurationRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8047192684233318282L;

	public LimitComponentRequest(eLimitType type, String ofParameter, String limit, eValueType limittype, String stateName, String description) {
		this.limit = new Limit(type, ofParameter, limit, limittype, stateName, description);
	}

	
	public Limit limit;
}
