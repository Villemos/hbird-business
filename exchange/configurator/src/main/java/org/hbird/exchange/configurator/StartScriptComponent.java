package org.hbird.exchange.configurator;

import java.util.HashMap;
import java.util.Map;

import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.scripting.ScriptExecutionRequest;

public class StartScriptComponent extends StartComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1184823993029236522L;
	
	public StartScriptComponent(String componentname, String scriptname, Map<String, String> bindings, Named output) {
		super(componentname, "StartScriptComponent", "Command to a configurator to start a scripting component.");

		ScriptExecutionRequest request = new ScriptExecutionRequest(scriptname, "", "javascript", output, bindings);
		addArgument("scriptdefinition",request);
	}		

	public StartScriptComponent(String issuedBy, String destination, String componentname, String scriptname, Map<String, String> bindings, Named output) {
		super(issuedBy, destination, componentname, "StartScriptComponent", "Command to a configurator to start a scripting component.");

		ScriptExecutionRequest request = new ScriptExecutionRequest(scriptname, "", "javascript", output, bindings);
		addArgument("scriptdefinition",request);
	}		

	/**
	 * Constructor to create a script component based on a script provided in the constructor and
	 * assigned an output object (Named)
	 * 
	 * @param issuedBy
	 * @param name
	 * @param script
	 * @param bindings
	 * @param output
	 */
	public StartScriptComponent(String componentname, String scriptname, String script, Named output, Map<String, String> bindings) {
		super(componentname, "StartScriptComponent", "Command to a configurator to start a scripting component.");

		ScriptExecutionRequest request = new ScriptExecutionRequest(scriptname, script, "javascript", output, bindings);
		addArgument("scriptdefinition", request);
	}		

	/**
	 * Constructor to create a script component using a script in the library and creating an output Parameter.
	 * 
	 * @param issuedBy
	 * @param name
	 * @param script
	 * @param bindings
	 * @param output
	 */	
	public StartScriptComponent(String componentname, String scriptname, String paraName, String paraType, String paraDescription, String paraUnit, String bindings) {
		super(componentname, "StartScriptComponent", "Command to a configurator to start a scripting component.");

		Parameter output = new Parameter("", paraName, paraType, paraDescription, 0d, paraUnit);

		Map<String, String> inputBinding = new HashMap<String, String>();
		String[] bindingPairs = bindings.split(":");
		for (String binding : bindingPairs) {
			String[] entries = binding.split("=");
			inputBinding.put(entries[0], entries[1]);
		}
		
		ScriptExecutionRequest request = new ScriptExecutionRequest(scriptname, "", "javascript", output, inputBinding);
		addArgument("scriptdefinition",request);
	}		
}
