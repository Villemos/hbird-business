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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.interfaces.ISatelliteSpecific;

/**
 * 
 * 
 * @author Gert Villemos
 * 
 */
public class TleOrbitalParameters extends EntityInstance implements ISatelliteSpecific {

    public static final String DESCRIPTION = "The TLE state of the satellite.";

    private static final long serialVersionUID = 5290030411022542230L;

    /** The name of the satellite. */
    protected String satelliteId;

    /** TLE line element 1. */
    protected String tleLine1;

    /** TLE line element 2. */
    protected String tleLine2;

    public TleOrbitalParameters(String ID, String name) {
        super(ID, name);
    }

    @Override
    public String getSatelliteId() {
        return satelliteId;
    }

    public String getTleLine1() {
        return tleLine1;
    }

    public void setTleLine1(String tleLine1) {
        this.tleLine1 = tleLine1;
    }

    public String getTleLine2() {
        return tleLine2;
    }

    public void setTleLine2(String tleLine2) {
        this.tleLine2 = tleLine2;
    }

    /**
     * @param satellite the satellite to set
     */
    public void setSatelliteId(String satelliteId) {
        this.satelliteId = satelliteId;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        builder.append("ID", getInstanceID());
        builder.append("name", getName());
        builder.append("satelliteId", satelliteId);
        return builder.build();
    }
}
