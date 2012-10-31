package org.hbird.business.configurator;

import org.hbird.business.systemmonitoring.CpuMonitor;
import org.hbird.business.systemmonitoring.HarddiskMonitor;
import org.hbird.business.systemmonitoring.HeapMemoryUsageMonitor;
import org.hbird.business.systemmonitoring.ThreadCountMonitor;
import org.hbird.exchange.systemmonitoring.SystemMonitoringServiceSpecification;

public class SystemMonitorComponentBuilder extends ComponentBuilder {

	@Override
	public void doConfigure() {
		card.provides.add(new SystemMonitoringServiceSpecification());
		
		from("timer://systemmonitor?fixedRate=true&period=10000").multicast().to("seda:heap", "seda:thread", "seda:cpu", "seda:harddisk");
		from("seda:heap").bean(new HeapMemoryUsageMonitor(request.getName())).setHeader("name", simple("${in.body.name}")).to("activemq:topic:parameters");
		from("seda:thread").bean(new ThreadCountMonitor(request.getName())).setHeader("name", simple("${in.body.name}")).to("activemq:topic:parameters");
		from("seda:cpu").bean(new CpuMonitor(request.getName())).setHeader("name", simple("${in.body.name}")).to("activemq:topic:parameters");
		from("seda:harddisk").bean(new HarddiskMonitor(request.getName())).setHeader("name", simple("${in.body.name}")).to("activemq:topic:parameters");
	}
}
