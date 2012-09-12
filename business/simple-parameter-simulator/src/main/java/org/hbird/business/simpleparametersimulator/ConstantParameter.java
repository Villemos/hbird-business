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

import org.apache.camel.Handler;
import org.hbird.exchange.core.Parameter;

/**
 * A constant parameter. Every time process is called, a new instance of the
 * parameter is send.
 */
public class ConstantParameter extends BaseParameter {

	public ConstantParameter(String name, String description, String unit, Double value) {
		super(name, description, value, unit);
	}

	/* (non-Javadoc)
	 * @see org.hbird.simpleparametersimulator.BaseParameter#process(org.apache.camel.Exchange)
	 */
	@Handler
	public Parameter process() {
		return new Parameter("simulator", name, description, value, unit);
	}

	public Double getConstantValue() {
		return (Double) value;
	}

	public void setConstantValue(Double constantValue) {
		this.value = constantValue;
	}
}
