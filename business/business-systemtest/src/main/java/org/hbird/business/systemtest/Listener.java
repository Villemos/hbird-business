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
package org.hbird.business.systemtest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hbird.exchange.core.Issued;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.State;

public class Listener {

	public List<Named> elements = new ArrayList<Named>();

	public Named lastReceived = null;

	public Map<String, State> states = new HashMap<String, State>();

	public void process(Named named) {
		synchronized (elements) {

			elements.add(named);
			lastReceived = named;

			if (named instanceof State) {
				State state = (State) named;
				states.put(state.getName(), state);
			}
		}
	}	
}

