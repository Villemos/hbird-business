package org.hbird.business.configurator;

import org.hbird.business.solr.SolrEndpoint;

public class MonitoringDataArchiveComponentBuilder extends ComponentBuilder {

	@Override
	protected void doConfigure() {
		
		from("activemq:topic:monitoring").to("solr:" + getComponentName() + "?exchangePattern=InOut");
		
		/** Route for commands to this component, i.e. configuration commands. */
		from(StandardEndpoints.commands + "?" + addDestinationSelector(getComponentName())).to("solr:" + getComponentName() + "?exchangePattern=InOut");
	};
}
