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
package org.hbird.business.calibration;

import javax.script.*;

import org.apache.camel.Exchange;
import org.hbird.exchange.core.StateParameter;
import org.hbird.exchange.scripting.ScriptExecutionRequest;

/** 
 * A camel executor of scripts. 
 */
public class ScriptExecutor {

	/** Create a script engine manager */
	protected ScriptEngineManager factory = new ScriptEngineManager();
	
	/**
	 * Camel processor method for execution of a script.
	 * 
	 * @param exchange The camel exchange carrying a CalibrationRequest.  
	 */
	public void process(Exchange exchange) {

		/** Get the request. */
		ScriptExecutionRequest request = (ScriptExecutionRequest) exchange.getIn().getBody();

		/** Create a script engine and run it on the script. */
		try {
			/** Make the request available to the script. For example in JavaScript, this
			 *  will create a global variable 'request' where the methods are available
			 *  through the syntax 'request.getName()'*/
			ScriptEngine engine = factory.getEngineByName(request.format); 
			engine.put("request", request);
			
			/** Execute the script. */
			engine.eval(request.script);			

			StateParameter state = new StateParameter("", "Script Execution State", "", request.getName(), (Boolean) engine.get("result"));
			exchange.getIn().setBody(state);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
