package org.hbird.business.systemmonitoring.bean;

import java.lang.management.ManagementFactory;

import org.apache.camel.Handler;
import org.hbird.business.core.naming.Base;
import org.hbird.exchange.core.Parameter;

public class UptimeMonitor extends Monitor {

    public static final String PARAMETER_RELATIVE_NAME = "uptime";

    private final String parameterName;

    public UptimeMonitor(String componentId) {
        super(componentId);
        parameterName = naming.createAbsoluteName(Base.HOST, HostInfo.getHostName(), PARAMETER_RELATIVE_NAME);
    }

    @Handler
    public Parameter check() {
    	Parameter parameter = new Parameter(componentId + "/uptime", parameterName);
    	parameter.setDescription("JVM uptime.");
    	parameter.setValue(ManagementFactory.getRuntimeMXBean().getUptime());
    	parameter.setUnit("ms");
    	parameter.setIssuedBy(componentId);
    	
    	return parameter;
    }
}
