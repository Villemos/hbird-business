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
package org.hbird.business.api.impl;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.model.RouteDefinition;
import org.hbird.business.core.HbirdRouteBuilder;
import org.hbird.exchange.core.Command;

public abstract class HbirdApi extends HbirdRouteBuilder {

	protected ProducerTemplate template = null;
	
	protected String inject = "seda:inject";
	
	protected String issuedBy = "";
	
	protected CamelContext context = null;
	
	public HbirdApi(String issuedBy) {
		this.issuedBy = issuedBy;
		
		this.context = getContext();

		try {
			context.addRoutes(this);
			template = context.createProducerTemplate();
			this.context.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void configure() throws Exception {
		RouteDefinition route = from(inject);
		addInjectionRoute(route);
	}	
}
