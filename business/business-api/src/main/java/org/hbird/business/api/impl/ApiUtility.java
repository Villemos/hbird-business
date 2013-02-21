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
package org.hbird.business.api.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;

public class ApiUtility {

	public static Map<Parameter, List<State>> sortParametersAndState(List<Named> elements) {

		/** The elements are sorted on timestamp. The state instances will have the same timestamp as
		 * the parameter instance they are the state of. The elements will be ordered as;
		 * 
		 * Parameter / State               Time
		 * PARA1                           A
		 * State1 (isStateOf PARA1)        A
		 * State2 (isStateOf PARA1)        A
		 * PARA2                           B
		 * PARA2                           C
		 * State3 (isStateOf PARA2)        C
		 * PARA1                           D
		 * State1 (isStateOf PARA1)        D
		 * State2 (isStateOf PARA1)        D
		 * ...
		 * 
		 * Where A < B < C < D
		 * */

		Map<Parameter, List<State>> results = new LinkedHashMap<Parameter, List<State>>();

		Long lastTimestamp = elements.get(0).getTimestamp();		

		List<State> states = new ArrayList<State>();
		Parameter parameter = null;
		
		for(Iterator<Named> it = elements.iterator(); it.hasNext();) {
			Named element = it.next();

			/** If this is a new time then put and reset. */
			if (lastTimestamp != element.getTimestamp()) {
				lastTimestamp = element.getTimestamp();
				
				results.put(parameter, states);
				parameter = null;
				states = new ArrayList<State>();
			}
		
			if (element instanceof Parameter) {
				parameter = (Parameter) element;
			}
			else if (element instanceof State) {
				states.add((State) element);
			}
		}
		
		/** Add the last element */
		if (parameter != null) {
			results.put(parameter, states);
		}
		return results;
	}
}
