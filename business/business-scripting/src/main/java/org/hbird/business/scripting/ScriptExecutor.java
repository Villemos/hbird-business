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
package org.hbird.business.scripting;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.*;

import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.StateParameter;
import org.hbird.exchange.scripting.ScriptExecutionRequest;

/** 
 * The script executor will evaluate scripts. 
 * 
 * The executor receives the script as a request.
 */
public class ScriptExecutor {

	/** The configuration request defining the script to be executed. */
	protected ScriptExecutionRequest request = null;

	/** The script engine that will execute the script.*/
	protected ScriptEngine engine = null;

	/** Map of the parameter that the script needs as input and the current value*/
	public Map<String, Object> input = new HashMap<String, Object>();

	/** The names of the parameters that the scripts requires as input. */
	protected Pattern inputPattern = Pattern.compile("input=\"(.*?)\"");

	/** The values of the parameter created using the script. */
	public String parameterName = null;
	public String parameterDescription = null;
	public String parameterUnit = null;
	public String parameterIsStateOf = null;

	public ScriptExecutor(ScriptExecutionRequest request) {
		this.request = request;
		ScriptEngineManager factory = new ScriptEngineManager();
		engine = factory.getEngineByName(request.format); 	

		/** Analyse script. 
		 *  1. Identify name, description, isStateOf (optional), type and unit of the Parameter to be created. 
		 *  2. Identify the names of the parameters needed. */

		parameterName = find(Pattern.compile("name=\"(.*?)\""), request.script);
		parameterDescription = find(Pattern.compile("description=\"(.*?)\""), request.script);
		parameterUnit = find(Pattern.compile("unit=\"(.*?)\""), request.script);
		parameterIsStateOf = find(Pattern.compile("isStateOf=\"(.*?)\""), request.script);

		Matcher matcher = inputPattern.matcher(request.script);
		while (matcher.find()) {
			input.put(matcher.group(1), null);
		}		
	}

	/**
	 * Method to find a pattern in a script.
	 * 
	 * @param pattern The pattern to be found.
	 * @param script The text which might contain the pattern.
	 * @return The found value of the pattern (group 1)
	 */
	protected String find(Pattern pattern, String script) {
		Matcher matcher = pattern.matcher(script);
		if (matcher.find()) {
			return matcher.group(1);
		}

		return null;
	}

	/**
	 * Method for receiving an input parameter and evaluating the value of the script. The
	 * method will only evaluate the script if all input parameters have been received. 
	 * 
	 * @param in A parameter required to evaluate the script.
	 * @return A parameter holding the return value of the script. May be null if the script could not evaluate.
	 */
	public Parameter calculate(Parameter in) {

		Parameter out = null;
		
		try {
			/** Bind the parameter to the script. */
			engine.put(in.getName(), in.getValue());
			
			/** Check if a binding has been created for all needed parameters. */
			if (ready()) {
				engine.eval(request.script);

				/** Create a Parameter or a StateParameter based on the script definition. */
				if (parameterIsStateOf == null) {
					out = new Parameter("script:" + request.getName(), parameterName, parameterDescription, engine.get(parameterName), parameterUnit);
				}
				else {
					out = new StateParameter("script:" + request.getName(), parameterName, parameterDescription, parameterIsStateOf, (Boolean) engine.get(parameterName));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return out;
	}

	
	/**
	 * Method to evaluate whether all required input parameters have been set.
	 * 
	 * @return True if all input parameters have been bound to the engine. Else false.
	 */
	protected boolean ready() {
		for (String parameter : input.keySet()) {
			if (engine.getBindings(ScriptContext.ENGINE_SCOPE).containsKey(parameter) == false) {
				return false;
			}
		}
		
		return true;
	}
}
