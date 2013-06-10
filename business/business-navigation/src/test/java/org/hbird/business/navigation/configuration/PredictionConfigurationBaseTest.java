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
package org.hbird.business.navigation.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class PredictionConfigurationBaseTest {

    private static final String SAT_ID = "SAT-ID";
    private static final long INTERVAL = 132412L;
    private static final int PERIOD = 332134223;

    private PredictionConfigurationBase config;

    /**
     * @throws java.lang.Exception
     */
    @SuppressWarnings("serial")
    @Before
    public void setUp() throws Exception {
        config = new PredictionConfigurationBase() {
        };
    }

    @Test
    public void testToString() throws Exception {
        config.setSatelliteId(SAT_ID);
        config.setPredictionInterval(INTERVAL);
        config.setPredictionPeriod(PERIOD);
        String s = config.toString();
        assertNotNull(s);
        assertTrue(s.contains(SAT_ID));
        assertTrue(s.contains(String.valueOf(INTERVAL)));
        assertTrue(s.contains(String.valueOf(PERIOD)));
    }

    @Test
    public void testGetSatelliteId() throws Exception {
        testSetSatelliteId();
    }

    @Test
    public void testSetSatelliteId() throws Exception {
        assertNull(config.getSatelliteId());
        config.setSatelliteId(SAT_ID);
        assertEquals(SAT_ID, config.getSatelliteId());
    }

    @Test
    public void testGetPredictionPeriod() throws Exception {
        testSetPredictionPeriod();
    }

    @Test
    public void testSetPredictionPeriod() throws Exception {
        assertEquals(PredictionConfigurationBase.DEFAULT_PREDICTION_PERIOD, config.getPredictionPeriod());
        config.setPredictionPeriod(PERIOD);
        assertEquals(PERIOD, config.getPredictionPeriod());
    }

    @Test
    public void testGetPredictionInterval() throws Exception {
        testSetPredictionInterval();
    }

    @Test
    public void testSetPredictionInterval() throws Exception {
        assertEquals(PredictionConfigurationBase.DEFAULT_PREDICTION_INTERVAL, config.getPredictionInterval());
        config.setPredictionInterval(INTERVAL);
        assertEquals(INTERVAL, config.getPredictionInterval());
    }
}
