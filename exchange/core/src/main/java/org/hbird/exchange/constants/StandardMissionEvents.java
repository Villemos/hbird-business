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

	   public static final Event LAUNCH 
	   = new Event(	"", 
					"Launch",
					"OccuredEvent",
					"The launch of the satellite.", 
					0l);

	   public static final Event PREDICTED_GROUND_STATION_CONTACT_ESTABLISH 
	   = new Event(	"", 
					"PredictedGroundStationContact",
					"PredictedEvent",
					"Prediction that contact with a specific satellite, ground station and antenna will occur at this time.", 
					0l);

	   public static final Event GROUND_STATION_CONTACT_ESTABLISHED 
	   = new Event(	"", 
					"GroundStationContactEstablished",
					"OccuredEvent",
					"Registration that contact with a specific satellite, ground station and antenna occured at this time.", 
					0l);

	   public static final Event PREDICTED_GROUND_STATION_CONTACT_LOST 
	   = new Event(	"", 
					"PredictedGroundStationContactLost",
					"PredictedEvent",
					"Prediction that a contact with a specific satellite, ground station and antenna will occur at this time.", 
					0l);

	   public static final Event GROUND_STATION_CONTACT_LOST 
	   = new Event(	"", 
					"GroundStationContactLost",
					"OccuredEvent",
					"Registration that contact with a specific satellite, ground station and antenna occured at this time.", 
					0l);

	   public static final Event SINGLE_UPSET_EVENT 
	   = new Event(	"", 
					"SingleUpsetEvent",
					"OccuredEvent",
					"Registration that a Single Upset Event (SUE) occured.", 
					0l);

	   public static final Event CONTROL_REESTABLISHED 
	   = new Event(	"", 
					"ControlReestablished",
					"OccuredEvent",
					"Registration that control of the satellite was reestablished after a SUE.", 
					0l);

	   public static final Event DECOMMISIONING 
	   = new Event(	"", 
					"Decommisioning",
					"OccuredEvent",
					"Decommisioning of the satellite.", 
					0l);
}
