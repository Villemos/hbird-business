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
package org.hbird.exchange.dataaccess;

import static org.hbird.exchange.dataaccess.Arguments.SATELLITE_NAME;
import static org.hbird.exchange.dataaccess.Arguments.create;

import java.util.List;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.constants.StandardComponents;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.navigation.TleOrbitalParameters;

public class TleRequest extends DataRequest {

    public static final String DESCRIPTION = "A request for the TLE parameters of a satellite.";

    private static final long serialVersionUID = 5283711249928543145L;

    public TleRequest(String issuedBy, String satellite) {
        super(issuedBy, StandardComponents.ARCHIVE, TleRequest.class.getSimpleName(), DESCRIPTION);
        setSatelliteName(satellite);
        setType(TleOrbitalParameters.class.getSimpleName());
        setIsInitialization(true);
        setSort(StandardArguments.TIMESTAMP);
        setSortOrder("DESC");
        setRows(1);
    }

    public TleRequest(String issuedBy, String satellite, long from, long to) {
        super(issuedBy, StandardComponents.ARCHIVE, TleRequest.class.getSimpleName(), DESCRIPTION);
        setSatelliteName(satellite);
        setType(TleOrbitalParameters.class.getSimpleName());
        setIsInitialization(true);
        setFrom(from);
        setTo(to);
    }

    /**
     * @see org.hbird.exchange.core.Command#getArgumentDefinitions()
     */
    @Override
    protected List<CommandArgument> getArgumentDefinitions() {
        List<CommandArgument> args = super.getArgumentDefinitions();
        args.add(create(SATELLITE_NAME));
        return args;
    }

    public void setSatelliteName(String satellite) {
        setArgumentValue(StandardArguments.SATELLITE_NAME, satellite);
    }

    public String getSatelliteName() {
        return getArgumentValue(StandardArguments.SATELLITE_NAME, String.class);
    }
}
