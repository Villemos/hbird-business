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
package org.hbird.business.validation.base;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.log4j.Logger;
import org.hbird.exchange.core.StateParameter;

/**
 * Handler which only triggers the next downstream handler if a specific number
 * of violations have occurred.
 * 
 * The handler counts the number of violations, i.e. is state full. As long as the count is
 * below the trigger limit, the route will be stopped. If the count is equal to or above, then
 * the message is parsed on.
 * 
 * The counter is reset automatically when a valid parameter state is received.
 *
 */
public class ViolationCountFilter {

	/** The class logger. */
	private static org.apache.log4j.Logger logger = Logger.getLogger(ViolationCountFilter.class);
	
	/** The number of consecutive violations. Will be reset to 0 when the first non-violation is received. */
	protected long violations = 0;

	/** Limit when to trigger the next downstream handler. Default value is 2 because a value 
	 * of 1 is the same as no limit handler... */
	protected long triggerLimit = 2;

	/**
	 * Constructor.
	 * 
	 * @param triggerLimit The limit of the system.
	 */
	public ViolationCountFilter(long triggerLimit) {
		this.triggerLimit = triggerLimit;
	}
	
	/**
	 * Default Constructor
	 */
	public ViolationCountFilter() {
		
	}
	
	/**
	 * Method to process a state. The method will count the number of violations. 
	 * 
	 * @param exchange The exchange carrying the state.
	 */
	public void process(Exchange exchange, @Body StateParameter state) {
		if ((Boolean) state.getValue() == false) {
			violations++;

			if (violations >= triggerLimit) {
				logger.debug(violations + " registered. Forwarding state " + state.getName() + " with value " + state.getValue());
				/** Continue route... */
			}
			else {
				logger.info("State change filtered out as number of violations (" + violations + ") is below trigger limit (" + triggerLimit + ").");
				exchange.setProperty(Exchange.ROUTE_STOP, true);
			}
		}
		else {
			/** In case the state is valid, then all violations are cleared. */
			logger.debug("Resetting violation counter");
			violations = 0;
		}
	}

	/**
	 * Resets the violations to 0.
	 */
	public void reset() {
		violations = 0;
	}

	/**
	 * Getter of the current number of violations that have been registered. The violations
	 * count is the number received consecutive with no valid instance. Upon reception of
	 * a valid instance the violation counter will be reset to 0.
	 * 
	 * @return The current count of violations.
	 */
	public long getViolations() {
		return violations;
	}
	
	/**
	 * Getter method for the trigger limit, i.e. the number of consecutive violations that must
	 * occur before a state parameter is issued.
	 * 
	 * @return Number of violations representing the limit.
	 */
	public long getTriggerLimit() {
		return triggerLimit;
	}
	
	/**
	 * Setter method for the number of violations that must occur before the state parameter is
	 * issued.
	 * 
	 * @param triggerLimit The limit to be set.
	 */
	public void setTriggerLimit(long triggerLimit) {
		this.triggerLimit = triggerLimit;
	}
}
