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
package org.hbird.business.groundstation.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class RadioDriverConfiguration extends GroundStationDriverConfiguration {

    private static final long serialVersionUID = -6578154012216479876L;

    /** Default value for min frequency. */
    public static final long DEFAULT_FREQUENCY_MIN = 145000000L;

    /** Default value for max frequency. */
    public static final long DEFAULT_FREQUENCY_MAX = 437000000L;

    /** Default value for isUplink. */
    public static final boolean DEFAULT_IS_UPLINK = true;

    /** Default value for isDownlink. */
    public static final boolean DEFAULT_IS_DOWNLINK = true;

    /** Default value for gain. */
    public static final long DEFAULT_GAIN = 16L;

    /** Default value for down-link VFO. */
    public static final String DEFAULT_DOWNLINK_VFO = "Main";

    /** Default value for up-link VFO. */
    public static final String DEFAULT_UPLINK_VFO = "Sub";

    /** Radio device minimal frequency. In Hz. */
    @Value("${radio.frequency.min:145000000}")
    protected long minFrequency = DEFAULT_FREQUENCY_MIN;

    /** Radio device maximum frequency. In Hz. */
    @Value("${radio.frequency.min:437000000}")
    protected long maxFrequency = DEFAULT_FREQUENCY_MAX;

    /** Shows if radio supports up-link. */
    @Value("${radio.uplink:true}")
    protected boolean isUplink = DEFAULT_IS_UPLINK;

    /** Shows if radio supports down-link. */
    @Value("${radio.downlink:true}")
    protected boolean isDownlink = DEFAULT_IS_DOWNLINK;

    /** Signal power. In dB. */
    @Value("${radio.gain:16}")
    protected long gain = DEFAULT_GAIN;

    /** Down-link VFO. */
    @Value("${radio.downlink.vfo:Main}")
    protected String downlinkVfo = DEFAULT_DOWNLINK_VFO;

    /** Up-link VFO. */
    @Value("${radio.uplink.vfo:Sub}")
    protected String uplinkVfo = DEFAULT_UPLINK_VFO;

    public RadioDriverConfiguration() {
    }

    /**
     * @return the minFrequency
     */
    public long getMinFrequency() {
        return minFrequency;
    }

    /**
     * @param minFrequency the minFrequency to set
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
     * @param maxFrequency the maxFrequency to set
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
     * @param isUplink the isUplink to set
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
     * @param isDownlink the isDownlink to set
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
     * @param gain the gain to set
     */
    public void setGain(long gain) {
        this.gain = gain;
    }

    /**
     * @return the downlinkVfo
     */
    public String getDownlinkVfo() {
        return downlinkVfo;
    }

    /**
     * @param downlinkVfo the downlinkVfo to set
     */
    public void setDownlinkVfo(String downlinkVfo) {
        this.downlinkVfo = downlinkVfo;
    }

    /**
     * @return the uplinkVfo
     */
    public String getUplinkVfo() {
        return uplinkVfo;
    }

    /**
     * @param uplinkVfo the uplinkVfo to set
     */
    public void setUplinkVfo(String uplinkVfo) {
        this.uplinkVfo = uplinkVfo;
    }
}
