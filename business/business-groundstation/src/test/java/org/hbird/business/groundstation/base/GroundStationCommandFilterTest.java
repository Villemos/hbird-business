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
package org.hbird.business.groundstation.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.hbird.business.groundstation.configuration.GroundStationDriverConfiguration;
import org.hbird.exchange.groundstation.Track;
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
public class GroundStationCommandFilterTest {

    private static final String ID = "GS_ID";

    @Mock
    private GroundStationDriverConfiguration config;

    @Mock
    private Track track;

    private GroundStationCommandFilter groundStationCommandFilter;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        groundStationCommandFilter = new GroundStationCommandFilter(config);
        inOrder = inOrder(config, track);
        when(config.getGroundstationId()).thenReturn(ID);
    }

    @Test
    public void testAcceptTrack() throws Exception {
        when(track.getDestination()).thenReturn(ID);
        assertTrue(groundStationCommandFilter.acceptTrack(track));
        inOrder.verify(track, times(1)).getDestination();
        inOrder.verify(config, times(1)).getGroundstationId();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testDenyTrack() throws Exception {
        when(track.getDestination()).thenReturn("!" + ID);
        assertFalse(groundStationCommandFilter.acceptTrack(track));
        inOrder.verify(track, times(1)).getDestination();
        inOrder.verify(config, times(1)).getGroundstationId();
        inOrder.verifyNoMoreInteractions();
    }
}
