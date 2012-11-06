package org.hbird.exchange.movementcontrol;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Parameter;


public class MoveBackward extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3800714830282328882L;

	public MoveBackward(String issuedBy) {
		super(issuedBy, "", "MoveBackward", "Move the bot backwards. The distance (in meter) is set by the argument 'Distance'.");
		arguments.put("Distance", new Parameter(issuedBy, "Distance", "Argument", "The distance in meter.", null, "Meter"));
	}

	public MoveBackward(String issuedBy, long executionTime) {
		super(issuedBy, "", "MoveBackward", "Move the bot backwards. The distance (in meter) is set by the argument 'Distance'.");
		this.executionTime = executionTime;
	}
}
