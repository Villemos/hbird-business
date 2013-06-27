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
package org.hbird.business.tracking.quartz;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.hbird.exchange.navigation.LocationContactEvent;
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
public class ScheduleDeltaCheckTest {

    private static final long NOW = System.currentTimeMillis();
    private static final long DELTA = 1000L * 60 * 60;

    @Mock
    private TrackingDriverConfiguration config;

    @Mock
    private Exchange exchange;

    @Mock
    private Message in;

    @Mock
    private LocationContactEvent event;

    private ScheduleDeltaCheck check;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        check = new ScheduleDeltaCheck(config);
        inOrder = inOrder(config, exchange, in, event);
        when(config.getScheduleDelta()).thenReturn(DELTA);
        when(exchange.getIn()).thenReturn(in);
        when(in.getBody(LocationContactEvent.class)).thenReturn(event);
    }

    @Test
    public void testCanSchedule() throws Exception {
        assertTrue(check.canSchedule(10, 20, 8));
        assertTrue(check.canSchedule(10, 20, 9));
        assertFalse(check.canSchedule(10, 20, 10));
        assertFalse(check.canSchedule(10, 20, 11));
    }

    @Test
    public void testMatchesTrue() throws Exception {
        when(event.getStartTime()).thenReturn(NOW + DELTA * 2);
        assertTrue(check.matches(exchange));
        inOrder.verify(exchange, times(1)).getIn();
        inOrder.verify(in, times(1)).getBody(LocationContactEvent.class);
        inOrder.verify(event, times(1)).getStartTime();
        inOrder.verify(config, times(1)).getScheduleDelta();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testMatchesFalse() throws Exception {
        when(event.getStartTime()).thenReturn(NOW + DELTA - 1);
        assertFalse(check.matches(exchange));
        inOrder.verify(exchange, times(1)).getIn();
        inOrder.verify(in, times(1)).getBody(LocationContactEvent.class);
        inOrder.verify(event, times(1)).getStartTime();
        inOrder.verify(config, times(1)).getScheduleDelta();
        inOrder.verifyNoMoreInteractions();
    }
}
