package org.hbird.business.configurator;

import org.hbird.business.taskexecutor.TaskExecutor;

public class TaskExecutorComponentBuilder extends ComponentBuilder {

	@Override
	public void doConfigure() {
		
		from("activemq:queue:tasks")
		.bean(new TaskExecutor())
		.setHeader("name", simple("${in.body.name}"))
		.setHeader("issuedBy", simple("${in.body.issuedBy}"))
		.setHeader("type", simple("${in.body.type}")) 
		.to("activemq:topic:monitoringdata");		
	}
}
