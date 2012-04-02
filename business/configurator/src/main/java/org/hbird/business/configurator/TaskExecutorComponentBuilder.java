package org.hbird.business.configurator;

import org.hbird.business.taskexecutor.TaskExecutor;
import org.hbird.exchange.tasking.TaskingServiceSpecification;

public class TaskExecutorComponentBuilder extends ComponentBuilder {

	@Override
	public void doConfigure() {
		
		card.provides.add(new TaskingServiceSpecification());
		
		from("activemq:queue:tasks")
		 .bean(new TaskExecutor())
		 .to("activemq:topic:parameters");		
	}
}
