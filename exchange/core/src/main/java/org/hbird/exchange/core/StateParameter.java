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
package org.hbird.exchange.core;

/** A boolean state parameter. 
 *  Any other type may have a (set of) states associated. The association is
 *  not through direct dependency, but through the states 'isstateOff' field. 
 *  
 *  A state parameter is a parameter with the following constrains;
 *  - Type must be 'Boolean'.
 *  - The field 'isStateOff' must provide the name of the object its a state of. */
public class StateParameter extends Parameter {
	
	/** The unique UID. */
	private static final long serialVersionUID = 6234660528925795242L;
	
	/** The ID of the object that this state parameter is a state of. */
	protected String isStateOf;
		
	/**
	 * Constructor of the state parameter. The timestamp will be set to the current time.
	 * 
	 * @param stateName the name of this state.
	 * @param description A description of this state.
	 * @param isStateOff The object that this state is a state off.
	 * @param state The current state.
	 */
	public StateParameter(String issuedBy, String stateName, String description, String isStateOff, boolean state) {
		super(issuedBy, stateName, description, state, "");
		this.isStateOf = isStateOff;
	}

	/**
	 * Constructor of the state parameter. The timestamp will be set to the current time.
	 * 
	 * @param stateName the name of this state.
	 * @param description A description of this state.
	 * @param timestamp The time at which the state parameter was calculated. Should be after the object
	 * that this parameter is a state off.
	 * @param isStateOff The object that this state is a state off.
	 * @param state The current state.
	 */
	public StateParameter(String issuedBy, String stateName, String description, String isStateOff, boolean state, long timestamp) {
		super(issuedBy, stateName, description, timestamp, state, "");
		this.isStateOf = isStateOff;
	}

	public String getIsStateOf() {
		return isStateOf;
	}

	public void setIsStateOf(String isStateOf) {
		this.isStateOf = isStateOf;
	}

	/**
	 * Method similar to the Parameter::getValue(), where the return value is
	 * case to a Booolean. The StateParameter value must be a Boolean value.
	 * 
	 * @return The Parameter value cast to a Boolean.
	 */
	public Boolean getStateValue() {
		return (Boolean) getValue();
	}
	
	/**
	 * Type safe setter for the StateParameter type. Checks that the value being set is
	 * indeed an instance of Boolean prior to setting it. If this is not the case, then
	 * the value is not set and a System.out warning is printed.
	 * 
	 * @param Object The value to be set. Must be a Boolean.
	 */
	public void setValue(Object value) {
		if (value instanceof Boolean == false) {
			/** Notice that this object is intended to be transfered as part of Camel
			 * exchanges. It therefore CANNOT contain a Logger instance depending on the local
			 * execution context. The System.out is the only way to get a message to the world 
			 * that something is being done wrong. Please do not attempt to update this to use
			 * a Logger.*/
			System.out.println("Trying to assign type '" + value.getClass().getName() + "' to state parameter. Only allowed type is 'Boolean'. Ignoring setting.");
		}
		else {
			this.value = value;
		}
	}
}
