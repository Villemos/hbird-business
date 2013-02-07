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
package org.hbird.queuemanagement;

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;

public class RemoveQueueElements extends Command {

	private static final long serialVersionUID = 4855448569441799079L;

	{
		arguments.put("queuename", new CommandArgument("queuename", "The name of the queue to be displayed.", "String (queue ID)", "", "hbird.requests", true));
		arguments.put("elements", new CommandArgument("elements", "List of JMS IDs of the elements to be removed. Can be set in paralle to the 'pattern'.", "String (JMS ID)", "", new ArrayList<String>(), true));
		arguments.put("pattern", new CommandArgument("pattern", "A pattern based on which to remove elements. Can be set on parallel to the specific 'elements' list. The pattern is matched against the 'properties' string which is in the format '{[property]=[value],[property]=[value],...}'.", "String (Reg.Ex.)", "", null, true));
	}
	
	public RemoveQueueElements(String issuedBy, String destination, List<String> elements) {
		super(issuedBy, destination, "RemoveQueueElements", "Will remove the elements from the queue. The elements are identified through a unique ID as assigned by the queue.");
		setElements(elements);
	}

	public RemoveQueueElements(String issuedBy, String destination, String element) {
		super(issuedBy, destination, "RemoveQueueElements", "Will remove the elements from the queue. The elements are identified through a unique ID as assigned by the queue.");
		getElements().add(element);
	}

	public RemoveQueueElements(String issuedBy, String destination, String element, boolean pattern) {
		super(issuedBy, destination, "RemoveQueueElements", "Will remove the elements from the queue. The elements are identified through a unique ID as assigned by the queue.");

		if (pattern == false) {
			getElements().add(element);
		}
		else {
			setPattern(element);
		}
	}

	public String getQueueName() {
		return (String) arguments.get("queuename").value;
	}
	
	public void setQueueName(String queuename) {
		arguments.get("queuename").value = queuename;
	}

	public List<String> getElements() {
		return (List<String>) arguments.get("elements").value;
	}
	
	public void setElements(List<String> elements) {
		arguments.get("elements").value = elements;
	}

	public String getPattern() {
		return (String) arguments.get("pattern").value;
	}
	
	public void setPattern(String pattern) {
		arguments.get("pattern").value = pattern;
	}
}
