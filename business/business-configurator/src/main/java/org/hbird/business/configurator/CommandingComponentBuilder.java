package org.hbird.business.configurator;

import org.apache.camel.model.ProcessorDefinition;
import org.hbird.business.command.releaser.CommandReleaser;
import org.hbird.business.core.FieldBasedScheduler;
import org.hbird.business.core.FieldBasedSplitter;



/** Route builder to create a commanding chain. 
 * 
 * The builder has the following routes;
 * 
 * activemq:queue:requests?selector=type='CommandRequest'--> 
 * 
 * 
 * Depends on the following routes
 * 
 * - activemq:queue:injectedCommands. The route through which commands should be injected. 
 * - activemq:topic:monitoringdata. The route distributing parameter updates.
 * 
 * The builder will create a number of additional routes
 * 
 * - activemq:queue:queuedCommands. A command queue holding time tagged commands to be released.
 * - activemq:queue:queuedTasks. A queue of time tagged tasks.
 * - activemq:queue:releasedCommands. A queue of commands that are ready to be pre validated.
 * - activemq:tpic:failedCommands. A topic of commands that failed validation.
 * - activemq:queue:ejectedCommands. A queue of commands that are ready for the transport tier.
 * 
 */
public class CommandingComponentBuilder extends ComponentBuilder {

	public void doConfigure() {

		CommandReleaser releaser = new CommandReleaser();

		FieldBasedSplitter splitter = new FieldBasedSplitter();

		/** Read from the scheduled queue and release the command. The release consists of the validation
		 * that all lock states are valid and the ejection of the command itself.
		 * */
		from("activemq:queue:scheduledCommandRequests")
		.bean(releaser)
		.wireTap("seda:reportCommandRequestState")
		.choice()
		.when(header("Valid").isEqualTo(true)).to("seda:validCommandRequests")
		.otherwise().to("activemq:topic:failedCommandRequests");		


		/** Add route for receiving state parameters. */
		from(StandardEndpoints.monitoring + "?" + addTypeSelector("State"))
		.bean(releaser, "state");


		/** Extract the tasks from the command request and schedule them. Extract the command and release it. */
		ProcessorDefinition route = from("seda:validCommandRequests")
		.wireTap("seda:taskExtractor")
		.setBody(simple("${in.body.command}"))
		.setHeader("destination", simple("${in.body.destination}"));
		addInjectionRoute(route);

		route = from("seda:taskExtractor")
		.split().method(splitter);	
		addInjectionRoute(route);
		
		/** Route to move the tasks that has been scheduled. */
		addInjectionRoute(from("activemq:queue:scheduledTasks"));

		route = from("seda:reportCommandRequestState")
		.bean(releaser, "reportState");
		addInjectionRoute(route);
		
		/** Route for commands to this component, i.e. configuration commands. */
		from("seda:processCommandFor" + getComponentName()).bean(defaultCommandHandler, "receiveCommand");
	}	
}
