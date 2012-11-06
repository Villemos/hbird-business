package org.hbird.business.configurator;

public class CommandArchiveComponentBuilder extends ComponentBuilder {

	@Override
	protected void doConfigure() {
		
		/** Setup the route to inject data into SOLR. */
		from(StandardEndpoints.commands).to("solr:commandstore");
		
		/** Route for commands to this component, i.e. configuration commands. */
		from("seda:processCommandFor" + getComponentName()).bean(defaultCommandHandler, "receiveCommand");
	};
}
