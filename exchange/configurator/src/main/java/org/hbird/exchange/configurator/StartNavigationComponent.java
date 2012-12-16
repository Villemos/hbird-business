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
package org.hbird.exchange.configurator;


public class StartNavigationComponent extends StartComponent {

	private static final long serialVersionUID = -7639647113103082392L;

	public StartNavigationComponent() {
		super("Navigation", "StartNavigationComponent", "Command to a configurator to start a command component.");
	}	

	public StartNavigationComponent(String componentname) {
		super(componentname, "StartNavigationComponent", "Command to a configurator to start a command component.");
	}	

	public StartNavigationComponent(String issuedBy, String destination, String componentname) {
		super(issuedBy, destination, componentname, "StartNavigationComponent", "Command to a configurator to start a command component.");
	}	
}
