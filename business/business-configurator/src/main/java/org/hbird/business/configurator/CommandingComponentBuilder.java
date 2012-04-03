package org.hbird.business.configurator;

import org.hbird.business.command.releaser.CommandReleaser;
import org.hbird.business.core.FieldBasedScheduler;
import org.hbird.business.core.FieldBasedSplitter;
import org.hbird.business.heartbeat.Heart;
import org.hbird.exchange.commandrelease.CommandReleaseServiceSpecification;
import org.hbird.exchange.parameters.ParameterAccessServiceSpecification;
import org.hbird.exchange.tasking.TaskingServiceSpecification;


/** Route builder to create a commanding chain. 
 * 
 * Depends on the following routes
 * 
 * - activemq:queue:injectedCommands. The route through which commands should be injected. 
 * - activemq:topic:parameters. The route distributing parameter updates.
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

		card.provides.add(new CommandReleaseServiceSpecification());
		card.requires.add(new TaskingServiceSpecification());
		card.requires.add(new ParameterAccessServiceSpecification());
		
		CommandReleaser releaser = new CommandReleaser();

		/** Configure a scheduler. */
		FieldBasedScheduler scheduler = new FieldBasedScheduler();
		scheduler.setFieldName("releaseTime");
		
		FieldBasedSplitter splitter = new FieldBasedSplitter();
		
		/** Add the route for scheduling. 
		 * 
		 * This route will look for a 'RELEASE_TIME' flag in the header. If it has been set,
		 * then the flag is used to set the JMS delay header and the message is injected
		 * into the commandQueue. The command queue will only release the command when the
		 * header time has expired, thus efficiently queuing the command.
		 * */
		from("activemq:queue:injectedCommands")
		     .bean(scheduler)
		     .to("activemq:queue:queuedCommands");

		/** Add the route for validation. 
		 * 
		 * This route will take commands that have been queued and check whether it is working.
		 * */
		from("activemq:queue:queuedCommands")
		     .bean(releaser)
		     .choice()
		     .when(header("Valid").isEqualTo(true)).to("activemq:queue:releasedCommands")
		     .otherwise().to("activemq:topic:failedCommands");		

		/** Extract the tasks from the command cartridge and schedule them, and release the command. */
		from("activemq:queue:releasedCommands")
		     .wireTap("seda:taskExtractor")
		     .to("activemq:topic:ejectedCommands");

		from("seda:taskExtractor")
		     .split().method(splitter)
		     .bean(scheduler)
		     .to("activemq:queue:queuedTasks");
		
		/** Route to move the tasks that has been scheduled. */
		from("activemq:queue:queuedTasks").to("activemq:queue:tasks");
		
		if (request.heartbeatPeriod != 0) {
			/** Create a heartbeat of this component. */
			Heart heart = new Heart(request.id, request.heartbeatPeriod);
			from("timer://heartbeat" + request.id + "?fixedRate=true&period=" + request.heartbeatPeriod).bean(heart).to("activemq:topic:systemMonitoring");
		}
		
	}	
}
