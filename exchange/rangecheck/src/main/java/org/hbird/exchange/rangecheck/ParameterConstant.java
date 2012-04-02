package org.hbird.exchange.rangecheck;


import java.io.Serializable;

import org.apache.log4j.Logger;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.StateParameter;


/**
 * This validation step validates that a specific parameter has a specific value
 * at the expected time.
 *
 */
public class ParameterConstant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2174528619539210196L;

	/** The class logger. */
	protected static Logger logger = Logger.getLogger(ParameterConstant.class);

	/** The upper value of the range. If NULL, then there is no upper range. */
	protected Parameter constValue = null;

	/** The state parameter to be created if range fails. */
	protected StateParameter state = null;
	
	public ParameterConstant(Parameter constValue) {
		this.constValue = constValue;
	}
}
