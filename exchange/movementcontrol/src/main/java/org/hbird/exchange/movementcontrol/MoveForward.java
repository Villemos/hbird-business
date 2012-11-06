package org.hbird.exchange.movementcontrol;


import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Parameter;

public class MoveForward extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8419315289862341607L;

	public MoveForward(String issuedBy) {
		super(issuedBy, "", "MoveForward", "Move the bot forwards. The distance (in meter) is set by the argument 'Distance'.");
		arguments.put("Distance", new Parameter(issuedBy, "Distance", "Argument", "The distance in meter.", null, "Meter"));
	}

	public MoveForward(String issuedBy, long executionTime) {
		super(issuedBy, "", "MoveForward", "Move the bot forwards. The distance (in meter) is set by the argument 'Distance'.");
		this.executionTime = executionTime;
	}

}
