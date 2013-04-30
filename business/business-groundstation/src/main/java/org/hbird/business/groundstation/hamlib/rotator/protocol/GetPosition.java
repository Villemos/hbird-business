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
package org.hbird.business.groundstation.hamlib.rotator.protocol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.camel.TypeConverter;
import org.hbird.business.groundstation.base.DriverContext;
import org.hbird.business.groundstation.configuration.RotatorDriverConfiguration;
import org.hbird.business.groundstation.device.response.ResponseHandler;
import org.hbird.business.groundstation.hamlib.protocol.HamlibProtocolHelper;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.interfaces.IEntityInstance;

/**
 *
 */
public class GetPosition implements ResponseHandler<RotatorDriverConfiguration, String, String> {

    public static final String KEY = "get_pos";

    public static final String COMMAND = "+p\n";

    public static final String PARAMETER_AZIMUTH = "Azimuth";
    public static final String PARAMETER_ELEVEATION = "Elevation";

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
    public List<IEntityInstance> handle(DriverContext<RotatorDriverConfiguration, String, String> driverContext, String response) {
        List<IEntityInstance> result;
        if (HamlibProtocolHelper.isErrorResponse(response)) {
            // TODO - 25.04.2013, kimmell - handle error case here; RPRT -1 etc
            result = Collections.emptyList();
        }
        else {
            Map<String, String> responseParams = HamlibProtocolHelper.toMap(response);
            String issuedBy = driverContext.getPart().getID();
            TypeConverter typeConverter = driverContext.getTypeConverter();
            Parameter azimuth = new Parameter(issuedBy, PARAMETER_AZIMUTH, "Azimuth of the antenna rotator", typeConverter.convertTo(Double.class,
                    responseParams.get(PARAMETER_AZIMUTH)), "Degree");
            Parameter elevation = new Parameter(issuedBy, PARAMETER_ELEVEATION, "Elevation of the antenna rotator", typeConverter.convertTo(Double.class,
                    responseParams.get(PARAMETER_ELEVEATION)), "Degree");

            result = new ArrayList<IEntityInstance>(2);
            result.add(azimuth);
            result.add(elevation);
        }
        return result;
    }

}
