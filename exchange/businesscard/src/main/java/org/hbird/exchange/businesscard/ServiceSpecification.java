package org.hbird.exchange.businesscard;

import java.util.HashMap;
import java.util.Map;

import org.hbird.exchange.core.Named;

public class ServiceSpecification extends Named {

	/**
	 * 
	 */
	private static final long serialVersionUID = -635297382694846503L;

	protected Map<String, Named> logicalIn = new HashMap<String, Named>();
}
