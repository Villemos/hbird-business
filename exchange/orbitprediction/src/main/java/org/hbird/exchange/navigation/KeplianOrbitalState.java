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
package org.hbird.exchange.navigation;


/** Class describing a specific orbital state at a give point in time. 
 *  The state is defined through
 *  * position. A vector defining the location of the body. 
 *  * velocity. A vector defining the velocity of the body. 
 *  * timestamp. The time the body have this state. May be in the future. */
public class KeplianOrbitalState extends OrbitalState {

	/** The unique UID. */
	private static final long serialVersionUID = -8112421610188582037L;

	/** Position measured in the TODO coordinate system. */
	public D3Vector position;

	/** Velocity measured in meters / second. */
	public D3Vector velocity;

	/**
	 * Constructor of an orbital state.
	 * 
	 * @param name The name of the orbital state. 
	 * @param description A description of the state.
	 * @param timestamp The timestamp at which the orbital state is relevant.
	 * @param datasetidentifier
	 * @param satellite 
	 * @param position The position of the orbit. 
	 * @param velocity The velocity of the orbit.
	 */
	public KeplianOrbitalState(String issuedBy, String name, String description, long timestamp, String datasetidentifier, Satellite satellite, D3Vector position, D3Vector velocity) {
		super(issuedBy, name, description, timestamp, datasetidentifier, satellite);
		this.position = position;
		this.velocity = velocity;
	}
	
	public KeplianOrbitalState(String issuedBy, String name, String description, long timestamp, Satellite satellite, D3Vector position, D3Vector velocity) {
		super(issuedBy, name, description, timestamp, "", satellite);
		this.position = position;
		this.velocity = velocity;
	}

	public D3Vector getPosition() {
		return position;
	}

	public void setPosition(D3Vector position) {
		this.position = position;
	}

	public D3Vector getVelocity() {
		return velocity;
	}

	public void setVelocity(D3Vector velocity) {
		this.velocity = velocity;
	}
}
