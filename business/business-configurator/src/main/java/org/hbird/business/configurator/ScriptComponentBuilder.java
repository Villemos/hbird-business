package org.hbird.business.configurator;

import org.hbird.business.scripting.ScriptExecutor;
import org.hbird.exchange.configurator.ScriptComponentRequest;
import org.hbird.exchange.core.State;

public class ScriptComponentBuilder extends ComponentBuilder {

	@Override
	protected void doConfigure() {
		
		ScriptExecutor executor = new ScriptExecutor(((ScriptComponentRequest) request).request);			
		
		/** Iterate over each dependency needed by this script. */
		for (String dependency : executor.getDependencies()) {
		
			/** Create the routes for receiving the data needed by the script. */
			from("activemq:topic:monitoringdata?selector=name%3D'" + dependency + "'")
			.bean(executor, "calculate")
			.setHeader("name", simple("${in.body.name}"))
			.setHeader("issuedBy", simple("${in.body.issuedBy}"))
			.setHeader("type", simple("${in.body.type}")) 
			
			.choice()
			.when(body().isInstanceOf(State.class))
			     .setHeader("isStateOf", simple("${in.body.isStateOf}"))
			     .to("activemq:topic:monitoringdata")
			.otherwise()
			     .to("activemq:topic:monitoringdata");
		}		
	}
}
