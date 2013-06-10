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
package org.hbird.business.navigation.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;

import org.hbird.business.navigation.configuration.OrbitalStatePredictionConfiguration;
import org.hbird.business.navigation.request.OrbitalStatePredictionRequest;
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
public class OrbitalStatePredictionRequestCreatorTest {

    @Mock
    private OrbitalStatePredictionConfiguration config;

    private OrbitalStatePredictionRequestCreator creator;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        creator = new OrbitalStatePredictionRequestCreator(config);
        inOrder = inOrder(config);
    }

    @Test
    public void testCreate() throws Exception {
        OrbitalStatePredictionRequest request = creator.create();
        assertNotNull(request);
        assertEquals(config, request.getConfiguration());
        long diff = System.currentTimeMillis() - request.getStartTime();
        assertTrue(diff >= 0 && diff < 1000L * 60 * 10); // for extra slow builds
        inOrder.verifyNoMoreInteractions();
    }
}
