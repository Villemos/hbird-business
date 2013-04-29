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
package org.hbird.business.groundstation.hamlib.radio;

import static org.junit.Assert.assertEquals;

import org.hbird.business.groundstation.configuration.RadioDriverConfiguration;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class RadioStateTest {

    private RadioState state;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        state = new RadioState();
    }

    @Test
    public void testSetCurrentVfo() throws Exception {
        assertEquals(RadioDriverConfiguration.DEFAULT_DOWNLINK_VFO, state.getCurrentVfo());
        state.setCurrentVfo(RadioDriverConfiguration.DEFAULT_UPLINK_VFO);
        assertEquals(RadioDriverConfiguration.DEFAULT_UPLINK_VFO, state.getCurrentVfo());
        state.setCurrentVfo(RadioDriverConfiguration.DEFAULT_DOWNLINK_VFO);
        assertEquals(RadioDriverConfiguration.DEFAULT_DOWNLINK_VFO, state.getCurrentVfo());
    }

    @Test
    public void testGetCurrentVfo() throws Exception {
        testSetCurrentVfo();
    }
}
