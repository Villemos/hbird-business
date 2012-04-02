package org.hbird.business.configurator;

import java.net.InetAddress;

import org.apache.camel.builder.RouteBuilder;
import org.hbird.exchange.businesscard.BusinessCard;
import org.hbird.exchange.businesscard.BusinessCardServiceSpecification;
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
	
		/** Setup the businesscard service. */
		card.component = request.id;
		card.host = InetAddress.getLocalHost().getHostName();
		card.provides.add(new BusinessCardServiceSpecification());
		
		/** Setup the component specific services. */
		doConfigure();
		
		/** Create the heartbeat / service publishing channel. */
		from("timer:heartbeat_" + request.id + "?fixedRate=true&amp;period=3000")
	     .setBody(bean(card))
	     .to("activemq:topic:system");				
	}
	
	/** The component specific configuration. */
	protected abstract void doConfigure();
}
