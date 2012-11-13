package org.hbird.exchange.tasking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Named;

public class SendCommand extends Task {

	public SendCommand(String issuedBy, String name, String description, long executionDelay, Command command) {
		super(issuedBy, name, description, executionDelay);
		this.command = command;
	}

	public SendCommand(String issuedBy, String name, String description, long executionDelay, long repeat, Command command) {
		super(issuedBy, name, description, executionDelay);
		this.command = command;
		this.repeat = repeat;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3795605171483125337L;
	
	/** The class logger. */
	protected static Logger LOG = Logger.getLogger(SendCommand.class);

	protected Command command = null;
	
	/**
	 * Method that will send the message to the parameter query. 
	 * 
	 * @param arg0 The exchange to be send.
	 */
	public List<Named> execute() {
		LOG.info("Sending command '" + command.getName() + "'.");
		
		command.setExecutionTime((new Date().getTime()));
		
		/** Send the preconfigured parameter. */
		List<Named> returnValue = new ArrayList<Named>();
		returnValue.add((Named) command);
		return returnValue;
	}
}
