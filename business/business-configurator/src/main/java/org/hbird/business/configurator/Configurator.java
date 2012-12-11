package org.hbird.business.configurator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.CamelContext;
import org.apache.log4j.Logger;
import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.configurator.ConfiguratorStatus;

public class Configurator {

	private static org.apache.log4j.Logger LOG = Logger.getLogger(Configurator.class);
	
	protected List<String> components = new ArrayList<String>();
	
	protected Map<String, String> classes = new HashMap<String, String>();
	{
		classes.put("org.hbird.exchange.configurator.StartCommandComponent", CommandingComponentBuilder.class.getName());
		classes.put("org.hbird.exchange.configurator.StartLimitComponent", LimitCheckComponentBuilder.class.getName());
		classes.put("org.hbird.exchange.configurator.StartNavigationComponent", NavigationComponentBuilder.class.getName());
		classes.put("org.hbird.exchange.configurator.StartSystemMonitoringComponent", SystemMonitorComponentBuilder.class.getName());
		classes.put("org.hbird.exchange.configurator.StartTaskExecutorComponent", TaskExecutorComponentBuilder.class.getName());
		classes.put("org.hbird.exchange.configurator.StartMonitoringDataArchiveComponent", MonitoringDataArchiveComponentBuilder.class.getName());	
		classes.put("org.hbird.exchange.configurator.StartScriptComponent", ScriptComponentBuilder.class.getName());
		classes.put("org.hbird.exchange.configurator.StartCommandArchiveComponent", CommandArchiveComponentBuilder.class.getName());
	}

	/** The name of the Configurator. */
	protected String name;
	
	public Configurator(String name) {
		this.name = name;
	}

	public void process(@Body StartComponent request, CamelContext context) {

		try {			
			LOG.info("Creating component '" + request.getArguments().get("componentname") + "' using builder '" + classes.get(request.getClass().getName()) + "'.");
			
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
