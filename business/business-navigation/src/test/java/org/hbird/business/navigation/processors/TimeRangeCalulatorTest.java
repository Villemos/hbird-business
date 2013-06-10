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

import org.hbird.business.navigation.configuration.PredictionConfigurationBase;
import org.hbird.business.navigation.request.PredictionRequest;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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
public class TimeRangeCalulatorTest {

    private static final long NOW = System.currentTimeMillis();
    private static final int HOURS = 72;

    @Mock
    private PredictionRequest<PredictionConfigurationBase> request;

    @Mock
    private PredictionConfigurationBase config;

    private TimeRangeCalulator calculator;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        calculator = new TimeRangeCalulator();
        inOrder = inOrder(request, config);
        when(request.getConfiguration()).thenReturn(config);
        when(config.getPredictionPeriod()).thenReturn(HOURS);
    }

    @Test
    public void testCalculate() throws Exception {
        when(request.getStartTime()).thenReturn(NOW);
        DateTime start = new DateTime(NOW, DateTimeZone.UTC).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
        DateTime end = start.plusHours(HOURS);
        assertEquals(request, calculator.calculate(request));
        inOrder.verify(request, times(1)).getConfiguration();
        inOrder.verify(config, times(1)).getPredictionPeriod();
        ArgumentCaptor<Long> startCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> endCaptor = ArgumentCaptor.forClass(Long.class);
        inOrder.verify(request, times(1)).setStartTime(startCaptor.capture());
        inOrder.verify(request, times(1)).setEndTime(endCaptor.capture());
        assertEquals(start.getMillis(), startCaptor.getValue().longValue());
        assertEquals(end.getMillis(), endCaptor.getValue().longValue());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCalculateStartTimeNotSet() throws Exception {
        when(request.getStartTime()).thenReturn(0L);
        DateTime start = new DateTime(0, DateTimeZone.UTC).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
        DateTime end = start.plusHours(HOURS);
        assertEquals(request, calculator.calculate(request));
        inOrder.verify(request, times(1)).getConfiguration();
        inOrder.verify(config, times(1)).getPredictionPeriod();
        ArgumentCaptor<Long> startCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> endCaptor = ArgumentCaptor.forClass(Long.class);
        inOrder.verify(request, times(1)).setStartTime(startCaptor.capture());
        inOrder.verify(request, times(1)).setEndTime(endCaptor.capture());
        assertEquals(start.getMillis(), startCaptor.getValue().longValue());
        assertEquals(end.getMillis(), endCaptor.getValue().longValue());
        inOrder.verifyNoMoreInteractions();
    }
}
