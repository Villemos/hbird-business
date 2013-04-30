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
package org.hbird.business.navigation.api;

import java.util.List;

import org.hbird.business.api.HbirdApi;
import org.hbird.business.api.IOrbitPrediction;
import org.hbird.business.navigation.NavigationComponent;
import org.hbird.business.navigation.orekit.NavigationUtilities;
import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.dataaccess.TlePropagationRequest;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.PointingData;
import org.hbird.exchange.navigation.Satellite;

public class OrbitPropagation extends HbirdApi implements IOrbitPrediction {

    public OrbitPropagation(String issuedBy) {
        super(issuedBy, NavigationComponent.ORBIT_PROPAGATOR_NAME);
    }

    @Override
    public List<EntityInstance> requestOrbitPropagation(String satellite) {
        TlePropagationRequest request = new TlePropagationRequest(issuedBy, satellite);
        return sendRequest(request);
    }

    @Override
    public List<EntityInstance> requestOrbitPropagation(String satellite, long from, long to) {
        TlePropagationRequest request = new TlePropagationRequest(issuedBy, satellite, from, to);
        return sendRequest(request);
    }

    @Override
    public List<EntityInstance> requestOrbitPropagation(String satellite, String location, long from, long to) {
        TlePropagationRequest request = new TlePropagationRequest(issuedBy, satellite, location, from, to);
        return sendRequest(request);
    }

    @Override
    public List<EntityInstance> requestOrbitPropagation(String satellite, List<String> locations, long from, long to) {
        TlePropagationRequest request = new TlePropagationRequest(issuedBy, satellite, locations, from, to);
        return sendRequest(request);
    }

    @Override
    public void requestOrbitPropagationStream(String satellite) {
        TlePropagationRequest request = new TlePropagationRequest(issuedBy, satellite);
        sendRequestStream(request);
    }

    @Override
    public void requestOrbitPropagationStream(String satellite, long from, long to) {
        TlePropagationRequest request = new TlePropagationRequest(issuedBy, satellite, from, to);
        sendRequestStream(request);
    }

    @Override
    public void requestOrbitPropagationStream(String satellite, String location, long from, long to) {
        TlePropagationRequest request = new TlePropagationRequest(issuedBy, satellite, location, from, to);
        sendRequestStream(request);
    }

    @Override
    public void requestOrbitPropagationStream(String satellite, List<String> locations, long from, long to) {
        TlePropagationRequest request = new TlePropagationRequest(issuedBy, satellite, locations, from, to);
        sendRequestStream(request);
    }

    protected List<EntityInstance> sendRequest(TlePropagationRequest request) {
        return executeRequestRespond(request);
    }

    protected void sendRequestStream(TlePropagationRequest request) {
        request.setArgumentValue(StandardArguments.PUBLISH, true);
        executeRequest(request);
    }

    @Override
    public List<PointingData> requestPointingDataFor(LocationContactEvent locationContactEvent, GroundStation groundStation,
            Satellite satellite, long contactDataStepSize) throws Exception {
        return NavigationUtilities.calculateContactData(locationContactEvent, groundStation, satellite, contactDataStepSize);
    }
}
