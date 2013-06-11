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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hbird.business.api.IPublish;
import org.hbird.business.navigation.configuration.ContactPredictionConfiguration;
import org.hbird.business.navigation.orekit.ContactEventCollector;
import org.hbird.business.navigation.orekit.IFrameProvider;
import org.hbird.business.navigation.orekit.IPropagatorProvider;
import org.hbird.business.navigation.request.ContactPredictionRequest;
import org.hbird.business.navigation.request.orekit.ContactData;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.navigation.GeoLocation;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.orekit.frames.Frame;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.events.EventDetector;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class OrekitContactPredictorTest {

    private static final String ISSUER = "ISSUER";
    private static final String SAT_ID = "SAT-1";
    private static final String GS_ID_1 = "GS-1";
    private static final String GS_ID_2 = "GS-2";
    private static final String GS_NAME_1 = "GS-A";
    private static final String GS_NAME_2 = "GS-B";

    private static final long NOW = System.currentTimeMillis();
    private static final long END = NOW + 1000L * 60 * 60 * 24;

    @Mock
    private IFrameProvider frameProvider;

    @Mock
    private IPropagatorProvider propagatorProvider;

    @Mock
    private IPublish publisher;

    @Mock
    private ContactPredictionRequest<ContactData> request;

    @Mock
    private Propagator propagator;

    @Mock
    private ContactPredictionConfiguration conf;

    private List<GroundStation> groundStations;

    @Mock
    private TleOrbitalParameters tleParameters;

    @Mock
    private Frame inertrialFrame;

    @Mock
    private GroundStation gs1;

    @Mock
    private GroundStation gs2;

    @Mock
    private GeoLocation location1;

    @Mock
    private GeoLocation location2;

    @Mock
    private ContactData contact1;

    @Mock
    private ContactData contact2;

    @Mock
    private ContactData contact3;

    @Mock
    private ContactEventCollector detector1;

    @Mock
    private ContactEventCollector detector2;

    private List<ContactData> results1;
    private List<ContactData> results2;

    private Collection<EventDetector> detectors;

    private OrekitContactPredictor predictor;

    private InOrder inOrder;

    @BeforeClass
    public static void beforeClass() {
        System.setProperty("orekit.data.path", "src/main/resources/orekit-data.zip");
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        predictor = new OrekitContactPredictor(ISSUER, propagatorProvider, publisher, frameProvider);
        groundStations = Arrays.asList(gs1, gs2);
        results1 = Arrays.asList(contact1);
        results2 = Arrays.asList(contact2, contact3);
        detectors = new ArrayList<EventDetector>();
        detectors.add(detector1);
        detectors.add(detector2);
        inOrder = inOrder(frameProvider, propagatorProvider, publisher, request, propagator, conf, tleParameters, inertrialFrame, gs1,
                gs2, location1, location2, contact1, contact2, contact3, detector1, detector2);

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testPredict() throws Exception {
        when(propagatorProvider.getPropagator(request)).thenReturn(propagator);
        when(request.getConfiguration()).thenReturn(conf);
        when(request.getGroundStations()).thenReturn(groundStations);
        when(request.getTleParameters()).thenReturn(tleParameters);
        when(conf.getSatelliteId()).thenReturn(SAT_ID);
        when(frameProvider.getInertialFrame()).thenReturn(inertrialFrame);
        when(gs1.getID()).thenReturn(GS_ID_1);
        when(gs1.getName()).thenReturn(GS_NAME_1);
        when(gs1.getGeoLocation()).thenReturn(location1);
        when(gs2.getID()).thenReturn(GS_ID_2);
        when(gs2.getName()).thenReturn(GS_NAME_2);
        when(gs2.getGeoLocation()).thenReturn(location2);
        when(request.getEndTime()).thenReturn(END);
        when(propagator.getEventsDetectors()).thenReturn(detectors);
        when(detector1.getDataSet()).thenReturn(results1);
        when(detector2.getDataSet()).thenReturn(results2);

        assertEquals(request, predictor.predict(request));

        inOrder.verify(propagatorProvider, times(1)).getPropagator(request);
        inOrder.verify(request, times(1)).getConfiguration();
        inOrder.verify(request, times(1)).getGroundStations();
        inOrder.verify(request, times(1)).getTleParameters();
        inOrder.verify(conf, times(1)).getSatelliteId();
        inOrder.verify(frameProvider, times(1)).getInertialFrame();
        inOrder.verify(gs1, times(1)).getID();
        inOrder.verify(gs1, times(1)).getName();
        inOrder.verify(gs1, times(1)).getGeoLocation();
        inOrder.verify(propagator, times(1)).addEventDetector(any(ContactEventCollector.class));
        inOrder.verify(gs2, times(1)).getID();
        inOrder.verify(gs2, times(1)).getName();
        inOrder.verify(gs2, times(1)).getGeoLocation();
        inOrder.verify(propagator, times(1)).addEventDetector(any(ContactEventCollector.class));
        ArgumentCaptor<AbsoluteDate> endDateCaptor = ArgumentCaptor.forClass(AbsoluteDate.class);
        inOrder.verify(propagator, times(1)).propagate(endDateCaptor.capture());
        assertNotNull(endDateCaptor.getValue());
        assertEquals(END, endDateCaptor.getValue().toDate(TimeScalesFactory.getUTC()).getTime());
        inOrder.verify(propagator, times(1)).getEventsDetectors();
        ArgumentCaptor<List> resultCaptor = ArgumentCaptor.forClass(List.class);
        inOrder.verify(request, times(1)).setPredictedEvents(resultCaptor.capture());
        assertNotNull(resultCaptor.getValue().size());
        assertEquals(contact1, resultCaptor.getValue().get(0));
        assertEquals(contact2, resultCaptor.getValue().get(1));
        assertEquals(contact3, resultCaptor.getValue().get(2));
        inOrder.verifyNoMoreInteractions();
    }
}
