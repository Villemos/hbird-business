package org.hbird.exchange.interfaces;

public interface IApplicableTo {

	/**
	 * Method to retrieve the identifier of the Named object that this Named object is retrieved from.
	 * 
	 * @return String in the format '[name]:[type]:[timestamp]'
	 */
	public String applicableTo(); 
}
