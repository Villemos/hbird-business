package org.hbird.business.configurator;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.CamelContext;
import org.apache.log4j.Logger;
import org.hbird.exchange.configurator.ComponentConfigurationRequest;

public class Configurator {

	private static org.apache.log4j.Logger LOG = Logger.getLogger(Configurator.class);
	
	protected Map<String, String> classes = new HashMap<String, String>();
	{
		classes.put("org.hbird.exchange.configurator.CommandComponentRequest", CommandingComponentBuilder.class.getName());
		classes.put("org.hbird.exchange.configurator.LimitComponentRequest", LimitCheckComponentBuilder.class.getName());
		classes.put("org.hbird.exchange.configurator.NavigationComponentRequest", NavigationComponentBuilder.class.getName());
		classes.put("org.hbird.exchange.configurator.SystemMonitoringComponentRequest", SystemMonitorComponentBuilder.class.getName());
		classes.put("org.hbird.exchange.configurator.TaskExecutorComponentRequest", TaskExecutorComponentBuilder.class.getName());
		classes.put("org.hbird.exchange.configurator.ParameterArchiveComponentRequest", ParameterArchiveComponentBuilder.class.getName());		
	}
	
	
	public void process(@Body ComponentConfigurationRequest request, CamelContext context) {

		try {
			LOG.info("Creating component of type '" + classes.get(request.getClass().getName()) + "'.");
			ComponentBuilder builder = (ComponentBuilder) Class.forName(classes.get(request.getClass().getName())).newInstance();
			builder.setRequest(request);
			context.addRoutes(builder);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
