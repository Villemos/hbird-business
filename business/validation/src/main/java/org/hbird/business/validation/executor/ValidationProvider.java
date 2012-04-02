package org.hbird.business.validation.executor;

import org.hbird.exchange.rangecheck.ParameterRange;

public class ValidationProvider {

	/** Method for setting up the routes to support validation of a range. */
	public void processRange(ParameterRange range) {
		new RangeValidationRoute(range);
	}
}
