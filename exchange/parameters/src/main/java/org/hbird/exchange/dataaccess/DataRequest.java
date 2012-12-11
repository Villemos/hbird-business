package org.hbird.exchange.dataaccess;

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.core.Command;

public class DataRequest extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5586555577244975789L;

	public DataRequest(String issuedBy, String destination, String name, String description) {
		super(issuedBy, destination, name, description);
	}
	
	public void setMaxSize(long maxsize) {
		arguments.put("maxsize", maxsize);
	}

	public void setType(String type) {
		arguments.put("type", type);
	}

	public void setClass(String clazz) {
		arguments.put("class", clazz);
	}

	public void setFrom(long from) {
		arguments.put("from", from);
	}

	public void setTo(long to) {
		arguments.put("to", to);
	}

	public void setIsStateOf(String isStateOf) {
		if (isStateOf != null) {
			arguments.put("isStateOf", isStateOf);
		}
	}

	public void addName(String name) {
		if (name != null) {
			if (arguments.containsKey("names") == false) {
				arguments.put("names", new ArrayList<String>());
			}
			((List<String>)arguments.get("names")).add(name);
		}
	}

	public void retrieveParameter(long from, long to, String parameter) {
		setClass("parameter");
		setFrom(from);
		setTo(to);
		addName(parameter);
	}

	public void retrieveState(long from, long to, String state, String isStateOf) {
		setClass("state");
		setFrom(from);
		setTo(to);
		addName(state);
		setIsStateOf(isStateOf);
	}
}
