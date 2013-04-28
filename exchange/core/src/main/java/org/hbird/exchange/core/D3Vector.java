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

package org.hbird.exchange.core;



/** A 3D vector, i.e. three values describing a spartial property such as position, velocity or acceleration. */
public class D3Vector extends EntityInstance {

	/** The unique UUID. */
	private static final long serialVersionUID = 1878961587496053034L;

	/**
	 * Constructor allowing the specific configuration of the vector elements. 
	 * 
	 * @param name The name of the vector.
	 * @param description The description of the vector.
	 * @param p1 The first element. The parameter instance can describe the element in detail.
	 * @param p2 The second element. The parameter instance can describe the element in detail.
	 * @param p3 The third element. The parameter instance can describe the element in detail.
	 */
	public D3Vector(String issuedBy, String name, String type, String description, Double p1, Double p2, Double p3) {
		super(issuedBy, name, description);
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}

	/** Spartial dimension 1. Depending on the applied reference frame, may be position / speed / acceleration in 
	 * Cartesian x-axis, longitude or similar. */
	public double p1;
	
	/** Spartial dimension 2. Depending on the applied reference frame, may be position / speed / acceleration in 
	 * Cartesian y-axis, latitude or similar. */
	public double p2;
	
	/** Spartial dimension 3. Depending on the applied reference frame, may be position / speed / acceleration in 
	 * Cartesian z-axis, elevation or similar. */
	public double p3;
}
