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
import org.hbird.business.core.SoftwareComponentDriver;
import org.hbird.exchange.businesscard.BusinessCardSender;
import org.hbird.exchange.configurator.ReportStatus;
import org.hbird.exchange.configurator.StandardEndpoints;
import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.configurator.StopComponent;
import org.hbird.exchange.constants.StandardComponents;
import org.hbird.exchange.constants.StandardMissionEvents;
import org.hbird.exchange.core.BusinessCard;
import org.hbird.exchange.core.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** TODO Create route IDs. */
public class ConfiguratorComponentDriver extends SoftwareComponentDriver {

    protected static final String ENDPOINT_TO_EVENTS = "direct:toEvents";

    private static final Logger LOG = LoggerFactory.getLogger(ConfiguratorComponentDriver.class);

    /** A list of components and the routes of the component. */
    protected Map<String, RoutesDefinition> components = new HashMap<String, RoutesDefinition>();

    /**
     * Default constructor.
     */
    public ConfiguratorComponentDriver() {
    }

    /**
     * Starts the Camel context.
     * 
     * @throws Exception
     */
    public void start() throws Exception {
        start(new ConfiguratorComponent(StandardComponents.CONFIGURATOR, this.getClass().getName()));
    }

    public void start(ConfiguratorComponent part) throws Exception {
        this.part = part;
        CamelContext context = getContext();

        try {
            context.addRoutes(this);
            context.start();
        }
        catch (Exception e) {
            LOG.error("Failed to start ConfiguatorComponentDriver", e);
        }
    }

    public synchronized void startComponent(@Body StartComponent request, CamelContext context) throws Exception {
        String qName = request.getPart().getQualifiedName();
        String driverName = request.getPart().getDriverName();
        LOG.info("Received start request for part '{}'. Will use driver '{}'.", qName, driverName);

        if (components.containsKey(qName)) {
            LOG.error("Received second request for start of the same component - '{}'.", qName);
        }
        else {
            /** Find the component builder and get it to setup and start the component. */
            if (driverName == null) {
                LOG.error("Cannot start part '{}'. No driver set.", qName);
            }
            else {
                try {
                    SoftwareComponentDriver builder = (SoftwareComponentDriver) Class.forName(driverName).newInstance();
                    builder.setCommand(request);
                    context.addRoutes(builder);

                    /** Register component in list of components maintained by this configurator. */
                    components.put(qName, builder.getRouteCollection());
                    context.createProducerTemplate().asyncSendBody(ENDPOINT_TO_EVENTS, createStartEvent(qName));
                }
                catch (Exception e) {
                    LOG.error("Failed to start part '{}'", qName, e);
                }
            }
        }
    }

    public synchronized void stopComponent(@Body StopComponent command, CamelContext context) throws Exception {
        String qName = command.getComponentName(); // same as qualified name in start component method
        if (components.containsKey(qName)) {
            LOG.info("Stopping part '{}'", qName);
            for (RouteDefinition route : components.get(qName).getRoutes()) {
                String routeId = route.getId();
                LOG.info("Stopping route '{}' of part '{}'.", routeId, qName);
                context.stopRoute(routeId);
            }

            /** Deregister. */
            components.remove(qName);
            getContext().createProducerTemplate().asyncSendBody(ENDPOINT_TO_EVENTS, createStopEvent(qName));
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
        from(StandardEndpoints.COMMANDS + "?" + addDestinationSelector(part.getName()))
                .choice()
                .when(body().isInstanceOf(StartComponent.class))
                .bean(this, "startComponent")
                .when(body().isInstanceOf(StopComponent.class))
                .bean(this, "stopComponent")
                .when(body().isInstanceOf(ReportStatus.class))
                .bean(this, "reportStatus")
                .end();

        /** Setup the BusinessCard */
        long heartbeat = part.getHeartbeat();

        BusinessCard card = new BusinessCard(part.getQualifiedName(), part.getCommands(), heartbeat);
        BusinessCardSender cardSender = new BusinessCardSender(card);
        ProcessorDefinition<?> route = from(addTimer("businesscard", heartbeat)).bean(cardSender);
        addInjectionRoute(route);

        ProcessorDefinition<?> toEvents = from(ENDPOINT_TO_EVENTS);
        addInjectionRoute(toEvents);

    }

    @Override
    protected void doConfigure() {
        /** Do null */
    }

    Event createStartEvent(String qualifiedName) {
        return new Event(qualifiedName, StandardMissionEvents.COMPONENT_START);
    }

    Event createStopEvent(String qualifiedName) {
        return new Event(qualifiedName, StandardMissionEvents.COMPONENT_STOP);
    }
}
