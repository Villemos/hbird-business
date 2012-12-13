package org.hbird.exchange.dataaccess;

import java.util.List;

import org.hbird.exchange.core.Parameter;

public class ParameterRequest extends DataRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2494842649084114764L;

	public ParameterRequest(String parameter, int rows) {
		super("Assembly", "ParameterArchive", "ParameterRequest", "A request for the latest value of a parameter");

		setClass(Parameter.class.getSimpleName());
		addName(parameter);

		addArgument("sort", "timestamp");
		addArgument("sortorder", "DESC");
		addArgument("rows", rows);
	}

	public ParameterRequest(String parameter, Long from, Long to) {
		super("Assembly", "ParameterArchive", "ParameterRequest", "A request for the latest value of a parameter");

		setClass(Parameter.class.getSimpleName());

		addName(parameter);

		if (from != null) {
			setFrom(from);
		}
		if (to != null) { 
			setTo(to);
		}

		addArgument("sort", "timestamp");
		addArgument("sortorder", "ASC");
	}

	public ParameterRequest(List<String> parameters, Long from, Long to) {
		super("Assembly", "ParameterArchive", "ParameterRequest", "A request for the latest value of a parameter");

		setClass(Parameter.class.getSimpleName());
		for (String parameter : parameters) {
			addName(parameter);
		}
		
		if (from != null) {
			setFrom(from);
		}
		if (to != null) { 
			setTo(to);
		}

		addArgument("sort", "timestamp");
		addArgument("sortorder", "ASC");
	}
}
