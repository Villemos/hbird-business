package org.hbird.business.configurator;

import org.hbird.business.scripting.ScriptExecutor;
import org.hbird.exchange.configurator.ScriptComponentRequest;
import org.hbird.exchange.core.StateParameter;

public class ScriptComponentBuilder extends ComponentBuilder {

	@Override
	protected void doConfigure() {
		
		ScriptExecutor executor = new ScriptExecutor(((ScriptComponentRequest) request).request);			
		
		for (String parameter : executor.input.keySet()) {
			/** Create the route for enabling/disabling limit checking. */
			from("activemq:topic:parameters?selector=name%3D'" + parameter + "'")
			.bean(executor, "calculate")
			.choice()
			.when(body().isInstanceOf(StateParameter.class)).setHeader("isStateOf", simple("${in.body.isStateOf}")).to("activemq:topic:parameters")
			.otherwise().setHeader("name", simple("${in.body.name}")).to("activemq:topic:parameters");
		}
		
//		String selector = "selector=";
//		String separator = "";
//		for (String parameter : executor.input.keySet()) {
//			selector += separator + "name='" + parameter + "'";
//			separator = " AND ";
//		}
//		
//		/** Create the route for enabling/disabling limit checking. */
//		from("activemq:topic:parameters?" + selector)
//		.bean(executor, "calculate")
//		.setHeader("isStateOf", simple("${in.body.isStateOf}"))
//		.setHeader("name", simple("${in.body.name}"))
//		.to("activemq:topic:parameters");
	}
}
