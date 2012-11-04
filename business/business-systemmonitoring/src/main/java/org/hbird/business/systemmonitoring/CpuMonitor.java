package org.hbird.business.systemmonitoring;

import java.lang.management.ManagementFactory;
import java.net.UnknownHostException;

import org.hbird.exchange.core.Parameter;

public class CpuMonitor extends Monitor {

	public CpuMonitor(String componentId) {
		super(componentId);
	}
	
	/**
	 * Method to create a new instance of the memory parameter. The body of the 
	 * exchange will be updated.
	 * 
	 * @param exchange The exchange to hold the new value.
	 * @throws UnknownHostException 
	 */
	public Parameter check() {		
		return new Parameter(componentId, "Average CPU Usage", "MonitoredResource", "The average CPU usage the last minute.", ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage(), "Percentage/CPU");
	}	
}
