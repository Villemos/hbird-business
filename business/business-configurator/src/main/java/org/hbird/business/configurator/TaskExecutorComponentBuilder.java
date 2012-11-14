package org.hbird.business.configurator;

import org.apache.camel.model.ProcessorDefinition;
import org.hbird.business.taskexecutor.TaskExecutor;

public class TaskExecutorComponentBuilder extends ComponentBuilder {

	@Override
	public void doConfigure() {

		ProcessorDefinition route = from(StandardEndpoints.tasks).split().method(new TaskExecutor(), "receive");
		addInjectionRoute(route);
				
		/** Route for commands to this component, i.e. configuration commands. */
		from("seda:processCommandFor" + getComponentName()).bean(defaultCommandHandler, "receiveCommand");
	}
}
