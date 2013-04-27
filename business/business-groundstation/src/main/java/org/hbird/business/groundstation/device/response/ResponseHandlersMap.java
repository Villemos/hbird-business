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
package org.hbird.business.groundstation.device.response;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Handler;
import org.hbird.business.groundstation.base.DriverContext;
import org.hbird.business.groundstation.configuration.GroundStationDriverConfiguration;
import org.hbird.exchange.interfaces.INamed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ResponseHandlersMap<C extends GroundStationDriverConfiguration, K, R> {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseHandlersMap.class);

    private final Map<K, ResponseHandler<C, K, R>> handlers = new HashMap<K, ResponseHandler<C, K, R>>();
    private final ResponseHandler<C, K, R> defaultHandler = new DefaultResponseHandler<C, K, R>();
    private final DriverContext<C, K, R> driverContext;

    public ResponseHandlersMap(DriverContext<C, K, R> driverContext) {
        this.driverContext = driverContext;
    }

    public void addHandler(ResponseHandler<C, K, R> handler) {
        handlers.put(handler.getKey(), handler);
    }

    @Handler
    public List<INamed> handle(R response) {
        if (response == null) {
            LOG.warn("Response is null");
            return Collections.emptyList();
        }

        if (driverContext == null) {
            LOG.warn("DriverContext is null");
            return Collections.emptyList();
        }

        ResponseKeyExtractor<K, R> keyExtractor = driverContext.getKeyExtractor();
        if (keyExtractor == null) {
            LOG.error("KeyExtractor is null");
            return Collections.emptyList();
        }

        K key = keyExtractor.getKey(response);
        if (key == null) {
            LOG.error("Unable to extract key from response {}", String.valueOf(response));
            return Collections.emptyList();
        }
        ResponseHandler<C, K, R> handler = handlers.get(key);
        if (handler == null) {
            handler = defaultHandler;
        }
        try {
            return handler.handle(driverContext, response);
        }
        catch (Exception e) {
            LOG.error("Handler for key {} failed to handle response {}", new Object[] { key, String.valueOf(response), e });
            return Collections.emptyList();
        }
    }
}
