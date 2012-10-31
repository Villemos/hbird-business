package org.hbird.business.systemmonitoring;

import java.io.File;
import java.net.UnknownHostException;

import org.hbird.exchange.core.Parameter;

public class HarddiskMonitor extends Monitor {

	public HarddiskMonitor(String componentId) {
		super(componentId);
	}

	protected File rootFile = null;
	
	{
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			rootFile = new File("c://");
		}
		else {
			rootFile = new File("/");
		}
	}
	
	/**
	 * Method to create a new instance of the memory parameter. The body of the 
	 * exchange will be updated.
	 * 
	 * @param exchange The exchange to hold the new value.
	 * @throws UnknownHostException 
	 */
	public Parameter check() throws UnknownHostException {
		return new Parameter(componentId, "Free Harddisk", "The free harddisk space.", rootFile.getFreeSpace(), "Byte");
	}
}
