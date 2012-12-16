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

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;

public class DeletionRequest extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7374733690642382839L;

	{
		arguments.put("deletionquery", new CommandArgument("deletionquery", "The query based on which data will be deleted.", "String", "", null, true));
	}
	
	public DeletionRequest(String issuedBy, String destination, String deletionquery) {
		super(issuedBy, destination, "DeletionRequest", "A request to delete part or all data in an archive.");
		addArgument("deletionquery", deletionquery);
	}	
}
