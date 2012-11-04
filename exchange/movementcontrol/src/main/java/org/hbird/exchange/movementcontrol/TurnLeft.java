package org.hbird.exchange.movementcontrol;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Parameter;

public class TurnLeft extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6985003473990743853L;	
	
	public TurnLeft() {};
	
	public TurnLeft(String issuedBy) {
		super(issuedBy, "TurnLeft", "Turns the bot left. The Angle (in degrees) is set by the argument 'Angle'.");
		this.arguments.add(new Parameter(issuedBy, "Angle", "Argument", "Angle to turn", 45,  "Degree"));	
	}

	public TurnLeft(String issuedBy, long executionTime) {
		super(issuedBy, "TurnLeft", "Turns the bot left. The Angle (in degrees) is set by the argument 'Angle'.");
		this.executionTime = executionTime;
	}
}