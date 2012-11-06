package org.hbird.exchange.configurator;

import java.util.List;


import org.hbird.exchange.core.Command;
import org.hbird.exchange.tasking.Task;
import org.hbird.exchange.validation.Limit;
import org.hbird.exchange.validation.Limit.eLimitType;


public class StartLimitComponent extends StartComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8047192684233318282L;

	public StartLimitComponent(String issuedBy, String destination, List<String> lockStates, List<Task> tasks, Command command, eLimitType type, String ofParameter, String limit, String stateName, String stateDescription) {
		super(issuedBy, "StartLimitComponent", "Command to a configurator to start a limit component.", lockStates, tasks, command);
		addLimit(type, ofParameter, limit, stateName, stateDescription);
	}	
	
	
	public StartLimitComponent(String issuedBy, String destination, eLimitType type, String ofParameter, String limit, String stateName, String stateDescription) {
		super(issuedBy, "StartLimitComponent", "Command to a configurator to start a limit component.", null, null, new Command(issuedBy, destination, "StartLimitComponent", "Command to a configurator to start a limit component."));
		addLimit(type, ofParameter, limit, stateName, stateDescription);
		addComponentName(stateName);
	}	

	public StartLimitComponent(eLimitType type, String ofParameter, String limit, String stateName, String stateDescription) {
		super("", "StartLimitComponent", "Command to a configurator to start a limit component.", null, null, new Command("", "Configurator", "StartLimitComponent", "Command to a configurator to start a limit component."));
		addLimit(type, ofParameter, limit, stateName, stateDescription);
		addComponentName(stateName);
	}	

	public void addLimit(eLimitType type, String ofParameter, String limit, String stateName, String description) {
		
		Number value = null;
		if (limit.endsWith("i")) {
			value = Integer.parseInt(limit);
		}
		else if (limit.endsWith("f")) {
			value = Float.parseFloat(limit);
		}
		else if (limit.endsWith("s")) 
		{
			value = Short.parseShort(limit);
		}
		else {
			value = Double.parseDouble(limit);
		}
		
		this.command.addArgument("limit", new Limit(type, ofParameter, value, stateName, description));
	}
}
