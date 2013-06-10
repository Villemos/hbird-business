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
package org.hbird.business.navigation.processors.orekit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import org.hbird.business.api.IPublish;
import org.hbird.business.api.IdBuilder;
import org.hbird.business.navigation.configuration.OrbitalStatePredictionConfiguration;
import org.hbird.business.navigation.orekit.IPropagatorProvider;
import org.hbird.business.navigation.orekit.OrbitalStateCollector;
import org.hbird.business.navigation.request.OrbitalStatePredictionRequest;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.orekit.propagation.Propagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class OrekitOrbitalStatePredictorTest {

    private static final String SAT_ID = "SAT-1";
    private static final long PREDICTION_STEP = 13000L;
    private static final long NOW = System.currentTimeMillis();
    private static final String TLE_ID = "SAT-1/TLE:" + NOW;
    private static final long END_TIME = NOW + 1000L * 60 * 60 * 24;

    @Mock
    private IdBuilder idBuilder;

    @Mock
    private IPropagatorProvider propagatorProvider;

    @Mock
    private IPublish publisher;

    @Mock
    private OrbitalStatePredictionRequest request;

    @Mock
    private Propagator propagator;

    @Mock
    private OrbitalStatePredictionConfiguration config;

    @Mock
    private TleOrbitalParameters tleParameters;

    private OrekitOrbitalStatePredictor predictor;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        System.setProperty("orekit.data.path", "src/main/resources/orekit-data.zip");
        predictor = new OrekitOrbitalStatePredictor(propagatorProvider, publisher, idBuilder);
        inOrder = inOrder(idBuilder, propagatorProvider, publisher, request, propagator, config, tleParameters);
        when(propagatorProvider.getPropagator(request)).thenReturn(propagator);
        when(request.getConfiguration()).thenReturn(config);
        when(request.getTleParameters()).thenReturn(tleParameters);
        when(config.getSatelliteId()).thenReturn(SAT_ID);
        when(config.getPredictionStep()).thenReturn(PREDICTION_STEP);
        when(tleParameters.getInstanceID()).thenReturn(TLE_ID);
        when(request.getEndTime()).thenReturn(END_TIME);
    }

    @Test
    public void testPredict() throws Exception {
        assertEquals(request, predictor.predict(request));

        inOrder.verify(propagatorProvider, times(1)).getPropagator(request);
        inOrder.verify(request, times(1)).getConfiguration();
        inOrder.verify(request, times(1)).getTleParameters();
        inOrder.verify(config, times(1)).getSatelliteId();
        inOrder.verify(config, times(1)).getPredictionStep();
        inOrder.verify(tleParameters, times(1)).getInstanceID();
        ArgumentCaptor<OrbitalStateCollector> collectorCaptor = ArgumentCaptor.forClass(OrbitalStateCollector.class);
        inOrder.verify(propagator, times(1)).setMasterMode(eq(PREDICTION_STEP / 1000D), collectorCaptor.capture());
        OrbitalStateCollector collector = collectorCaptor.getValue();
        assertNotNull(collector);
        assertEquals(publisher, collector.getPublisher());
        inOrder.verify(request, times(1)).getEndTime();
        ArgumentCaptor<AbsoluteDate> endTimeCaptor = ArgumentCaptor.forClass(AbsoluteDate.class);
        inOrder.verify(propagator, times(1)).propagate(endTimeCaptor.capture());
        AbsoluteDate endTime = endTimeCaptor.getValue();
        assertNotNull(endTime);
        assertEquals(new AbsoluteDate(new Date(END_TIME), TimeScalesFactory.getUTC()), endTime);
        List<OrbitalState> result = collector.getDataSet();
        inOrder.verify(request, times(1)).setResult(result);
        inOrder.verifyNoMoreInteractions();
    }
}
