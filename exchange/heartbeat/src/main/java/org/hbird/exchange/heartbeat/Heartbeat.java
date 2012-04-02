package org.hbird.exchange.heartbeat;

import org.hbird.exchange.core.Named;

public class Heartbeat extends Named {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2792762576624569229L;

	{
		this.name = "Heartbeat";
		this.description = "A heartbeat signal. The component was alive as this signal was issued.";
	}

	protected long timestamp = 0;
	
	protected long nextBeat = 10000;
	
	public Heartbeat() {};
	
	public Heartbeat(String issuedBy, long timestamp, long nextBeat) {
		super(issuedBy);
		
		this.timestamp = timestamp;
		this.nextBeat = nextBeat;
	}
}
