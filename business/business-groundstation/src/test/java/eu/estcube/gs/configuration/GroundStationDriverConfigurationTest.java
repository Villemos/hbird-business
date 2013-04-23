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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class GroundStationDriverConfigurationTest {

    private GroundStationDriverConfiguration config;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        config = new GroundStationDriverConfiguration();
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.configuration.GroundStationDriverConfiguration#GroundStationDriverConfiguration(java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String, java.lang.Long, int, int, long, long)}
     * .
     */
    @Test
    public void testGroundStationDriverConfigurationStringStringIntStringStringStringIntStringLongIntIntLongLong() {
        config = new GroundStationDriverConfiguration("SERVICE-ID", "BETA-VERSION", 4000L, "GS-ID", "DEVICE-NAME", "DEVICE-TYPE", 3002, "localhost",
                2345L, 6674L, 9831L);
        assertEquals("SERVICE-ID", config.getServiceId());
        assertEquals("BETA-VERSION", config.getServiceVersion());
        assertEquals(4000, config.getHeartBeatInterval());
        assertEquals("GS-ID", config.getGroundstationId());
        assertEquals("DEVICE-NAME", config.getDeviceName());
        assertEquals("DEVICE-TYPE", config.getDeviceType());
        assertEquals(3002, config.getDevicePort());
        assertEquals("localhost", config.getDeviceHost());
        assertEquals(2345, config.getDevicePollInterval());
        assertEquals(6674L, config.getPreContactDelta());
        assertEquals(9831L, config.getPostContactDelta());
    }

    /**
     * Test method for {@link eu.estcube.gs.configuration.GroundStationDriverConfiguration#getDeviceName()}.
     */
    @Test
    public void testGetDeviceName() {
        testSetDeviceName();
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.configuration.GroundStationDriverConfiguration#setDeviceName(java.lang.String)}.
     */
    @Test
    public void testSetDeviceName() {
        assertNull(config.getDeviceName());
        config.setDeviceName("D1");
        assertEquals("D1", config.getDeviceName());
        config.setDeviceName("D-4");
        assertEquals("D-4", config.getDeviceName());
    }

    /**
     * Test method for {@link eu.estcube.gs.configuration.GroundStationDriverConfiguration#getDeviceType()}.
     */
    @Test
    public void testGetDeviceType() {
        testSetDeviceType();
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.configuration.GroundStationDriverConfiguration#setDeviceType(java.lang.String)}.
     */
    @Test
    public void testSetDeviceType() {
        assertNull(config.getDeviceType());
        config.setDeviceType("TYPE-A");
        assertEquals("TYPE-A", config.getDeviceType());
        config.setDeviceType("DO-001");
        assertEquals("DO-001", config.getDeviceType());
    }

    /**
     * Test method for {@link eu.estcube.gs.configuration.GroundStationDriverConfiguration#getDevicePort()}.
     */
    @Test
    public void testGetDevicePort() {
        testSetDevicePort();
    }

    /**
     * Test method for {@link eu.estcube.gs.configuration.GroundStationDriverConfiguration#setDevicePort(int)}.
     */
    @Test
    public void testSetDevicePort() {
        assertEquals(GroundStationDriverConfiguration.DEFAULT_DEVICE_PORT, config.getDevicePort());
        config.setDevicePort(5433);
        assertEquals(5433, config.getDevicePort());
        config.setDevicePort(11992);
        assertEquals(11992, config.getDevicePort());
    }

    /**
     * Test method for {@link eu.estcube.gs.configuration.GroundStationDriverConfiguration#getDeviceHost()}.
     */
    @Test
    public void testGetDeviceHost() {
        testSetDeviceHost();
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.configuration.GroundStationDriverConfiguration#setDeviceHost(java.lang.String)}.
     */
    @Test
    public void testSetDeviceHost() {
        assertEquals(GroundStationDriverConfiguration.DEFAULT_DEVICE_HOST, config.getDeviceHost());
        config.setDeviceHost("hbird.de");
        assertEquals("hbird.de", config.getDeviceHost());
        config.setDeviceHost("hbird.org");
        assertEquals("hbird.org", config.getDeviceHost());
    }

    /**
     * Test method for {@link eu.estcube.gs.configuration.GroundStationDriverConfiguration#getDevicePollInterval()}.
     */
    @Test
    public void testGetDevicePollInterval() {
        testSetDevicePollInterval();
    }

    /**
     * Test method for {@link eu.estcube.gs.configuration.GroundStationDriverConfiguration#setDevicePollInterval(long)}.
     */
    @Test
    public void testSetDevicePollInterval() {
        assertEquals(GroundStationDriverConfiguration.DEFAULT_DEVICE_POLL_INTERVAL, config.getDevicePollInterval());
        config.setDevicePollInterval(340L);
        assertEquals(340L, config.getDevicePollInterval());
        config.setDevicePollInterval(-34L);
        assertEquals(-34L, config.getDevicePollInterval());
    }

    /**
     * Test method for {@link eu.estcube.gs.configuration.GroundStationDriverConfiguration#getPreContactDelta()}.
     */
    @Test
    public void testGetPreContactDelta() {
        testSetPreContactDelta();
    }

    /**
     * Test method for {@link eu.estcube.gs.configuration.GroundStationDriverConfiguration#setPreContactDelta(long)}.
     */
    @Test
    public void testSetPreContactDelta() {
        assertEquals(GroundStationDriverConfiguration.DEFAULT_PRE_CONTACT_DELTA, config.getPreContactDelta());
        config.setPreContactDelta(3433L);
        assertEquals(3433L, config.getPreContactDelta());
        config.setPreContactDelta(123433L);
        assertEquals(123433L, config.getPreContactDelta());
    }

    /**
     * Test method for {@link eu.estcube.gs.configuration.GroundStationDriverConfiguration#getPostContactDelta()}.
     */
    @Test
    public void testGetPostContactDelta() {
        testSetPostContactDelta();
    }

    /**
     * Test method for {@link eu.estcube.gs.configuration.GroundStationDriverConfiguration#setPostContactDelta(long)}.
     */
    @Test
    public void testSetPostContactDelta() {
        assertEquals(GroundStationDriverConfiguration.DEFAULT_POST_CONTACT_DELTA, config.getPostContactDelta());
        config.setPostContactDelta(554532L);
        assertEquals(554532L, config.getPostContactDelta());
        config.setPostContactDelta(9L);
        assertEquals(9L, config.getPostContactDelta());
    }

    /**
     * Test method for {@link eu.estcube.gs.configuration.GroundStationDriverConfiguration#getCommandInterval()}.
     */
    @Test
    public void testGetCommandInterval() {
        testSetCommandInterval();
    }

    /**
     * Test method for {@link eu.estcube.gs.configuration.GroundStationDriverConfiguration#setCommandInterval(long)}.
     */
    @Test
    public void testSetCommandInterval() {
        assertEquals(GroundStationDriverConfiguration.DEFAULT_COMMAND_INTERVAL, config.getCommandInterval());
        config.setCommandInterval(9912L);
        assertEquals(9912L, config.getCommandInterval());
        config.setCommandInterval(400L);
        assertEquals(400L, config.getCommandInterval());
    }

    /**
     * Test method for {@link eu.estcube.gs.configuration.GroundStationDriverConfiguration#getDelayInCommandGroup()}.
     */
    @Test
    public void testGetDelayInCommandGroup() {
        testSetDelayInCommandGroup();
    }

    /**
     * Test method for {@link eu.estcube.gs.configuration.GroundStationDriverConfiguration#setDelayInCommandGroup(long)}
     * .
     */
    @Test
    public void testSetDelayInCommandGroup() {
        assertEquals(GroundStationDriverConfiguration.DEFAULT_DELAY_IN_COMMAND_GROUP, config.getDelayInCommandGroup());
        config.setDelayInCommandGroup(35L);
        assertEquals(35L, config.getDelayInCommandGroup());
        config.setDelayInCommandGroup(351L);
        assertEquals(351L, config.getDelayInCommandGroup());
    }

    /**
     * Test method for {@link eu.estcube.gs.configuration.GroundStationDriverConfiguration#getAddress()}.
     */
    @Test
    public void testGetAddress() {
        assertEquals(config.getDeviceHost() + ":" + config.getDevicePort(), config.getAddress());
        config.setDeviceHost("127.0.0.1");
        config.setDevicePort(9450);
        assertEquals(config.getDeviceHost() + ":" + config.getDevicePort(), config.getAddress());
    }
}
