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

/**
 * A parameter. The parameter type is at the core of the information model. It 
 * is used to describe a name-value pair, attaching the meta-data for description 
 * and unit.
 * 
 * There is no predefined set of parameters in the form of a database. Creating
 * a new parameter is simply the creation of a Parameter with a new name.   
 *
 */
public class Parameter extends Named {

	/** The unique UID. */
	private static final long serialVersionUID = 889400984561961325L;
	
	/** The value of the parameter. May be any type. */
	protected Object value;

	/** The unit of the argument. */
	protected String unit;

	/**
	 * Creates a Parameter with a timestamp set to 'now'.
	 * 
	 * @param name The name of the parameter
	 * @param description A description of the parameter.
	 * @param value An object holding the value.
	 * @param unit The unit of the value.
	 */
	public Parameter(String issuedBy, String name, String description, Object value, String unit) {
		super(issuedBy, name, description);
		this.unit = unit;
		this.value = value;
	}

	/**
	 * Creates a Parameter with a timestamp set to 'now'.
	 * 
	 * @param name The name of the parameter
	 * @param description A description of the parameter.
	 * @param value An object holding the value.
	 * @param unit The unit of the value.
	 */
	public Parameter(String issuedBy, String name, String description, Object value, String unit, long timestamp) {
		super(issuedBy, name, description, timestamp);
		this.unit = unit;
		this.value = value;
	}

	/**
	 * Constructor of the parameter.
	 * 
	 * @param name The name of the parameter
	 * @param description A description of the parameter.
	 * @param timestamp The timestamp of the parameter.
	 * @param value An object holding the value.
	 * @param unit The unit of the value.
	 */
	public Parameter(String issuedBy, String name, String description, long timestamp, Object value, String unit) {
		super(issuedBy, name, description, timestamp);
		this.unit = unit;
		this.value = value;
	}
	
	public Parameter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Comparison method overriding the default Named compare method. Will trigger a
	 * comparison of the two values contains within the parameters. 
	 *  
	 * @param rhs The Parameter against which this parameter should be compared.
	 * @return -1 if this document is less, 0 if equal, 1 if this is larger.
	 */
	public int compareTo(Parameter rhs) {
		return Comperator.compare(this.value, rhs.value);
	}
	
	/**
	 * Returns the value of the Parameter as an Object. 
	 * 
	 * @return An Object holding the value. The class type can of cause be found using 
	 * reflection, or through the 'clazz' attribute.
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Setter for the value. Will also set the 'clazz' attribute to the Java name of the
	 * class set as value.
	 * 
	 * @param value The value to be set.
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	/**
	 * Returns the string representing the unit of this parameter.
	 * 
	 * @return STring representing the unit.
	 */
	public String getUnit() {
		return unit;
	}
}
