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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.camel.TypeConverter;
import org.hbird.business.groundstation.base.DriverContext;
import org.hbird.business.groundstation.configuration.RadioDriverConfiguration;
import org.hbird.business.groundstation.device.response.ResponseHandler;
import org.hbird.business.groundstation.hamlib.protocol.HamlibProtocolHelper;
import org.hbird.business.navigation.orekit.NavigationUtilities;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.interfaces.IEntityInstance;

/**
 *
 */
public class SetFrequency implements ResponseHandler<RadioDriverConfiguration, String, String> {

    public static final String KEY = "set_freq";

    /**
     * @see org.hbird.business.groundstation.device.response.ResponseHandler#getKey()
     */
    @Override
    public String getKey() {
        return KEY;
    }

    /**
     * @see org.hbird.business.groundstation.device.response.ResponseHandler#handle(java.lang.Object,
     *      org.hbird.exchange.interfaces.IPart)
     */
    @Override
    public List<IEntityInstance> handle(DriverContext<RadioDriverConfiguration, String, String> driverContext, String response) {
        List<IEntityInstance> result;
        if (HamlibProtocolHelper.isErrorResponse(response)) {
            // TODO - 25.04.2013, kimmell - handle error case here; RPRT -1 etc
            result = Collections.emptyList();
        }
        else {
            TypeConverter converter = driverContext.getTypeConverter();
            String linkName = GetFrequency.getLinkName(driverContext);
            String name = String.format("%s Target Frequency", linkName);
            String description = String.format("%s Target Frequency for the radio", linkName);

            String id = driverContext.getPart().getID();
            Parameter param = new Parameter(id + "/" + name, name);
            param.setIssuedBy(id);
            param.setDescription(description);
            param.setValue(converter.convertTo(Long.class, HamlibProtocolHelper.toMap(response).get(KEY)));
            param.setUnit("Hz");
            result = new ArrayList<IEntityInstance>(1);
            result.add(param);
        }
        return result;
    }

    public static String createCommand(long frequency, double doppler) {
        double dopplerShift = NavigationUtilities.calculateDopplerShift(doppler, frequency);
        long actualFrequency = (long) (frequency + dopplerShift);
        return createCommand(actualFrequency);
    }

    public static String createCommand(long frquency) {
        StringBuilder sb = new StringBuilder();
        sb.append("+F ").append(frquency).append("\n");
        return sb.toString();
    }
}
