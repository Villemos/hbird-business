/**
 * Licensed to the Hummingbird Foundation (HF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The HF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
		arguments.put("includeStates", new CommandArgument("getStates", "Flag defining that all states applicable to the named objects should also be retrieved", "Boolean", "", false, true));		
		arguments.put("sortorder", new CommandArgument("sortorder", "The order in which the returned data should be returned.", "String", "", "ASC", true));
		arguments.put("sort", new CommandArgument("sort", "The sort field. Default is timestamp.", "String", "", "timestamp", true));
		arguments.put("rows", new CommandArgument("rows", "The maximum number of rows to be retrieved.", "Integer", "", 1000, true));
		arguments.put("initialization", new CommandArgument("initialization", "If set to true, then the value below the 'to' time of each named object matching the search criterions will be retrieved.", "Boolean", "", false, true));
	}
	
	public DataRequest(String issuedBy, String destination) {
		super(issuedBy, destination, "DataRequest", "A generic request to the archive for data.");
	}

	public DataRequest(String issuedBy, String destination, String name, String description) {
		super(issuedBy, destination, name, description);
	}

	public void setRows(int rows) {
		addArgument("rows", rows);
	}

	public void setType(String type) {
		addArgument("type", type);
	}

	public void setClass(String clazz) {
		addArgument("class", clazz);
	}

	public void setFrom(Long from) {
		if (from != null) {
			addArgument("from", from);
		}
	}

	public void setTo(Long to) {
		if (to != null) {
			addArgument("to", to);
		}
	}

	public void setIsStateOf(String isStateOf) {
		addArgument("isStateOf", isStateOf);
	}

	public void setIsInitialization(Boolean isInitialization) {
		addArgument("initialization", isInitialization);
	}

	public void addName(List<String> names) {
		for (String name : names) {
			addName(name);
		}
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
	
	public void setIncludeStates(boolean includeStates) {
		addArgument("includeStates", includeStates);
	}

	public boolean shallIncludeStates() {
		return (Boolean) getArgument("includeStates");
	}

	public Integer getRows() {
		return (Integer) getArgument("rows");
	}

	public Long getFrom() {
		return (Long) getArgument("from");
	}

	public Long getTo() {
		return (Long) getArgument("to");
	}
}
