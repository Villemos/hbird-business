package org.hbird.exchange.heartbeat;

import org.hbird.exchange.core.ServiceSpecification;

public class HeartbeatServiceSpecification extends ServiceSpecification {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1243650293438351374L;

	{
		this.name = "Heartbeat Service";
		this.description = "The heartbeat service will at configurable intervals send a heartbeat message. The heartbeat message contains a The attribute 'issuedBy' will define the ";
		this.logicalIn.put("output", new Heartbeat());
	}
	
}
