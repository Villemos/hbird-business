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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hbird.business.api.ICatalogue;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.navigation.configuration.ContactPredictionConfiguration;
import org.hbird.business.navigation.request.ContactPredictionRequest;
import org.hbird.exchange.groundstation.GroundStation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GroundStationResolverTest {

    private static final String ID_1 = "GS_1";
    private static final String ID_2 = "GS_2";

    @Mock
    private ICatalogue catalogue;

    @Mock
    private IDataAccess dao;

    @Mock
    private GroundStation gs1;

    @Mock
    private GroundStation gs2;

    @Mock
    private ContactPredictionConfiguration config;

    @Mock
    private ContactPredictionRequest<ContactPredictionConfiguration> request;

    private List<GroundStation> groundStations;

    private List<String> groundStationIds;

    private RuntimeException exception;

    private GroundStationResolver resolver;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        resolver = new GroundStationResolver(dao, catalogue);
        inOrder = inOrder(catalogue, dao, gs1, gs2, config, request);
        groundStations = new ArrayList<GroundStation>();
        groundStations.add(gs1);
        groundStations.add(gs2);
        groundStationIds = new ArrayList<String>();
        groundStationIds.add(ID_1);
        groundStationIds.add(ID_2);
        exception = new RuntimeException("Mutchos problemos");
        when(request.getConfiguration()).thenReturn(config);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testResolveWithIds() throws Exception {
        when(config.getGroundStationsIds()).thenReturn(groundStationIds);
        when(dao.resolve(ID_1)).thenReturn(gs1);
        when(dao.resolve(ID_2)).thenReturn(gs2);
        assertEquals(request, resolver.resolve(request));
        inOrder.verify(request, times(1)).getConfiguration();
        inOrder.verify(config, times(1)).getGroundStationsIds();
        inOrder.verify(dao, times(1)).resolve(ID_1);
        inOrder.verify(dao, times(1)).resolve(ID_2);
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        inOrder.verify(request, times(1)).setGroundStations(captor.capture());
        assertEquals(2, captor.getValue().size());
        assertEquals(gs1, captor.getValue().get(0));
        assertEquals(gs2, captor.getValue().get(1));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testResolveWithNoIds() throws Exception {
        when(config.getGroundStationsIds()).thenReturn(Collections.<String> emptyList());
        when(catalogue.getGroundStations()).thenReturn(groundStations);
        assertEquals(request, resolver.resolve(request));
        inOrder.verify(request, times(1)).getConfiguration();
        inOrder.verify(config, times(1)).getGroundStationsIds();
        inOrder.verify(catalogue, times(1)).getGroundStations();
        inOrder.verify(request, times(1)).setGroundStations(groundStations);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testResolveWithNullIds() throws Exception {
        when(config.getGroundStationsIds()).thenReturn(null);
        when(catalogue.getGroundStations()).thenReturn(groundStations);
        assertEquals(request, resolver.resolve(request));
        inOrder.verify(request, times(1)).getConfiguration();
        inOrder.verify(config, times(1)).getGroundStationsIds();
        inOrder.verify(catalogue, times(1)).getGroundStations();
        inOrder.verify(request, times(1)).setGroundStations(groundStations);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetAllGroundStations() throws Exception {
        when(catalogue.getGroundStations()).thenReturn(groundStations);
        assertEquals(groundStations, resolver.getAllGroundStations(catalogue));
        inOrder.verify(catalogue, times(1)).getGroundStations();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testResolveGroundStationsNoIds() throws Exception {
        List<GroundStation> result = resolver.resolveGroundStations(dao, Collections.<String> emptyList());
        assertNotNull(result);
        assertEquals(0, result.size());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testResolveGroundStations() throws Exception {
        when(dao.resolve(ID_1)).thenReturn(gs1);
        when(dao.resolve(ID_2)).thenReturn(gs2);
        List<GroundStation> result = resolver.resolveGroundStations(dao, groundStationIds);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(gs1, result.get(0));
        assertEquals(gs2, result.get(1));
        inOrder.verify(dao, times(1)).resolve(ID_1);
        inOrder.verify(dao, times(1)).resolve(ID_2);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testResolveGroundStationsNotFound() throws Exception {
        when(dao.resolve(ID_1)).thenReturn(gs1);
        when(dao.resolve(ID_2)).thenReturn(null);
        List<GroundStation> result = resolver.resolveGroundStations(dao, groundStationIds);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(gs1, result.get(0));
        inOrder.verify(dao, times(1)).resolve(ID_1);
        inOrder.verify(dao, times(1)).resolve(ID_2);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testResolveGroundStationsWithException() throws Exception {
        doThrow(exception).when(dao).resolve(ID_1);
        when(dao.resolve(ID_2)).thenReturn(gs2);
        List<GroundStation> result = resolver.resolveGroundStations(dao, groundStationIds);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(gs2, result.get(0));
        inOrder.verify(dao, times(1)).resolve(ID_1);
        inOrder.verify(dao, times(1)).resolve(ID_2);
        inOrder.verifyNoMoreInteractions();
    }
}
