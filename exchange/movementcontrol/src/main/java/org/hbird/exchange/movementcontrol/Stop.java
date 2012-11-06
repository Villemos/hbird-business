package org.hbird.exchange.movementcontrol;

import org.hbird.exchange.core.Command;

public class Stop extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7051648798211264707L;

	public Stop(String issuedBy) {
		super(issuedBy, "", "Stop", "Stop all movement.");
	}

	public Stop(String issuedBy, long executionTime) {
		super(issuedBy, "", "Stop", "Stop all movement.");
		this.executionTime = executionTime;
	}
}
