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

import org.hbird.business.core.StartablePart;
import org.hbird.business.tracking.bean.TrackingComponentDriver;
import org.hbird.business.tracking.configuration.TrackingDriverConfiguration;
import org.hbird.exchange.interfaces.IPart;

/**
 * @author Gert Villemos
 * 
 */
public class TrackingComponent extends StartablePart {

    private static final long serialVersionUID = -4634379319818576131L;

    public static final String DEFAULT_NAME = "TrackingAutomation";
    public static final String DEFAULT_DESCRIPTION = "Component for automating tracking of a satellite from a specific ground station.";
    public static final String DEFAULT_DRIVER = TrackingComponentDriver.class.getName();

    private final TrackingDriverConfiguration configuration;

    /**
     * @param name
     * @param description
     * @param commands
     */
    public TrackingComponent(IPart isPartOf, String name, String description, String driver, TrackingDriverConfiguration configuration) {
        super(isPartOf, name, description, driver);
        this.configuration = configuration;
    }

    public TrackingComponent(IPart isPartOf, String name, String description, TrackingDriverConfiguration configuration) {
        this(isPartOf, name, description, DEFAULT_DRIVER, configuration);
    }

    public TrackingComponent(IPart isPartOf, String name, TrackingDriverConfiguration configuration) {
        this(isPartOf, name, DEFAULT_DESCRIPTION, DEFAULT_DRIVER, configuration);
    }

    public TrackingComponent(IPart isPartOf, TrackingDriverConfiguration configuration) {
        this(isPartOf, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_DRIVER, configuration);
    }

    public TrackingDriverConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * @see org.hbird.exchange.core.Entity#getDescription()
     */
    @Override
    public String getDescription() {
        return String.format("%s for ground station %s; version: %s", getClass().getSimpleName(), configuration.getGroundstationId(),
                configuration.getServiceVersion());
    }
}
