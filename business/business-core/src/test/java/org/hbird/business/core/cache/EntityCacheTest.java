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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hbird.business.api.IDataAccess;
import org.hbird.exchange.core.EntityInstance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class EntityCacheTest {

    private static final String ID = "ID";

    @Mock
    private Map<String, EntityInstance> map;

    @Mock
    private CacheResolver<EntityInstance> resolver;

    @Mock
    private EntityInstance entity;

    @Mock
    private IDataAccess dao;

    private EntityCache<EntityInstance> entityCache;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        entityCache = new EntityCache<EntityInstance>(resolver);
        inOrder = Mockito.inOrder(map, resolver, entity, dao);
    }

    @Test
    public void testEntityCache() throws Exception {
        when(resolver.resolveById(ID)).thenReturn(entity);
        entityCache = new EntityCache<EntityInstance>(resolver) {
            @Override
            protected Map<String, EntityInstance> createCaheMap() {
                return EntityCacheTest.this.map;
            }
        };

        assertEquals(entity, entityCache.getById(ID));
        inOrder.verify(map, times(1)).get(ID);
        inOrder.verify(resolver, times(1)).resolveById(ID);
        inOrder.verify(map, times(1)).put(ID, entity);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetById() throws Exception {
        when(resolver.resolveById(ID)).thenReturn(entity);
        assertEquals(entity, entityCache.getById(ID));
        inOrder.verify(resolver, times(1)).resolveById(ID);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetByIdNotFound() throws Exception {
        when(resolver.resolveById(ID)).thenReturn(null);
        when(resolver.resolveByInstanceId(ID)).thenReturn(null);
        assertNull(entityCache.getById(ID));
        assertNull(entityCache.getById(ID));
        assertNull(entityCache.getById(ID));
        inOrder.verify(resolver, times(1)).resolveById(ID);
        inOrder.verify(resolver, times(1)).resolveByInstanceId(ID);
        inOrder.verify(resolver, times(1)).resolveById(ID);
        inOrder.verify(resolver, times(1)).resolveByInstanceId(ID);
        inOrder.verify(resolver, times(1)).resolveById(ID);
        inOrder.verify(resolver, times(1)).resolveByInstanceId(ID);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetByInstanceId() throws Exception {
        when(resolver.resolveById(ID)).thenReturn(null);
        when(resolver.resolveByInstanceId(ID)).thenReturn(entity);
        assertEquals(entity, entityCache.getById(ID));
        inOrder.verify(resolver, times(1)).resolveById(ID);
        inOrder.verify(resolver, times(1)).resolveByInstanceId(ID);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetByIdSeveralTimes() throws Exception {
        when(resolver.resolveById(ID)).thenReturn(entity);
        assertEquals(entity, entityCache.getById(ID));
        assertEquals(entity, entityCache.getById(ID));
        assertEquals(entity, entityCache.getById(ID));
        inOrder.verify(resolver, times(1)).resolveById(ID);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testGetByInstanceIdSeveralTimes() throws Exception {
        when(resolver.resolveById(ID)).thenReturn(null);
        when(resolver.resolveByInstanceId(ID)).thenReturn(entity);
        assertEquals(entity, entityCache.getById(ID));
        assertEquals(entity, entityCache.getById(ID));
        assertEquals(entity, entityCache.getById(ID));
        inOrder.verify(resolver, times(1)).resolveById(ID);
        inOrder.verify(resolver, times(1)).resolveByInstanceId(ID);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testCreateCaheMap() throws Exception {
        Map<String, EntityInstance> map = entityCache.createCaheMap();
        assertNotNull(map);
        assertTrue(map.isEmpty());
        assertEquals(ConcurrentHashMap.class, map.getClass());
    }

    @Test
    public void testForType() throws Exception {
        EntityCache<EntityInstance> cache = EntityCache.forType(dao, EntityInstance.class);
        when(dao.getById(ID, EntityInstance.class)).thenReturn(null);
        when(dao.getByInstanceId(ID, EntityInstance.class)).thenReturn(entity);
        assertEquals(entity, cache.getById(ID));
        inOrder.verify(dao, times(1)).getById(ID, EntityInstance.class);
        inOrder.verify(dao, times(1)).getByInstanceId(ID, EntityInstance.class);
        inOrder.verifyNoMoreInteractions();
    }
}
