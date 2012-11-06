package org.hbird.business.configurator;

import org.apache.camel.builder.RouteBuilder;
import org.hbird.business.heartbeat.Heart;
import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.core.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
		
		/** Create route to receive commands to this component. */
		from(StandardEndpoints.commands + "?" + addDestinationSelector(getComponentName())).to("seda:processCommandFor" + getComponentName());
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
}
