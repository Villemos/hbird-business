package org.hbird.business.systemmonitoring.bean;

import java.lang.management.ManagementFactory;

import org.apache.camel.Handler;
import org.hbird.business.api.IdBuilder;
import org.hbird.exchange.core.Parameter;

public class UptimeMonitor extends Monitor {

    public static final String PARAMETER_RELATIVE_NAME = "Uptime";
    public static final String DESCRIPTION_JVM_UPTIME = "JVM uptime.";

    public UptimeMonitor(String componentId, IdBuilder idBuilder) {
        super(componentId, idBuilder);
    }

    @Handler
    public Parameter check() {
        Parameter parameter = new Parameter(idBuilder.buildID(componentId, PARAMETER_RELATIVE_NAME), PARAMETER_RELATIVE_NAME);
        parameter.setDescription(DESCRIPTION_JVM_UPTIME);
        parameter.setValue(ManagementFactory.getRuntimeMXBean().getUptime());
        parameter.setUnit(UNIT_MILLISECONDS);
        parameter.setIssuedBy(componentId);
        parameter.setApplicableTo(componentId);
        return parameter;
    }
}
