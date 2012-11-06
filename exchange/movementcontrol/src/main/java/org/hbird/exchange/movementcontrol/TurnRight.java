package org.hbird.exchange.movementcontrol;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Parameter;

public class TurnRight extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2747536543216032446L;

	public TurnRight(String issuedBy) {
		super(issuedBy, "", "TurnLeft", "Turns the bot left. The Angle (in degrees) is set by the argument 'Angle'.");
		this.arguments.put("Angle", new Parameter(issuedBy, "Angle", "Argument", "Angle to turn", 45,  "Degree"));	
	}

	public TurnRight(String issuedBy, long executionTime) {
		super(issuedBy, "", "TurnLeft", "Turns the bot left. The Angle (in degrees) is set by the argument 'Angle'.");
		this.executionTime = executionTime;
	}
}
