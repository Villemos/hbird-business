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

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class OrbitalStatePredictionConfigurationTest {

    private static final long STEP = 12132412L;

    private OrbitalStatePredictionConfiguration config;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        config = new OrbitalStatePredictionConfiguration();
    }

    @Test
    public void testGetPredictionStep() throws Exception {
        testSetPredictionStep();
    }

    @Test
    public void testSetPredictionStep() throws Exception {
        assertEquals(OrbitalStatePredictionConfiguration.DEFAULT_PREDICTION_STEP, config.getPredictionStep());
        config.setPredictionStep(STEP);
        assertEquals(STEP, config.getPredictionStep());
    }
}
