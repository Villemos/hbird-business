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
import static org.junit.Assert.fail;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.hbird.business.api.IOrbitalDataAccess;
import org.hbird.business.api.exceptions.NotFoundException;
import org.hbird.business.navigation.configuration.PredictionConfigurationBase;
import org.hbird.business.navigation.request.PredictionRequest;
import org.hbird.exchange.navigation.TleOrbitalParameters;
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
public class TleResolverTest {

    private static final String SAT_ID = "SAT-ID";

    @Mock
    private IOrbitalDataAccess dao;

    @Mock
    private PredictionRequest<PredictionConfigurationBase> request;

    @Mock
    private PredictionConfigurationBase config;

    @Mock
    private TleOrbitalParameters tle;

    private NotFoundException exception;

    private TleResolver tleResolver;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        exception = new NotFoundException();
        tleResolver = new TleResolver(dao);
        inOrder = inOrder(dao, request, config, tle);
        when(request.getConfiguration()).thenReturn(config);
        when(config.getSatelliteId()).thenReturn(SAT_ID);
    }

    @Test
    public void testResolveTle() throws Exception {
        when(dao.getTleFor(SAT_ID)).thenReturn(tle);
        assertEquals(request, tleResolver.resolveTle(request));
        inOrder.verify(request, times(1)).getConfiguration();
        inOrder.verify(config, times(1)).getSatelliteId();
        inOrder.verify(dao, times(1)).getTleFor(SAT_ID);
        inOrder.verify(request, times(1)).setTleParameters(tle);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testResolveTleNotFound() throws Exception {
        when(dao.getTleFor(SAT_ID)).thenThrow(exception);
        try {
            tleResolver.resolveTle(request);
            fail("Exception excpected");
        }
        catch (Exception e) {
            assertEquals(IllegalStateException.class, e.getClass());
        }
        inOrder.verify(request, times(1)).getConfiguration();
        inOrder.verify(config, times(1)).getSatelliteId();
        inOrder.verify(dao, times(1)).getTleFor(SAT_ID);
        inOrder.verifyNoMoreInteractions();
    }
}
