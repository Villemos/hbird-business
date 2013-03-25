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
package org.hbird.exchange.tasking;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.Parameter;

/**
 * Task that sets a parameter to a specific value at a given time. The task
 * will when executed create a parameter exchange and send it to the route
 * start point configured through the 'routeEntryName'.
 * 
 * The task can be used to set any type of parameter, including state parameters.
 * 
 * The task among others be used to;
 * 1) Enable / disable state checking in specific intervals.
 * 2) Change a limit to a new value.
 * 3) Set the state of an object/
 *
 */
public class SetParameter extends Task {

	/** Unique UID. */
	private static final long serialVersionUID = -4583816287276036640L;

	/** The class logger. */
	protected static Logger LOG = Logger.getLogger(SetParameter.class);

	/** The parameter to be set. */
	protected Parameter parameter = null;
	
	/**
	 * Constructor, accepting the execution time and the parameter to be set.
	 * @param executionTime The time at which the task should be executed.
	 * @param parameter The parameter to be set.
	 */
	public SetParameter(String issuedBy, String name, String description, long executionTime, Parameter parameter) {
		super(issuedBy, name, description, executionTime);
		this.parameter = parameter;
	}
	
	public SetParameter(SetParameter base) {
		this(base.issuedBy, base.name, base.description, base.executionTime, base.parameter);
	}

	public SetParameter(String issuedBy, String name, String description, int executionTime, String paraName, String paraDescription, Number value, String unit) {
		super(issuedBy, name, description, executionTime);
		this.parameter = new Parameter(name, paraName, paraDescription, value, unit);
	}

	/**
	 * Method that will send the message to the parameter query. 
	 * 
	 * @param arg0 The exchange to be send.
	 */
	public List<Named> execute() {
		LOG.info("Setting parameter '" + parameter.getName() + "' to value '" + parameter.getValue().toString() + "'.");
		
		/** Send the preconfigured parameter. */
		List<Named> results = new ArrayList<Named>();
		results.add(parameter);
		
		return results;
	}

	public Parameter getParameter() {
		return parameter;
	}

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}
	
	
}
