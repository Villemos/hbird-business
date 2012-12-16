/**
 * Licensed to the Hummingbird Foundation (HF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The HF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hbird.business.configurator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.camel.Body;
import org.apache.camel.CamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.RoutesDefinition;
import org.apache.log4j.Logger;
import org.hbird.exchange.businesscard.BusinessCard;
import org.hbird.exchange.configurator.ReportStatus;
import org.hbird.exchange.configurator.StandardEndpoints;
import org.hbird.exchange.configurator.StartArchiveComponent;
import org.hbird.exchange.configurator.StartCommandComponent;
import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.configurator.StartLimitComponent;
import org.hbird.exchange.configurator.StartNavigationComponent;
import org.hbird.exchange.configurator.StartScriptComponent;
import org.hbird.exchange.configurator.StartSystemMonitoringComponent;
import org.hbird.exchange.configurator.StartTaskExecutorComponent;
import org.hbird.exchange.configurator.StopComponent;
import org.hbird.exchange.validation.Limit.eLimitType;

/** TODO Create route IDs. */
public class Configurator extends ComponentBuilder {

	private static org.apache.log4j.Logger LOG = Logger.getLogger(Configurator.class);

	/** A list of components and the routes of the component. */
	protected Map<String, RoutesDefinition> components = new HashMap<String, RoutesDefinition>();

	/** The name of the Configurator. */
	protected String name = "Configurator";

	protected Map<String, String> classes = new HashMap<String, String>();
	{
		classes.put("org.hbird.exchange.configurator.StartCommandComponent", CommandingComponentBuilder.class.getName());
		classes.put("org.hbird.exchange.configurator.StartLimitComponent", LimitCheckComponentBuilder.class.getName());
		classes.put("org.hbird.exchange.configurator.StartNavigationComponent", NavigationComponentBuilder.class.getName());
		classes.put("org.hbird.exchange.configurator.StartSystemMonitoringComponent", SystemMonitorComponentBuilder.class.getName());
		classes.put("org.hbird.exchange.configurator.StartTaskExecutorComponent", TaskExecutorComponentBuilder.class.getName());
		classes.put("org.hbird.exchange.configurator.StartArchiveComponent", ArchiveComponentBuilder.class.getName());	
		classes.put("org.hbird.exchange.configurator.StartScriptComponent", ScriptComponentBuilder.class.getName());
	}

	{	
		commands.add(new StartArchiveComponent(""));
		commands.add(new StartCommandComponent(""));
		commands.add(new StartLimitComponent("", eLimitType.Lower, "", "", "", ""));
		commands.add(new StartNavigationComponent(""));
		commands.add(new StartScriptComponent("", "", null, null));
		commands.add(new StartSystemMonitoringComponent(""));
		commands.add(new StartTaskExecutorComponent(""));
		commands.add(new StopComponent("", "", ""));
	}

	protected CamelContext context = null;

	public Configurator() {
		this.context = getContext();

		try {
			context.addRoutes(this);
			this.context.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Configurator(String name, CamelContext context) {
		this.name = name;
		this.context = context;

		try {
			context.addRoutes(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void startComponent(@Body StartComponent request, CamelContext context) throws Exception {
		LOG.info("Creating component '" + request.getArgument("componentname") + "' using builder '" + classes.get(request.getClass().getName()) + "'.");

		/** Find the component builder and get it to setup and start the component. */
		ComponentBuilder builder = (ComponentBuilder) Class.forName(classes.get(request.getClass().getName())).newInstance();
		builder.setCommand((StartComponent) request);
		context.addRoutes(builder);

		/** Register component in list of components maintained by this configurator. */
		components.put((String) request.getArgument("componentname"), builder.getRouteCollection());
	}

	public synchronized void stopComponent(@Body StopComponent command) throws Exception {
		if (components.containsKey(((StopComponent) command).getComponent())) {
			LOG.info("Stopping component '" + command.getComponent() + "'");
			for (RouteDefinition route : components.get(command.getComponent()).getRoutes()) {
				LOG.info("Stopping route '" + route.getId() + "' of component '" + command.getComponent() + "'.");
				context.stopRoute(route);
			}

			/** Deregister. */
			components.remove(command.getComponent());
		}
	}

	public synchronized Map<String, String> reportStatus(@Body ReportStatus request) {
		Map<String, String> values = new HashMap<String, String>();
		Iterator<Entry<String, RoutesDefinition>> it = components.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, RoutesDefinition> entry = it.next();
			values.put(entry.getKey(), entry.getValue().toString());
		}

		return values;
	}

	@Override
	public void configure() throws Exception {

		/** Setup route to receive commands. */
		from(StandardEndpoints.commands + "?" + addDestinationSelector(name))
		.choice()
		.when(body().isInstanceOf(StartComponent.class))
		.bean(this, "startComponent")
		.when(body().isInstanceOf(StopComponent.class))
		.bean(this, "stopComponent")
		.when(body().isInstanceOf(ReportStatus.class))
		.bean(this, "reportStatus")
		.end();

		/** Setup the BusinessCard */
		BusinessCard card = new BusinessCard(name, 5000l, commands);
		ProcessorDefinition<?> route = from("timer:businessCard_" + name + "?period=5000").bean(card);
		addInjectionRoute(route);
	}

	@Override
	protected void doConfigure() {
		/** Do null */
	}
}
