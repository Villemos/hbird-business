package org.hbird.business.configurator;

import org.hbird.business.systemmonitoring.HeapMemoryUsageMonitor;
import org.hbird.business.systemmonitoring.ThreadCountMonitor;
import org.hbird.exchange.systemmonitoring.SystemMonitoringServiceSpecification;

public class SystemMonitorComponentBuilder extends ComponentBuilder {

	@Override
	public void doConfigure() {
		card.provides.add(new SystemMonitoringServiceSpecification());
		
		from("timer://systemmonitor?fixedRate=true&period=10000").multicast().to("seda:heap", "seda:thread");
		from("seda:heap").bean(new HeapMemoryUsageMonitor()).setHeader("name", simple("${in.body.name}")).to("activemq:topic:parameters");
		from("seda:thread").bean(new ThreadCountMonitor()).setHeader("name", simple("${in.body.name}")).to("activemq:topic:parameters");
	}
}
