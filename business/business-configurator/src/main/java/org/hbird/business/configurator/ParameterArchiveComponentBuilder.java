package org.hbird.business.configurator;

import org.hbird.exchange.parameters.ParameterAccessServiceSpecification;

public class ParameterArchiveComponentBuilder extends ComponentBuilder {

	@Override
	protected void doConfigure() {
		card.provides.add(new ParameterAccessServiceSpecification());
		
		/** Setup the route to inject data into SOLR. */
		from("activemq:topic:parameters").to("solr:parameterstore");
	};
}
