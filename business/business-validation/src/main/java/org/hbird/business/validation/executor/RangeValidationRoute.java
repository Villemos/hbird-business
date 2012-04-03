package org.hbird.business.validation.executor;

import org.apache.camel.builder.RouteBuilder;
import org.hbird.exchange.rangecheck.ParameterRange;

public class RangeValidationRoute extends RouteBuilder{

	protected ParameterRange range = null;
	
	public RangeValidationRoute(ParameterRange range) {
		this.range = range;
	}

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
