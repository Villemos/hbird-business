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
package eu.estcube.gs.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Configuration values for the Rotator.
 */
@Component
public class RotatorDriverConfiguration extends GroundStationDriverConfiguration {

    private static final long serialVersionUID = 8568373948253684605L;

    /** Default value for min azimuth. */
    public static final double DEFAULT_MIN_AZIMUTH = 0D;

    /** Default value for max azimuth. */
    public static final double DEFAULT_MAX_AZIMUTH = 360D;

    /** Default value for min elevation. */
    public static final double DEFAULT_MIN_ELEVATION = 0D;

    /** Default value for max elevation. */
    public static final double DEFAULT_MAX_ELEVATION = 180D;

    /** Default value for elevation threshold. */
    public static final double DEFAULT_ELEVATION_THRESHOLD = 0D;

    /** Default value for tolerance. */
    public static final double DEFAULT_TOLERANCE = 7.5D;

    /** The minimal azimuth the antenna can be rotated to. In degrees. */
    @Value("${rotator.azimuth.min:0}")
    protected double minAzimuth = DEFAULT_MIN_AZIMUTH;

    /** The maximum azimuth the antenna can be rotated to. In degrees. */
    @Value("${rotator.azimuth.max:360}")
    protected double maxAzimuth = DEFAULT_MAX_AZIMUTH;

    /** The minimal elevation the antenna can be rotated to. In degrees. */
    @Value("${rotator.elevation.min:0}")
    protected double minElevation = DEFAULT_MIN_ELEVATION;

    /** The maximal elevation the antenna can be rotated to. In degrees. */
    @Value("${rotator.elevation.max:180}")
    protected double maxElevation = DEFAULT_MAX_ELEVATION;

    /** The minimal elevation of the antenna for successful communication. In degrees. */
    @Value("${rotator.elevation.threshold:0}")
    protected double thresholdElevation = DEFAULT_ELEVATION_THRESHOLD;

    /**
     * Antenna tolerance in degrees. Maximum angle between the antenna and satellite location to for successful
     * communication.
     */
    @Value("${rotator.tolerance:7.5}")
    protected double tolerance = DEFAULT_TOLERANCE;

    public RotatorDriverConfiguration() {
        super();
    }

    /**
     * @return the minAzimuth
     */
    public double getMinAzimuth() {
        return minAzimuth;
    }

    /**
     * @param minAzimuth the minAzimuth to set
     */
    public void setMinAzimuth(double minAzimuth) {
        this.minAzimuth = minAzimuth;
    }

    /**
     * @return the maxAzimuth
     */
    public double getMaxAzimuth() {
        return maxAzimuth;
    }

    /**
     * @param maxAzimuth the maxAzimuth to set
     */
    public void setMaxAzimuth(double maxAzimuth) {
        this.maxAzimuth = maxAzimuth;
    }

    /**
     * @return the minElevation
     */
    public double getMinElevation() {
        return minElevation;
    }

    /**
     * @param minElevation the minElevation to set
     */
    public void setMinElevation(double minElevation) {
        this.minElevation = minElevation;
    }

    /**
     * @return the maxElevation
     */
    public double getMaxElevation() {
        return maxElevation;
    }

    /**
     * @param maxElevation the maxElevation to set
     */
    public void setMaxElevation(double maxElevation) {
        this.maxElevation = maxElevation;
    }

    /**
     * @return the thresholdElevation
     */
    public double getThresholdElevation() {
        return thresholdElevation;
    }

    /**
     * @param thresholdElevation the thresholdElevation to set
     */
    public void setThresholdElevation(double thresholdElevation) {
        this.thresholdElevation = thresholdElevation;
    }

    /**
     * @return the tolerance
     */
    public double getTolerance() {
        return tolerance;
    }

    /**
     * @param tolerance the tolerance to set
     */
    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }
}
