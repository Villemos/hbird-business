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
import org.hbird.business.core.SoftwareComponentDriver;
import org.hbird.exchange.businesscard.BusinessCardSender;
import org.hbird.exchange.configurator.ReportStatus;
import org.hbird.exchange.configurator.StandardEndpoints;
import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.configurator.StopComponent;
import org.hbird.exchange.core.BusinessCard;

/** TODO Create route IDs. */
public class ConfiguratorComponentDriver extends SoftwareComponentDriver {

	private static org.apache.log4j.Logger LOG = Logger.getLogger(ConfiguratorComponentDriver.class);

	/** A list of components and the routes of the component. */
	protected Map<String, RoutesDefinition> components = new HashMap<String, RoutesDefinition>();

	/** The name of the Configurator. */
	protected String name = "Configurator";

	

	protected CamelContext context = null;

	public ConfiguratorComponentDriver() {
		this.context = getContext();
		this.part = new ConfiguratorComponent("Configurator", this.getClass().getName());
		
		try {
			context.addRoutes(this);
			this.context.start();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		

	}

	public ConfiguratorComponentDriver(String name, CamelContext context) {
		this.name = name;
		this.context = context;
		this.part = new ConfiguratorComponent(name, this.getClass().getName());
		
		try {
			context.addRoutes(this);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		

	}

	public synchronized void startComponent(@Body StartComponent request, CamelContext context) throws Exception {
		LOG.info("Received start request for part '" + request.getPart().getQualifiedName() + "'. Will use driver '" + request.getPart().getDriverName() + "'.");

		/** Find the component builder and get it to setup and start the component. */
		if (request.getPart().getDriverName() == null) {
			LOG.error("Cannot start part '" + request.getPart().getQualifiedName() + "'. No driver set.");
		}
		else {
			try {
				SoftwareComponentDriver builder = (SoftwareComponentDriver) Class.forName(request.getPart().getDriverName()).newInstance();
				builder.setCommand(request);
				context.addRoutes(builder);

				/** Register component in list of components maintained by this configurator. */
				components.put(request.getPart().getQualifiedName(), builder.getRouteCollection());
			}
			catch (Exception e) {
				LOG.error("Received exception '" + e + "'. Failed to start part '" + request.getPart().getQualifiedName());
			}
		}
	}

	@SuppressWarnings("deprecation")
	public synchronized void stopComponent(@Body StopComponent command) throws Exception {
		if (components.containsKey(command.getComponentName())) {
			LOG.info("Stopping component '" + command.getComponentName() + "'");
			for (RouteDefinition route : components.get(command.getComponentName()).getRoutes()) {
				LOG.info("Stopping route '" + route.getId() + "' of component '" + command.getComponentName() + "'.");
				context.stopRoute(route);
			}

			/** Deregister. */
			components.remove(command.getComponentName());
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
        BusinessCardSender cardSender = new BusinessCardSender(new BusinessCard(part.getQualifiedName(), part.getCommands()), 5000l);
        ProcessorDefinition<?> route = from(addTimer("businesscard", part.getHeartbeat())).bean(cardSender);
        addInjectionRoute(route);
	}

	@Override
	protected void doConfigure() {
		/** Do null */
	}
}
