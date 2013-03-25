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

import static org.hbird.exchange.dataaccess.Arguments.CLASS;
import static org.hbird.exchange.dataaccess.Arguments.DERIVED_FROM;
import static org.hbird.exchange.dataaccess.Arguments.FROM;
import static org.hbird.exchange.dataaccess.Arguments.INCLUDE_STATES;
import static org.hbird.exchange.dataaccess.Arguments.INITIALIZATION;
import static org.hbird.exchange.dataaccess.Arguments.IS_STATE_OF;
import static org.hbird.exchange.dataaccess.Arguments.NAMES;
import static org.hbird.exchange.dataaccess.Arguments.ROWS;
import static org.hbird.exchange.dataaccess.Arguments.SORT;
import static org.hbird.exchange.dataaccess.Arguments.SORT_ORDER;
import static org.hbird.exchange.dataaccess.Arguments.TO;
import static org.hbird.exchange.dataaccess.Arguments.TYPE;
import static org.hbird.exchange.dataaccess.Arguments.create;

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.core.NamedInstanceIdentifier;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;

public class DataRequest extends Command {

	public static final String DESCRIPTION = "A generic request to the archive for data.";

	private static final long serialVersionUID = 344371026722929043L;

	public DataRequest(String issuedBy, String destination) {
		super(issuedBy, destination, DataRequest.class.getSimpleName(), DESCRIPTION);
	}

	public DataRequest(String issuedBy, String destination, String name, String description) {
		super(issuedBy, destination, name, description);
	}

	/**
	 * @see org.hbird.exchange.core.Command#getArgumentDefinitions()
	 */
	@Override
	protected List<CommandArgument> getArgumentDefinitions(List<CommandArgument> args) {
		args.add(create(TYPE));
		args.add(create(CLASS));
		args.add(create(FROM));
		args.add(create(TO));
		args.add(create(IS_STATE_OF));
		args.add(create(NAMES));
		args.add(create(INCLUDE_STATES));
		args.add(create(SORT_ORDER));
		args.add(create(SORT));
		args.add(create(ROWS));
		args.add(create(INITIALIZATION));
		args.add(create(DERIVED_FROM));
		return args;
	}

	public void setRows(int rows) {
		setArgumentValue(StandardArguments.ROWS, rows);
	}

	@Override
	public void setType(String type) {
		setArgumentValue(StandardArguments.TYPE, type);
	}

	public void setClass(String clazz) {
		setArgumentValue(StandardArguments.CLASS, clazz);
	}

	public void setFrom(Long from) {
		if (from != null) {
			setArgumentValue(StandardArguments.FROM, from);
		}
	}

	public void setTo(Long to) {
		if (to != null) {
			setArgumentValue(StandardArguments.TO, to);
		}
	}

	public void setIsStateOf(String isStateOf) {
		setArgumentValue(StandardArguments.IS_STATE_OF, isStateOf);
	}

	public void setIsInitialization(Boolean isInitialization) {
		setArgumentValue(StandardArguments.INITIALIZATION, isInitialization);
	}

	public void addName(List<String> names) {
		for (String name : names) {
			addName(name);
		}
	}

	public void addName(String name) {
		if (name != null) {
			if (!hasArgumentValue(StandardArguments.NAMES)) {
				setArgumentValue(StandardArguments.NAMES, new ArrayList<String>());
			}
			List<String> names = getArgumentValue(StandardArguments.NAMES, List.class);
			names.add(name);
		}
	}

	public void addNames(List<String> names) {
		if (names != null) {
			for (String name : names) {
				addName(name);
			}
		}
	}

	public void retrieveParameter(long from, long to, String parameter) {
		setClass(Parameter.class.getSimpleName());
		setFrom(from);
		setTo(to);
		addName(parameter);
	}

	public void retrieveState(long from, long to, String state, String isStateOf) {
		setClass(State.class.getSimpleName());
		setFrom(from);
		setTo(to);
		addName(state);
		setIsStateOf(isStateOf);
	}

	public void setIncludeStates(boolean includeStates) {
		setArgumentValue(StandardArguments.INCLUDE_STATES, includeStates);
	}

	public boolean shallIncludeStates() {
		return getArgumentValue(StandardArguments.INCLUDE_STATES, Boolean.class);
	}

	public Integer getRows() {
		return getArgumentValue(StandardArguments.ROWS, Integer.class);
	}

	public Long getFrom() {
		return getArgumentValue(StandardArguments.FROM, Long.class);
	}

	public Long getTo() {
		return getArgumentValue(StandardArguments.TO, Long.class);
	}

	public void setDerivedFrom(String name, long timestamp, String type) {
		setArgumentValue(StandardArguments.DERIVED_FROM, new NamedInstanceIdentifier(name, timestamp, type));
	}

	public void setDerivedFrom(NamedInstanceIdentifier identifier) {
		setArgumentValue(StandardArguments.DERIVED_FROM, identifier);
	}

	public NamedInstanceIdentifier getDerivedFrom() {
		return getArgumentValue(StandardArguments.DERIVED_FROM, NamedInstanceIdentifier.class);
	}

	public void setSort(String sortBy) {
		setArgumentValue(StandardArguments.SORT, sortBy);
	}

	public void setSortOrder(String order) {
		setArgumentValue(StandardArguments.SORT_ORDER, order);
	}

	public String getSort() {
		return getArgumentValue(StandardArguments.SORT, String.class);
	}

	public String getSortOrder() {
		return getArgumentValue(StandardArguments.SORT_ORDER, String.class);
	}
}
