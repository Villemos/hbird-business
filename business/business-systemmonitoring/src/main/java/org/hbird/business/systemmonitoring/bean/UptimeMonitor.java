package org.hbird.business.systemmonitoring.bean;

import java.lang.management.ManagementFactory;

import org.apache.camel.Handler;
import org.hbird.exchange.core.Parameter;

public class UptimeMonitor extends Monitor {

    public static final String PARAMETER_RELATIVE_NAME = "Uptime";

    public UptimeMonitor(String componentId) {
        super(componentId);
    }

    @Handler
    public Parameter check() {
        Parameter parameter = new Parameter(naming.buildId(componentId, PARAMETER_RELATIVE_NAME), PARAMETER_RELATIVE_NAME);
        parameter.setDescription("JVM uptime.");
        parameter.setValue(ManagementFactory.getRuntimeMXBean().getUptime());
        parameter.setUnit("ms");
        return parameter;
    }
}
