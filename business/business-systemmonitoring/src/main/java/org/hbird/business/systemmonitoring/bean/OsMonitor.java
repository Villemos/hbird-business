package org.hbird.business.systemmonitoring.bean;

import org.apache.camel.Handler;
import org.hbird.business.api.IdBuilder;
import org.hbird.exchange.core.Label;

public class OsMonitor extends Monitor {

    public static final String PARAMETER_RELATIVE_NAME = "OS Info";
    public static final String DESCRIPTION_OS_INFORMATION = "OS information";

    public OsMonitor(String componentId, IdBuilder idBuilder) {
        super(componentId, idBuilder);
    }

    @Handler
    public Label check() {
        Label label = new Label(idBuilder.buildID(componentId, PARAMETER_RELATIVE_NAME), PARAMETER_RELATIVE_NAME);
        label.setDescription(DESCRIPTION_OS_INFORMATION);
        label.setValue(HostInfo.getHostInfo());
        label.setIssuedBy(componentId);
        label.setApplicableTo(componentId);
        return label;
    }
}
