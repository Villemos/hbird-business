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
package org.hbird.business.navigation.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.hbird.business.navigation.configuration.PredictionConfigurationBase;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class PredictionRequestTest {

    private static final long NOW = System.currentTimeMillis();
    private static final long START = NOW - 1000L * 60 * 60;
    private static final long END = NOW + 1000L * 60 * 60;

    @Mock
    private PredictionConfigurationBase configuration;

    @Mock
    private TleOrbitalParameters tleParameters;

    private InOrder inOrder;

    private PredictionRequest<PredictionConfigurationBase> request;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        request = new PredictionRequest<PredictionConfigurationBase>(NOW) {
        };
        inOrder = Mockito.inOrder(configuration, tleParameters);
    }

    @After
    public void tearDown() {
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetConfiguration() throws Exception {
        testSetConfiguration();
    }

    @Test
    public void testSetConfiguration() throws Exception {
        assertNull(request.getConfiguration());
        request.setConfiguration(configuration);
        assertEquals(configuration, request.getConfiguration());
    }

    @Test
    public void testGetTleParameters() throws Exception {
        testSetTleParameters();
    }

    @Test
    public void testSetTleParameters() throws Exception {
        assertNull(request.getTleParameters());
        request.setTleParameters(tleParameters);
        assertEquals(tleParameters, request.getTleParameters());
    }

    @Test
    public void testGetStartTime() throws Exception {
        testSetStartTime();
    }

    @Test
    public void testSetStartTime() throws Exception {
        assertEquals(NOW, request.getStartTime());
        request.setStartTime(START);
        assertEquals(START, request.getStartTime());
    }

    @Test
    public void testGetEndTime() throws Exception {
        testSetEndTime();
    }

    @Test
    public void testSetEndTime() throws Exception {
        assertEquals(0L, request.getEndTime());
        request.setEndTime(END);
        assertEquals(END, request.getEndTime());
    }
}
