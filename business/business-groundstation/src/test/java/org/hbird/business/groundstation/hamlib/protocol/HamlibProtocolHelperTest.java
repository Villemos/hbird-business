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
package org.hbird.business.groundstation.hamlib.protocol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

/**
 *
 */
public class HamlibProtocolHelperTest {

    @Test
    public void testIsFullMessge() throws Exception {
        assertTrue(HamlibProtocolHelper.isFullMessge("set_freq:a\nRPRT -1\n"));
        assertTrue(HamlibProtocolHelper.isFullMessge("set_freq:a\nRPRT -1"));
        assertTrue(HamlibProtocolHelper.isFullMessge("set_freq:145000001\nRPRT 0"));
        assertTrue(HamlibProtocolHelper.isFullMessge("set_freq:145000001\nRPRT 0\n"));
        assertTrue(HamlibProtocolHelper.isFullMessge("set_freq:145000001\nRPRT\n"));
        assertTrue(HamlibProtocolHelper.isFullMessge("set_freq:145000001\nRPRT"));
        assertFalse(HamlibProtocolHelper.isFullMessge("set_freq:145000001\n"));
        assertFalse(HamlibProtocolHelper.isFullMessge("set_freq:145000001\nRPR"));
        assertFalse(HamlibProtocolHelper.isFullMessge("set_freq:145000001\nRPR 0"));
        assertFalse(HamlibProtocolHelper.isFullMessge("set_freq:145000001\nRPR 0\n"));
        assertFalse(HamlibProtocolHelper.isFullMessge("set_freq:145000001"));
        assertFalse(HamlibProtocolHelper.isFullMessge("0"));
        assertFalse(HamlibProtocolHelper.isFullMessge(""));
        assertFalse(HamlibProtocolHelper.isFullMessge(" "));
    }

    @Test(expected = NullPointerException.class)
    public void testIsFullMessgeNull() throws Exception {
        HamlibProtocolHelper.isFullMessge(null);
    }

    @Test
    public void testGetLastLine() throws Exception {
        assertEquals("RPRT -1", HamlibProtocolHelper.getLastLine("set_freq:a\nRPRT -1\n"));
        assertEquals("RPRT -1", HamlibProtocolHelper.getLastLine("set_freq:a\nRPRT -1"));
        assertEquals("RPRT 0", HamlibProtocolHelper.getLastLine("set_freq:145000001\nRPRT 0"));
        assertEquals("RPRT 0", HamlibProtocolHelper.getLastLine("set_freq:145000001\nRPRT 0\n"));
        assertEquals("abc", HamlibProtocolHelper.getLastLine("ABC\nDEF\nabc"));
        assertEquals("abc", HamlibProtocolHelper.getLastLine("ABC\nDEF\nabc\n"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetLastLineNull() throws Exception {
        HamlibProtocolHelper.getLastLine(null);
    }

    @Test
    public void testGetErrorCode() throws Exception {
        assertEquals("-1", HamlibProtocolHelper.getErrorCode("set_freq:a\nRPRT -1\n"));
        assertEquals("-1", HamlibProtocolHelper.getErrorCode("set_freq:a\nRPRT -1"));
        assertEquals("0", HamlibProtocolHelper.getErrorCode("set_freq:145000001\nRPRT 0"));
        assertEquals("0", HamlibProtocolHelper.getErrorCode("set_freq:145000001\nRPRT 0\n"));
        assertEquals("0 A B C 123", HamlibProtocolHelper.getErrorCode("set_freq:145000001\nRPRT 0 A B C 123\n"));
        assertEquals("0 A B C 123", HamlibProtocolHelper.getErrorCode("set_freq:145000001\nRPRT 0 A B C 123"));
        assertNull(HamlibProtocolHelper.getErrorCode("ABC\nDEF\nabc"));
        assertNull(HamlibProtocolHelper.getErrorCode("ABC\nDEF\nabc\n"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetErrorCodeNull() throws Exception {
        HamlibProtocolHelper.getErrorCode(null);
    }

    @Test
    public void testIsErrorResponse() throws Exception {
        assertTrue(HamlibProtocolHelper.isErrorResponse("set_freq:a\nRPRT -1\n"));
        assertTrue(HamlibProtocolHelper.isErrorResponse("set_freq:a\nRPRT -1"));
        assertTrue(HamlibProtocolHelper.isErrorResponse("set_freq:a\nRPRT 1\n"));
        assertTrue(HamlibProtocolHelper.isErrorResponse("set_freq:a\nRPRT 1"));
        assertFalse(HamlibProtocolHelper.isErrorResponse("set_freq:145000001\nRPRT 0"));
        assertFalse(HamlibProtocolHelper.isErrorResponse("set_freq:145000001\nRPRT 0\n"));
    }

    @Test(expected = NullPointerException.class)
    public void testIsErrorResponseNull() throws Exception {
        HamlibProtocolHelper.isErrorResponse(null);
    }

    @Test
    public void testToMap() {
        Map<String, String> map = HamlibProtocolHelper.toMap("get_freq:\nFrequency: 145000001\nRPRT 0\n");
        assertEquals(3, map.size());
        assertEquals("", map.get("get_freq"));
        assertEquals("145000001", map.get("Frequency"));
        assertEquals("0", map.get("RPRT"));

        map = HamlibProtocolHelper.toMap("set_freq: 145000001\nRPRT 0\n");
        assertEquals(2, map.size());
        assertEquals("145000001", map.get("set_freq"));
        assertEquals("0", map.get("RPRT"));

        map = HamlibProtocolHelper.toMap("set_freq: a\nRPRT -1\n");
        assertEquals(2, map.size());
        assertEquals("a", map.get("set_freq"));
        assertEquals("-1", map.get("RPRT"));

        map = HamlibProtocolHelper.toMap("get_pos:\nAzimuth: 14.000000\nElevation: 18.000999\nRPRT 0\n");
        assertEquals(4, map.size());
        assertEquals("", map.get("get_pos"));
        assertEquals("14.000000", map.get("Azimuth"));
        assertEquals("18.000999", map.get("Elevation"));
        assertEquals("0", map.get("RPRT"));

        map = HamlibProtocolHelper.toMap("set_pos: 6 --2\nRPRT -1\n");
        assertEquals(2, map.size());
        assertEquals("6 --2", map.get("set_pos"));
        assertEquals("-1", map.get("RPRT"));

        map = HamlibProtocolHelper.toMap("set_pos: 62.125 89.0123\nRPRT 0\n");
        assertEquals(2, map.size());
        assertEquals("62.125 89.0123", map.get("set_pos"));
        assertEquals("0", map.get("RPRT"));

        map = HamlibProtocolHelper
                .toMap("dump_caps:\nCaps dump for model:    1\nModel name:             Dummy\nMfg name:               Hamlib\nBackend version:        0.2\nCan get Info:           Y\n\nOverall backend warnings: 0\n\nRPRT 0\n");
        assertEquals(8, map.size());
        assertEquals("", map.get("dump_caps"));
        assertEquals("1", map.get("Caps dump for model"));
        assertEquals("Dummy", map.get("Model name"));
        assertEquals("Hamlib", map.get("Mfg name"));
        assertEquals("0.2", map.get("Backend version"));
        assertEquals("Y", map.get("Can get Info"));
        assertEquals("0", map.get("Overall backend warnings"));
        assertEquals("0", map.get("RPRT"));

        map = HamlibProtocolHelper
                .toMap("dump_caps:\nCaps dump for model:    1\nModel name:             Dummy\nMfg name:               Hamlib\nBackend version:        0.2\nCan get Info:           Y\n   \n   \nOverall backend warnings: 0\n\nRPRT 0\n");
        assertEquals(8, map.size());
        assertEquals("", map.get("dump_caps"));
        assertEquals("1", map.get("Caps dump for model"));
        assertEquals("Dummy", map.get("Model name"));
        assertEquals("Hamlib", map.get("Mfg name"));
        assertEquals("0.2", map.get("Backend version"));
        assertEquals("Y", map.get("Can get Info"));
        assertEquals("0", map.get("Overall backend warnings"));
        assertEquals("0", map.get("RPRT"));
    }
}
