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
package org.hbird.exchange.validation;

import org.hbird.exchange.core.Parameter;

public class Limit extends Parameter {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4304941431961981299L;

    public Limit(String ID, String name) {
        super(ID, name);
    }

    /**
     * The type of the limit. The type can be;
     * - Lower. The limit is violated if the parameter value falls BELOW the limit.
     * - Upper. The limit is violated if the parameter value goes ABOVE the limit.
     * - Static. The limit is violated if it has any other value than the defined.
     */
    public enum eLimitType {
        Lower, Upper, Static, Differential
    };

    protected eLimitType type = eLimitType.Lower;

    protected Boolean enabled = true;

    /**
     * The number of points after the comma to which the value must be accurate. Can be used
     * in particular with STATIC limits, where absolutely equal may be too hard a constrain.
     */
    protected int accuracy = 32;

    /** The name of the parameter being checker. */
    protected String limitOfParameter = null;

    protected String stateName = "";
    
	/**
	 * @return the type
	 */
	public eLimitType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(eLimitType type) {
		this.type = type;
	}

	/**
	 * @return the enabled
	 */
	public Boolean getEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the accuracy
	 */
	public int getAccuracy() {
		return accuracy;
	}

	/**
	 * @param accuracy the accuracy to set
	 */
	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}

	/**
	 * @return the limitOfParameter
	 */
	public String getLimitOfParameter() {
		return limitOfParameter;
	}

	/**
	 * @param limitOfParameter the limitOfParameter to set
	 */
	public void setLimitOfParameter(String limitOfParameter) {
		this.limitOfParameter = limitOfParameter;
	}

	/**
	 * @return the stateName
	 */
	public String getStateName() {
		return stateName;
	}

	/**
	 * @param stateName the stateName to set
	 */
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
}
