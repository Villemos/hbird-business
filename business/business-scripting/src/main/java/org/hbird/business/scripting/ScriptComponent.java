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
package org.hbird.business.scripting;

import java.util.HashMap;
import java.util.Map;

import org.hbird.business.core.StartableEntity;
import org.hbird.business.scripting.bean.ScriptComponentDriver;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.scripting.ScriptExecutionRequest;

/**
 * @author Admin
 *
 */
public class ScriptComponent extends StartableEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -78671649806617847L;
	
	public static final String DESC_STRING = "Component executing a script.";
	
	protected ScriptExecutionRequest request = null;
	
    /**
     * Constructor of a script engine request based on a script in a local script library.
     * 
     * The output is a named object. This is frequently a Parameter, but can be any named object. The script can thus
     * generate not a parameter, but a Command, or a Task or any other Named subtype. The script can access and change
     * the
     * attributes of the named object.
     * 
     * @param componentname The name of the script component to start. Used for monitoring of the scripting component.
     * @param scriptname The name of the script. The script will be lookedup in the script library (use the runtime
     *            property '-Dhbird.scriptlibrary=[library root]' to set the library folder)
     * @param output The object that is the output of the script. The script may manipulate some or all of the values,
     *            i.e. set the value based on input parameters.
     * @param bindings A Map defining the bindings between monitoring parameters of the system and the parameters in the
     *            script.
     */
    public ScriptComponent(String ID, String name) {
        super(ID, name);
        setDriverName(ScriptComponentDriver.class.getName());
        request = new ScriptExecutionRequest();
    }

	public ScriptExecutionRequest getRequest() {
		return request;
	}

	public void setRequest(ScriptExecutionRequest request) {
		this.request = request;
	}
    
	public void setBinding(String bindings) {
        Map<String, String> inputBinding = new HashMap<String, String>();
        String[] bindingPairs = bindings.split(":");
        for (String binding : bindingPairs) {
            String[] entries = binding.split("=");
            inputBinding.put(entries[0], entries[1]);
        }

        request.inputBinding = inputBinding;
	}
	
	public void setReturnType(EntityInstance obj) {
		request.output = obj;
	}
	
	public void setFormat(String format) {
		request.format = format;
	}
	
	public void setScript(String script) {
		request.script = script;
	}
	
	public void setScriptName(String scriptName) {
		request.name = scriptName;
	}
}
