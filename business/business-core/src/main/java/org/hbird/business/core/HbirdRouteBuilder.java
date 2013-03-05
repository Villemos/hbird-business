package org.hbird.business.core;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ProcessorDefinition;
import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.configurator.StandardEndpoints;
import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.ILocationSpecific;
import org.hbird.exchange.core.ISatelliteSpecific;
import org.hbird.exchange.core.State;
import org.hbird.exchange.tasking.Task;

public abstract class HbirdRouteBuilder extends RouteBuilder {

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

                /** Set object specific headers. */
                .choice()
                .when(body().isInstanceOf(State.class))
                .setHeader(StandardArguments.IS_STATE_OF, simple("${in.body.isStateOf}"))
                .when(body().isInstanceOf(Command.class))
                .setHeader(StandardArguments.DESTINATION, simple("${in.body.destination}"))
                .when(body().isInstanceOf(ILocationSpecific.class))
                .setHeader(StandardArguments.LOCATION, simple("${in.body.locationName}"))
                .when(body().isInstanceOf(ISatelliteSpecific.class))
                .setHeader(StandardArguments.SATELLITE_NAME, simple("${in.body.satelliteName}"))
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
