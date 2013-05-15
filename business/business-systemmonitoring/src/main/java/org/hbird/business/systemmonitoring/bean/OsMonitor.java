package org.hbird.business.systemmonitoring.bean;

import org.apache.camel.Handler;
import org.hbird.exchange.core.Label;

public class OsMonitor extends Monitor {

    public static final String PARAMETER_RELATIVE_NAME = "OS Info";

    public OsMonitor(String componentId) {
        super(componentId);
    }

    @Handler
    public Label check() {
        Label label = new Label(naming.buildId(componentId, PARAMETER_RELATIVE_NAME), PARAMETER_RELATIVE_NAME);
        label.setDescription("OS information");
        label.setValue(HostInfo.getHostInfo());
    	label.setIssuedBy(componentId);
        return label;
    }
}
