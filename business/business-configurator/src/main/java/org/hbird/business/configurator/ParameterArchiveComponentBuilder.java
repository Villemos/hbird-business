package org.hbird.business.configurator;

public class ParameterArchiveComponentBuilder extends ComponentBuilder {

	@Override
	protected void doConfigure() {
		
		/** Setup the route to inject data into SOLR. */
		from("activemq:topic:monitoring").to("solr:parameterstore");
		
		/** Route for commands to this component, i.e. configuration commands. */
		from("seda:processCommandFor" + getComponentName()).bean(defaultCommandHandler, "receiveCommand");
	};
}
