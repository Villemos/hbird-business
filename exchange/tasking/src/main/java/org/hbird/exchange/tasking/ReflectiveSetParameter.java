package org.hbird.exchange.tasking;

import org.hbird.exchange.core.Parameter;

/**
 * A reflective set parameter sets the value of a parameter based on an 
 * argument value of a command defined when the command was scheduled. 
 * This can be used to reflect a value based on the argument of a command, 
 * i.e. ensure that a given value has changed as expected.
 */
public class ReflectiveSetParameter extends SetParameter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2247014379748682184L;

	/** The argument that this parameter should reflect. */
	protected Parameter argument;

	/**
	 * Constructor of a reflective parameter.
	 * 
	 * @param name The name of this task
	 * @param description A description of this task
	 * @param executionTime The execution time of this task.
	 * @param parameter The parameter to be set by this task
	 * @param argument The argument that the parameter set by this task should reflect. 
	 */
	public ReflectiveSetParameter(String issuedBy, String name, String description, long executionTime, Parameter parameter, Parameter argument) {
		super(issuedBy, name, description, executionTime, parameter);
		this.argument = argument;
	}
	
	/**
	 * Method that will send the message to the parameter query. 
	 * 
	 * @param arg0 The exchange to be send.
	 */
	public Object execute() {
		logger.info("Setting reflective parameter '" + parameter.getName() + "' to value '" + parameter.getValue().toString() + "'.");
		
		/** Send the parameter. */
		parameter.setValue(argument.getValue());
		return parameter;
	}

}
