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

import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.script.*;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.scripting.ScriptExecutionRequest;

/** 
 * The script executor will evaluate scripts. 
 * 
 * The executor receives the script as a request.
 */
public class ScriptExecutor {

	private static org.apache.log4j.Logger LOG = Logger.getLogger(ScriptExecutor.class);

	/** The configuration request defining the script to be executed. */
	protected ScriptExecutionRequest request = null;

	/** The script engine that will execute the script.*/
	protected ScriptEngine engine = null;

	public ScriptExecutor(ScriptExecutionRequest request) {
		this.request = request;

		ScriptEngineManager factory = new ScriptEngineManager();
		engine = factory.getEngineByName(request.format); 	

		engine.put("output", request.output);

		/** */
		if (request.script == null || request.script.equals("") == true) {

			/** Default root is the the resource folder of the current project. */
			String root = "src/main/resources/library/";
			if (System.getProperty("hbird.scriptlibrary") != null) {
				root = System.getProperty("hbird.scriptlibrary");
				if (root.endsWith("/") == false) {
					root += "/";
				}
			}			

			File file = new File(root + request.name + ".js");
			if (file.exists() == false) {
				LOG.error("Failed to find script file '" + file.getAbsolutePath() + "'.");
				LOG.error("Use the runtime system property '-Dhbird.scriptlibrary=[path]' to point to the script library. Script will not be evaluated.");
			}
			else {
				try {
					request.script = FileUtils.readFileToString(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Method for receiving an input parameter and evaluating the value of the script. The
	 * method will only evaluate the script if all input parameters have been received. 
	 * 
	 * @param in A parameter required to evaluate the script.
	 * @return A parameter holding the return value of the script. May be null if the script could not evaluate.
	 */
	public Named calculate(Named in) {

		LOG.debug("Script '" + request.name + "': received dependent object '" + in.getName() + "' with timestamp '" + in.getTimestamp() + "'.");

		Named returnValue = null;

		try {
			/** Bind the parameter to the script. */
			if (request.inputBinding.get(in.getName()) == null) {
				LOG.error("Script '" + request.name + "': Error in binding of parameters. The runtime parameter '" + in.getName() + "' was received, but has not been bound to any script parameter.");
			}
			else {
				engine.put(request.inputBinding.get(in.getName()), in);

				/** Check if a binding has been created for all needed parameters. */
				if (ready()) {
					LOG.info("Script '" + request.name + "': All dependent values set. Evaluating script that will create object '" + request.output.getName() + "'");
					engine.eval(request.script);

					/** The request output object has been bound to the engine. The script can
					 * thus update it directly. Only the timestamp and uuid should also be set. */
					returnValue = request.output.instance();
					returnValue.setTimestamp(in.getTimestamp());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnValue;
	}


	/**
	 * Method to evaluate whether all required input parameters have been set.
	 * 
	 * @return True if all input parameters have been bound to the engine. Else false.
	 */
	protected boolean ready() {
		for (String parameter : request.inputBinding.values()) {
			if (engine.getBindings(ScriptContext.ENGINE_SCOPE).containsKey(parameter) == false) {
				LOG.debug("Script '" + request.name + "' Still missing dependency '" + parameter + "'.");
				return false;
			}
		}

		return true;
	}

	public Set<String> getDependencies() {
		return request.inputBinding.keySet();
	}
}
