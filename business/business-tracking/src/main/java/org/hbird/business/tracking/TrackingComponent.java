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
package org.hbird.business.tracking;

import org.hbird.business.core.StartableEntity;
import org.hbird.business.tracking.quartz.TrackingDriverConfiguration;
import org.hbird.business.tracking.timer.TrackingComponentDriver;

/**
 * @author Gert Villemos
 * 
 */
public class TrackingComponent extends StartableEntity {

    private static final long serialVersionUID = -4634379319818576131L;

    public static final String DEFAULT_NAME = "TrackingAutomation";
    public static final String DEFAULT_DESCRIPTION = "Component for automating tracking of a satellite from a specific ground station.";
    public static final String DEFAULT_DRIVER = TrackingComponentDriver.class.getName();

    private TrackingDriverConfiguration configuration;

    protected String satellite;
    protected String location;
    
    /**
     * @param name
     * @param description
     * @param commands
     */
	public TrackingComponent(String ID) {
		super(ID, DEFAULT_NAME);
		setDescription(DEFAULT_DESCRIPTION);
		setDriverName(DEFAULT_DRIVER);
	}

    public TrackingDriverConfiguration getConfiguration() {
        return configuration;
    }

	/**
	 * @param configuration the configuration to set
	 */
	public void setConfiguration(TrackingDriverConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * @return the satellite
	 */
	public String getSatellite() {
		return satellite;
	}

	/**
	 * @param satellite the satellite to set
	 */
	public void setSatellite(String satellite) {
		this.satellite = satellite;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
}
