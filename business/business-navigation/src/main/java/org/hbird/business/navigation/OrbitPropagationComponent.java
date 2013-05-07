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
package org.hbird.business.navigation;

import org.hbird.business.navigation.orekit.OrbitPropagationComponentDriver;

/**
 * Controller to manage the propagation of an predicted orbit for a satellite
 *  
 * The minimum interval for which a propagation will be available is 
 * <li>NOW to NOW + [Lead time], where the leadtime is typically 6 hours.</li>
 * 
 * A precondition for the controller to work is that the TLE parameters for the satellite have been 
 * published to the system.
 * 
 * 
 * @author Gert Villemos
 */
public class OrbitPropagationComponent extends NavigationComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2033213698026151689L;

    public static final String DEFAULT_NAME = "OrbitPropagator";
	protected static String DEFAULT_DESCRIPTION = "Component for automatically propagating the orbit of a satellite.";
	protected static String DEFAULT_DRIVER = OrbitPropagationComponentDriver.class.getName();	
	
	/**
	 * Default constructor.
	 */
	public OrbitPropagationComponent(String ID) {
		super(ID, DEFAULT_NAME);
		this.setDriverName(DEFAULT_DRIVER);
		this.setDescription(DEFAULT_DESCRIPTION);
	}
}
