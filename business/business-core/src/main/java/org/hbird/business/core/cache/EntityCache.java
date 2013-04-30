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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hbird.exchange.interfaces.IEntity;

/**
 *
 */
public class EntityCache<E extends IEntity> {

    protected final CacheResolver<E> resolver;

    protected final Map<String, E> map = createCaheMap();

    public EntityCache(CacheResolver<E> resolver) {
        this.resolver = resolver;
    }

    public E getById(String id) {
        E result = map.get(id);
        if (result == null) {
            result = resolver.resolveById(id);
            if (result != null) {
                map.put(id, result);
            }
        }
        return result;
    }

    protected Map<String, E> createCaheMap() {
        return new ConcurrentHashMap<String, E>();
    }
}
