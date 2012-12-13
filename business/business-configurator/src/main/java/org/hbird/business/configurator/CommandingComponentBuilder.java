package org.hbird.business.configurator;

import org.apache.camel.builder.ProxyBuilder;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;
import org.hbird.business.command.releaser.CommandReleaser;
import org.hbird.business.core.FieldBasedSplitter;
import org.hbird.exchange.dataaccess.IDataAccess;



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

		FieldBasedSplitter splitter = new FieldBasedSplitter();

		/** Create the route and the interface for getting the command states. */
		from("direct:statestore").to("solr:statestore");

		CommandReleaser releaser = new CommandReleaser();

		try {
			releaser.setStore(new ProxyBuilder(getContext()).endpoint("direct:statestore").build(IDataAccess.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/** Read from the scheduled queue and release the command. The release consists of the validation
		 * that all lock states are valid and the ejection of the command itself.
		 * */
		from("activemq:queue:scheduledCommandRequests")
		.bean(releaser)
		.wireTap("seda:reportCommandRequestState")
		.choice()
		.when(header("Valid").isEqualTo(true)).to("seda:validCommandRequests")
		.otherwise().to("activemq:topic:failedCommandRequests");		

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
		from(StandardEndpoints.commands + "?" + addDestinationSelector(getComponentName())).bean(defaultCommandHandler, "receiveCommand");
	}	
}
