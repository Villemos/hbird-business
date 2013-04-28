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
 * 
 */
package org.hbird.exchange.groundstation;

import org.hbird.exchange.core.ConfigurationBase;

/**
 * GroundStation specific configuration base class.
 */
public class GroundStationConfigurationBase extends ConfigurationBase {

    private static final long serialVersionUID = 8719054972735774123L;

    protected String groundstationId;

    public GroundStationConfigurationBase() {
        super();
    }

    public GroundStationConfigurationBase(String serviceId, String serviceVersion, long heartbeatInterval, String groundstationId) {
        super(serviceId, serviceVersion, heartbeatInterval);
        this.groundstationId = groundstationId;
    }

    /**
     * @return the groundstationId
     */
    public String getGroundstationId() {
        return groundstationId;
    }

    /**
     * @param groundstationId the groundstationId to set
     */
    public void setGroundstationId(String groundstationId) {
        this.groundstationId = groundstationId;
    }
}
