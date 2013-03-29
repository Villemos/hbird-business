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

import org.hbird.business.navigation.orekit.NavigationComponentDriver;
import org.hbird.exchange.configurator.StartablePart;
import org.hbird.exchange.dataaccess.OrbitPredictionRequest;
import org.hbird.exchange.dataaccess.TlePropagationRequest;

/**
 * @author Gert Villemos
 *
 */
public class NavigationComponent extends StartablePart {

	/**
	 * 
	 */
	private static final long serialVersionUID = -148692363129164616L;

	public static final String DEFAULT_NAME = "OrbitPropagator";
	public static final String DEFAULT_DESCRIPTION = "Component for performing TLE based orbit prediction, including contact events and orbital states.";
	public static final String DEFAULT_DRIVER = NavigationComponentDriver.class.getName();
	
	public NavigationComponent() {
		super(DEFAULT_NAME, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_DRIVER);
	}
	
	{
		commandsIn.add(new TlePropagationRequest("", ""));
		commandsIn.add(new OrbitPredictionRequest("", "", "", ""));
	}
}
