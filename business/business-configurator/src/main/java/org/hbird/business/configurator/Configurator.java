package org.hbird.business.configurator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.CamelContext;
import org.apache.log4j.Logger;
import org.hbird.exchange.configurator.ComponentConfigurationRequest;
import org.hbird.exchange.configurator.ConfiguratorStatus;

public class Configurator {

	private static org.apache.log4j.Logger LOG = Logger.getLogger(Configurator.class);
	
	protected List<String> components = new ArrayList<String>();
	
	protected Map<String, String> classes = new HashMap<String, String>();
	{
		classes.put("org.hbird.exchange.configurator.CommandComponentRequest", CommandingComponentBuilder.class.getName());
		classes.put("org.hbird.exchange.configurator.LimitComponentRequest", LimitCheckComponentBuilder.class.getName());
		classes.put("org.hbird.exchange.configurator.NavigationComponentRequest", NavigationComponentBuilder.class.getName());
		classes.put("org.hbird.exchange.configurator.SystemMonitoringComponentRequest", SystemMonitorComponentBuilder.class.getName());
		classes.put("org.hbird.exchange.configurator.TaskExecutorComponentRequest", TaskExecutorComponentBuilder.class.getName());
		classes.put("org.hbird.exchange.configurator.ParameterArchiveComponentRequest", ParameterArchiveComponentBuilder.class.getName());	
		classes.put("org.hbird.exchange.configurator.ScriptComponentRequest", ScriptComponentBuilder.class.getName());
		classes.put("org.hbird.exchange.configurator.CommandArchiveComponentRequest", CommandArchiveComponentBuilder.class.getName());
	}

	protected String name;
	
	public Configurator(String name) {
		this.name = name;
	}

	public void process(@Body ComponentConfigurationRequest request, CamelContext context) {

		try {			
			LOG.info("Creating component of type '" + classes.get(request.getClass().getName()) + "'.");
			
			/** Find the component builder and get it to setup and start the component. */
			ComponentBuilder builder = (ComponentBuilder) Class.forName(classes.get(request.getClass().getName())).newInstance();
			builder.setRequest(request);
			context.addRoutes(builder);

			/** Register component in list of components maintained by this configurator. */
			components.add(request.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public ConfiguratorStatus reportStatus() {
		return new ConfiguratorStatus(this.name, components);
	}
}
