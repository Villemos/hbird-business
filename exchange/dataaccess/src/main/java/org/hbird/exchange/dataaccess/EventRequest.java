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


import static org.hbird.exchange.dataaccess.Arguments.TO;
import static org.hbird.exchange.dataaccess.Arguments.FROM;
import static org.hbird.exchange.dataaccess.Arguments.create;

import java.util.List;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.constants.StandardComponents;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.core.Event;

/**
 * @author Gert Villemos
 *
 */
public class EventRequest extends DataRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8445762922526282503L;

	/**
	 * @param issuedBy
	 * @param destination
	 */
	public EventRequest(String issuedBy, Long from, Long to) {
		super(issuedBy, StandardComponents.ARCHIVE_NAME);
        setClass(Event.class.getSimpleName());
        setArgumentValue(StandardArguments.TO, to);
        setArgumentValue(StandardArguments.FROM, from);
	}
	
    /**
     * @see org.hbird.exchange.core.Command#getArgumentDefinitions()
     */
    @Override
    protected List<CommandArgument> getArgumentDefinitions(List<CommandArgument> args) {
        args = super.getArgumentDefinitions(args);
        args.add(create(TO));
        args.add(create(FROM));
        return args;
    }
}
