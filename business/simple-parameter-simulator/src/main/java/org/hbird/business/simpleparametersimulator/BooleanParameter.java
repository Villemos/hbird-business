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
import org.apache.log4j.Logger;
import org.hbird.exchange.core.State;

/**
 * Class simulating a boolean parameter. The parameter flips each time the process
 * method is called, i.e. value = !value.
 */
public class BooleanParameter {

	/** The class logger. */
	protected static Logger LOG = Logger.getLogger(BooleanParameter.class);

	protected String issuedBy;
	protected String name;
	protected String description;
	protected Boolean value;
	protected String isStateOf;
	
	/**
	 * Basic constructor, setting the initial value and the name of the boolean
	 * parameter.  
	 * 
	 * @param value The initial value of the parameter.
	 * @param name The name of the parameter to be generated.
	 */
	public BooleanParameter(String issuedBy, String name, String description, Boolean value, String isStateOf) {
		this.issuedBy = issuedBy;
		this.name = name;
		this.description = description;
		this.value = value;
		this.isStateOf = isStateOf;
	}

	/* (non-Javadoc)
	 * @see org.hbird.simpleparametersimulator.BaseParameter#process(org.apache.camel.Exchange)
	 */
	@Handler
	public State process() {
		try {
			LOG.debug("Sending new boolean value with name '" + name + "'.");
			this.value = new Boolean(!(Boolean)value);
		} 
		catch (Exception e) {
			LOG.error("Courght exception " + e);
			e.printStackTrace();
		}
		
		return new State(issuedBy, name, "", description, isStateOf, value);
	}
}