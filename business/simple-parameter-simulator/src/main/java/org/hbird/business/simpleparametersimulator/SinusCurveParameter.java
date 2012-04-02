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

import org.apache.camel.Exchange;
import org.apache.log4j.Logger;

/**
 * Class for generating a parameter developing like a sinus curve. The value
 * is calculated as
 *   f(time) = intercept + amplitude * sin(angularFrequency * (time - startTime) % modolus + phase)
 *   
 * The modolus defines the delta time at which the graph repeats itself, i.e.  
 *   f(time) == f(time + N*modolus), where N = {0, 1, 2, ...} 
 *  
 * The intercept is the base height of the sinus curve.
 * 
 * The amplitude is the height of the curve changes. The value will lie between {intercept - Amplitude, intercept + Amplitude}  
 * 
 * The frequency is measured in radians (2*pi radians == 360 degrees) per millisecond. The curve will repeat itself when
 * deltaTime * frequency == 2*pi. To get a specific time length of the curve, set the frequency = 2*pi / deltaTime
 * 
 * The phase is the start position of the sinus curve. A phase == 0 means that the curve start by 0 and will increase 
 * to + amplitude, then decrease to - Amplitude, then reach 0 again. A phase == pi means is a inversion; the curve will
 * start at 0 and decrease to - amplitude, then increase to + amplitude, the reach 0 again. A phase == pi / 2 means that the
 * curve will start at +Amplitude, decrease accros 0 to -Amplitude, the increase back to +Amplitude.  
 */
public class SinusCurveParameter extends BaseParameter {

	/***/
	private static final long serialVersionUID = 8881379617713090933L;

	/** The class logger. */
	protected static Logger logger = Logger.getLogger(SinusCurveParameter.class);

	/** The delta change in angle per milisecond. Defaults to one full curve every 10 seconds. */
	protected double angularFrequency = 2*Math.PI/10000;
	
	/** The maximal change in value. */
	protected double amplitude = 1;
	
	/** The starting phase measured in radians. 2*pi radians == 360 degrees.*/
	protected double phase = 0;
	
	/** The starting height of the curve. */
	protected double intercept = 0;

	/** The time modolus, i.e. the deta time at which the value resets. */
	protected long modolus = 60000;

	/** The time at which the parameter was first created. */
	protected Date startTime = null;

	/**
	 * Basic constructor setting all field values.
	 * 
	 * @param angularFrequency
	 * @param amplitude
	 * @param phase
	 * @param intercept
	 * @param modolus
	 * @param name
	 */
	public SinusCurveParameter(String name, String description, String unit, double angularFrequency, double amplitude, double phase, double intercept, long modolus) {
		super(name, description);
		this.angularFrequency = angularFrequency;
		this.amplitude = amplitude;
		this.phase = phase;
		this.modolus = modolus;
		this.intercept = intercept;
		
		startTime = new Date();
		this.value = new Double(amplitude * Math.sin(angularFrequency * ((((new Date()).getTime() - startTime.getTime()))%modolus) + phase) + intercept);
		this.unit = unit;		
	}

	/* (non-Javadoc)
	 * @see org.hbird.simpleparametersimulator.BaseParameter#process(org.apache.camel.Exchange)
	 */
	public void process(Exchange exchange) {
		try {
			logger.debug("Sending new sinus value with name '" + name + "'.");
			newInstance();
			this.value = new Double(amplitude * Math.sin(angularFrequency * ((((new Date()).getTime() - startTime.getTime()))%modolus) + phase) + intercept);
			exchange.getIn().setBody(this);
		} 
		catch (Exception e) {
			logger.error("Courght exception " + e);
			e.printStackTrace();
		}
	}

	public double getAngularFrequency() {
		return angularFrequency;
	}

	public void setAngularFrequency(double angularFrequency) {
		this.angularFrequency = angularFrequency;
	}

	public double getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(double amplitude) {
		this.amplitude = amplitude;
	}

	public double getPhase() {
		return phase;
	}

	public void setPhase(double phase) {
		this.phase = phase;
	}

	public double getIntercept() {
		return intercept;
	}

	public void setIntercept(double intercept) {
		this.intercept = intercept;
	}

	public long getModolus() {
		return modolus;
	}

	public void setModolus(long modolus) {
		this.modolus = modolus;
	}
}
