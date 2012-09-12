package org.hbird.business.configurator;

import org.hbird.exchange.parameters.ParameterAccessServiceSpecification;

public class CommandArchiveComponentBuilder extends ComponentBuilder {

	@Override
	protected void doConfigure() {
		
		/** Setup the route to inject data into SOLR. */
		from("activemq:topic:releasedCommands").to("solr:commandstore");
	};
}
