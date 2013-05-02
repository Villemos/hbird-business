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

import org.hbird.business.api.IDataAccess;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.validation.Limit;

/**
 * A differential limit is a limit that checks whether a change to a parameter, whether
 * positive or negative, is not larger than a given delta. Each change can only be
 * of a given size. 
 * 
 * Notice that over multiple updates, the parameter may change its value beyond the
 * differential limit. In the sum the changes are larger, but each step is lower.
 * 
 * @author Gert Villemos
 */
public class DifferentialLimitChecker extends BaseLimitChecker {

	protected IDataAccess api = null;
	
	/**
	 * Constructor
	 * 
	 * @param limit The definition of the limit.
	 */
	public DifferentialLimitChecker(Limit limit) {
		super(limit);
		
		/** Get the current value. */
		lastValue = getApi().getParameter(limit.getLimitOfParameter());
	}

	@Override
	protected boolean checkLimit(Parameter parameter) {
		/** Calculate difference between lastValue and parameter. */
		return Math.abs(lastValue.getValue().doubleValue() - parameter.getValue().doubleValue()) < limit.getValue().doubleValue();
	}

	public IDataAccess getApi() {
		return api;
	}

	public void setApi(IDataAccess api) {
		this.api = api;
	}
	
	
}
