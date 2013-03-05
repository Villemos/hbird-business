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

import org.hbird.exchange.core.DerivedNamed;
import org.hbird.exchange.core.IGroundStationSpecific;
import org.hbird.exchange.core.ISatelliteSpecific;

/**
 * Class describing the attributes between two objects, such as
 * a specific satellite and a specific location (antenna).
 * 
 * @author Admin
 * 
 */
public class PointingData extends DerivedNamed implements ISatelliteSpecific, IGroundStationSpecific {

    private static final long serialVersionUID = -5455362840245907345L;

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
     * @see NavigationUtilities#calculateDopplerShift(double doppler, doubel frequency)
     */
    protected Double doppler;

    /** Name of the satellite. */
    protected String satelliteName;

	public PointingData(String issuedBy, String type, long timestamp, Double azimuth, Double elevation, Double doppler, Double dopplerShift, String satellite, String location, String derivedFromName, long derivedFromTimestamp, String derivedFromType) {
		super(issuedBy, "PointingData", type, "Contact data between a satellite and a location", timestamp, derivedFromName, derivedFromTimestamp, derivedFromType);

    public PointingData(String issuedBy, long timestamp, Double azimuth, Double elevation, Double doppler, Double dopplerShift, String satelliteName,
            String groundStationName, String derivedFromName, long derivedFromTimestamp, String derivedFromType) {
        super(issuedBy, PointingData.class.getSimpleName(), "ContactData", "Contact data between a satellite and a location", timestamp, derivedFromName,
                derivedFromTimestamp, derivedFromType);
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
    public String getSatelliteName() {
        return satelliteName;
    }

    @Override
    public void setSatelliteName(String satellite) {
        this.satelliteName = satellite;
    }

    @Override
    public String getGroundStationName() {
        return groundStationName;
    }

    @Override
    public void setGroundStationName(String location) {
        this.groundStationName = location;
    }

    @Override
    public String prettyPrint() {
        return String.format("%s{name=%s, gs=%s, sat=%s, timestamp=%s, azimuth=%.3f, elevation=%.3f, doppler=%.3f}", this.getClass().getSimpleName(), name,
                getGroundStationName(), getSatelliteName(), timestamp, azimuth, elevation, doppler);
    }
}
