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

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.model.RouteDefinition;
import org.hbird.business.core.HbirdRouteBuilder;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.dataaccess.DataRequest;

public abstract class HbirdApi extends HbirdRouteBuilder {

    protected ProducerTemplate template = null;

    protected String inject = "seda:inject";

    protected String issuedBy = "";

    protected String destination = "";
    
    protected CamelContext context = null;

    public HbirdApi(String issuedBy, String destination) {
        this.issuedBy = issuedBy;
        this.destination = destination;

        this.context = getContext();

        try {
            context.addRoutes(this);
            template = context.createProducerTemplate();
            this.context.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void configure() throws Exception {
        RouteDefinition route = from(inject);
        addInjectionRoute(route);
    }

    
	/**
	 * Method to publish a Named object. 
	 * 
	 * @param object
	 * @return
	 */
	public Named publish(Named object) {
		object.setIssuedBy(issuedBy);
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
	public String getDestination() {
		return destination;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * @return the issuedBy
	 */
	public String getIssuedBy() {
		return issuedBy;
	}

	/**
	 * @param issuedBy the issuedBy to set
	 */
	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}
}
