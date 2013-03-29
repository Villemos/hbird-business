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
package org.hbird.exchange.groundstation;

import org.hbird.exchange.configurator.StartablePart;


public abstract class Rotator extends StartablePart implements ICommandableAntennaPart {

    private static final long serialVersionUID = 3342094403064808285L;

    private int thresholdElevation;
    private int minAzimuth;
    private int maxAzimuth;
    private int minElevation;
    private int maxElevation;

    public Rotator(String ID, String name, int thresholdElevation, int minAzimuth, int maxAzimuth, int minElevation, int maxElevation, String driver) {
		super(ID, name, "A rotator of an antenna.", driver);
		this.thresholdElevation = thresholdElevation;
		this.minAzimuth = minAzimuth;
		this.maxAzimuth = maxAzimuth;
		this.minElevation = minElevation;
		this.maxElevation = maxElevation;
	}

	/**
     * @return the thresholdElevation
     */
    public int getThresholdElevation() {
        return thresholdElevation;
    }

    /**
     * @param thresholdElevation
     *            the thresholdElevation to set
     */
    public void setThresholdElevation(int thresholdElevation) {
        this.thresholdElevation = thresholdElevation;
    }

    /**
     * @return the minAzimuth
     */
    public int getMinAzimuth() {
        return minAzimuth;
    }

    /**
     * @param minAzimuth
     *            the minAzimuth to set
     */
    public void setMinAzimuth(int minAzimuth) {
        this.minAzimuth = minAzimuth;
    }

    /**
     * @return the maxAzimuth
     */
    public int getMaxAzimuth() {
        return maxAzimuth;
    }

    /**
     * @param maxAzimuth
     *            the maxAzimuth to set
     */
    public void setMaxAzimuth(int maxAzimuth) {
        this.maxAzimuth = maxAzimuth;
    }

    /**
     * @return the minElevation
     */
    public int getMinElevation() {
        return minElevation;
    }

    /**
     * @param minElevation
     *            the minElevation to set
     */
    public void setMinElevation(int minElevation) {
        this.minElevation = minElevation;
    }

    /**
     * @return the maxElevation
     */
    public int getMaxElevation() {
        return maxElevation;
    }

    /**
     * @param maxElevation
     *            the maxElevation to set
     */
    public void setMaxElevation(int maxElevation) {
        this.maxElevation = maxElevation;
    }

}
