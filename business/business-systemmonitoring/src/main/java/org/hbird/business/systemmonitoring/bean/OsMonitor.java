package org.hbird.business.systemmonitoring.bean;

import org.apache.camel.Handler;
import org.hbird.business.core.naming.Base;
import org.hbird.exchange.core.Label;

public class OsMonitor extends Monitor {

	public static final String PARAMETER_RELATIVE_NAME = "OS info";

	private final String parameterName;

	public OsMonitor(String componentId) {
		super(componentId);
		parameterName = naming.createAbsoluteName(Base.HOST, HostInfo.getHostName(), PARAMETER_RELATIVE_NAME);
	}

	@Handler
	public Label check() {

		Label label = new Label(componentId + "/osMonitor", parameterName);
		label.setDescription("OS information");
		label.setValue(HostInfo.getHostInfo());

		return label;
	}
}
