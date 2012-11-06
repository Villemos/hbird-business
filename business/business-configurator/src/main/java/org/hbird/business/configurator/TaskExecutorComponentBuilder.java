package org.hbird.business.configurator;

import org.hbird.business.taskexecutor.TaskExecutor;

public class TaskExecutorComponentBuilder extends ComponentBuilder {

	@Override
	public void doConfigure() {
		
		from(StandardEndpoints.tasks)
		.bean(new TaskExecutor())
		.setHeader("name", simple("${in.body.name}"))
		.setHeader("issuedBy", simple("${in.body.issuedBy}"))
		.setHeader("type", simple("${in.body.type}")) 
		.to(StandardEndpoints.monitoring);		
		
		/** Route for commands to this component, i.e. configuration commands. */
		from("seda:processCommandFor" + getComponentName()).bean(defaultCommandHandler, "receiveCommand");
	}
}
