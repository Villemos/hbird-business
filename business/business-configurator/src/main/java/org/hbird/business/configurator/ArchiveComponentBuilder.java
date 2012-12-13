package org.hbird.business.configurator;

public class ArchiveComponentBuilder extends ComponentBuilder {

	@Override
	protected void doConfigure() {
		
		from(StandardEndpoints.monitoring).to("solr:" + getComponentName());
		from(StandardEndpoints.commands).to("solr:" + getComponentName());
		
		/** Notice that this component does not need a separate commanding route. It listens to all commands per default. */
	};
}
