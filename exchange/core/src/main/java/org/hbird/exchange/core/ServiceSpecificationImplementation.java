package org.hbird.exchange.core;

import java.util.HashMap;
import java.util.Map;

/** This class represents a specific implementation of a service. Where as the 
 * service specification is logical this defines an actual deployment of a service.
 * 
 * The physicalIn and physicalOut are directly related to the service specification. */
public class ServiceSpecificationImplementation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7930881881438003048L;

	protected ServiceSpecification specification = null;
	
	/** A list of inputs. The key is the logical name of the input channel. The value is the
	 *  message type that can be received. */
	Map<String, String> physicalIn = new HashMap<String, String>();

	/** A list of inputs. The key is the logical name of the output channel. The value is the
	 *  message type that will be send. */
	Map<String, String> physicalOut = new HashMap<String, String>();
}

