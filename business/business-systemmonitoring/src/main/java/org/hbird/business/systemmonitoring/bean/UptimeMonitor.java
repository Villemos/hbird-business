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
        return new Parameter(componentId, parameterName, "JVM uptime",
                ManagementFactory.getRuntimeMXBean().getUptime(), "ms");
    }
}
