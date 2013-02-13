package org.hbird.exchange.core;

import java.io.Serializable;

public class NamedInstanceIdentifier implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2768378707513305322L;

	public String name;
	public long timestamp;	
	public String type;
	
	public NamedInstanceIdentifier(String name, long timestamp, String type) {
		super();
		this.name = name;
		this.timestamp = timestamp;
		this.type = type;
	}
}
