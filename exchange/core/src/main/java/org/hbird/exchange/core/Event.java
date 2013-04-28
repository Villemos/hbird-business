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
package org.hbird.exchange.core;

/**
 * An event is something that occured on a timeline.
 * 
 * An event is something that happens either spontaniously (unforseen) or planned within the
 * mission.
 * 
 * Onboard events is not part of the housekeeping of the spacecraft; it may affect the housekeeping data, but
 * is itself not part of the data.
 * 
 * 
 * @author Gert Villemos
 * 
 */
public class Event extends EntityInstance {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8958078334517112743L;

    public Event(String issuedBy, String name, String description, long timestamp) {
        super(issuedBy, name, description);
        this.timestamp = timestamp;
    }

    public Event(String issuedBy, Event eventTemplate, long timestamp) {
        this(issuedBy, eventTemplate.getName(), eventTemplate.getDescription(), timestamp);
    }

    public Event(String issuedBy, Event eventTemplate) {
        this(issuedBy, eventTemplate, System.currentTimeMillis());
    }
}
