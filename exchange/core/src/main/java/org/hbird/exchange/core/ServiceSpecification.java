package org.hbird.exchange.core;

import java.util.HashMap;
import java.util.Map;


/** This class describes a service. The description is at the logical level; it
 *  describes the sequence of the exchange, the content received and send, but not
 *  the physical channels used to carry these.
 * 
 *  The description of the interaction is defined in the 'description' field. The
 *  description shall specify the interchange sequence and the conditions. It shall
 *  define which output results from which input. 
 *  
 *  The actual messages carried are defined in 'logicalIn' and 'logicalOut'. */
public abstract class ServiceSpecification extends Named {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8339268719443767904L;

	/** A list of inputs. The key is the logical name of the input channel. The value is the
	 *  message type that can be received. */
	public Map<String, Object> logicalIn = new HashMap<String, Object>();

	/** A list of inputs. The key is the logical name of the output channel. The value is the
	 *  message type that will be send. */
	public Map<String, Object> logicalOut = new HashMap<String, Object>();
}
