package org.hbird.exchange.scripting;

import org.hbird.exchange.core.ServiceSpecification;

public class ScriptingServiceSpecification extends ServiceSpecification {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6636849881184588639L;

	{
		this.name = "Script Execution Service";
		this.description = "This service takes a script as input and executes it.";
		
		this.logicalIn.put("script", new ScriptExecutionRequest());
	}
	
}
