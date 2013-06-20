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
package org.hbird.business.navigation.orekit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.hbird.business.navigation.configuration.PredictionConfigurationBase;
import org.hbird.business.navigation.request.PredictionRequest;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.analytical.tle.TLEPropagator;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TlePropagatorProviderTest {

    private static final long SAT_MASS = 1240L;
    private static final String LINE_1 = "1 39161U 13021C   13136.21724948  .00001020  00000-0  18275-3 0   280";
    private static final String LINE_2 = "2 39161  98.1285 214.6400 0009123 209.0111 151.0590 14.68904954  1306";

    @Mock
    private PredictionRequest<PredictionConfigurationBase> request;

    @Mock
    private TleOrbitalParameters tle;

    @Mock
    private Satellite sat;

    private TlePropagatorProvider provider;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        System.setProperty("orekit.data.path", "src/main/resources/orekit-data.zip");
        provider = new TlePropagatorProvider();
        inOrder = inOrder(request, tle, sat);
        when(request.getTleParameters()).thenReturn(tle);
        when(tle.getTleLine1()).thenReturn(LINE_1);
        when(tle.getTleLine2()).thenReturn(LINE_2);
        when(request.getSatellite()).thenReturn(sat);
        when(sat.getSatelliteMass()).thenReturn(SAT_MASS);
    }

    @Test
    public void testGetPropagator() throws Exception {
        Propagator propagator = provider.getPropagator(request);
        assertNotNull(propagator);
        assertTrue(propagator instanceof TLEPropagator);
        TLEPropagator tp = (TLEPropagator) propagator;
        assertEquals(LINE_1, tp.getTLE().getLine1());
        assertEquals(LINE_2, tp.getTLE().getLine2());
        inOrder.verify(request, times(1)).getTleParameters();
        inOrder.verify(request, times(1)).getSatellite();
        inOrder.verify(tle, times(1)).getTleLine1();
        inOrder.verify(tle, times(1)).getTleLine2();
        inOrder.verify(sat, times(1)).getSatelliteMass();
        inOrder.verifyNoMoreInteractions();
    }
}
