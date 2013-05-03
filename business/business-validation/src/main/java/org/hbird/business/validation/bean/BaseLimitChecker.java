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

import org.apache.camel.Body;
import org.apache.log4j.Logger;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.validation.Limit;

/** 
 * The baselimit is an abstract base class for all limit check types. It
 * holds all methods to receive messages through camel routes and 
 * parse them on, i.e. it is a camel processor bean.
 * 
 * A limit bean transform a parameter value into a parameter state value, i.e.
 * it validates whether the parameter is within the limit and sends a corresponding
 * a parameter state value of 'true' (state is in limit) or false (outside limit).
 * 
 * A specific limit instance is configured through
 * - StateName. The name of the state parameter that this limit will issue. 
 * - Enabled. A parameter setting whether this limit is enabled or disabled.
 * - limit. The actual limit to be validated against.
 * - parameter. The last received parameter value.
 * 
 * The state name is defined at creation, where as the enabled, limit and parameter values 
 * are dynamically updated through camel updates. Three camel routes are defined into the bean;
 * - processParameter. Processes a camel Exchange containing a parameter instance to be limit checked.  
 * - processLimit. Process a camel Exchange containing an update to the limit value. 
 * - processEnabled. Process a camel Exchange enabling / disabling this limit.
 *
 */
public abstract class BaseLimitChecker {

	/** The class logger. */
	private static org.apache.log4j.Logger logger = Logger.getLogger(BaseLimitChecker.class);

	/** The limit definition. */
	protected Limit limit = null;

	/** The last received parameter to be checked. Stored in case the limit is changed or the
	 * limit is enabled. Then the last received value is used to re-check the limit. */
	protected Parameter lastValue = null;

	public BaseLimitChecker(Limit limit) {
		this.limit = limit;
	}
	
	/**
	 * Method to process a parameter update. The method is expected called as part of a camel
	 * route. The method will set the local parameter value, then process the limit check and
	 * possibly issue a state parameter instance.
	 * 
	 * @param arg0 A camel exchange carrying a parameter instance.
	 * @throws Exception
	 */
	public State processParameter(@Body Parameter newParameter) {
		logger.info("Limit state '" + limit.getName() + "' received parameter value for validation.");
		State state = doProcess(newParameter);
		lastValue = newParameter;
		return state;
	}

	/**
	 * Method for receiving a notification of a limit update. The method is expected called as part of a camel
	 * route. The method will set the local limit value, then process the limit check and
	 * possibly issue a state parameter instance. 
	 * 
	 * @param arg0 A camel exchange carrying a Double instance expressing the size of the limit.
	 * @throws Exception
	 */
	public State processLimit(@Body Parameter newLimit) {
		limit.setValue(newLimit.getValue());
		logger.info("Limit '" + limit.getName() + "' set to '" + limit.getValue() + "'.");
		return doProcess(lastValue);
	}

	/**
	 * Method for receiving a notification of a enable / disable switch. The method is expected called as part of a camel
	 * route. The method will set the local enabled flag, then process the limit check and
	 * possibly issue a state parameter instance.
	 * 
	 * @param arg0 A camel exchange carrying a Boolean instance switching the limit on or off.
	 * @throws Exception
	 */
	public State processEnabled(@Body State newEnabled) throws Exception {
		limit.setEnabled((Boolean) newEnabled.getValue());
		logger.info("Limit '" + limit.getName() + "' switches to ENABLED state '" + limit.getEnabled() + "'.");
		return doProcess(lastValue);
	}


	/**
	 * The actual method for processing the limit check. The limit will only be checked if the 
	 * limit is 'ready' and 'enabled';
	 * 
	 * A limit is 'ready' if the limit is not null AND the parameter is not null.
	 * 
	 * A limit is 'enabled' if the enabled flag is null OR the flag is equal to true. 
	 * 
	 * @param exchange A camel exchange carrying a Boolean instance switching the limit on or off.
	 * @throws NotComparableTypeException
	 */
	protected State doProcess(Parameter parameter) {
		State state = null;

		if (isEnabled() == true && isReady()) {
			state = new State(limit.getName(), limit.getName());
			state.setDescription(limit.getDescription());
			state.setIsStateOf(parameter.getID());
			state.setState(checkLimit(parameter));
		}
		
		return state;
	}

	/**
	 * Method to determine whether this limit is 'enabled'. The limit will be
	 * enabled if;
	 * 1) The parameter determining whether it is enabled is 'null', i.e. not set. OR
	 * 2) The parameter determining whether it is enabled has the value 'true'.
	 * 
	 * @return Flag that will be true of the limit is enabled. Else false.
	 */
	protected boolean isEnabled() {
		return limit.getEnabled() == null || limit.getEnabled() == true;
	}

	/**
	 * Method to determine whether this limit is 'ready'. The limit will be ready if;
	 * 1) The limit is not null. AND
	 * 2) The parameter value is not null.
	 * 
	 * @return Flag indicating whether the limit can be considered ready.
	 */
	protected boolean isReady() {
		return limit != null;
	}

	
	
	public Limit getLimit() {
		return limit;
	}

	public void setLimit(Limit limit) {
		this.limit = limit;
	}

	/**
	 * Method actually performing the limit check, for example larger / smaller than.
	 * 
	 * Before calling the method the user must ensure that the parameter and the limit are 
	 * not null. This should be done using the 'isReady()' method.
	 * 
	 * @return boolean which is true if the parameter is within the limit, else false.
	 * @throws NotComparableTypeException
	 */
	protected abstract boolean checkLimit(Parameter parameter);
}
