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
package org.hbird.exchange.scripting;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.hbird.exchange.core.Issued;


/**
 * A calibration request. The request contains a script in some scripting language. The
 * script may by itself contact the underlying transport layer, using for example
 * stomp (http://stomp.codehaus.org/). The script can thus by itself request and 
 * receive data for the processing.
 * 
 * The format of the script is defined by the the 'format' attribute. This can be 
 * used to create the script execution engine and run the script.
 *
 */
public class ScriptExecutionRequest implements Serializable {

	/** The unique UID */
	private static final long serialVersionUID = 1638200113394259171L;

	/** Definition of the return type. The binding to the type is based on convention. */
	public Issued output = null;
	
	/** List of the parameter that the script needs as input. */
	public Map<String, String> inputBinding = new HashMap<String, String>();

	/** Can be used to reference a named script. The script executor will lookup the
	 * script. The field 'script' is in this case not used. */
	public String name = null;
	
	/** The actual script to be used. If set, then the script name is not used to find a library script. */
	public String script;
	
	/** The format of the script. Default value is 'JavaScript'. */
	public String format = "JavaScript";

	
	/**
	 * Constructor.
	 * 
	 * @param name The name of the script.
	 * @param description A description of the script.
	 * @param script The actual script.
	 * @param format The name of the script format, such as 'JavaScript'
	 */
	public ScriptExecutionRequest(String name, String script, String format, Issued output, Map<String, String> inputBinding) {
		this.name = name;
		this.script = script;
		this.format = format;
		this.output = output;
		this.inputBinding = inputBinding;
	}	

	public ScriptExecutionRequest(String name, String script, String format, Issued output, String input, String binding) {
		this.name = name;
		this.script = script;
		this.format = format;
		this.output = output;
		this.inputBinding = new HashMap<String, String>();
		this.inputBinding.put(input, binding);
	}	

	public ScriptExecutionRequest() {}
}
