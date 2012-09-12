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
package org.hbird.business.validation.limits;

import org.hbird.exchange.core.Comperator;
import org.hbird.exchange.validation.Limit;


/**
 * Limit class for checking whether an upper end limit has been violated.
 */
public class UpperLimitChecker extends BaseLimitChecker {

	/***/
	private static final long serialVersionUID = -6367547285976628021L;

	/**
	 * Constructor with no initial limit value. The limit will NOT start processing
	 * before a limit value has been received through the processLimit() method.
	 * 
	 * @param stateName Name of the state parameter that will be issued.
	 */
	public UpperLimitChecker(String name, String description, Limit limit) {
		super(name, description);
		this.limit = limit;
	}
	
	/* (non-Javadoc)
	 * @see org.hbird.validation.parameter.BaseLimit#checkLimit()
	 */
	protected boolean checkLimit() {
		return Comperator.compare(lastValue.getValue(), limit.limit) <= 0;
	}	
}
