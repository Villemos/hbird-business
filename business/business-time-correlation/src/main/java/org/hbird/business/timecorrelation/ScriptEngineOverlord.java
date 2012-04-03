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
