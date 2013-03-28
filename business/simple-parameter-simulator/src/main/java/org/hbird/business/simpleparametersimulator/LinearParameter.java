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
import org.apache.log4j.Logger;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.interfaces.IPart;

/**
 * A linearly changing parameter. The value is calculated as
 *   f(time) = intercept + deltaFrequency * (time - startTime) % modolus
 *   
 * The modolus defines the delta time at which the graph repeats itself, i.e.  
 *   f(time) == f(time + N*modolus), where N = {0, 1, 2, ...} 
 */
public class LinearParameter extends BaseParameter {

	/** The class logger. */
	protected static Logger logger = Logger.getLogger(LinearParameter.class);

	/** The initial value of the parameter. The intersection with the Y-axis so to say. */
	protected double intercept = 0;

	/** Delta rate of change, measured in value per milisecond. */
	protected double deltaFrequency = 0.1;

	/** The time modolus, i.e. the deta time at which the value resets. */
	protected long modolus = 60000;

	/** The time at which the parameter was first created. */
	protected Date startTime = null;

	/**
	 * Constructor setting all variables needed for the linear graph.
	 * 
	 * @param initialValue The initial value at time = 0
	 * @param deltaFrequency The delta change per elapsed ms
	 * @param modolus The delta time at which the value resets.
	 * @param name The name of the parameter to be generated.
	 */
	public LinearParameter(IPart issuedBy, String name, String description, String unit, double intercept, double deltaFrequency, long modolus) {
		super(issuedBy, name, description);
		this.intercept = intercept;
		this.deltaFrequency = deltaFrequency;	
		this.modolus = modolus;

		this.startTime = new Date();
		this.value = new Double(intercept + deltaFrequency * (((new Date()).getTime() - startTime.getTime()) % modolus));
		this.unit  = unit;
	}

	/* (non-Javadoc)
	 * @see org.hbird.simpleparametersimulator.BaseParameter#process(org.apache.camel.Exchange)
	 */
	@Handler
	public Parameter process() {
		logger.debug("Sending new linear value with name '" + name + "'.");
		this.value = new Double(intercept + deltaFrequency * (((new Date()).getTime() - startTime.getTime()) % modolus));

		return new Parameter(issuedBy, name, description, value, unit);
	}

	public double getIntercept() {
		return intercept;
	}

	public void setIntercept(double intercept) {
		this.intercept = intercept;
	}

	public double getDeltaFrequency() {
		return deltaFrequency;
	}

	public void setDeltaFrequency(double deltaFrequency) {
		this.deltaFrequency = deltaFrequency;
	}

	public long getModolus() {
		return modolus;
	}

	public void setModolus(long modolus) {
		this.modolus = modolus;
	}
}
