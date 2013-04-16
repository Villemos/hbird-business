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

import org.hbird.business.core.StartablePart;
import org.hbird.business.scripting.bean.ScriptComponentDriver;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.scripting.ScriptExecutionRequest;

/**
 * @author Admin
 *
 */
public class ScriptComponent extends StartablePart {

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
    public ScriptComponent(String componentname, String scriptname, Named output, Map<String, String> bindings) {
        super(componentname, DESC_STRING, ScriptComponentDriver.class.getName());
        request = new ScriptExecutionRequest(scriptname, "", "javascript", output, bindings);
    }

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
    public ScriptComponent(String componentname, String scriptName, String script, Named output, Map<String, String> bindings) {
        super(componentname, DESC_STRING, ScriptComponentDriver.class.getName());

        request = new ScriptExecutionRequest(scriptName, script, "JavaScript", output, bindings);
    }

    /**
     * Constructor of a script engine request based on a script in a local script library.
     * 
     * The output of the script is a Parameter.
     * 
     * Use a short form of the binding definition to simplify Spring based configuration. The binding defines how
     * runtime
     * monitoring parameters of the system are mapped to script parameters. For example the script <li>var
     * value=RAW.asDouble() / 10 + MOD.asDouble();</li> Use the runtime parameter 'RAW' and 'MOD'. To define that the
     * runtime parameters 'para1' and 'para2' corresponds to
     * the script parameters RAW and MOD the following binding can be used <li>para1=RAW:para2=MOD</li> The script
     * component will read para1 and para2 from the system and use them to evaluate the script.
     * 
     * @param componentname The name of the script component to start. Used for monitoring of the scripting component.
     * @param scriptname The name of the script. The script will be lookedup in the script library (use the runtime
     *            property '-Dhbird.scriptlibrary=[library root]' to set the library folder)
     * @param paraName The name of the parameter to be created.
     * @param paraType The type of the parameter to be created. Must be a Number (double, float, int, long).
     * @param paraDescription The description of the parameter.
     * @param paraUnit The unit of the parameter.
     * @param bindings A String defining the mapping from runtime parameters to script parameters. Must be in the format
     *            '[def 1]:[def 2]:...' with [def] being '[runtime name]=[script name]'.
     */
    public ScriptComponent(String componentname, String scriptname, String paraName, String paraDescription, String paraUnit,
            String bindings) {
        super(componentname, DESC_STRING, ScriptComponentDriver.class.getName());

        Parameter output = new Parameter("", paraName, paraDescription, 0d, paraUnit);

        Map<String, String> inputBinding = new HashMap<String, String>();
        String[] bindingPairs = bindings.split(":");
        for (String binding : bindingPairs) {
            String[] entries = binding.split("=");
            inputBinding.put(entries[0], entries[1]);
        }

        request = new ScriptExecutionRequest(scriptname, "", "javascript", output, inputBinding);
    }

    /**
     * Constructor of a script engine request, based on a provided script.
     * 
     * The output of the script is a Parameter.
     * 
     * Use a short form of the binding definition to simplify Spring based configuration. The binding defines how
     * runtime
     * monitoring parameters of the system are mapped to script parameters. For example the script <li>var
     * value=RAW.asDouble() / 10 + MOD.asDouble();</li> Use the runtime parameter 'RAW' and 'MOD'. To define that the
     * runtime parameters 'para1' and 'para2' corresponds to
     * the script parameters RAW and MOD the following binding can be used <li>para1=RAW:para2=MOD</li> The script
     * component will read para1 and para2 from the system and use them to evaluate the script.
     * 
     * @param componentname The name of the script component to start. Used for monitoring of the scripting component.
     * @param scriptname The name of the script.
     * @param script The script.
     * @param format The format of the script. Typically 'JavaScript'.
     * @param paraName The name of the parameter to be created.
     * @param paraType The type of the parameter to be created. Must be a Number (double, float, int, long).
     * @param paraDescription The description of the parameter.
     * @param paraUnit The unit of the parameter.
     * @param bindings A String defining the mapping from runtime parameters to script parameters. Must be in the format
     *            '[def 1]:[def 2]:...' with [def] being '[runtime name]=[script name]'.
     */
    public ScriptComponent(String componentname, String scriptName, String script, String format, String paraName, 
            String paraDescription, String paraUnit, String bindings) {
        super(componentname, DESC_STRING, ScriptComponentDriver.class.getName());

        Parameter output = new Parameter("", paraName, paraDescription, 0d, paraUnit);

        Map<String, String> inputBinding = new HashMap<String, String>();
        String[] bindingPairs = bindings.split(":");
        for (String binding : bindingPairs) {
            String[] entries = binding.split("=");
            inputBinding.put(entries[0], entries[1]);
        }

        request = new ScriptExecutionRequest(scriptName, script, format, output, inputBinding);
    }

	public ScriptExecutionRequest getRequest() {
		return request;
	}

	public void setRequest(ScriptExecutionRequest request) {
		this.request = request;
	}
    
    
}
