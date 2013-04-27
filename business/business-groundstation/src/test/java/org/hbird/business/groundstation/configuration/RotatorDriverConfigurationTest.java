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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.hbird.business.groundstation.configuration.RotatorDriverConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-context.xml")
public class RotatorDriverConfigurationTest {

    @Autowired(required = true)
    private RotatorDriverConfiguration config;

    @Test
    public void testConfig() {
        assertNotNull(config);
        assertEquals(5000, config.getHeartBeatInterval());
        assertEquals("TEST-SERVICE", config.getServiceId());
        assertEquals("BETA", config.getServiceVersion());
        assertEquals(5000, config.getHeartBeatInterval());
        assertEquals("TEST-GS", config.getGroundstationId());
        assertEquals("localhost", config.getDeviceHost());
        assertEquals(-1, config.getDevicePort());
        assertEquals("${device.name}", config.getDeviceName());
        assertEquals("${device.type}", config.getDeviceType());
        assertEquals(1000L * 3, config.getDevicePollInterval());
        assertEquals(1000L * 60 * 3, config.getPostContactDelta());
        assertEquals(1000L * 60 * 3, config.getPreContactDelta());
        assertEquals(500L, config.getCommandInterval());
        assertEquals(20L, config.getDelayInCommandGroup());
        assertEquals(0D, config.getMinAzimuth(), 0.0D);
        assertEquals(360D, config.getMaxAzimuth(), 0.0D);
        assertEquals(0D, config.getMinElevation(), 0.0D);
        assertEquals(180D, config.getMaxElevation(), 0.0D);
        assertEquals(0D, config.getThresholdElevation(), 0.0D);
        assertEquals(7.5D, config.getTolerance(), 0.0D);
    }

    @Test
    public void testToSrting() {
        String str = config.toString();
        assertTrue(str.contains(RotatorDriverConfiguration.class.getSimpleName()));
        assertTrue(str.contains("minAzimuth"));
        assertTrue(str.contains("maxAzimuth"));
        assertTrue(str.contains("minElevation"));
        assertTrue(str.contains("maxElevation"));
        assertTrue(str.contains("thresholdElevation"));
        assertTrue(str.contains("tolerance"));
    }

    @Test
    public void testGetMinElevation() {
        assertEquals(0D, config.getMinElevation(), 0.0D);
    }

    @Test
    public void testSetMinElevation() {
        assertEquals(0D, config.getMinElevation(), 0.0D);
        config.setMinElevation(-10D);
        assertEquals(-10D, config.getMinElevation(), 0.0D);
        config.setMinElevation(-0D);
        assertEquals(0D, config.getMinElevation(), 0.0D);
        config.setMinElevation(108D);
        assertEquals(108D, config.getMinElevation(), 0.0D);
    }

    @Test
    public void testGeMaxElevation() {
        assertEquals(180D, config.getMaxElevation(), 0.0D);
    }

    @Test
    public void testSetMaxElevation() {
        assertEquals(180D, config.getMaxElevation(), 0.0D);
        config.setMaxElevation(90D);
        assertEquals(90D, config.getMaxElevation(), 0.0D);
        config.setMaxElevation(210D);
        assertEquals(210D, config.getMaxElevation(), 0.0D);
    }

    @Test
    public void testGeMinAzimuth() {
        assertEquals(0D, config.getMinAzimuth(), 0.0D);
    }

    @Test
    public void testSetMinAzimuth() {
        assertEquals(0D, config.getMinAzimuth(), 0.0D);
        config.setMinAzimuth(-90D);
        assertEquals(-90D, config.getMinAzimuth(), 0.0D);
        config.setMinAzimuth(210D);
        assertEquals(210D, config.getMinAzimuth(), 0.0D);
    }

    @Test
    public void testGeMaxAzimuth() {
        assertEquals(360D, config.getMaxAzimuth(), 0.0D);
    }

    @Test
    public void testSetMaxAzimuth() {
        assertEquals(360D, config.getMaxAzimuth(), 0.0D);
        config.setMaxAzimuth(450D);
        assertEquals(450D, config.getMaxAzimuth(), 0.0D);
        config.setMaxAzimuth(290D);
        assertEquals(290D, config.getMaxAzimuth(), 0.0D);
    }

    @Test
    public void testGetThresholdElevation() {
        assertEquals(0D, config.getThresholdElevation(), 0.0D);
    }

    @Test
    public void testSetThresholdElevation() {
        assertEquals(0D, config.getThresholdElevation(), 0.0D);
        config.setThresholdElevation(45D);
        assertEquals(45D, config.getThresholdElevation(), 0.0D);
        config.setThresholdElevation(20D);
        assertEquals(20D, config.getThresholdElevation(), 0.0D);
    }

    @Test
    public void testGetTolerance() {
        assertEquals(7.5D, config.getTolerance(), 0.0D);
    }

    @Test
    public void testSetTolerance() {
        assertEquals(7.5D, config.getTolerance(), 0.0D);
        config.setTolerance(4.55D);
        assertEquals(4.55D, config.getTolerance(), 0.0D);
        config.setTolerance(1.2D);
        assertEquals(1.2D, config.getTolerance(), 0.0D);
    }
}
