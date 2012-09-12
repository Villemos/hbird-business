package org.hbird.exchange.configurator;

import org.hbird.exchange.scripting.ScriptExecutionRequest;

public class ScriptComponentRequest extends ComponentConfigurationRequest {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1184823993029236522L;
	
	public ScriptExecutionRequest request = null;
	
	public ScriptComponentRequest(String issuedBy, String name, String description, String script) {
		request = new ScriptExecutionRequest(issuedBy, name, description, script);
	}
}
