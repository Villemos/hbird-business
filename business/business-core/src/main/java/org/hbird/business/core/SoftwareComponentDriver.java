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
package org.hbird.business.core;

import org.apache.camel.model.ProcessorDefinition;
import org.hbird.exchange.businesscard.BusinessCardSender;
import org.hbird.exchange.configurator.StandardEndpoints;
import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.BusinessCard;
import org.hbird.exchange.interfaces.IStartablePart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base classs for all software component drivers.
 * 
 * A component builder is a class that based on a StartComponent command will
 * create the beans and routes needed to assembly the component. The assembly
 * is done at runtime.
 * 
 * @author Gert Villemos
 * 
 */
public abstract class SoftwareComponentDriver extends HbirdRouteBuilder {

    private static Logger LOG = LoggerFactory.getLogger(SoftwareComponentDriver.class);

    /** The Start request of the component. */
    protected StartComponent command = null;

    /** The part that this driver starts. */
    protected IStartablePart part = null;

    /**
     * Sets the command which this builder use to create the component.
     * 
     * @param command The command to start a component
     */
    public void setCommand(StartComponent command) {
        this.command = command;
    };

    @Override
    public void configure() throws Exception {

        /** Get the part specification from the start request. */
        if (command != null) {
            part = command.getPart();
        }

        if (part != null) {
            String qName = part.getQualifiedName();
            long heartbeat = part.getHeartbeat();
            LOG.info("Starting driver for part '{}'.", qName);

            /** Setup the component specific services. */
            doConfigure();

            /** Setup the BusinessCard */
            BusinessCard card = new BusinessCard(qName, part.getCommands(), heartbeat);
            BusinessCardSender cardSender = new BusinessCardSender(card);
            ProcessorDefinition<?> route = from(addTimer("businesscard", heartbeat)).bean(cardSender);
            addInjectionRoute(route);
        }
        else {
            LOG.error("No part has been defined for this driver. Cannot start nothing...");
        }
    }

    protected void addCommandHandler() {
        /** Route for commands to this component, i.e. configuration commands. */
        from(StandardEndpoints.COMMANDS + "?" + addDestinationSelector()).bean(new DefaultCommandHandler(), "receiveCommand");
    }

    /** The component specific configuration. */
    protected abstract void doConfigure();

    /**
     * Creates a string that can be used as a selector on activemq
     * 
     * @param name The type of the object to be retrieved
     * @return The string to be used as selector
     */
    protected String addTypeSelector(String type) {
        return "selector=" + StandardArguments.TYPE + "='" + type + "'";
    }

    protected String addClassSelector(String clazz) {
        return "selector=" + StandardArguments.CLASS + "='" + clazz + "'";
    }

    protected String addTimer(String prefix, long period) {
        return "timer://" + addUniqueId(prefix) + "?period=" + period;
    }

    protected String addUniqueId(String prefix) {
        return prefix + part.getQualifiedName(".");
    }

    protected String addOptions() {
        return "?";
    }

    /**
     * Creates a string that can be used as a selector on activemq
     * 
     * @param name The destination of the object to be retrieved
     * @return The string to be used as selector
     */
    protected String addDestinationSelector(String destination) {
        return "selector=destination='" + destination + "'";
    }

    protected String addDestinationSelector() {
        return "selector=destination='" + part.getQualifiedName(".") + "'";
    }

    /**
     * Creates a string that can be used as a selector on activemq
     * 
     * @param name The name of the object to be retrieved
     * @return The string to be used as selector
     */
    protected String addNameSelector(String name) {
        return "selector=name='" + name + "'";
    }

    public IStartablePart getPart() {
        return part;
    }

    public void setPart(IStartablePart part) {
        this.part = part;
    }
}
