package org.hbird.business.configurator;

import org.hbird.business.queuemanagement.QueueManager;
import org.hbird.exchange.configurator.StandardEndpoints;
import org.hbird.queuemanagement.ClearQueue;
import org.hbird.queuemanagement.ListQueues;
import org.hbird.queuemanagement.RemoveQueueElements;
import org.hbird.queuemanagement.ViewQueue;

public class QueueManagerBuilder extends ComponentBuilder {

	{
		commands.add(new ClearQueue("", "", ""));
		commands.add(new ListQueues("", ""));
		commands.add(new RemoveQueueElements("", "", ""));
		commands.add(new ViewQueue("", "", ""));
	}
	
	@Override
	protected void doConfigure() {

		QueueManager manager = new QueueManager("");
		
		from(StandardEndpoints.commands + "?" + addDestinationSelector(getComponentName()))
		.choice()
		.when(body().isInstanceOf(ClearQueue.class))
		.bean(manager, "clearQueue")
		.when(body().isInstanceOf(RemoveQueueElements.class))
		.bean(manager, "removeQueueElements")
		.when(body().isInstanceOf(ViewQueue.class))
		.bean(manager, "viewQueue")				
		.when(body().isInstanceOf(ListQueues.class))
		.bean(manager, "listQueues")				
		.end();
	}
}
