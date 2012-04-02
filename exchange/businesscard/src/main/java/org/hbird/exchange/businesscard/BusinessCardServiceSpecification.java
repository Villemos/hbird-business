package org.hbird.exchange.businesscard;

import org.hbird.exchange.core.ServiceSpecification;

public class BusinessCardServiceSpecification extends ServiceSpecification {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5715500672199194135L;
	
	{
		this.name = "Business Card Service";
		
		this.description = "This service takes as input in a Greeting Message (Business Card specialization) and in return provides its own business card. The service is intended to be used to query a running system what is already running and which services they provide.";
		
		this.logicalIn.put("input", new Greeting());
		this.logicalIn.put("output", new BusinessCard());
	}
	
}
