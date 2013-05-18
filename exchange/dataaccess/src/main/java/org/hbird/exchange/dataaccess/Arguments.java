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

import java.util.List;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.TleOrbitalParameters;

/**
 * {@link CommandArgument} templates used in org.hbird.exchange.dataaccess package.
 */
public class Arguments {

    public static final CommandArgument APPLICABLE_TO = new CommandArgument(StandardArguments.APPLICABLE_TO,
            "The ID of IEntity the the IApplicableTo must be applicale.", String.class, false);

    public static final CommandArgument CLASS = new CommandArgument(StandardArguments.CLASS, "The class of the Named object.", String.class, false);

    public static final CommandArgument CONTACT_DATA_STEP_SIZE = new CommandArgument(StandardArguments.CONTACT_DATA_STEP_SIZE,
            "The propagation step size when calculating Contact Data between a location and a satellite between which visibility exist.", Long.class,
            "Milliseconds", 500L, true);

    public static final CommandArgument DELETE_ALL = new CommandArgument(StandardArguments.DELETE_ALL,
            "Flag indicating that the complete archive should be deleted.", Boolean.class, false);

    public static final CommandArgument DELTA_PROPAGATION = new CommandArgument(StandardArguments.DELTA_PROPAGATION,
            "The delta propagation from the starttime.", Long.class, "Seconds", 6 * 60 * 60l, true);

    public static final CommandArgument DERIVED_FROM = new CommandArgument(StandardArguments.DERIVED_FROM,
            "Identifier of the named object of which an object must be derived from.", String.class, false);

    public static final CommandArgument ISSUED_BY = new CommandArgument(StandardArguments.ISSUED_BY,
            "The source from which the data was 'issuedBy'", String.class, false);

    public static final CommandArgument FROM = new CommandArgument(StandardArguments.FROM, "The start of a range search on timestamp. Default to '*'.",
            Long.class, "Seconds", null, false);

    public static final CommandArgument GROUND_STATION_ID = new CommandArgument(StandardArguments.GROUND_STATION_ID, "The ID of the ground station.",
            String.class, "ID", null, true);

    public static final CommandArgument GROUND_STATION_IDS = new CommandArgument(StandardArguments.GROUND_STATION_IDS,
            "The list of ground station IDs to which contact shall be calculated.", List.class, true);

    public static final CommandArgument INCLUDE_STATES = new CommandArgument(StandardArguments.INCLUDE_STATES,
            "Flag defining that all states applicable to the named objects should also be retrieved", Boolean.class, "", Boolean.FALSE, true);

    public static final CommandArgument INITIAL_STATE = new CommandArgument(StandardArguments.INITIAL_STATE,
            "The initial orbital state (time, position, velocity) from which shall be propagated. Default is last known state of the satellite.",
            OrbitalState.class, false);

    public static final CommandArgument INITIALIZATION = new CommandArgument(StandardArguments.INITIALIZATION,
            "If set to true, then the value below the 'to' time of each named object matching the search criterions will be retrieved.", Boolean.class, "",
            Boolean.FALSE, true);

    public static final CommandArgument NAMES = new CommandArgument(StandardArguments.NAMES, "List of names of named objects to be retrieved.", List.class,
            false);

    public static final CommandArgument ENTITY_ID = new CommandArgument(StandardArguments.ENTITY_ID, "The entity ID of the entry.", String.class,
            false);

    public static final CommandArgument ENTITY_INSTANCE_ID = new CommandArgument(StandardArguments.ENTITY_INSTANCE_ID, "The entity instance ID of the entry.",
            String.class,
            false);

    public static final CommandArgument PUBLISH = new CommandArgument(StandardArguments.PUBLISH,
            "Flag indicating that the resulting predictions should be published to the system instead of returned as a response.", Boolean.class, "",
            Boolean.class, false);

    public static final CommandArgument ROWS = new CommandArgument(StandardArguments.ROWS, "The maximum number of rows to be retrieved.", Integer.class, "",
            1000, true);

    public static final CommandArgument SATELLITE_ID = new CommandArgument(StandardArguments.SATELLITE_ID,
            "The ID of the satellite making the contact.", String.class, "ID", null, false);

    public static final CommandArgument SORT = new CommandArgument(StandardArguments.SORT, "The sort field. Default is timestamp.", String.class, "",
            StandardArguments.TIMESTAMP, true);

    public static final CommandArgument SORT_ORDER = new CommandArgument(StandardArguments.SORT_ORDER,
            "The order in which the returned data should be returned.", String.class, "", "ASC", true);

    public static final CommandArgument START_TIME = new CommandArgument(StandardArguments.START_TIME, "The start time of the propagation.", Long.class,
            "Seconds", null, true);

    public static final CommandArgument STEP_SIZE = new CommandArgument(StandardArguments.STEP_SIZE, "The propagation step size.", Long.class, "Seconds", 60L,
            true);

    public static final CommandArgument TLE_PARAMETERS = new CommandArgument(StandardArguments.TLE_PARAMETERS,
            "The two line elements of a specific satellite. If left empty the latest TLE for the satellite will be taken.", TleOrbitalParameters.class, false);

    public static final CommandArgument TO = new CommandArgument(StandardArguments.TO, "The end of a range search on timestamp. Default to '*'.", Long.class,
            "Seconds", null, false);

    public static final CommandArgument VISIBILITY = new CommandArgument(StandardArguments.VISIBILITY,
            "Whether the contact event is a start of contact (true) or end of contact (false).", Boolean.class, "", Boolean.TRUE, true);

    public static final CommandArgument IS_PART_OF = new CommandArgument(StandardArguments.IS_PART_OF,
            "The name of the object that the found objects must be defined as being a part of.", String.class, "", null, true);

    public static CommandArgument create(CommandArgument template) {
        return new CommandArgument(template);
    }
}
