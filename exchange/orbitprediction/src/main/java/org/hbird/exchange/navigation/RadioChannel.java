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

import java.io.Serializable;

import org.hbird.exchange.core.Named;
import org.hbird.exchange.interfaces.IPartOf;

/**
 *
 */
public class RadioChannel implements Serializable, IPartOf {

    private static final long serialVersionUID = -8145860056366710301L;

    private long minFrequency;
    private long maxFrequency;
    private boolean isUplink;
    private boolean isDownlink;
    private long gain;

    protected Named isPartOf = null;
    
    public RadioChannel(long minFrequency, long maxFrequency, boolean isUplink, boolean isDownlink, long gain) {
		super();
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

	public Named getIsPartOf() {
		return isPartOf;
	}

	public void setIsPartOf(Named isPartOf) {
		this.isPartOf = isPartOf;
	}
}
