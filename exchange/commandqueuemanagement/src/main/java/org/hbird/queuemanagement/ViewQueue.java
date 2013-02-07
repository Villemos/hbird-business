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

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;

public class ViewQueue extends Command {

	private static final long serialVersionUID = 3485417554095535572L;
	
	{
		arguments.put("queuename", new CommandArgument("queuename", "The name of the queue to be displayed.", "String (ID)", "", "hbird.requests", true));
	}

	public ViewQueue(String issuedBy, String destination, String queuename) {
		super(issuedBy, destination, "ViewQueue", "Command to get a dump of all messages in the queue.");
		
		setQueueName(queuename);
	}	
	
	public String getQueueName() {
		return (String) arguments.get("queuename").value;
	}
	
	public void setQueueName(String queuename) {
		arguments.get("queuename").value = queuename;
	}
}
