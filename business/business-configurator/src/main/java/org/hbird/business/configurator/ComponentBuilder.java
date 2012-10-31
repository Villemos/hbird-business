package org.hbird.business.configurator;

import org.apache.camel.builder.RouteBuilder;
import org.hbird.business.heartbeat.Heart;
import org.hbird.exchange.businesscard.BusinessCard;
import org.hbird.exchange.configurator.ComponentConfigurationRequest;

public abstract class ComponentBuilder extends RouteBuilder {

	protected ComponentConfigurationRequest request = null;

	protected BusinessCard card = new BusinessCard();

	public ComponentConfigurationRequest getRequest() {
		return request;
	}

	public void setRequest(ComponentConfigurationRequest request) {
		this.request = request;
	};

	@Override
	public void configure() throws Exception {

		/** Setup the component specific services. */
		doConfigure();

		/** Create the heartbeat route for this component. */
		if (request.heartbeatPeriod != 0) {
			from("timer:heartbeat_" + request.getName() + "?fixedRate=true&period=" + request.heartbeatPeriod)
			.setBody(bean(new Heart(request.getName(), request.heartbeatPeriod)))
			.to("activemq:topic:system");	
		}
	}

	/** The component specific configuration. */
	protected abstract void doConfigure();
}
