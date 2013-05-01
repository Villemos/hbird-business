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

import org.hbird.exchange.core.Part;
import org.hbird.exchange.interfaces.IPart;
import org.hbird.exchange.interfaces.ISatelliteSpecific;

/**
 * Class representing a satellite.
 * 
 */
public class Satellite extends Part implements ISatelliteSpecific {

    private static final long serialVersionUID = -1158112946640794999L;

    /**
     * Default frequency for the radio in Hz. Safe value - radio devices should support this. Alternative value would be
     * 437 MHz.
     */
    public static final long DEFAULT_RADIO_FREQUENCY = 145000000L;

    protected String designator;

    protected String satelliteNumber;

    /** Satellite up-link radio frequency in Hz. From ground to satellite. */
    protected long uplinkFrequency = DEFAULT_RADIO_FREQUENCY;

    /** Satellite down-link frequency in Hz. From satellite to ground. */
    protected long downlinkFrequency = DEFAULT_RADIO_FREQUENCY;

    /**
     * Constructor.
     * 
     * @param name The name of the satellite.
     * @param description The description of the satellite.
     */
    public Satellite(IPart isPartOf, String name, String description) {
        super(isPartOf, name, description);
    }

    /**
     * Constructor.
     * 
     * @param name The name of the satellite.
     * @param description The description of the satellite.
     */
    public Satellite(String name, String description) {
        super(name, description);
    }

    public String getDesignator() {
        return designator;
    }

    public void setDesignator(String designator) {
        this.designator = designator;
    }

    public String getSatelliteNumber() {
        return satelliteNumber;
    }

    public void setSatelliteNumber(String satelliteNumber) {
        this.satelliteNumber = satelliteNumber;
    }

    @Override
    public String getSatelliteId() {
        return name;
    }

    public long getUplinkFrequency() {
        return uplinkFrequency;
    }

    public void setUplinkFrequency(long frequency) {
        this.uplinkFrequency = frequency;
    }

    /**
     * @return the downlinkFrequency
     */
    public long getDownlinkFrequency() {
        return downlinkFrequency;
    }

    /**
     * @param downlinkFrequency the downlinkFrequency to set
     */
    public void setDownlinkFrequency(long downlinkFrequency) {
        this.downlinkFrequency = downlinkFrequency;
    }
}
