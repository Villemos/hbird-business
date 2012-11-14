package org.hbird.business.configurator;

public class CommandArchiveComponentBuilder extends ComponentBuilder {

	@Override
	protected void doConfigure() {
		
		/** Setup the route to inject data into SOLR. */
		from(StandardEndpoints.commands).to("solr:commandstore");
		

		/** Create route to receive commands to this component. */
		from(StandardEndpoints.commands + "?" + addDestinationSelector(getComponentName())).bean(defaultCommandHandler, "receiveCommand");
	};
}
