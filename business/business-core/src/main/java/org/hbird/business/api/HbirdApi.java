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
package org.hbird.business.api;

import java.util.List;
import java.util.UUID;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.Route;
import org.apache.camel.model.RouteDefinition;
import org.hbird.business.core.HbirdRouteBuilder;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.dataaccess.DataRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HbirdApi extends HbirdRouteBuilder implements IHbirdApi {

    private static final Logger LOG = LoggerFactory.getLogger(HbirdApi.class);

    protected ProducerTemplate template = null;

    protected String inject = "seda://inject";

    protected String issuedBy = "";

    protected String destination = "";

    protected CamelContext context = null;

    public HbirdApi(String issuedBy, String destination) {
        this.issuedBy = issuedBy;
        this.destination = destination;
        this.context = getContext(); // XXX - 24.04.2013, kimmell - this will create new camel context

        LOG.info("Creating new instance of '{}', using '{}', issuedBy '{}'", new Object[] { getClass().getSimpleName(), this.context, issuedBy });

        try {
            context.addRoutes(this);
            template = context.createProducerTemplate();
            this.context.start();
        }
        catch (Exception e) {
            LOG.error("Failed to create new instanceof '{}'", getClass().getSimpleName(), e);
        }
    }

    public HbirdApi(String issuedBy, String destination, CamelContext context) {
        this.issuedBy = issuedBy;
        this.destination = destination;
        this.context = context;

        LOG.info("Creating new instance of '{}', using {}', issuedBy '{}'", new Object[] { getClass().getSimpleName(), this.context, issuedBy });

        try {
            context.addRoutes(this);
            template = context.createProducerTemplate();
            this.context.start();
        }
        catch (Exception e) {
            LOG.error("Failed to create new instanceof '{}'", getClass().getSimpleName(), e);
        }
    }

    @Override
    public void configure() throws Exception {

        /** Make this injection route unique. */
        inject += "_" + UUID.randomUUID().toString();

        LOG.info("Creating injection route '{}'.", inject);
        RouteDefinition route = from(inject);
        addInjectionRoute(route);

        // if (containsRoute(inject) == false) {
        // LOG.info("Creating injection route '" + inject + "'.");
        // RouteDefinition route = from(inject);
        // addInjectionRoute(route);
        // }
        // else {
        // LOG.info("Found existing injection route '" + inject + "'.");
        // }
    }

    protected boolean containsRoute(String routeUri) {
        boolean found = false;
        for (Route route : getContext().getRoutes()) {
            if (route.getEndpoint().getEndpointUri().equals(routeUri)) {
                found = true;
                break;
            }
        }

        return found;
    }

    /**
     * Method to publish a Named object.
     * 
     * @param object
     * @return
     */
    public EntityInstance publish(EntityInstance object) {
    	if(object.getIssuedBy() == null) { // XXX: Not sure about that
    		object.setIssuedBy(issuedBy);
    	}

        if (object instanceof Command && ((Command) object).getDestination() == null) {
            ((Command) object).setDestination(destination);
        }

        template.sendBody(inject, object);
        return object;
    }

    /**
     * Method to send a data request that demands a reply.
     * 
     * @param request
     * @return
     */
    protected <T> List<T> executeRequestRespond(DataRequest request) {
        request.setIssuedBy(issuedBy);

        if (request.getDestination() == null) {
            request.setDestination(destination);
        }

        @SuppressWarnings("unchecked")
        List<T> list = template.requestBody(inject, request, List.class);
        return list;
    }

    /**
     * Method to send a data request that demands a reply.
     * 
     * @param request
     * @return
     */
    protected void executeRequest(Command request) {
        request.setIssuedBy(issuedBy);

        if (request.getDestination() == null) {
            request.setDestination(destination);
        }

        template.sendBody(inject, request);
    }

    protected <T> T getFirst(List<T> list) {
        return list == null || list.isEmpty() ? null : list.get(0);
    }

    /**
     * @return the destination
     */
    @Override
    public String getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    @Override
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * @return the issuedBy
     */
    @Override
    public String getIssuedBy() {
        return issuedBy;
    }

    /**
     * @param issuedBy the issuedBy to set
     */
    @Override
    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    protected String getID() {
        return issuedBy + "/request";
    }

    /**
     * @see org.hbird.business.api.IHbirdApi#dispose()
     */
    @Override
    public void dispose() throws Exception {
        if (context != null) {
            try {
                context.stop();
                context = null;
            }
            catch (Exception e) {
                LOG.error("Failed to stop context '{}', in '{}', issuedBy '{}'", new Object[] { context, getClass().getSimpleName(), issuedBy });
                throw e;
            }
        }
    }
}
