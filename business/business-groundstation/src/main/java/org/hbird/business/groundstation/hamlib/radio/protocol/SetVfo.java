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
package org.hbird.business.groundstation.hamlib.radio.protocol;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hbird.business.groundstation.base.DriverContext;
import org.hbird.business.groundstation.configuration.RadioDriverConfiguration;
import org.hbird.business.groundstation.device.response.ResponseHandler;
import org.hbird.business.groundstation.hamlib.protocol.HamlibProtocolHelper;
import org.hbird.business.groundstation.hamlib.radio.RadioState;
import org.hbird.exchange.interfaces.IEntityInstance;

/**
 *
 */
public class SetVfo implements ResponseHandler<RadioDriverConfiguration, String, String> {

    public static final String KEY = "set_vfo";
    public static final String COMMAND_TEMPLATE = "+V %s\n";

    /**
     * @see org.hbird.business.groundstation.device.response.ResponseHandler#getKey()
     */
    @Override
    public String getKey() {
        return KEY;
    }

    /**
     * @see org.hbird.business.groundstation.device.response.ResponseHandler#handle(org.hbird.business.groundstation.base.DriverContext,
     *      java.lang.Object)
     */
    @Override
    public List<IEntityInstance> handle(DriverContext<RadioDriverConfiguration, String, String> driverContext, String response) {
        List<IEntityInstance> result = Collections.emptyList(); // no parameters to return; just update the device state
        if (HamlibProtocolHelper.isErrorResponse(response)) {
            // TODO - 26.04.2013, kimmell - handle error here
        }
        else {
            RadioState state = (RadioState) driverContext.getDeviceState();
            Map<String, String> params = HamlibProtocolHelper.toMap(response);
            state.setCurrentVfo(params.get(KEY));
        }
        return result;
    }

    public static String createCommand(String targetVfo) {
        return String.format(COMMAND_TEMPLATE, targetVfo);
    }
}
