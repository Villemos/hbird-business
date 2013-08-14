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

import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.exceptions.NotFoundException;
import org.hbird.exchange.interfaces.IEntityInstance;

/**
 *
 */
public class GenericCacheResolver<E extends IEntityInstance> implements CacheResolver<E> {

    private final Class<E> type;
    private final IDataAccess dao;

    public GenericCacheResolver(IDataAccess dao, Class<E> type) {
        this.dao = dao;
        this.type = type;
    }

    /**
     * @see org.hbird.business.core.cache.CacheResolver#resolveById(java.lang.String)
     */
    @Override
    public E resolveById(String id) throws Exception {
        try {
            return dao.getById(id, type);
        }
        catch (NotFoundException e) {
            return null;
        }
    }

    /**
     * @see org.hbird.business.core.cache.CacheResolver#resolveByInstanceId(java.lang.String)
     */
    @Override
    public E resolveByInstanceId(String id) throws Exception {
        try {
            return dao.getByInstanceId(id, type);
        }
        catch (NotFoundException e) {
            return null;
        }
    }
}
