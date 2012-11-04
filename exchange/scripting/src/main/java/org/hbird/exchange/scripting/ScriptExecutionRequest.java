package org.hbird.exchange.scripting;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.Scheduled;


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
public class ScriptExecutionRequest implements Scheduled, Serializable {

	/** The unique UID */
	private static final long serialVersionUID = 1638200113394259171L;

	/** Definition of the return type. The binding to the type is based on convention. */
	public Named output = null;
	
	/** List of the parameter that the script needs as input. */
	public Map<String, String> inputBinding = new HashMap<String, String>();

	/** Can be used to reference a named script. The script executor will lookup the
	 * script. The field 'script' is in this case not used. */
	public String name = null;
	
	/** The actual script to be used. */
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
	public ScriptExecutionRequest(String name, String script, String format, Named output, Map<String, String> inputBinding) {
		this.name = name;
		this.script = script;
		this.format = format;
		this.output = output;
		this.inputBinding = inputBinding;
	}	

	public ScriptExecutionRequest() {}
}
