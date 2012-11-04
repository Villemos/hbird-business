package org.hbird.business.configurator;

public class CommandArchiveComponentBuilder extends ComponentBuilder {

	@Override
	protected void doConfigure() {
		
		/** Setup the route to inject data into SOLR. */
		from("activemq:topic:releasedCommands").to("solr:commandstore");
	};
}
