package org.hbird.exchange.configurator;

public class LimitComponentRequest extends ComponentConfigurationRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8047192684233318282L;

	
	
	public LimitComponentRequest(String parameter, String name, String description, String expression) {
		super();
		this.parameter = parameter;
		this.name = name;
		this.description = description;
		this.expression = expression;
	}

	public String parameter;
	public String name;
	public String description;
	
	/** The expresssion in the format '<>[value]' */
	public String expression;

}
