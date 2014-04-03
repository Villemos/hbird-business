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

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A NUMERICAL parameter. The parameter type is at the core of the information model. It is used to describe a name-value pair, attaching the
 * meta-data for description and unit. Also holds data about being calibrated or not.
 * 
 * There is no predefined set of parameters in the form of a database. Creating a new parameter is simply the creation of a Parameter with a new name.
 * 
 */
public class Parameter extends ApplicableTo {

	/** The unique UID. */
	private static final long serialVersionUID = 889400984561961325L;

	/** The value of the parameter. May be any type. */
	protected Number value;

	/** The unit of the value. */
	protected String unit;

	/** The state of calibration of the value */
	protected boolean calibrated;

	/**
	 * Creates a Parameter with a timestamp set to 'now'.
	 * 
	 * @param name
	 *            The name of the parameter
	 * @param description
	 *            A description of the parameter.
	 * @param value
	 *            An object holding the value.
	 * @param unit
	 *            The unit of the value.
	 */
	public Parameter(String ID, String name) {
		super(ID, name);
	}

	/**
	 * Returns the value of the Parameter as an Object.
	 * 
	 * @return An Object holding the value. The class type can of cause be found using reflection, or through the 'clazz' attribute.
	 */
	public Number getValue() {
		return value;
	}

	public void setDoubleValue(double value) {
		this.value = value;
	}

	public double asDouble() {
		return value.doubleValue();
	}

	public int asInt() {
		return value.intValue();
	}

	/**
	 * Setter for the value. Will also set the 'clazz' attribute to the Java name of the class set as value.
	 * 
	 * @param value
	 *            The value to be set.
	 */
	public void setValue(Number value) {
		this.value = value;
	}

	/**
	 * Returns the string representing the unit of this parameter.
	 * 
	 * @return String representing the unit.
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit
	 *            the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * Returns whether the value has been calibrated or not.
	 * 
	 * @return <code>true</code> when parameter has been calibrated, <code>false</code> otherwise.
	 */
	public boolean isCalibrated() {
		return calibrated;
	}

	/**
	 * @param calibrated
	 *            new calibration state
	 */
	public void setCalibrated(boolean calibrated) {
		this.calibrated = calibrated;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * 
	 * Notice that the method has extra been implemented to allow comparison of types that are not the same, i.e. Integer and Double.
	 */
	@Override
	public int compareTo(EntityInstance cTo) {

		if (cTo instanceof Parameter) {
			Parameter rhs = (Parameter) cTo;
			if (this.value instanceof Short && rhs.value instanceof Short) {
				return ((Short) this.value).compareTo((Short) rhs.value);
			} else if (this.value instanceof Long && rhs.value instanceof Long) {
				return ((Long) this.value).compareTo((Long) rhs.value);
			} else if (this.value instanceof Integer && rhs.value instanceof Integer) {
				return ((Integer) this.value).compareTo((Integer) rhs.value);
			} else if (this.value instanceof Float && rhs.value instanceof Float) {
				return ((Float) this.value).compareTo((Float) rhs.value);
			} else if (this.value instanceof Double && rhs.value instanceof Double) {
				return ((Double) this.value).compareTo((Double) rhs.value);
			} else if (this.value instanceof Byte && rhs.value instanceof Byte) {
				return ((Byte) this.value).compareTo((Byte) rhs.value);
			} else if (this.value instanceof BigInteger && rhs.value instanceof BigInteger) {
				return ((BigInteger) this.value).compareTo((BigInteger) rhs.value);
			} else if (this.value instanceof BigDecimal && rhs.value instanceof BigDecimal) {
				return ((BigDecimal) this.value).compareTo((BigDecimal) rhs.value);
			} else {
				String message = String.format("Failed to compare values of %s and %s; value types are %s and %s", this.toString(), rhs.toString(), this.value != null ? this.value.getClass() : "null", rhs.value != null ? rhs.value.getClass() : "null");
				throw new RuntimeException(message);
			}
		}

		return -1;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		builder.append("ID", getInstanceID());
		builder.append("name", name);
		builder.append("value", value);
		builder.append("issuedBy", issuedBy);
		builder.append("applicableTo", applicableTo);
		builder.append("calibrated", calibrated);
		return builder.build();
	}
}
