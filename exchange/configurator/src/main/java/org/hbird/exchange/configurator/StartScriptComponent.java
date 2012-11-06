package org.hbird.exchange.configurator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.scripting.ScriptExecutionRequest;
import org.hbird.exchange.tasking.Task;

public class StartScriptComponent extends StartComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1184823993029236522L;
	
	public StartScriptComponent(String issuedBy, String destination, List<String> lockStates, List<Task> tasks, Command command) {
		super(issuedBy, "StartScriptComponent", "Command to a configurator to start a scripting component.", lockStates, tasks, command);
	}	
	

	public StartScriptComponent(String issuedBy, String name, Map<String, String> bindings, Named output) {
		super(issuedBy, "StartScriptComponent", "Command to a configurator to start a scripting component.", null, null, new Command(issuedBy, "Configurator", "StartScriptComponent", "Command to a configurator to start a system monitoring component."));

		ScriptExecutionRequest request = new ScriptExecutionRequest(name, "", "javascript", output, bindings);
		this.command.addArgument("scriptdefinition",request);
		
		addComponentName(output.getName());
	}		

	/**
	 * Constructor to create a script component based on a script provided in the constructor.
	 * 
	 * @param issuedBy
	 * @param name
	 * @param script
	 * @param bindings
	 * @param output
	 */
	public StartScriptComponent(String issuedBy, String script, Named output, Map<String, String> bindings) {
		super(issuedBy, "StartScriptComponent", "Command to a configurator to start a scripting component.", null, null, new Command(issuedBy, "Configurator", "StartScriptComponent", "Command to a configurator to start a system monitoring component."));

		ScriptExecutionRequest request = new ScriptExecutionRequest(name, script, "javascript", output, bindings);
		this.command.addArgument("scriptdefinition",request);
		
		addComponentName(output.getName());
	}		

	public StartScriptComponent(String issuedBy, String name, String paraName, String paraType, String paraDescription, String paraUnit, String bindings) {
		super(issuedBy, "StartScriptComponent", "Command to a configurator to start a scripting component.", null, null, new Command(issuedBy, "Configurator", "StartScriptComponent", "Command to a configurator to start a system monitoring component."));

		Parameter output = new Parameter("", paraName, paraType, paraDescription, 0d, paraUnit);

		Map<String, String> inputBinding = new HashMap<String, String>();
		String[] bindingPairs = bindings.split(":");
		for (String binding : bindingPairs) {
			String[] entries = binding.split("=");
			inputBinding.put(entries[0], entries[1]);
		}
		
		ScriptExecutionRequest request = new ScriptExecutionRequest(name, "", "javascript", output, inputBinding);
		this.command.addArgument("scriptdefinition",request);
		
		addComponentName(paraName);
	}		
}
