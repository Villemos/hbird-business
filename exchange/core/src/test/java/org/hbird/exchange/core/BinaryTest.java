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

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class BinaryTest {

    private static final String NAME = "NAME";
    private static final String ISSUER = "issuer";
    private static final String DESCRIPTION = "description";
    private static final byte[] DATA = new byte[] { 0x0D, 0x00, 0x0B, 0x0E };

    private Binary binary;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        binary = new Binary(ISSUER, NAME, DESCRIPTION, DATA);
    }

    /**
     * Test method for
     * {@link org.hbird.exchange.core.Binary#Binary(java.lang.String, java.lang.String, java.lang.String, byte[])}.
     */
    @Test
    public void testBinary() {
        assertEquals(ISSUER, binary.getIssuedBy());
        assertEquals(NAME, binary.getName());
        assertEquals(DESCRIPTION, binary.getDescription());
        assertEquals(Named.DEFAULT_QUALIFIED_NAME_SEPARATOR + NAME, binary.getQualifiedName());
        assertTrue(Arrays.equals(DATA, binary.getRawData()));
        assertTrue(binary.getTimestamp() <= System.currentTimeMillis() && binary.getTimestamp() > System.currentTimeMillis() - 3 * 1000L);
        assertNotNull(binary.getID());
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Binary#getRawData()}.
     */
    @Test
    public void testGetRawData() {
        testSetRawData();
    }

    /**
     * Test method for {@link org.hbird.exchange.core.Binary#setRawData(byte[])}.
     */
    @Test
    public void testSetRawData() {
        assertTrue(Arrays.equals(DATA, binary.getRawData()));
        binary.setRawData(null);
        assertNull(binary.getRawData());
        byte[] bytes = new byte[] { 0x42 };
        binary.setRawData(bytes);
        assertTrue(Arrays.equals(bytes, binary.getRawData()));
    }
}
