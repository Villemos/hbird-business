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

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class RadioDriverConfigurationTest {

    private RadioDriverConfiguration config;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        config = new RadioDriverConfiguration();
    }

    /**
     * Test method for {@link org.hbird.business.groundstation.configuration.RadioDriverConfiguration#getMinFrequency()}
     * .
     */
    @Test
    public void testGetMinFrequency() {
        testSetMinFrequency();
    }

    /**
     * Test method for
     * {@link org.hbird.business.groundstation.configuration.RadioDriverConfiguration#setMinFrequency(long)}.
     */
    @Test
    public void testSetMinFrequency() {
        assertEquals(RadioDriverConfiguration.DEFAULT_FREQUENCY_MIN, config.getMinFrequency());
        config.setMinFrequency(-1L);
        assertEquals(-1L, config.getMinFrequency());
        config.setMinFrequency(10000L);
        assertEquals(10000L, config.getMinFrequency());
    }

    /**
     * Test method for {@link org.hbird.business.groundstation.configuration.RadioDriverConfiguration#getMaxFrequency()}
     * .
     */
    @Test
    public void testGetMaxFrequency() {
        testSetMaxFrequency();
    }

    /**
     * Test method for
     * {@link org.hbird.business.groundstation.configuration.RadioDriverConfiguration#setMaxFrequency(long)}.
     */
    @Test
    public void testSetMaxFrequency() {
        assertEquals(RadioDriverConfiguration.DEFAULT_FREQUENCY_MAX, config.getMaxFrequency());
        config.setMaxFrequency(1234567890L);
        assertEquals(1234567890L, config.getMaxFrequency());
        config.setMaxFrequency(0L);
        assertEquals(0L, config.getMaxFrequency());
    }

    /**
     * Test method for {@link org.hbird.business.groundstation.configuration.RadioDriverConfiguration#isUplink()}.
     */
    @Test
    public void testIsUplink() {
        testSetUplink();
    }

    /**
     * Test method for
     * {@link org.hbird.business.groundstation.configuration.RadioDriverConfiguration#setUplink(boolean)}.
     */
    @Test
    public void testSetUplink() {
        assertEquals(RadioDriverConfiguration.DEFAULT_IS_UPLINK, config.isUplink());
        config.setUplink(!RadioDriverConfiguration.DEFAULT_IS_UPLINK);
        assertEquals(!RadioDriverConfiguration.DEFAULT_IS_UPLINK, config.isUplink());
        config.setUplink(RadioDriverConfiguration.DEFAULT_IS_UPLINK);
        assertEquals(RadioDriverConfiguration.DEFAULT_IS_UPLINK, config.isUplink());
    }

    /**
     * Test method for {@link org.hbird.business.groundstation.configuration.RadioDriverConfiguration#isDownlink()}.
     */
    @Test
    public void testIsDownlink() {
        testSetDownlink();
    }

    /**
     * Test method for
     * {@link org.hbird.business.groundstation.configuration.RadioDriverConfiguration#setDownlink(boolean)}.
     */
    @Test
    public void testSetDownlink() {
        assertEquals(RadioDriverConfiguration.DEFAULT_IS_DOWNLINK, config.isDownlink());
        config.setDownlink(!RadioDriverConfiguration.DEFAULT_IS_DOWNLINK);
        assertEquals(!RadioDriverConfiguration.DEFAULT_IS_DOWNLINK, config.isDownlink());
        config.setDownlink(RadioDriverConfiguration.DEFAULT_IS_DOWNLINK);
        assertEquals(RadioDriverConfiguration.DEFAULT_IS_DOWNLINK, config.isDownlink());
    }

    /**
     * Test method for {@link org.hbird.business.groundstation.configuration.RadioDriverConfiguration#getGain()}.
     */
    @Test
    public void testGetGain() {
        testSetGain();
    }

    /**
     * Test method for {@link org.hbird.business.groundstation.configuration.RadioDriverConfiguration#setGain(long)}.
     */
    @Test
    public void testSetGain() {
        assertEquals(RadioDriverConfiguration.DEFAULT_GAIN, config.getGain());
        config.setGain(-189L);
        assertEquals(-189L, config.getGain());
        config.setGain(189123L);
        assertEquals(189123L, config.getGain());
    }

    /**
     * Test method for {@link org.hbird.business.groundstation.configuration.RadioDriverConfiguration#getGain()}.
     */
    @Test
    public void testGetDownlinkVfo() {
        testSetDownlinkVfo();
    }

    /**
     * Test method for {@link org.hbird.business.groundstation.configuration.RadioDriverConfiguration#setGain(long)}.
     */
    @Test
    public void testSetDownlinkVfo() {
        assertEquals(RadioDriverConfiguration.DEFAULT_DOWNLINK_VFO, config.getDownlinkVfo());
        config.setDownlinkVfo("VFOA");
        assertEquals("VFOA", config.getDownlinkVfo());
        config.setDownlinkVfo("VFOB");
        assertEquals("VFOB", config.getDownlinkVfo());
    }

    /**
     * Test method for {@link org.hbird.business.groundstation.configuration.RadioDriverConfiguration#getGain()}.
     */
    @Test
    public void testGetUplinkkVfo() {
        testSetUplinkVfo();
    }

    /**
     * Test method for {@link org.hbird.business.groundstation.configuration.RadioDriverConfiguration#setGain(long)}.
     */
    @Test
    public void testSetUplinkVfo() {
        assertEquals(RadioDriverConfiguration.DEFAULT_UPLINK_VFO, config.getUplinkVfo());
        config.setUplinkVfo("VFOA");
        assertEquals("VFOA", config.getUplinkVfo());
        config.setUplinkVfo("VFOB");
        assertEquals("VFOB", config.getUplinkVfo());
    }
}
