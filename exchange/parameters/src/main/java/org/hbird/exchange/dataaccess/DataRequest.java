package org.hbird.exchange.dataaccess;

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;

public class DataRequest extends Command {

	private static final long serialVersionUID = -5586555577244975789L;

	{
		arguments.put("type", new CommandArgument("type", "The type of the Named object.", "String", "", null, false));
		arguments.put("class", new CommandArgument("class", "The class of the Named object.", "", "", null, false));
		arguments.put("from", new CommandArgument("from", "The start of a range search on timestamp. Default to '*'.", "Long", "Seconds", null, false));
		arguments.put("to", new CommandArgument("to", "The end of a range search on timestamp. Default to '*'.", "Long", "Seconds", null, false));
		arguments.put("isStateOf", new CommandArgument("isStateOf", "Name of the object which this is the state of.", "String", "", null, false));
		arguments.put("names", new CommandArgument("names", "List of names of named objects to be retrieved.", "String", "", null, false));
		arguments.put("sortorder", new CommandArgument("sortorder", "The order in which the returned data should be returned.", "String", "", "ASC", true));
		arguments.put("sort", new CommandArgument("sort", "The sort field. Default is timestamp.", "String", "", "timestamp", true));
		arguments.put("rows", new CommandArgument("rows", "The maximum number of rows to be retrieved.", "Long", "", 1000, true));
	}
	
	public DataRequest(String issuedBy, String destination, String name, String description) {
		super(issuedBy, destination, name, description);
	}

	public void setRows(long rows) {
		addArgument("rows", rows);
	}

	public void setType(String type) {
		addArgument("type", type);
	}

	public void setClass(String clazz) {
		addArgument("class", clazz);
	}

	public void setFrom(long from) {
		addArgument("from", from);
	}

	public void setTo(long to) {
		addArgument("to", to);
	}

	public void setIsStateOf(String isStateOf) {
		addArgument("isStateOf", isStateOf);
	}

	public void addName(String name) {
		if (name != null) {
			if (arguments.get("names").value == null) {
				arguments.get("names").value = new ArrayList<String>();
			}
			((List<String>)arguments.get("names").value).add(name);
		}
	}

	public void addNames(List<String> names) {
		for (String name : names) {
			addName(name);
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
