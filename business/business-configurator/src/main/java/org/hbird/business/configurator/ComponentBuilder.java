package org.hbird.business.configurator;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ProcessorDefinition;
import org.hbird.business.core.Scheduler;
import org.hbird.business.heartbeat.Heart;
import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.State;
import org.hbird.exchange.tasking.Task;


public abstract class ComponentBuilder extends RouteBuilder {

	protected StartComponent request = null;

	protected static DefaultCommandHandler defaultCommandHandler = new DefaultCommandHandler();

	public StartComponent getRequest() {
		return request;
	}

	public void setRequest(StartComponent request) {
		this.request = request;
	};


	@Override
	public void configure() throws Exception {

		/** Setup the component specific services. */
		doConfigure();

		/** Create the heartbeat route for this component. */
		if (request.getArguments().get("heartbeat") != null) {
			long heartbeat = (Long) request.getArguments().get("heartbeat");

			from("timer:heartbeat_" + request.getName() + "?fixedRate=true&period=" + heartbeat)
			.setBody(bean(new Heart(request.getName(), heartbeat)))
			.to(StandardEndpoints.monitoring);	
		}
	}

	/** The component specific configuration. */
	protected abstract void doConfigure();

	protected String addTypeSelector(String type) {
		return "selector=type='" + type + "'";
	}

	protected String addDestinationSelector(String destination) {
		return "selector=destination='" + destination + "'";
	}

	protected String addNameSelector(String name) {
		return "selector=name='" + name + "'";
	}

	public String getComponentName() {
		return (String) request.getArguments().get("componentname");
	}

	protected void addInjectionRoute(ProcessorDefinition route) {
		Scheduler scheduler = new Scheduler();

		route

		/** Dont route messages with a NULL body. */
		.choice()
		.when(simple("${in.body} == null"))
		.stop()
		.end()
		
		/** Set standard headers. */
		.setHeader("name", simple("${in.body.name}"))
		.setHeader("issuedBy", simple("${in.body.issuedBy}"))
		.setHeader("type", simple("${in.body.type}"))
		.setHeader("datasetidentifier", simple("${in.body.datasetidentifier}"))

		
		/** Set object specific headers. */
		.choice()
		.when(body().isInstanceOf(State.class))
		.setHeader("isStateOf", simple("${in.body.isStateOf}"))
		.when(body().isInstanceOf(Command.class))
		.setHeader("destination", simple("${in.body.destination}"))
		.end()

		/** Schedule the release, if this object implements IScheduled. */
		.bean(scheduler)

		/** Route to the appropiate topic / query. */
		.choice()
		.when((body().isInstanceOf(Task.class))).to(StandardEndpoints.tasks)
		.when((body().isInstanceOf(CommandRequest.class))).to(StandardEndpoints.requests)
		.when((body().isInstanceOf(Command.class))).to(StandardEndpoints.commands)
		.otherwise().to(StandardEndpoints.monitoring);
	}
}
