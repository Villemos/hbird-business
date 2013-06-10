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
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.List;

import org.hbird.business.navigation.request.OrbitalStatePredictionRequest;
import org.hbird.exchange.navigation.OrbitalState;
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
public class OrbitalStateExtractorTest {

    @Mock
    private OrbitalStatePredictionRequest request;

    @Mock
    private List<OrbitalState> list;

    private OrbitalStateExtractor extractor;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        extractor = new OrbitalStateExtractor();
        inOrder = inOrder(request, list);
        when(request.getResult()).thenReturn(list);
    }

    @Test
    public void testExtract() throws Exception {
        assertEquals(list, extractor.extract(request));
        inOrder.verify(request, times(1)).getResult();
        inOrder.verifyNoMoreInteractions();
    }
}
