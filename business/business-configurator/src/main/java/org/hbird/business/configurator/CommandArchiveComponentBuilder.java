package org.hbird.business.configurator;

import org.hbird.business.solr.SolrEndpoint;

public class CommandArchiveComponentBuilder extends ComponentBuilder {

	@Override
	protected void doConfigure() {
		
		/** Setup the route to inject data into SOLR. */
		from(StandardEndpoints.commands).to("solr:" + getComponentName());
		
		/** Create route to receive commands to this component. */
		from(StandardEndpoints.commands + "?" + addDestinationSelector(getComponentName())).to("solr:" + getComponentName());
	};
}
