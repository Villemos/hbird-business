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

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ProcessorDefinition;
import org.hbird.exchange.businesscard.BusinessCardSender;
import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.configurator.StandardEndpoints;
import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.BusinessCard;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.State;
import org.hbird.exchange.interfaces.IStartablePart;
import org.hbird.exchange.tasking.Task;

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
public abstract class SoftwareComponentDriver extends RouteBuilder {

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
    	part = command.getPart();
    	
        /** Setup the component specific services. */
        doConfigure();

        /** Setup the BusinessCard */
        BusinessCardSender cardSender = new BusinessCardSender(new BusinessCard(part.getQualifiedName(), part.getCommands()), command.getHeartbeat());
        ProcessorDefinition<?> route = from(addTimer("businesscard", part.getHeartbeat())).bean(cardSender);
        addInjectionRoute(route);
    }

    protected void addCommandHandler() {
        /** Route for commands to this component, i.e. configuration commands. */
        from(StandardEndpoints.commands + "?" + addDestinationSelector()).bean(new DefaultCommandHandler(), "receiveCommand");
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
        return "selector=type='" + type + "'";
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

    /**
     * Returns the name of the component to be build.
     * 
     * @return The name of the component to be build, as set in the received command.
     */
//    public String getComponentName() {
//        return command.getArgumentValue(StandardArguments.COMPONENT_NAME, String.class);
//    }

    /**
     * Adds to a route the injection path into hummingbird. The injection path can
     * be complex. It will set headers for routing and filtering purposes. It will
     * route different kinds of messages to different endpoints for distribution.
     * 
     * @param route
     */
    protected void addInjectionRoute(ProcessorDefinition<?> route) {
        Scheduler scheduler = new Scheduler();

        route

                /** Dont route messages with a NULL body. */
                .choice()
                .when(simple("${in.body} == null"))
                .stop()
                .end()

                /** Set standard headers. */
                .setHeader(StandardArguments.NAME, simple("${in.body.name}"))
                .setHeader(StandardArguments.ISSUED_BY, simple("${in.body.issuedBy}"))
                .setHeader(StandardArguments.TYPE, simple("${in.body.type}"))
                .setHeader(StandardArguments.DATA_SET_ID, simple("${in.body.datasetidentifier}"))
                .setHeader("class", simple("${in.body.class.simpleName}"))
                
                /** Set object specific headers. */
                .choice()
                .when(body().isInstanceOf(State.class))
                .setHeader(StandardArguments.IS_STATE_OF, simple("${in.body.isStateOf}"))
                .when(body().isInstanceOf(Command.class))
                .setHeader(StandardArguments.DESTINATION, simple("${in.body.destination}"))
                .end()

                /** Schedule the release, if this object implements IScheduled. */
                .bean(scheduler)

                /** Route to the topic / query. */
                .choice()
                .when((body().isInstanceOf(Task.class))).to(StandardEndpoints.tasks)
                .when((body().isInstanceOf(CommandRequest.class))).to(StandardEndpoints.requests)
                .when((body().isInstanceOf(Command.class))).to(StandardEndpoints.commands)
                .otherwise().to(StandardEndpoints.monitoring);
    }
}
