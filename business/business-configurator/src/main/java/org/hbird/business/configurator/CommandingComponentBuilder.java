package org.hbird.business.configurator;

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

		/** Configure a scheduler. */
		FieldBasedScheduler scheduler = new FieldBasedScheduler();
		scheduler.setFieldName("executionTime");

		FieldBasedSplitter splitter = new FieldBasedSplitter();

		/** Add the route for scheduling. 
		 * 
		 * This route will look for a 'RELEASE_TIME' flag in the header. If it has been set,
		 * then the flag is used to set the JMS delay header and the message is injected
		 * into the commandQueue. The command queue will only release the command when the
		 * header time has expired, thus efficiently queuing the command.
		 * */
		from("activemq:queue:requests?selector=type='CommandRequest'")
		.bean(scheduler)
		.to("activemq:queue:scheduledCommandRequests");


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
		from("activemq:topic:monitoringdata?selector=type='State'")
		.bean(releaser, "state");


		/** Extract the tasks from the command request and schedule them. Extract the command and release it. */
		from("seda:validCommandRequests")
		.wireTap("seda:taskExtractor")
		.setBody(simple("${in.body.command}"))
		.setHeader("name", simple("${in.body.name}"))
		.setHeader("issuedBy", simple("${in.body.issuedBy}"))
		.setHeader("type", simple("${in.body.type}"))
		.setHeader("destination", simple("${in.body.destination}"))
		.to("activemq:topic:releasedCommands");

		from("seda:taskExtractor")
		.split().method(splitter)
		.bean(scheduler)
		.setHeader("name", simple("${in.body.name}"))
		.setHeader("issuedBy", simple("${in.body.issuedBy}"))
		.setHeader("type", simple("${in.body.type}"))
		.to("activemq:queue:scheduledTasks");

		/** Route to move the tasks that has been scheduled. */
		from("activemq:queue:scheduledTasks").to("activemq:queue:tasks");


		from("seda:reportCommandRequestState")
		.bean(scheduler, "reportState")
		.setHeader("name", simple("${in.body.name}"))
		.setHeader("issuedBy", simple("${in.body.issuedBy}"))
		.setHeader("type", simple("${in.body.type}"))
		.setHeader("isStateOf", simple("${in.body.isStateOf}"))
		.to("activemq:topic:Parameter");
	}	
}
