package org.hbird.business.configurator;

import org.hbird.business.scripting.ScriptExecutor;
import org.hbird.exchange.configurator.StartScriptComponent;
import org.hbird.exchange.core.State;
import org.hbird.exchange.scripting.ScriptExecutionRequest;

public class ScriptComponentBuilder extends ComponentBuilder {

	@Override
	protected void doConfigure() {
		
		ScriptExecutor executor = new ScriptExecutor((ScriptExecutionRequest) request.getArguments().get("scriptdefinition"));			
		
		/** Iterate over each dependency needed by this script. */
		for (String dependency : executor.getDependencies()) {
		
			/** Create the routes for receiving the data needed by the script. */
			from(StandardEndpoints.monitoring + "?" + addNameSelector(dependency))
			.bean(executor, "calculate")
			.setHeader("name", simple("${in.body.name}"))
			.setHeader("issuedBy", simple("${in.body.issuedBy}"))
			.setHeader("type", simple("${in.body.type}")) 
			
			.choice()
			.when(body().isInstanceOf(State.class))
			     .setHeader("isStateOf", simple("${in.body.isStateOf}"))
			     .to(StandardEndpoints.monitoring)
			.otherwise()
			     .to(StandardEndpoints.monitoring);
			
			/** Route for commands to this component, i.e. configuration commands. */
			from("seda:processCommandFor" + getComponentName()).bean(defaultCommandHandler, "receiveCommand");
		}		
	}
}
