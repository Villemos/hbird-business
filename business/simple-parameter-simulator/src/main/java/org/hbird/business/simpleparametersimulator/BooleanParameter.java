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

import org.apache.camel.Exchange;
import org.apache.log4j.Logger;

/**
 * Class simulating a boolean parameter. The parameter flips each time the process
 * method is called, i.e. value = !value.
 */
public class BooleanParameter extends BaseParameter {

	/***/
	private static final long serialVersionUID = -5694636851932750007L;

	/** The class logger. */
	protected static Logger logger = Logger.getLogger(BooleanParameter.class);

	/**
	 * Basic constructor, setting the initial value and the name of the boolean
	 * parameter.  
	 * 
	 * @param value The initial value of the parameter.
	 * @param name The name of the parameter to be generated.
	 */
	public BooleanParameter(String name, String description, Boolean value, String unit) {
		super(name, description, value, unit);
	}

	/* (non-Javadoc)
	 * @see org.hbird.simpleparametersimulator.BaseParameter#process(org.apache.camel.Exchange)
	 */
	public void process(Exchange exchange) {
		try {
			logger.debug("Sending new boolean value with name '" + name + "'.");
			newInstance();
			this.value = new Boolean(!(Boolean)value);
			exchange.getIn().setBody(this);
		} 
		catch (Exception e) {
			logger.error("Courght exception " + e);
			e.printStackTrace();
		}
	}
}
