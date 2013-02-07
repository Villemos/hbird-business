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
		setFrom(from);
		setTo(to);
	}

	public ParameterRequest(String parameter, Long from, Long to, int rows) {
		super("Assembly", "ParameterArchive", "ParameterRequest", "A request for the latest value of a parameter");

		setClass(Parameter.class.getSimpleName());
		addName(parameter);
		setFrom(from);
		setTo(to);
		addArgument("rows", rows);
	}

	public ParameterRequest(List<String> parameters, int rows) {
		super("Assembly", "ParameterArchive", "ParameterRequest", "A request for the latest value of a parameter");

		setClass(Parameter.class.getSimpleName());
		addName(parameters);
		addArgument("rows", rows);
	}

	public ParameterRequest(List<String> parameters, Long from, Long to) {
		super("Assembly", "ParameterArchive", "ParameterRequest", "A request for the latest value of a parameter");

		setClass(Parameter.class.getSimpleName());
		addName(parameters);
		setFrom(from);
		setTo(to);
	}

	public ParameterRequest(List<String> parameters, Long from, Long to, int rows) {
		super("Assembly", "ParameterArchive", "ParameterRequest", "A request for the latest value of a parameter");

		setClass(Parameter.class.getSimpleName());
		addName(parameters);
		setFrom(from);
		setTo(to);
		addArgument("rows", rows);
	}
}
