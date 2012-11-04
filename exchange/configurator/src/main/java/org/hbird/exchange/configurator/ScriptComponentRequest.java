package org.hbird.exchange.configurator;

import java.util.HashMap;
import java.util.Map;

import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.scripting.ScriptExecutionRequest;

public class ScriptComponentRequest extends ComponentConfigurationRequest {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1184823993029236522L;
	
	public ScriptExecutionRequest request = null;

	
	/**
	 * Constructor which is better suited for Spring type initialization
	 * 
	 * @param name The name of the script to be executed.
	 * @param script The script itself. Can be empty if the name reference a script in the script library.
	 * @param format The format of the script.
	 * @param paraName The name of the generated parameter.
	 * @param paraType The type of the generated parameter.
	 * @param paraDescription The description of the generated parameter.
	 * @param paraUnit The unit of the generated parameter.
	 * @param inputBindings A string defining the runtime parameter to script parameter bindings. Must be in the format '[runtime name]=[script name]:[runtime name]=[script name]:...'
	 */
	public ScriptComponentRequest(String name, String script, String format, String paraName, String paraType, String paraDescription, String paraUnit, String inputBindings) {
		
		Parameter output = new Parameter("", paraName, paraType, paraDescription, 0d, paraUnit);

		Map<String, String> inputBinding = new HashMap<String, String>();
		String[] bindingPairs = inputBindings.split(":");
		for (String binding : bindingPairs) {
			String[] entries = binding.split("=");
			inputBinding.put(entries[0], entries[1]);
		}
		
		request = new ScriptExecutionRequest(name, script, format, output, inputBinding);
	}
	
	public ScriptComponentRequest(String name, String script, String format, Named output, Map<String, String>  inputBinding) {
		request = new ScriptExecutionRequest(name, script, format, output, inputBinding);
	}
}
