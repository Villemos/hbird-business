/**
* Licensed to the Hummingbird Foundation (HF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The HF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License. */

package org.hbird.exchange.navigation;

import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.Parameter;

/** A 3D vector, i.e. three values describing a spartial property such as position, velocity or acceleration. */
public class D3Vector extends Named {

	/** The unique UUID. */
	private static final long serialVersionUID = 1878961587496053034L;

	/**
	 * Constructor based on values of the vector elements. For each vector
	 * element a Parameter instance will be created, with default name and
	 * description, and an empty unit.
	 * 
	 * @param name The name of the vector
	 * @param description A description of the vector.
	 * @param p1 The value of the first element of the vector.
	 * @param p2 The value of the second element of the vector.
	 * @param p3 The value of the third element of the vector.
	 */
	public D3Vector(String issuedBy, String name, String description, double p1, double p2, double p3) {
		super(issuedBy, name, description);
		this.p1 = new Parameter(issuedBy, "p1", "Dimension 1 of vector.", p1, "");
		this.p2 = new Parameter(issuedBy, "p2", "Dimension 2 of vector.", p2, "");
		this.p3 = new Parameter(issuedBy, "p3", "Dimension 3 of vector.", p3, "");
	}

	
	/**
	 * Constructor allowing the specific configuration of the vector elements. 
	 * 
	 * @param name The name of the vector.
	 * @param description The description of the vector.
	 * @param p1 The first element. The parameter instance can describe the element in detail.
	 * @param p2 The second element. The parameter instance can describe the element in detail.
	 * @param p3 The third element. The parameter instance can describe the element in detail.
	 */
	public D3Vector(String issuedBy, String name, String description, Parameter p1, Parameter p2, Parameter p3) {
		super(issuedBy, name, description);
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}

	/** Spartial dimension 1. Depending on the applied reference frame, may be position / speed / acceleration in 
	 * Cartesian x-axis, longitude or similar. */
	public Parameter p1;
	
	/** Spartial dimension 2. Depending on the applied reference frame, may be position / speed / acceleration in 
	 * Cartesian y-axis, latitude or similar. */
	public Parameter p2;
	
	/** Spartial dimension 3. Depending on the applied reference frame, may be position / speed / acceleration in 
	 * Cartesian z-axis, elevation or similar. */
	public Parameter p3;
}
