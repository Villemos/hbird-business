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
package eu.estcube.gs.radio.nativecommands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 *
 */
public class SetFrequencyTest {

    private static final long FREQUENSY = 1000000001L;

    private static final double DOPPLER = 1.32D;

    /**
     * Test method for
     * {@link eu.estcube.gs.radio.nativecommands.SetFrequency#createCommand(org.hbird.exchange.navigation.Satellite, org.hbird.exchange.navigation.PointingData)}
     * .
     */
    @Test
    public void testCreateCommandFrequency() {
        assertEquals("+F 1000000001\n", SetFrequency.createCommand(FREQUENSY));
    }

    /**
     * Test method for
     * {@link eu.estcube.gs.radio.nativecommands.SetFrequency#createCommand(org.hbird.exchange.navigation.Satellite, org.hbird.exchange.navigation.PointingData)}
     * .
     */
    @Test
    public void testCreateCommandFrequencyAndDoppler() {
        assertEquals("+F 999999996\n", SetFrequency.createCommand(FREQUENSY, DOPPLER));
    }
}
