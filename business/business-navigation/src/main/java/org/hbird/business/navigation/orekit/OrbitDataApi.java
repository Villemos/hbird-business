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
package org.hbird.business.navigation.orekit;

import java.util.List;

import org.apache.camel.CamelContext;
import org.hbird.business.api.IPointingData;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.PointingData;
import org.orekit.errors.OrekitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Admin
 * 
 */
public class OrbitDataApi implements IPointingData {

    private static final Logger LOG = LoggerFactory.getLogger(OrbitDataApi.class);

    protected PointingDataCalculator pointingDataCalculator = new PointingDataCalculator();

    protected String issuedBy;

    protected CamelContext camelContext;

    public OrbitDataApi(String issuedBy) {
        this(issuedBy, null);
    }

    public OrbitDataApi(String issuedBy, CamelContext camelContext) {
        this.issuedBy = issuedBy;
        this.camelContext = camelContext;
    }

    /**
     * @see org.hbird.business.api.IOrbitData#calculateContactData(org.hbird.exchange.navigation.LocationContactEvent,
     *      org.hbird.exchange.groundstation.GroundStation, long)
     */
    @Override
    public List<PointingData> calculateContactData(LocationContactEvent locationContactEvent, GroundStation groundStation, long contactDataStepSize) {
        List<PointingData> data = null;
        try {
            data = pointingDataCalculator.calculateContactData(locationContactEvent, groundStation, contactDataStepSize);
        }
        catch (OrekitException e) {
            LOG.error("Failed to calculate contact data for LocationContactEvent '{}'", locationContactEvent.getInstanceID(), e);
        }

        return data;
    }
}
