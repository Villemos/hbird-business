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
package org.hbird.exchange.constants;

import org.hbird.exchange.core.Event;

/**
 * A set of standard mission events.,
 * 
 * The events can be used as templates. The generated events will be issued by a specific
 * entity and have a specific timestamp.
 * 
 * @author Gert Villemos
 * 
 */
public class StandardMissionEvents {

	
    public static final Event LAUNCH = new Event("Event/Launch", "Launch");

    public static final Event PREDICTED_GROUND_STATION_CONTACT_ESTABLISH = new Event("Event/PredictedGroundStationContact", "PredictedGroundStationContact");

    public static final Event GROUND_STATION_CONTACT_ESTABLISHED = new Event("Event/GroundStationContactEstablished", "GroundStationContactEstablished");

    public static final Event PREDICTED_GROUND_STATION_CONTACT_LOST = new Event("Event/PredictedGroundStationContactLost", "PredictedGroundStationContactLost");

    public static final Event GROUND_STATION_CONTACT_LOST = new Event("Event/GroundStationContactLost", "GroundStationContactLost");

    public static final Event SINGLE_UPSET_EVENT = new Event("Event/SingleUpsetEvent","SingleUpsetEvent");

    public static final Event CONTROL_REESTABLISHED = new Event("Event/ControlReestablished", "ControlReestablished");

    public static final Event DECOMMISIONING = new Event("Event/Decommisioning", "Decommisioning");

    public static final Event COMPONENT_START = new Event("Event/ComponentStart", "ComponentStart");

    public static final Event COMPONENT_STOP = new Event("Event/ComponentStop", "ComponentStop");

	static {
		LAUNCH.setDescription("The launch of the satellite.");
		PREDICTED_GROUND_STATION_CONTACT_ESTABLISH.setDescription("Prediction that contact with a specific satellite, ground station and antenna will occur at this time.");
		GROUND_STATION_CONTACT_ESTABLISHED.setDescription("Registration that contact with a specific satellite, ground station and antenna occured at this time.");
		PREDICTED_GROUND_STATION_CONTACT_LOST.setDescription("Prediction that a contact with a specific satellite, ground station and antenna will occur at this time.");
		GROUND_STATION_CONTACT_LOST.setDescription("Registration that contact with a specific satellite, ground station and antenna occured at this time.");
		SINGLE_UPSET_EVENT.setDescription("Registration that a Single Upset Event (SUE) occured.");
		CONTROL_REESTABLISHED.setDescription("Registration that control of the satellite was reestablished after a SUE.");
		DECOMMISIONING.setDescription("Decommisioning of the satellite.");
		COMPONENT_START.setDescription("Component started");
		COMPONENT_STOP.setDescription("Component stopping");
	}

}
