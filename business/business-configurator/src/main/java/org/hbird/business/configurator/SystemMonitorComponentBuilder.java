package org.hbird.business.configurator;

import org.hbird.business.systemmonitoring.CpuMonitor;
import org.hbird.business.systemmonitoring.HarddiskMonitor;
import org.hbird.business.systemmonitoring.HeapMemoryUsageMonitor;
import org.hbird.business.systemmonitoring.ThreadCountMonitor;

public class SystemMonitorComponentBuilder extends ComponentBuilder {

	@Override
	public void doConfigure() {
		from("timer://systemmonitor?fixedRate=true&period=10000").multicast().to("seda:heap", "seda:thread", "seda:cpu", "seda:harddisk");

		from("seda:heap").bean(new HeapMemoryUsageMonitor(request.getName())).to("seda:out");
		from("seda:thread").bean(new ThreadCountMonitor(request.getName())).to("seda:out");
		from("seda:cpu").bean(new CpuMonitor(request.getName())).to("seda:out");
		from("seda:harddisk").bean(new HarddiskMonitor(request.getName())).to("seda:out");

		from("seda:out")
		.setHeader("name", simple("${in.body.name}"))
		.setHeader("issuedBy", simple("${in.body.issuedBy}"))
		.setHeader("type", simple("${in.body.type}")) 
		.to("activemq:topic:monitoringdata");
		
	}
}
