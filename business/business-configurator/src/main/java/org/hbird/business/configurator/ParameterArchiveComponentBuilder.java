package org.hbird.business.configurator;

public class ParameterArchiveComponentBuilder extends ComponentBuilder {

	@Override
	protected void doConfigure() {
		/** Setup the route to inject data into SOLR. */
		from("activemq:topic:monitoringdata").to("solr:parameterstore");
	};
}
