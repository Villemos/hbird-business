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
        dataRequest = new DataRequest(ISSUER, DESTINATION);
    }

    /**
     */
    @Test
    public void testArguments() {
        assertTrue(dataRequest.hasArgument(StandardArguments.TYPE));
        assertTrue(dataRequest.hasArgument(StandardArguments.CLASS));
        assertTrue(dataRequest.hasArgument(StandardArguments.FROM));
        assertTrue(dataRequest.hasArgument(StandardArguments.TO));
        assertTrue(dataRequest.hasArgument(StandardArguments.IS_STATE_OF));
        assertTrue(dataRequest.hasArgument(StandardArguments.NAMES));
        assertTrue(dataRequest.hasArgument(StandardArguments.INCLUDE_STATES));
        assertTrue(dataRequest.hasArgument(StandardArguments.SORT_ORDER));
        assertTrue(dataRequest.hasArgument(StandardArguments.SORT));
        assertTrue(dataRequest.hasArgument(StandardArguments.ROWS));
        assertTrue(dataRequest.hasArgument(StandardArguments.INITIALIZATION));
        assertTrue(dataRequest.hasArgument(StandardArguments.DERIVED_FROM));
        assertEquals(12, dataRequest.getArguments().size());
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
}
