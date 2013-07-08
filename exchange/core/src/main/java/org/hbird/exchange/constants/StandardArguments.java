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
package org.hbird.exchange.constants;

/**
 * Constant names of command arguments in Hummingbird based systems.
 */
// TODO - 18.05.2013, kimmell - check code before changing constant values; most likely some of them are used in xml
// config or camel route definitions; use grep -i -r "the value you are changing" * | grep -v "/target/" to search all
// occurrences.
public class StandardArguments {

    public static final String ANGLE = "Angle";

    public static final String APPLICABLE_TO = "applicableTo";

    public static final String ARGUMENTS_LENGTH = "argumentsLength";

    public static final String AZIMUTH = "azimuth";

    public static final String CLASS = "class";

    // TODO - 18.05.2013, kimmell - change to componentName
    public static final String COMPONENT_NAME = "componentname";

    public static final String CONTACT = "contact";

    public static final String CONTACT_ID = "contactId";

    public static final String CONTACT_START = "contactStartTime";

    public static final String CONTACT_DATA_STEP_SIZE = "contactDataStepSize";

    public static final String COUNT = "count";

    public static final String DELETE_ALL = "deleteAll";

    public static final String DELTA_PROPAGATION = "deltaPropagation";

    public static final String DERIVED_FROM = "derivedFrom";

    public static final String DESCRIPTION = "description";

    public static final String DESTINATION = "destination";

    public static final String DOPPLER = "doppler";

    public static final String DOPPLER_SHIFT = "dopplerShift";

    public static final String ELEMENTS = "elements";

    public static final String ELEVATION = "elevation";

    public static final String END = "end";

    public static final String ENTITY = "entity";

    public static final String FROM = "from";

    public static final String FREQUENCY = "frequency";

    public static final String GROUND_STATION_ID = "groundStationId";

    public static final String GROUND_STATION_IDS = "groundStationIds";

    public static final String HAS_URI = "hasUri";

    public static final String HEART_BEAT = "heartbeat";

    public static final String ENTITY_ID = "entityID";

    public static final String ENTITY_INSTANCE_ID = "entityInstanceID";

    public static final String INCLUDE_STATES = "includeStates";

    // TODO - 18.05.2013, kimmell - change to initialState
    public static final String INITIAL_STATE = "initialstate";

    public static final String INITIALIZATION = "initialization";

    public static final String ISSUED_BY = "issuedBy";

    public static final String LENGTH = "length";

    public static final String LIMIT = "limit";

    // TODO - 18.05.2013, kimmell - change to limitValue
    public static final String LIMIT_VALUE = "limitvalue";

    public static final String NAME = "name";

    public static final String NAMES = "names";

    public static final String ORBIT_NUMBER = "orbitNumber";

    public static final String PATTERN = "pattern";

    public static final String PRIORITY = "priority";

    public static final String PUBLISH = "publish";

    // TODO - 18.05.2013, kimmell - change to queueName
    public static final String QUEUE_NAME = "queuename";

    public static final String ROWS = "rows";

    // TODO - 18.05.2013, kimmell - change to scriptDefinition
    public static final String SCRIPT_DEFINITION = "scriptdefinition";

    public static final String SATELLITE_ID = "satelliteId";

    public static final String SORT = "sort";

    // TODO - 18.05.2013, kimmell - change to sortOrder
    public static final String SORT_ORDER = "sortorder";

    public static final String SOURCE = "source";

    public static final String START = "start";

    // TODO - 18.05.2013, kimmell - change to startTime
    public static final String START_TIME = "starttime";

    public static final String STATE = "state";

    public static final String STEP_SIZE = "stepSize";

    public static final String TIMESTAMP = "timestamp";

    // TODO - 18.05.2013, kimmell - change to tleParameters
    public static final String TLE_PARAMETERS = "tleparameters";

    public static final String TO = "to";

    public static final String TYPE = "type";

    public static final String UNIT = "unit";

    public static final String USERNAME = "username";

    public static final String VALUE = "value";

    public static final String VALUE_HAS_CHANGED = "valueHasChanged";

    // TODO - 18.05.2013, kimmell - change to valueParameter
    public static final String VALUE_PARAMETER = "valueparameter";

    public static final String VERSION = "version";

    public static final String VISIBILITY = "visibility";

    // TODO - 18.05.2013, kimmell - change to isPartOf
    // TODO - 18.05.2013, kimmell - check if needed any more
    public static final String IS_PART_OF = "ispartof";

    public static final String PART = "part";

    public static final String FROM_SOURCE = "source";

    // public static final String QUALIFIED_NAME = "qualifiedname";
}
