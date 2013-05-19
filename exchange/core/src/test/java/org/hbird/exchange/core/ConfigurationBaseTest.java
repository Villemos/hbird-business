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
package org.hbird.exchange.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class ConfigurationBaseTest {

    private ConfigurationBase config;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        config = new ConfigurationBase();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.ConfigurationBase#ConfigurationBase(String, String, long)}.
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testConfigurationBase() {
        config = new ConfigurationBase("SERVICE-ID", "V2.0", 10000L);
        assertEquals("SERVICE-ID", config.getServiceId());
        assertEquals("V2.0", config.getServiceVersion());
        assertEquals(10000L, config.getHeartBeatInterval());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.ConfigurationBase#getServiceId()}.
     */
    @Test
    public void testGetServiceId() {
        testSetServiceId();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.ConfigurationBase#setServiceId(java.lang.String)}.
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testSetServiceId() {
        assertNull(config.getServiceId());
        config.setServiceId("ID");
        assertEquals("ID", config.getServiceId());
        config.setServiceId("ASAP");
        assertEquals("ASAP", config.getServiceId());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.ConfigurationBase#getHeartBeatInterval()}.
     */
    @Test
    public void testGetHeartBeatInterval() {
        testSetHeartBeatInterval();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.ConfigurationBase#setHeartBeatInterval(long)}.
     */
    @Test
    public void testSetHeartBeatInterval() {
        assertEquals(ConfigurationBase.DEFAULT_HEART_BEAT_INTERVAL, config.getHeartBeatInterval());
        config.setHeartBeatInterval(-500L);
        assertEquals(-500L, config.getHeartBeatInterval());
        config.setHeartBeatInterval(1500L);
        assertEquals(1500L, config.getHeartBeatInterval());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.ConfigurationBase#getServiceVersion()}.
     */
    @Test
    public void testGetServiceVersion() {
        testSetServiceVersion();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.ConfigurationBase#setServiceVersion(java.lang.String)}.
     */
    @Test
    public void testSetServiceVersion() {
        assertNull(config.getServiceVersion());
        config.setServiceVersion("BETA");
        assertEquals("BETA", config.getServiceVersion());
        config.setServiceVersion("1.0.0");
        assertEquals("1.0.0", config.getServiceVersion());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.ConfigurationBase#toString()}.
     */
    @Test
    public void testToString() {
        String str = config.toString();
        assertNotNull(str);
        assertTrue(str.contains("serviceId"));
        assertTrue(str.contains("serviceVersion"));
        assertTrue(str.contains("heartBeatInterval"));
        assertTrue(str.startsWith(config.getClass().getSimpleName()));
    }
}
