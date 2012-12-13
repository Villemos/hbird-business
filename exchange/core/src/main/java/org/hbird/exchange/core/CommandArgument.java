package org.hbird.exchange.core;

import java.io.Serializable;

public class CommandArgument implements Serializable {

	private static final long serialVersionUID = 8198716970891183855L;

	public String name;
	public String description;
	public String type;
	public String unit;
	public Object value = null;
	public Boolean mandatory = true;

	public CommandArgument(String name, String description, String type, String unit, Object value, Boolean mandatory) {
		this.name = name;
		this.description = description;
		this.type = type;
		this.unit = unit;
		this.value = value;
		this.mandatory = mandatory;
	}
}
