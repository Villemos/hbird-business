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
package org.hbird.exchange.navigation;

import java.util.Collections;
import java.util.List;

import org.hbird.exchange.core.IGroundStationSpecific;
import org.hbird.exchange.core.Named;

/**
 *
 */
public class GroundStation extends Named implements IGroundStationSpecific {

    public static final String DESCRIPTION = "GroundStation definition";
    public static final String TYPE = GroundStation.class.getSimpleName();

    private static final long serialVersionUID = -8558418536858999621L;

    private D3Vector geoLocation;
    private RotatorProperties rotatorProperties;
    private final List<RadioChannel> radioChannels = Collections.emptyList();

    public GroundStation() {
        super();
        setDescription(DESCRIPTION);
        setType(TYPE);
    }

    /**
     * @see org.hbird.exchange.core.IGroundStationSpecific#getGroundStationName()
     */
    @Override
    public String getGroundStationName() {
        return name;
    }

    /**
     * @see org.hbird.exchange.core.IGroundStationSpecific#setGroundStationName(java.lang.String)
     */
    @Override
    public void setGroundStationName(String location) {
        this.name = location;
    }

    /**
     * @return the rotatorProperties
     */
    public RotatorProperties getRotatorProperties() {
        return rotatorProperties;
    }

    /**
     * @param rotatorProperties
     *            the rotatorProperties to set
     */
    public void setRotatorProperties(RotatorProperties rotatorProperties) {
        this.rotatorProperties = rotatorProperties;
    }

    /**
     * @return the radioChannels
     */
    public List<RadioChannel> getRadioChannels() {
        return radioChannels;
    }

    /**
     * @param location
     *            the location to set
     */
    public void setGeoLocation(D3Vector location) {
        this.geoLocation = location;
    }

    /**
     * @return the geoLocation
     */
    public D3Vector getGeoLocation() {
        return geoLocation;
    }
}
