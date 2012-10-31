package org.hbird.business.configurator;

import org.apache.camel.builder.RouteBuilder;
import org.hbird.business.heartbeat.Heart;

public class HeartbeatRouteBuilder extends RouteBuilder {

	protected String name;
	protected long period;
	
	public HeartbeatRouteBuilder(String name, long period) {
		this.name = name;
		this.period = period;
	}
	
	@Override
	public void configure() throws Exception {
		Heart heart = new Heart(name, period);
		
		from("timer:heartbeat_" + name + "?fixedRate=true&period=" + period)
	     .setBody(bean(heart))
	     .to("activemq:topic:system");						
	}
}
