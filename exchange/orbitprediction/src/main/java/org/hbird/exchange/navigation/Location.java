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
package org.hbird.exchange.navigation;

import org.hbird.exchange.core.ILocationSpecific;

/**
 * As the name indicates, a 'Location' is a fixed place on (or near) earths surface.
 * The location is defined through its;
 * * position. A vector from the centre of the earth measured in meters.
 * * name. The name of the location.
 * * type. The type of location. Can be used to group locations logically, but is not used for processing.
 * * description. A description of the location.
 * * thresholdElevation. The elevation that a satellite must be above the horizon to see this location.
 * 
 */
@Deprecated
public class Location extends D3Vector implements ILocationSpecific {

    /** Unique UID. */
    private static final long serialVersionUID = -2884807949988009796L;

    /**
     * Constructor allowing the specific configuration of the vector elements.
     * 
     * @param name The name of the vector.
     * @param description The description of the vector.
     * @param p1 The first element (Latitude). The parameter instance can describe the element in detail.
     * @param p2 The second element (Longitude). The parameter instance can describe the element in detail.
     * @param p3 The third element (Elevation). The parameter instance can describe the element in detail.
     */
    public Location(String issuedBy, String name, String description, Double p1, Double p2, Double p3, Double frequency) {
        super(issuedBy, name, "Location", description, p1, p2, p3);
        this.frequency = frequency;
    }

    /**
     * The elevation above the horizon that a satellite must have for this location
     * to be visible. Is used among others to calculate when the location becomes
     * into contact and leave contact.
     */
    protected double thresholdElevation = Math.toRadians(5.);

    protected double frequency = 146.92 * 1000000; // 146.92 MHz

    /**
     * Getter of the threshold elevation. The elevation defines how high a satellite must
     * be above the horizon to be visible from this location.
     * 
     * @return The elevation.
     */
    public double getThresholdElevation() {
        return thresholdElevation;
    }

    public double distanceTo(Location that) {
        double lat1 = Math.toRadians(this.p1);
        double lon1 = Math.toRadians(this.p2);
        double lat2 = Math.toRadians(that.p1);
        double lon2 = Math.toRadians(that.p2);

        // great circle distance in radians, using law of cosines formula
        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        // each degree on a great circle of Earth is 60 nautical miles
        double meters = 2 * Math.PI * 6371000 * angle / 2 * Math.PI;
        return meters;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public void setThresholdElevation(double thresholdElevation) {
        this.thresholdElevation = thresholdElevation;
    }

    @Override
    public String getLocationName() {
        return name;
    }

    @Override
    public void setLocationName(String location) {
        this.name = location;
    }
}
