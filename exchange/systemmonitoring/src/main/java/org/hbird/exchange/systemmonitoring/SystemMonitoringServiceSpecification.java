package org.hbird.exchange.systemmonitoring;

import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.ServiceSpecification;

public class SystemMonitoringServiceSpecification extends ServiceSpecification {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2126772575708567216L;

	{
		this.name = "";
		this.description = "";
		
		this.logicalOut.put("monitoring", new Parameter());
	}
	
}
