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
package org.hbird.business.timecorrelation;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

/**
 * Oversees script engines.
 * 
 * @author Mark Doyle
 * 
 */
public class ScriptEngineOverlord {

	private final ScriptEngineManager factory;

	public ScriptEngineOverlord() {
		factory = new ScriptEngineManager();
		getAvailableScriptingEngines();
	}

	private List<ScriptEngineFactory> getAvailableScriptingEngines() {
		List<ScriptEngineFactory> engineFactories = factory.getEngineFactories();
		for (ScriptEngineFactory factory : engineFactories) {
			System.out.println(factory.getEngineName());
		}
		return engineFactories;
	}

	public final ScriptEngine loadScriptEngine(String scriptEngineName) {
		// create a script engine manager
		ScriptEngineManager factory = new ScriptEngineManager();
		// create a JavaScript engine
		ScriptEngine engine = factory.getEngineByName(scriptEngineName);

		Object time1 = engine.get("time1");
		System.out.println(time1);
		return engine;
	}
}
