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

import org.apache.log4j.Logger;

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
	protected static Logger logger = Logger.getLogger(SetParameter.class);

	/** The parameter to be set. */
	protected Parameter parameter = null;
	
	/**
	 * Constructor, accepting the execution time and the parameter to be set.
	 * @param executionTime The time at which the task should be executed.
	 * @param parameter The parameter to be set.
	 */
	public SetParameter(String name, String description, long executionTime, Parameter parameter) {
		super("", name, description, executionTime);
		this.parameter = parameter;
	}

	/**
	 * Method that will send the message to the parameter query. 
	 * 
	 * @param arg0 The exchange to be send.
	 */
	public Object execute() {
		logger.info("Setting parameter '" + parameter.getName() + "' to value '" + parameter.getValue().toString() + "'.");
		
		/** Send the preconfigured parameter. */
		return parameter;
	}
}
