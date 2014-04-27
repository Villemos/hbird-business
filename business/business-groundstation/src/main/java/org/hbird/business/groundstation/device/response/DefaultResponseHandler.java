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
import java.util.List;

import org.hbird.business.groundstation.base.DriverContext;
import org.hbird.business.groundstation.configuration.GroundStationDriverConfiguration;
import org.hbird.business.groundstation.hamlib.protocol.HamlibProtocolConstants;
import org.hbird.exchange.interfaces.IEntityInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class DefaultResponseHandler<C extends GroundStationDriverConfiguration, K, R> implements ResponseHandler<C, K, R> {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultResponseHandler.class);

    /**
     * @see org.hbird.business.groundstation.device.response.ResponseHandler#getKey()
     */
    @Override
    public K getKey() {
        return null;
    }

    /**
     * @see org.hbird.business.groundstation.device.response.ResponseHandler#handle(java.lang.String,
     *      org.hbird.exchange.interfaces.IPart)
     */
    @Override
    public List<IEntityInstance> handle(DriverContext<C, K, R> driverContext, R response) {
        // Hamlib response for commands that do not return value, doesn't return the key of the command, therefore custom handler is not reached.
        // Hence, we need to check whether it contains 0 (success) or something else
        if (String.valueOf(response).contains(HamlibProtocolConstants.RESPONSE_END_MARKER)) {
            if (!String.valueOf(response).contains("0")) {
                LOG.error("Failed response {}", String.valueOf(response));
            }
            // No else block required for success response
        } else {
            LOG.warn("No handler for response {}", String.valueOf(response));
        }
        return Collections.emptyList();
    }
}
