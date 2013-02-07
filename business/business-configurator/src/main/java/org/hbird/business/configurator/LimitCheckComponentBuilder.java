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

import org.apache.camel.model.ProcessorDefinition;
import org.hbird.business.validation.limits.BaseLimitChecker;
import org.hbird.business.validation.limits.LowerLimitChecker;
import org.hbird.business.validation.limits.StaticLimitChecker;
import org.hbird.business.validation.limits.UpperLimitChecker;
import org.hbird.exchange.configurator.StandardEndpoints;
import org.hbird.exchange.configurator.StartLimitComponent;
import org.hbird.exchange.validation.Limit;
import org.hbird.exchange.validation.Limit.eLimitType;

/**
 * Component builder to create a Limit component
 * 
 * @author Gert Villemos
 *
 */
public class LimitCheckComponentBuilder extends ComponentBuilder {

	@Override
	public void doConfigure() {

		StartLimitComponent request = (StartLimitComponent) this.command;
		
		Limit limit = (Limit) request.getArgument("limit");

		String componentname = (String) request.getArgument("componentname");

		String limitValueParameterName = componentname + "_VALUE";
		if (request.getArguments().containsKey("limitvalue")) {
			limitValueParameterName = (String) request.getArgument("valueparameter");
		}
		
		if (limit.type == eLimitType.Lower) {
			createRoute(limit.limitOfParameter, new LowerLimitChecker(limit), componentname, limitValueParameterName);			
		}
		else if (limit.type == eLimitType.Upper) {
			createRoute(limit.limitOfParameter, new UpperLimitChecker(limit), componentname, limitValueParameterName);			
		}
		else if (limit.type == eLimitType.Static) {
			createRoute(limit.limitOfParameter, new StaticLimitChecker(limit), componentname, limitValueParameterName);			
		}
		
		addCommandHandler();
	}

	/**
	 * Method to create the limit bean and the routes that will route data into and out of the bean
	 * 
	 * @param parameter The name of the parameter being validated
	 * @param limit The limit to be applied
	 * @param componentname The name of this component
	 * @param limitValueName The name of the value defining the limit
	 */
	protected void createRoute(String parameter, BaseLimitChecker limit, String componentname, String limitValueName) {

		/** Create the route for limit checking. */
		ProcessorDefinition<?> route = from(StandardEndpoints.monitoring + "?selector=name='" + parameter + "'")
				.bean(limit, "processParameter");
		addInjectionRoute(route);

		/** Create the route for enabling/disabling limit checking. */
		route = from(StandardEndpoints.monitoring + "?selector=isStateOf='" + componentname + "'")
				.bean(limit, "processEnabled");
		addInjectionRoute(route);

		/** Create the route for changing the limit value. */
		route = from(StandardEndpoints.monitoring + "?selector=name='" + limitValueName + "'")
				.bean(limit, "processLimit");
		addInjectionRoute(route);
	}
}