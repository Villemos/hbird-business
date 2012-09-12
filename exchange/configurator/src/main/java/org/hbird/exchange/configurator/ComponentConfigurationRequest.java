package org.hbird.exchange.configurator;

import java.util.Map;

import org.hbird.exchange.core.Named;

public abstract class ComponentConfigurationRequest extends Named {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5607028481851891556L;

	/** The name of the configurator where the request should be executed. */
	public String configurator = "any";

	/** If set to 0, then the heartbeat is not started for this component. */
	public long heartbeatPeriod = 10000;	
	
	/** Configuration of all physical channels for input. */
	public Map<String, String> physicalIn = null;
	
	/** Configuration of all physical channels for output. */
	public Map<String, String> physicalOut = null;
}
