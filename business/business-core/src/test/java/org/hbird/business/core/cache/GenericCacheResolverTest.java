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
import static org.junit.Assert.fail;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.exceptions.NotFoundException;
import org.hbird.exchange.core.EntityInstance;
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
public class GenericCacheResolverTest {

    private static final String ID = "identificator";

    @Mock
    private IDataAccess dao;

    private Class<EntityInstance> type;

    @Mock
    private EntityInstance entity;

    private GenericCacheResolver<EntityInstance> resolver;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        type = EntityInstance.class;
        resolver = new GenericCacheResolver<EntityInstance>(dao, type);
        inOrder = inOrder(dao, entity);
    }

    @Test
    public void testResolveById() throws Exception {
        when(dao.getById(ID, type)).thenReturn(entity);
        assertEquals(entity, resolver.resolveById(ID));
        inOrder.verify(dao, times(1)).getById(ID, type);
        inOrder.verifyNoMoreInteractions();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testResolveByIdNotFound() throws Exception {
        when(dao.getById(ID, type)).thenThrow(NotFoundException.class);
        assertNull(resolver.resolveById(ID));
        inOrder.verify(dao, times(1)).getById(ID, type);
        inOrder.verifyNoMoreInteractions();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testResolveByIdWithException() throws Exception {
        when(dao.getById(ID, type)).thenThrow(Exception.class);
        try {
            resolver.resolveById(ID);
            fail("Exception expected");
        }
        catch (Exception e) {
        }
        inOrder.verify(dao, times(1)).getById(ID, type);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testResolveByInstanceId() throws Exception {
        when(dao.getByInstanceId(ID, type)).thenReturn(entity);
        assertEquals(entity, resolver.resolveByInstanceId(ID));
        inOrder.verify(dao, times(1)).getByInstanceId(ID, type);
        inOrder.verifyNoMoreInteractions();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testResolveByInstanceIdNotFound() throws Exception {
        when(dao.getByInstanceId(ID, type)).thenThrow(NotFoundException.class);
        assertNull(resolver.resolveByInstanceId(ID));
        inOrder.verify(dao, times(1)).getByInstanceId(ID, type);
        inOrder.verifyNoMoreInteractions();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testResolveByInstanceIdWithException() throws Exception {
        when(dao.getByInstanceId(ID, type)).thenThrow(Exception.class);
        try {
            resolver.resolveByInstanceId(ID);
            fail("Exception expected");
        }
        catch (Exception e) {
        }
        inOrder.verify(dao, times(1)).getByInstanceId(ID, type);
        inOrder.verifyNoMoreInteractions();
    }
}
