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
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.RoutesDefinition;
import org.apache.camel.spring.spi.ApplicationContextRegistry;
import org.hbird.business.core.SoftwareComponentDriver;
import org.hbird.exchange.configurator.ReportStatus;
import org.hbird.exchange.configurator.StandardEndpoints;
import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.configurator.StopComponent;
import org.hbird.exchange.constants.StandardMissionEvents;
import org.hbird.exchange.core.Event;
import org.hbird.exchange.interfaces.IStartableEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * Driver of the configurator component. Creates a configurator bean and the routes needed to receive
 * configuration requests.
 * 
 * @author Gert Villemos
 * 
 */
public class ConfiguratorComponentDriver extends SoftwareComponentDriver {

    protected static final String ENDPOINT_TO_EVENTS = "direct:toEvents";

    private static final Logger LOG = LoggerFactory.getLogger(ConfiguratorComponentDriver.class);

    /** A list of components and the routes of the component. */
    protected Map<String, RoutesDefinition> components = new HashMap<String, RoutesDefinition>();

    protected ApplicationContext applicationContext;

    /**
     * Default constructor.
     */
    public ConfiguratorComponentDriver() {
        this(null);
        LOG.warn("Started ConfigurationComponentDriver without applcation context. Bean registry will not be available in started components!");
    }

    public ConfiguratorComponentDriver(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Method to start the configurator part.
     * 
     * @param part The configurator component to be started
     * @throws Exception
     */
    public void start(ConfiguratorComponent part) throws Exception {
        this.entity = part;
        CamelContext context = createContext(applicationContext);
        LOG.info("Starting ConfiguratorComponent '{}'; CamelContext {};", new Object[] { part.getName(), context.getName() });
        try {
            context.addRoutes(this);
            context.start();
            LOG.info("ConfiguratorComponent '{}' started", part.getName());
        }
        catch (Exception e) {
            LOG.error("Failed to start ConfiguatorComponentDriver", e);
        }
    }

    CamelContext createContext(ApplicationContext applicationContext) {
        CamelContext camelContext;
        if (applicationContext == null) {
            camelContext = getContext();
            LOG.warn("No Spring ApplicationContext available; using default CamelContext '{}' without bean registry", camelContext.getName());
        }
        else {
            camelContext = new DefaultCamelContext(new ApplicationContextRegistry(applicationContext));
            LOG.info("Created new CamelContext '{}' using Spring ApplicationContext; bean registry should be availabel", camelContext.getName());
        }
        return camelContext;
    }

    /**
     * Request to start a component.
     * 
     * @param command The start component request
     * @param context The camel context in which the component is running.
     * @throws Exception
     */
    public synchronized void startComponent(@Body StartComponent command, CamelContext context) throws Exception {
        IStartableEntity entity = command.getEntity();
        String id = entity.getID();
        String name = entity.getName();
        String driverName = entity.getDriverName();
        entity.setContext(context);
        LOG.info("Received start request for IStartableEntity ID={}, name={}. Will use driver '{}' and CamelContex '{}'.", new Object[] { id, name, driverName,
                context.getName() });

        if (components.containsKey(id)) {
            LOG.error("Received second request for start of the same IStartableEntity - '{}'.", id);
        }
        else {
            /* Find the component builder and get it to setup and start the component. */
            if (driverName == null) {
                LOG.error("Cannot start IStartableEntity '{}'. No driver set.", id);
            }
            else {
                try {
                    SoftwareComponentDriver builder = (SoftwareComponentDriver) Class.forName(driverName).newInstance();
                    builder.setCommand(command);
                    builder.setContext((ModelCamelContext) context);
                    context.addRoutes(builder);

                    /* Register component in list of components maintained by this configurator. */
                    components.put(id, builder.getRouteCollection());
                    context.createProducerTemplate().asyncSendBody(ENDPOINT_TO_EVENTS, createStartEvent(this.entity.getID(), id));
                }
                catch (Exception e) {
                    LOG.error("Failed to start IStartableEntity '{}'", id, e);
                }
            }
        }
    }

    /**
     * A request to stop a component.
     * 
     * @param command The request to stop a component
     * @param context The camel context in which the component is running.
     * @throws Exception
     */
    public synchronized void stopComponent(@Body StopComponent command, CamelContext context) throws Exception {
        String id = command.getEntityID();
        if (components.containsKey(id)) {
            LOG.info("Stopping part '{}'", id);
            for (RouteDefinition route : components.get(id).getRoutes()) {
                String routeId = route.getId();
                LOG.info("Stopping route '{}' of part '{}'.", routeId, id);
                context.stopRoute(routeId);
            }

            /* Deregister. */
            components.remove(id);
            getContext().createProducerTemplate().asyncSendBody(ENDPOINT_TO_EVENTS, createStopEvent(entity.getID(), id));
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

    /**
     * @see org.hbird.business.core.SoftwareComponentDriver#configure()
     */
    @Override
    public void configure() throws Exception {
        String id = entity.getID();
        LOG.info("Accepting Commands with destination '{}'", id);

        /* Setup route to receive commands. */
        from(StandardEndpoints.COMMANDS + "?" + addDestinationSelector(id))
                .choice()
                .when(body().isInstanceOf(StartComponent.class))
                .bean(this, "startComponent")
                .when(body().isInstanceOf(StopComponent.class))
                .bean(this, "stopComponent")
                .when(body().isInstanceOf(ReportStatus.class))
                .bean(this, "reportStatus")
                .end();

        /* Setup the BusinessCard */
        long heartbeat = entity.getHeartbeat();

        ProcessorDefinition<?> route = from(addTimer("businesscard", heartbeat)).bean(entity, "getBusinessCard");
        addInjectionRoute(route);

        ProcessorDefinition<?> toEvents = from(ENDPOINT_TO_EVENTS);
        addInjectionRoute(toEvents);
    }

    /**
     * @see org.hbird.business.core.SoftwareComponentDriver#doConfigure()
     */
    @Override
    protected void doConfigure() {
        /** Do null */
    }

    /**
     * @param qualifiedName
     * @return
     */
    protected Event createStartEvent(String issuedBy, String applicableTo) {
        Event event = StandardMissionEvents.COMPONENT_START.cloneEntity();
        event.setIssuedBy(issuedBy);
        event.setApplicableTo(applicableTo);
        return event;
    }

    /**
     * @param qualifiedName
     * @return
     */
    protected Event createStopEvent(String issuedBy, String applicableTo) {
        Event event = StandardMissionEvents.COMPONENT_STOP.cloneEntity();
        event.setIssuedBy(issuedBy);
        event.setApplicableTo(applicableTo);
        return event;
    }
}
