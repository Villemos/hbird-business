package org.hbird.exchange.configurator;

import java.io.Serializable;
import java.util.UUID;

public abstract class ComponentConfigurationRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5607028481851891556L;

	/** The ID to be assigned to the component. */
	public String id = UUID.randomUUID().toString();
	
	/** The name of the configurator where the request should be executed. */
	public String configurator;

	/** If set to 0, then the heartbeat is not started for this component. */
	public long heartbeatPeriod = 10000;	
}
