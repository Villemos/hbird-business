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
package org.hbird.exchange.groundstation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class LosTest {

    private static final String ID = "ID";

    private Los los;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        los = new Los(ID);
    }

    @Test
    public void testAos() throws Exception {
        assertNotNull(los);
        assertEquals(ID, los.getID());
        assertEquals(Los.EVENT_NAME, los.getName());
    }
}
