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

import java.util.List;

import org.hbird.business.navigation.orekit.ContactEventComponentDriver;

/**
 * A component for calculating contact events between a satellite and one or
 * more locations.
 * 
 * @author Gert Villemos
 * 
 */
public class ContactEventComponent extends NavigationComponent {

    private static final long serialVersionUID = 4017755260726979987L;

    public static final String DEFAULT_NAME = "LocationContactEventPredictor";
    public static final String DEFAULT_DESCRIPTION = "Component for performing TLE based orbit prediction, including contact events and orbital states.";
    public static final String DEFAULT_DRIVER = ContactEventComponentDriver.class.getName();

    /** The locations for which to calculate contact events. */
    protected List<String> locations = null;
    
	/**
	 * Default constructor.
	 */
	public ContactEventComponent(String ID) {
		super(ID, DEFAULT_NAME);
		setDescription(DEFAULT_DESCRIPTION);
		setDriverName(DEFAULT_DRIVER);
	}

	/**
	 * @return the locations
	 */
	public List<String> getLocations() {
		return locations;
	}

	/**
	 * @param locations the locations to set
	 */
	public void setLocations(List<String> locations) {
		this.locations = locations;
	}
}
