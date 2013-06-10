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
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.hbird.business.navigation.request.orekit.ContactData;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.frames.TopocentricFrame;
import org.orekit.propagation.SpacecraftState;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class AbstractContactDetailCalculatorTest {

    @Mock
    private ContactData contactData;

    @Mock
    private SpacecraftState startState;

    @Mock
    private SpacecraftState endState;

    @Mock
    private TopocentricFrame locationOnEarth;

    @Mock
    private Frame inertialFrame;

    @Mock
    private LocationContactEvent event;

    private AbstractContactDetailCalculator calculator;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        calculator = new AbstractContactDetailCalculator() {
            @Override
            public void calculate(SpacecraftState startState, SpacecraftState endState, TopocentricFrame locationOnEarth, Frame inertialFrame,
                    LocationContactEvent event) throws OrekitException {
                assertEquals(AbstractContactDetailCalculatorTest.this.startState, startState);
                assertEquals(AbstractContactDetailCalculatorTest.this.endState, endState);
                assertEquals(AbstractContactDetailCalculatorTest.this.locationOnEarth, locationOnEarth);
                assertEquals(AbstractContactDetailCalculatorTest.this.inertialFrame, inertialFrame);
                assertEquals(AbstractContactDetailCalculatorTest.this.event, event);
            }
        };

        inOrder = inOrder(contactData, startState, endState, locationOnEarth, inertialFrame, event);
        when(contactData.getStartState()).thenReturn(startState);
        when(contactData.getEndState()).thenReturn(endState);
        when(contactData.getLocationOnEarth()).thenReturn(locationOnEarth);
        when(contactData.getInertialFrame()).thenReturn(inertialFrame);
        when(contactData.getEvent()).thenReturn(event);
    }

    @Test
    public void testProcess() throws Exception {
        assertEquals(contactData, calculator.process(contactData));
        inOrder.verify(contactData, times(1)).getStartState();
        inOrder.verify(contactData, times(1)).getEndState();
        inOrder.verify(contactData, times(1)).getLocationOnEarth();
        inOrder.verify(contactData, times(1)).getInertialFrame();
        inOrder.verify(contactData, times(1)).getEvent();
        inOrder.verifyNoMoreInteractions();
    }
}
