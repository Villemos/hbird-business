package org.hbird.business.heartbeat;

import java.util.Date;

import org.apache.camel.Exchange;
import org.hbird.exchange.heartbeat.Heartbeat;

public class Heart {

	protected String componentId = "unknown";
	
	protected long period = 10000;
	
	public Heart(String componentId, long period) {
		super();
		this.componentId = componentId;
		this.period = period;
	}

	public void process(Exchange exchange) {
		Date now = new Date();
		exchange.getIn().setBody(new Heartbeat(componentId, now.getTime(), now.getTime() + period));
	}
}
