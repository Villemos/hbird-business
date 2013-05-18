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

import static org.hbird.exchange.dataaccess.Arguments.SATELLITE_ID;
import static org.hbird.exchange.dataaccess.Arguments.create;

import java.util.List;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.navigation.OrbitalState;

public class OrbitalStateRequest extends DataRequest {

    public static final String DESCRIPTION = "A request for the latest orbital state of a satellite";

    private static final long serialVersionUID = -5920513650830048315L;

    public OrbitalStateRequest(String ID) {
        super(ID, OrbitalStateRequest.class.getSimpleName());
        setDescription(DESCRIPTION);
        setIsInitialization(true);
        setRows(1);
        setSort(StandardArguments.TIMESTAMP);
        setSortOrder("DESC");
        setClass(OrbitalState.class.getSimpleName());
    }

    /**
     * @see org.hbird.exchange.dataaccess.DataRequest#getArgumentDefinitions()
     */
    @Override
    protected List<CommandArgument> getArgumentDefinitions(List<CommandArgument> args) {
        args = super.getArgumentDefinitions(args);
        args.add(create(SATELLITE_ID));
        return args;
    }

    public void setSatelliteID(String satelliteID) {
        setArgumentValue(StandardArguments.SATELLITE_ID, satelliteID);
    }

    public void setFromTime(Long from) {
        setFrom(from);
    }

    public void setToTime(Long to) {
        setTo(to);
    }
}
