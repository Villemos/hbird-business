package org.hbird.business.systemmonitoring;

import org.hbird.business.core.naming.Base;
import org.hbird.exchange.core.Label;

public class OsMonitor extends Monitor {

    public static final String PARAMETER_RELATIVE_NAME = "OS info";

    private final String parameterName;

    public OsMonitor(String componentId) {
        super(componentId);
        parameterName = naming.createAbsoluteName(Base.HOST, HostInfo.getHostName(), PARAMETER_RELATIVE_NAME);
    }

    public Label check() {
        return new Label(componentId, parameterName, "MonitoredResource", "OS information",
                HostInfo.getHostInfo());
    }
}
