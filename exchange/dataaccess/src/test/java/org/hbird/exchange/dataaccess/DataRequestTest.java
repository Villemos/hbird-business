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
package org.hbird.exchange.dataaccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.hbird.exchange.constants.StandardArguments;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class DataRequestTest {

    public static final String ISSUER = "issuer";

    public static final String DESTINATION = "/dev/null";

    private DataRequest dataRequest;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        dataRequest = new DataRequest(ISSUER);
    }

    /**
     */
    @Test
    public void testArguments() {
        assertTrue(dataRequest.hasArgument(StandardArguments.CLASS));
        assertTrue(dataRequest.hasArgument(StandardArguments.ENTITY_ID));
        assertTrue(dataRequest.hasArgument(StandardArguments.ENTITY_INSTANCE_ID));
        assertTrue(dataRequest.hasArgument(StandardArguments.FROM));
        assertTrue(dataRequest.hasArgument(StandardArguments.TO));
        assertTrue(dataRequest.hasArgument(StandardArguments.APPLICABLE_TO));
        assertTrue(dataRequest.hasArgument(StandardArguments.NAMES));
        assertTrue(dataRequest.hasArgument(StandardArguments.INCLUDE_STATES));
        assertTrue(dataRequest.hasArgument(StandardArguments.SORT_ORDER));
        assertTrue(dataRequest.hasArgument(StandardArguments.SORT));
        assertTrue(dataRequest.hasArgument(StandardArguments.ROWS));
        assertTrue(dataRequest.hasArgument(StandardArguments.INITIALIZATION));
        assertTrue(dataRequest.hasArgument(StandardArguments.DERIVED_FROM));
        assertTrue(dataRequest.hasArgument(StandardArguments.ISSUED_BY));
        assertEquals(14, dataRequest.getArguments().size());
    }

    @Test
    public void testGetRows() {
        testSetRows();
    }

    @Test
    public void testSetRows() {
        assertTrue(dataRequest.hasArgumentValue(StandardArguments.ROWS));
        assertEquals(new Integer(1000), dataRequest.getRows());
        dataRequest.setRows(-1011);
        assertEquals(new Integer(-1011), dataRequest.getRows());
    }

    @Test
    public void testSetEntityInstanceIDNull() {
        dataRequest.setEntityInstanceID(null);
        assertNull(dataRequest.getFrom());
        assertNull(dataRequest.getTo());
        assertFalse(dataRequest.hasArgumentValue(StandardArguments.ENTITY_ID));
        assertFalse(dataRequest.hasArgumentValue(StandardArguments.ENTITY_INSTANCE_ID));
    }

    @Test
    public void testSetEntityInstanceID() {
        dataRequest.setEntityInstanceID("ID:12345");
        assertEquals(new Long(12345), dataRequest.getFrom());
        assertEquals(new Long(12345), dataRequest.getTo());
        assertEquals("ID", dataRequest.getArgumentValue(StandardArguments.ENTITY_ID, String.class));
        assertEquals("ID:12345", dataRequest.getArgumentValue(StandardArguments.ENTITY_INSTANCE_ID, String.class));
    }

    @Test
    public void testSetEntityInstanceIDNoTimestamp() {
        dataRequest.setEntityInstanceID("ID");
        assertNull(dataRequest.getFrom());
        assertNull(dataRequest.getTo());
        assertEquals("ID", dataRequest.getArgumentValue(StandardArguments.ENTITY_ID, String.class));
        assertFalse(dataRequest.hasArgumentValue(StandardArguments.ENTITY_INSTANCE_ID));
    }

    @Test
    public void testSetEntityInstanceIDMultipleSeparators() {
        dataRequest.setEntityInstanceID("ID:12345:67890");
        assertEquals(new Long(67890), dataRequest.getFrom());
        assertEquals(new Long(67890), dataRequest.getTo());
        assertEquals("ID:12345", dataRequest.getArgumentValue(StandardArguments.ENTITY_ID, String.class));
        assertEquals("ID:12345:67890", dataRequest.getArgumentValue(StandardArguments.ENTITY_INSTANCE_ID, String.class));
    }

    @Test
    public void testSetEntityInstanceIDMultipleSeparatorsInvalidTimestamp() {
        dataRequest.setEntityInstanceID("ID:12345:67890A");
        assertNull(dataRequest.getFrom());
        assertNull(dataRequest.getTo());
        assertEquals("ID:12345:67890A", dataRequest.getArgumentValue(StandardArguments.ENTITY_ID, String.class));
        assertFalse(dataRequest.hasArgumentValue(StandardArguments.ENTITY_INSTANCE_ID));
    }
}
