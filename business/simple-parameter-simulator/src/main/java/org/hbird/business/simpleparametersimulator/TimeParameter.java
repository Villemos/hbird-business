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
package org.hbird.business.simpleparametersimulator;

import java.util.Date;

import org.apache.camel.Handler;
import org.hbird.exchange.core.Parameter;

public class TimeParameter extends BaseParameter {

	protected String ID;
	
	public TimeParameter(String ID, String issuedBy, String name, String description, String unit) {
		super(issuedBy, name, description, 0d, unit);
		this.ID = ID;
	}

	@Override
	@Handler
	protected Parameter process() {
		value = (new Date()).getTime();		
		Parameter newParameter = new Parameter(ID, name);
		newParameter.setIssuedBy(issuedBy);
		newParameter.setDescription(description);
		newParameter.setValue(value);
		newParameter.setUnit(unit);
		return newParameter;
	}
}
