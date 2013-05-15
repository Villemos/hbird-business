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
package org.hbird.business.validation.bean;

import org.apache.camel.model.ProcessorDefinition;
import org.hbird.business.api.ApiFactory;
import org.hbird.business.core.SoftwareComponentDriver;
import org.hbird.business.validation.LimitCheckComponent;
import org.hbird.exchange.configurator.StandardEndpoints;
import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.validation.Limit;
import org.hbird.exchange.validation.Limit.eLimitType;

/**
 * Component builder to create a Limit component
 * 
 * @author Gert Villemos
 * 
 */
public class LimitCheckComponentDriver extends SoftwareComponentDriver {

	@Override
	public void doConfigure() {

		LimitCheckComponent request = (LimitCheckComponent) entity;
		Limit limit = request.getLimit();

		String componentID = request.getID();
		String limitValueName = componentID + "/" + "LimitValue";
		
		if (limit.getType() == eLimitType.Lower) {
			createRoute(limit.getLimitOfParameter(), new LowerLimitChecker(limit), componentID, limitValueName);
		}
		else if (limit.getType() == eLimitType.Upper) {
			createRoute(limit.getLimitOfParameter(), new UpperLimitChecker(limit), componentID, limitValueName);
		}
		else if (limit.getType() == eLimitType.Static) {
			createRoute(limit.getLimitOfParameter(), new StaticLimitChecker(limit), componentID, limitValueName);
		}
		else if (limit.getType() == eLimitType.Differential) {
			DifferentialLimitChecker checker = new DifferentialLimitChecker(limit);
			checker.setApi(ApiFactory.getDataAccessApi(entity.getName()));
			createRoute(limit.getLimitOfParameter(), checker, componentID, limitValueName);
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
	protected void createRoute(String parameter, BaseLimitChecker limit, String componentid, String limitValueName) {

		/** Create the route for limit checking. */
		ProcessorDefinition<?> route = from(StandardEndpoints.MONITORING + "?selector=" + StandardArguments.ENTITY_ID + "='" + parameter + "'")
				.bean(limit, "processParameter");
		addInjectionRoute(route);

		/** Create the route for enabling/disabling limit checking. */
		route = from(StandardEndpoints.MONITORING + "?selector=isStateOf='" + componentid + "'")
				.bean(limit, "processEnabled");
		addInjectionRoute(route);

		/** Create the route for changing the limit value. */
		route = from(StandardEndpoints.MONITORING + "?selector=name='" + limitValueName + "'")
				.bean(limit, "processLimit");
		addInjectionRoute(route);
	}
}
