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
import static org.mockito.Mockito.inOrder;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ContactPredictionConfigurationTest {

    private static final long STEP = 55534563445243L;

    @Mock
    private List<String> groundStationsIds;

    private ContactPredictionConfiguration config;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        config = new ContactPredictionConfiguration();
        inOrder = inOrder(groundStationsIds);
    }

    @After
    public void tearDown() {
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetDetailsCalculationStep() throws Exception {
        testSetDetailsCalculationStep();
    }

    @Test
    public void testSetDetailsCalculationStep() throws Exception {
        assertEquals(ContactPredictionConfiguration.DEFAULT_DETAILS_CALCULATION_STEP, config.getDetailsCalculationStep());
        config.setDetailsCalculationStep(STEP);
        assertEquals(STEP, config.getDetailsCalculationStep());
    }

    @Test
    public void testGetGroundStationsIds() throws Exception {
        testSetGroundStationsIds();
    }

    @Test
    public void testSetGroundStationsIds() throws Exception {
        List<String> ids = config.getGroundStationsIds();
        assertNotNull(ids);
        assertEquals(0, ids.size());
        config.setGroundStationsIds(groundStationsIds);
        assertEquals(groundStationsIds, config.getGroundStationsIds());
    }
}
