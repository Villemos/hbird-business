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

import static org.hbird.exchange.dataaccess.Arguments.NAMES;
import static org.hbird.exchange.dataaccess.Arguments.IS_PART_OF;
import static org.hbird.exchange.dataaccess.Arguments.create;

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.constants.StandardComponents;
import org.hbird.exchange.core.CommandArgument;

/**
 * @author Gert Villemos
 *
 */
public class PartRequest extends DataRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4423923288346261214L;

	/**
	 * @param issuedBy
	 * @param destination
	 */
	public PartRequest(String issuedBy) {
		super(issuedBy, StandardComponents.ARCHIVE_NAME);
        setClass("Part");
	}

    /**
     * @see org.hbird.exchange.core.Command#getArgumentDefinitions()
     */
    @Override
    protected List<CommandArgument> getArgumentDefinitions(List<CommandArgument> args) {
        args = super.getArgumentDefinitions(args);
        args.add(create(NAMES));
        args.add(create(IS_PART_OF));
        return args;
    }
    
    public void setIsPartOf(String isPartOf) {
    	setArgumentValue(StandardArguments.IS_PART_OF, isPartOf);
    }

    public void setName(String name) {
    	List<String> names = new ArrayList<String>();
    	names.add(name);
    	setArgumentValue(StandardArguments.NAMES, names);
    }
}
