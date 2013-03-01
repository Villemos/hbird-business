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
/**
 * 
 */
package org.hbird.exchange.dataaccess;

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.constants.StandardComponents;
import org.hbird.exchange.navigation.GroundStation;

/**
 * 
 */
public class GroundStationRequest extends DataRequest {

    public static final String DESCRIPTION = "A request for the definition of ground stations.";

    private static final long serialVersionUID = -2463827175606652486L;

    public GroundStationRequest(String issuedBy) {
        super(issuedBy, StandardComponents.ARCHIVE, GroundStationRequest.class.getSimpleName(), DESCRIPTION);
        setType(GroundStation.class.getSimpleName());
        setIsInitialization(true);
    }

    /**
     * @param issuedBy
     * @param destination
     */
    public GroundStationRequest(String issuedBy, List<String> groundStationNames) {
        this(issuedBy);
        addNames(groundStationNames);
    }

    public GroundStationRequest(String issuedBy, String groundStation) {
        this(issuedBy);
        List<String> groundStations = new ArrayList<String>();
        groundStations.add(groundStation);
        addNames(groundStations);
    }
}
