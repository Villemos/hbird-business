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
package eu.estcube.gs.base;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.hbird.exchange.core.State;

/**
 * @author Admin
 *
 */
public class ReplyForwarder {

	protected String sourceName;
	
	public ReplyForwarder(String name) {
		this.sourceName = name;
	}
	
	@Handler
	public State handle(Exchange exchange) {

		String message = (String) exchange.getOut().getBody();		
        String[] messageSplit = message.split("\n");
        String[] name = messageSplit[0].split(":");

        return new State(sourceName, "ExecutionState", "Raw response from Hamlib", name[0], message.contains("RPRT 0"));
	}
}
