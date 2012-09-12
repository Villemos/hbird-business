package org.hbird.exchange.businesscard;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.ServiceSpecification;



public class BusinessCard extends Named {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3066879678593848704L;
	
	public BusinessCard() {};
	
	public BusinessCard(String issuedBy, String component) {
		super(issuedBy);
		this.component = component;
		try {
			this.host = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	{
		this.name = "BusinessCard";
		this.description = "A business card describing this component.";
	}
	
	public String host;
	
	public String component;
	
	/** A list of the services this provider provides. */
	public List<ServiceSpecification> provides = new ArrayList<ServiceSpecification>();

	/** A list of the services this provider requires. */
	public List<ServiceSpecification> requires = new ArrayList<ServiceSpecification>();
}
