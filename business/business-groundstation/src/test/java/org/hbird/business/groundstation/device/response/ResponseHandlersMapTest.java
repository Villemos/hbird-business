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
package org.hbird.business.groundstation.device.response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.List;

import org.hbird.business.groundstation.base.DriverContext;
import org.hbird.business.groundstation.configuration.GroundStationDriverConfiguration;
import org.hbird.exchange.interfaces.INamed;
import org.hbird.exchange.interfaces.IPart;
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
public class ResponseHandlersMapTest {

    private static final String KEY = "key";

    private static final String RESPONSE = "key:\nname: value\nRPRT 0\n";

    @Mock
    private ResponseKeyExtractor<String, String> keyExtractor;

    @Mock
    private IPart part;

    @Mock
    private DriverContext<GroundStationDriverConfiguration, String, String> driverContext;

    @Mock
    private ResponseHandler<GroundStationDriverConfiguration, String, String> handler;

    @Mock
    private List<INamed> params;

    private ResponseHandlersMap<GroundStationDriverConfiguration, String, String> responseHandlers;

    private InOrder inOrder;

    private RuntimeException exception;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        responseHandlers = new ResponseHandlersMap<GroundStationDriverConfiguration, String, String>(driverContext);
        exception = new RuntimeException("Mutchos problemos");
        inOrder = inOrder(part, keyExtractor, handler, params, driverContext);
        when(handler.getKey()).thenReturn(KEY);
        when(driverContext.getKeyExtractor()).thenReturn(keyExtractor);
        when(driverContext.getPart()).thenReturn(part);
    }

    @Test
    public void testAddHandler() throws Exception {
        responseHandlers.addHandler(handler);
        inOrder.verify(handler, times(1)).getKey();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testHandleNull() throws Exception {
        List<INamed> result = responseHandlers.handle(null);
        assertNotNull(result);
        assertEquals(0, result.size());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testHandleDriverContextIsNull() throws Exception {
        responseHandlers = new ResponseHandlersMap<GroundStationDriverConfiguration, String, String>(null);
        List<INamed> result = responseHandlers.handle(RESPONSE);
        assertNotNull(result);
        assertEquals(0, result.size());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testHandleKeyExtractorIsNull() {
        when(driverContext.getKeyExtractor()).thenReturn(null);
        responseHandlers = new ResponseHandlersMap<GroundStationDriverConfiguration, String, String>(driverContext);
        List<INamed> result = responseHandlers.handle(RESPONSE);
        assertNotNull(result);
        assertEquals(0, result.size());
        inOrder.verify(driverContext, times(1)).getKeyExtractor();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testHandleKeyExtractorReturnsNull() {
        when(keyExtractor.getKey(RESPONSE)).thenReturn(null);
        List<INamed> result = responseHandlers.handle(RESPONSE);
        assertNotNull(result);
        assertEquals(0, result.size());
        inOrder.verify(driverContext, times(1)).getKeyExtractor();
        inOrder.verify(keyExtractor, times(1)).getKey(RESPONSE);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testHandleKeyExtractorReturnsUnknownKey() {
        when(keyExtractor.getKey(RESPONSE)).thenReturn("!" + KEY);
        responseHandlers.addHandler(handler);
        List<INamed> result = responseHandlers.handle(RESPONSE);
        assertNotNull(result);
        assertEquals(0, result.size());
        inOrder.verify(handler, times(1)).getKey();
        inOrder.verify(driverContext, times(1)).getKeyExtractor();
        inOrder.verify(keyExtractor, times(1)).getKey(RESPONSE);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testHandleHandlerWithException() {
        when(keyExtractor.getKey(RESPONSE)).thenReturn(KEY);
        when(handler.handle(driverContext, RESPONSE)).thenThrow(exception);
        responseHandlers.addHandler(handler);
        List<INamed> result = responseHandlers.handle(RESPONSE);
        assertNotNull(result);
        assertEquals(0, result.size());
        inOrder.verify(handler, times(1)).getKey();
        inOrder.verify(driverContext, times(1)).getKeyExtractor();
        inOrder.verify(keyExtractor, times(1)).getKey(RESPONSE);
        inOrder.verify(handler, times(1)).handle(driverContext, RESPONSE);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testHandle() {
        when(keyExtractor.getKey(RESPONSE)).thenReturn(KEY);
        when(handler.handle(driverContext, RESPONSE)).thenReturn(params);
        responseHandlers.addHandler(handler);
        List<INamed> result = responseHandlers.handle(RESPONSE);
        assertEquals(params, result);
        inOrder.verify(handler, times(1)).getKey();
        inOrder.verify(driverContext, times(1)).getKeyExtractor();
        inOrder.verify(keyExtractor, times(1)).getKey(RESPONSE);
        inOrder.verify(handler, times(1)).handle(driverContext, RESPONSE);
        inOrder.verifyNoMoreInteractions();
    }
}
