package org.hbird.business.configurator;

import org.apache.camel.model.ProcessorDefinition;
import org.hbird.business.core.Scheduler;
import org.hbird.business.taskexecutor.TaskExecutor;
import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.tasking.Task;

public class TaskExecutorComponentBuilder extends ComponentBuilder {

	@Override
	public void doConfigure() {

//		Scheduler scheduler = new Scheduler();
		
		ProcessorDefinition route = from(StandardEndpoints.tasks).split().method(new TaskExecutor(), "receive");
		addInjectionRoute(route);

//		.setHeader("name", simple("${in.body.name}"))
//		.setHeader("issuedBy", simple("${in.body.issuedBy}"))
//		.setHeader("type", simple("${in.body.type}"))
//		
//		.bean(scheduler)
//		
//		.choice()
//		.when((body().isInstanceOf(Task.class))).to(StandardEndpoints.tasks)
//		.when((body().isInstanceOf(CommandRequest.class))).to(StandardEndpoints.requests)
//		.when((body().isInstanceOf(Command.class))).to(StandardEndpoints.commands)
//		.otherwise().to(StandardEndpoints.monitoring);
				
		/** Route for commands to this component, i.e. configuration commands. */
		from("seda:processCommandFor" + getComponentName()).bean(defaultCommandHandler, "receiveCommand");
	}
}
