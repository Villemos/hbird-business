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

import java.util.List;

import org.hbird.exchange.navigation.OrbitalState;
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
public class OrbitalStatePredictionRequestTest {

    private static final long NOW = System.currentTimeMillis();

    @Mock
    private List<OrbitalState> result;

    private OrbitalStatePredictionRequest request;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        request = new OrbitalStatePredictionRequest(NOW);
        inOrder = Mockito.inOrder(result);
    }

    @Test
    public void testGetResult() throws Exception {
        testSetResult();
    }

    @Test
    public void testSetResult() throws Exception {
        assertNull(request.getResult());
        request.setResult(result);
        assertEquals(result, request.getResult());
        inOrder.verifyNoMoreInteractions();
    }
}
