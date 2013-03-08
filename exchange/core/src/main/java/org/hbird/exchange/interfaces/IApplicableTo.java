package org.hbird.exchange.interfaces;

import org.hbird.exchange.core.NamedInstanceIdentifier;

public interface IApplicableTo {

	/**
	 * Method to retrieve the identifier of the Named object that this Named object is retrieved from.
	 * 
	 * @return
	 */
	public NamedInstanceIdentifier applicableTo(); 
}
