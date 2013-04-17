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
package eu.estcube.gs.base;

import org.hbird.business.core.StartablePart;
import org.hbird.exchange.groundstation.ICommandableAntennaPart;


/**
 *
 */
public abstract class RadioDevice extends StartablePart implements ICommandableAntennaPart {

    private static final long serialVersionUID = -8145860056366710301L;

	/** Speed of light */ 
	public static final double SPEED_OF_LIGHT = 299792458.0;                            
    
    private long minFrequency;
    private long maxFrequency;
    private boolean isUplink;
    private boolean isDownlink;
    private long gain;

    public RadioDevice(String name, long minFrequency, long maxFrequency, boolean isUplink, boolean isDownlink, long gain, String driver) {
		super(name, "A Radio Device.", driver);
		this.minFrequency = minFrequency;
		this.maxFrequency = maxFrequency;
		this.isUplink = isUplink;
		this.isDownlink = isDownlink;
		this.gain = gain;
	}

	/**
     * @return the minFrequency
     */
    public long getMinFrequency() {
        return minFrequency;
    }

    /**
     * @param minFrequency
     *            the minFrequency to set
     */
    public void setMinFrequency(long minFrequency) {
        this.minFrequency = minFrequency;
    }

    /**
     * @return the maxFrequency
     */
    public long getMaxFrequency() {
        return maxFrequency;
    }

    /**
     * @param maxFrequency
     *            the maxFrequency to set
     */
    public void setMaxFrequency(long maxFrequency) {
        this.maxFrequency = maxFrequency;
    }

    /**
     * @return the isUplink
     */
    public boolean isUplink() {
        return isUplink;
    }

    /**
     * @param isUplink
     *            the isUplink to set
     */
    public void setUplink(boolean isUplink) {
        this.isUplink = isUplink;
    }

    /**
     * @return the isDownlink
     */
    public boolean isDownlink() {
        return isDownlink;
    }

    /**
     * @param isDownlink
     *            the isDownlink to set
     */
    public void setDownlink(boolean isDownlink) {
        this.isDownlink = isDownlink;
    }

    /**
     * @return the gain
     */
    public long getGain() {
        return gain;
    }

    /**
     * @param gain
     *            the gain to set
     */
    public void setGain(long gain) {
        this.gain = gain;
    }
	
    protected double getFrequency() {
        // TODO - 05.03.2013, kimmell - implement
        return -1.0D;
    }

    protected static double calculateDopplerShift(double doppler, double frequency) {
        return ((1 - (doppler / SPEED_OF_LIGHT)) * frequency) - frequency;
    }

}
