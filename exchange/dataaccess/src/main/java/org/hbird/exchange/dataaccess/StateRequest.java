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

import java.util.List;

import org.hbird.exchange.core.State;

public class StateRequest extends DataRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5274506775743830200L;

	public StateRequest(String issuedBy, String isStateOf) {
		super(issuedBy, "any", "StateRequest", "A request for the state(s) of a Named object");

		setClass(State.class.getSimpleName());
		setType("State");
		addArgument("isStateOf", isStateOf);
		setIsInitialization(true);
	}	

	public StateRequest(String issuedBy, String isStateOf, long from, long to) {
		super(issuedBy, "any", "StateRequest", "A request for the state(s) of a Named object");

		setClass(State.class.getSimpleName());
		setType("State");
		addArgument("isStateOf", isStateOf);
		setFrom(from);
		setTo(to);
		setIsInitialization(true);
	}	

	public StateRequest(String issuedBy, String isStateOf, List<String> names) {
		super(issuedBy, "any", "StateRequest", "A request for the state(s) of a Named object");

		setClass(State.class.getSimpleName());
		setType("State");
		addArgument("isStateOf", isStateOf);
		addArgument("names", names);
		setIsInitialization(true);
	}	

	public StateRequest(String issuedBy, String destination, String isStateOf) {
		super(issuedBy, destination, "StateRequest", "A request for the state(s) of a Named object");

		setClass(State.class.getSimpleName());
		setType("State");
		addArgument("isStateOf", isStateOf);
		setIsInitialization(true);
	}	
}
