package org.hbird.exchange.parameters;

import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.ServiceSpecification;

public class ParameterAccessServiceSpecification extends ServiceSpecification {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8754428054826898070L;

	{
		this.name = "Parameter Access Service";
		this.description = "Service to store and access Parameters. The input String is a SQL92 formatted queue string. It may contain specifications of maximal count and/or sort criterion. Specific archives may support alternative queue formats. The result is zero or more Parameters.";
		
		this.logicalIn.put("storage", new Parameter());
		this.logicalIn.put("retrieve", new String());
		this.logicalOut.put("results", new Parameter());
	}
	
}
