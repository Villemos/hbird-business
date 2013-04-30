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
import org.hbird.exchange.navigation.Satellite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class SatelliteResolver implements CacheResolver<Satellite> {

    private static final Logger LOG = LoggerFactory.getLogger(SatelliteResolver.class);

    private final IDataAccess dao;

    public SatelliteResolver(IDataAccess dao) {
        this.dao = dao;
    }

    /**
     * @see org.hbird.business.core.cache.CacheResolver#resolveById(java.lang.String)
     */
    @Override
    public Satellite resolveById(String id) {
        try {
            return (Satellite) dao.resolve(id);
        }
        catch (Exception e) {
            LOG.warn("Failed to resolve Satellite for ID {}", id, e);
            return null;
        }
    }
}
