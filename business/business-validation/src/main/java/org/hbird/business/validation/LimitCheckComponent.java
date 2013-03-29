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
package org.hbird.business.validation;

import org.hbird.business.validation.bean.LimitCheckComponentDriver;
import org.hbird.exchange.configurator.StartablePart;
import org.hbird.exchange.validation.Limit;
import org.hbird.exchange.validation.Limit.eLimitType;

/**
 * @author Admin
 *
 */
public class LimitCheckComponent extends StartablePart {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2025228049084739851L;

	protected Limit limit = null;
	
	/**
	 * @param name
	 * @param description
	 */
	public LimitCheckComponent(String ID, String name, Limit limit, String driver) {
		super(ID, name, "A limit check component", driver);
		this.limit = limit;
	}

	public LimitCheckComponent(String ID, String name, Limit limit) {
		super(ID, name, "A limit check component", LimitCheckComponentDriver.class.getName());
		this.limit = limit;
	}

	public LimitCheckComponent(String ID, String name, String limitName, eLimitType type, String ofParameter, Double value, String stateName, String stateDescription) {
		super(ID, name, "A limit check component", LimitCheckComponentDriver.class.getName());

		this.limit = new Limit(type, ofParameter, value, stateName, stateDescription);
	}

	public Limit getLimit() {
		return limit;
	}

	public void setLimit(Limit limit) {
		this.limit = limit;
	}
}
