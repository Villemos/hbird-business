package org.hbird.business.configurator;

import org.apache.camel.model.ProcessorDefinition;
import org.hbird.business.taskexecutor.TaskExecutor;

public class TaskExecutorComponentBuilder extends ComponentBuilder {

	@Override
	public void doConfigure() {

		String componentname = (String) request.getArgument("componentname");
		ProcessorDefinition route = from(StandardEndpoints.tasks).split().method(new TaskExecutor(componentname), "receive");
		addInjectionRoute(route);
					
		/** Route for commands to this component, i.e. configuration commands. */
		from(StandardEndpoints.commands + "?" + addDestinationSelector(getComponentName())).bean(defaultCommandHandler, "receiveCommand");
	}
}
