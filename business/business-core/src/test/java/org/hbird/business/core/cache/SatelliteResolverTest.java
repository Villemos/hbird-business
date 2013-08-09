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
package org.hbird.business.core.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.hbird.business.api.IDataAccess;
import org.hbird.exchange.navigation.Satellite;
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
public class SatelliteResolverTest {

    private static final String ID = "SAT-1";

    @Mock
    private Satellite sat;

    @Mock
    private IDataAccess dao;

    private RuntimeException exception;

    private SatelliteResolver satelliteResolver;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        satelliteResolver = new SatelliteResolver(dao);
        exception = new RuntimeException("Muchos problemos!");
        inOrder = inOrder(sat, dao);
    }

    @Test
    public void testResolveById() throws Exception {
        when(dao.getById(ID, Satellite.class)).thenReturn(sat);
        assertEquals(sat, satelliteResolver.resolveById(ID));
        inOrder.verify(dao, times(1)).getById(ID, Satellite.class);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testResolveByIdNotFound() throws Exception {
        when(dao.getById(ID, Satellite.class)).thenReturn(null);
        assertNull(satelliteResolver.resolveById(ID));
        inOrder.verify(dao, times(1)).getById(ID, Satellite.class);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testResolveByIdWithException() throws Exception {
        when(dao.getById(ID, Satellite.class)).thenThrow(exception);
        assertNull(satelliteResolver.resolveById(ID));
        inOrder.verify(dao, times(1)).getById(ID, Satellite.class);
        inOrder.verifyNoMoreInteractions();
    }
}
