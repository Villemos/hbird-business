package org.hbird.business.configurator;

import org.apache.camel.model.ProcessorDefinition;
import org.hbird.business.scripting.ScriptExecutor;
import org.hbird.exchange.scripting.ScriptExecutionRequest;

public class ScriptComponentBuilder extends ComponentBuilder {

	@Override
	protected void doConfigure() {
		
		ScriptExecutor executor = new ScriptExecutor((ScriptExecutionRequest) request.getArgument("scriptdefinition"));			
		
		/** Iterate over each dependency needed by this script. */
		for (String dependency : executor.getDependencies()) {
		
			/** Create the routes for receiving the data needed by the script. */
			ProcessorDefinition route = from(StandardEndpoints.monitoring + "?" + addNameSelector(dependency)).bean(executor, "calculate");
			addInjectionRoute(route);			
		}		
				
		/** Route for commands to this component, i.e. configuration commands. */
		from(StandardEndpoints.commands + "?" + addDestinationSelector(getComponentName())).bean(defaultCommandHandler, "receiveCommand");
	}
}
