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

import java.util.Date;

import org.hbird.exchange.interfaces.IGroundStationSpecific;
import org.hbird.exchange.interfaces.ISatelliteSpecific;

/**
 * Class describing the attributes between two objects, such as
 * a specific satellite and a specific location (antenna).
 * 
 * @author Admin
 * 
 */
public class PointingData implements ISatelliteSpecific, IGroundStationSpecific {

    /** Azimuth to look at the ground station to see the satellite; in degrees. */
    protected Double azimuth;

    /** Elevation to look at the ground station to see the satellite; in degrees. */
    protected Double elevation;

    /**
     * Value to calculate Doppler shift for the radio frequencies.
     * 
     * <strong>NOTE</strong>: this is not the value of the Doppler shift it self.
     * 
     * Doppler shift can be calculated for the given frequnecy using formula:
     * 
     * <pre>
     *  doppler shift = ((1 - (doppler / SPEED_OF_LIGHT)) * frquecy) - frequency
     * </pre>
     * 
     * This formula is implemented in package org.hbird.business.business-navigation.
     * 
     * See Doppler factor - http://en.wikipedia.org/wiki/Relativistic_Doppler_effect#Motion_along_the_line_of_sight
     * 
     * @see NavigationUtilities#calculateDopplerShift(double doppler, doubel frequency)
     */
    protected Double doppler;

    /** Name of the satellite. */
    protected String satelliteName;

    protected String groundStationName;

    protected long timestamp;

    public PointingData(long timestamp, Double azimuth, Double elevation, Double doppler, String satelliteName, String groundStationName) {
        this.timestamp = timestamp;
        this.azimuth = azimuth;
        this.elevation = elevation;
        this.doppler = doppler;
        this.satelliteName = satelliteName;
        this.groundStationName = groundStationName;
    }

    public Double getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(Double azimuth) {
        this.azimuth = azimuth;
    }

    public Double getElevation() {
        return elevation;
    }

    public void setElevation(Double elevation) {
        this.elevation = elevation;
    }

    public Double getDoppler() {
        return doppler;
    }

    public void setDoppler(Double doppler) {
        this.doppler = doppler;
    }

    @Override
    public String getSatelliteId() {
        return satelliteName;
    }

    @Override
    public String getGroundStationId() {
        return groundStationName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String prettyPrint() {
        // TODO - 17.04.2013, kimmell - fix this
        return (new Date(timestamp)).toLocaleString() + ", Azimuth:" + azimuth + ", Elevation:" + elevation + ", doppler:" + doppler;
    }
}
