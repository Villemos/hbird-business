package org.hbird.exchange.scripting;

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
public class ScriptExecutionRequest extends Named implements Scheduled {

	/** The unique UID */
	private static final long serialVersionUID = 1638200113394259171L;
	
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
	public ScriptExecutionRequest(String issuedBy, String name, String description, String script, String format) {
		super(issuedBy, name, description, 0);
		this.script = script;
		this.format = format;
	}	

	/**
	 * Constructor, for a JavaScript (default format).
	 * 
	 * @param name The name of the script.
	 * @param description A description of the script.
	 * @param script The 'JavaScript' script.
	 */
	public ScriptExecutionRequest(String issuedBy, String name, String description, String script) {
		super(issuedBy, name, description, 0);
		this.script = script;
	}

	public ScriptExecutionRequest() {};	
}
