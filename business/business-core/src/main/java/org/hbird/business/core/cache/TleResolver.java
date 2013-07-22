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

import org.hbird.business.api.deprecated.IDataAccess;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class TleResolver implements CacheResolver<TleOrbitalParameters> {

    private static final Logger LOG = LoggerFactory.getLogger(TleResolver.class);

    private final IDataAccess dao;

    public TleResolver(IDataAccess dao) {
        this.dao = dao;
    }

    /**
     * @see org.hbird.business.core.cache.CacheResolver#resolveById(java.lang.String)
     */
    @Override
    public TleOrbitalParameters resolveById(String id) {
        try {
            //return (TleOrbitalParameters) dao.resolve(id);
        	return dao.resolve(id, TleOrbitalParameters.class);
        }
        catch (Exception e) {
            LOG.error("Failed to resolve TLE for ID {}", id, e);
            return null;
        }
    }
}
