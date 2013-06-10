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
package org.hbird.business.navigation.request.orekit;

import static org.junit.Assert.assertEquals;

import org.hbird.exchange.navigation.LocationContactEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.orekit.frames.Frame;
import org.orekit.frames.TopocentricFrame;
import org.orekit.propagation.SpacecraftState;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ContactDataTest {

    @Mock
    private SpacecraftState startState;

    @Mock
    private SpacecraftState endState;

    @Mock
    private LocationContactEvent event;

    @Mock
    private Frame inertialFrame;

    @Mock
    private TopocentricFrame locationOnEarth;

    private ContactData contactData;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        contactData = new ContactData(startState, endState, locationOnEarth, inertialFrame, event);
        inOrder = Mockito.inOrder(startState, endState, locationOnEarth, inertialFrame, event);
    }

    @After
    public void tearDown() {
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetStartState() throws Exception {
        assertEquals(startState, contactData.getStartState());
    }

    @Test
    public void testGetEndState() throws Exception {
        assertEquals(endState, contactData.getEndState());
    }

    @Test
    public void testGetLocationOnEarth() throws Exception {
        assertEquals(locationOnEarth, contactData.getLocationOnEarth());
    }

    @Test
    public void testGetInertialFrame() throws Exception {
        assertEquals(inertialFrame, contactData.getInertialFrame());
    }

    @Test
    public void testGetEvent() throws Exception {
        assertEquals(event, contactData.getEvent());
    }
}
