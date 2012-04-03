package org.hbird.business.configurator;

import org.hbird.business.parameterstorage.InMemoryParameterBuffer;
import org.hbird.exchange.parameters.ParameterAccessServiceSpecification;

public class ParameterArchiveComponentBuilder extends ComponentBuilder {

	@Override
	protected void doConfigure() {
		card.provides.add(new ParameterAccessServiceSpecification());
		
		InMemoryParameterBuffer localBuffer = new InMemoryParameterBuffer();
		
		/** Start a simple route to support the releasers retrieval of parameters. */
		from("direct:parameterRequest").bean(localBuffer, "getParameter");

		/** Setup the route to maintain the local buffer. */
		from("activemq:topic:parameters").bean(localBuffer, "storeParameter");
	};
}
