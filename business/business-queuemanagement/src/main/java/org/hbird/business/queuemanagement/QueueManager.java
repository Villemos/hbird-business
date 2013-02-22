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
package org.hbird.business.queuemanagement;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import javax.jms.InvalidSelectorException;
import javax.management.MalformedObjectNameException;

import org.apache.camel.Body;
import org.hbird.business.api.IQueueManagement;
import org.hbird.business.queuemanagement.api.QueueManagerApi;
import org.hbird.queuemanagement.ClearQueue;
import org.hbird.queuemanagement.RemoveQueueElements;
import org.hbird.queuemanagement.ViewQueue;

import javax.management.openmbean.OpenDataException;

/**
 * The queue manager component support the monitoring and control of a activemq queue.
 * 
 * The component is a wrapper of the Queue Manager API. The component allows
 * the queue manager to run centrally, with 'commands' being send to it.
 * 
 * @author Gert Villemos
 *
 */
public class QueueManager {

	/** The connection to the server. */
	protected IQueueManagement api = null;
	
	protected String name;
	
	public QueueManager(String name) {
		this.name = name;
		this.api = new QueueManagerApi(name);
	}
	
	/**
	 * Method to list all queues in the system.
	 * 
	 * @return List of all queues. The list is a pretty printed queue.
	 * @throws MalformedObjectNameException
	 * @throws MalformedURLException
	 * @throws NullPointerException
	 * @throws IOException
	 */
	public List<String> listQueues() throws MalformedObjectNameException, MalformedURLException, NullPointerException, IOException {
		return api.listQueues();
	}


	/**
	 * Method to get all elements of a specific queue
	 * 
	 * @param command Command requesting the listing. Holds the name of the queue.
	 * @return
	 * @throws InvalidSelectorException
	 * @throws OpenDataException
	 * @throws MalformedObjectNameException
	 * @throws MalformedURLException
	 * @throws NullPointerException
	 * @throws IOException
	 */
	public Map<String, String> viewQueue(@Body ViewQueue command) throws InvalidSelectorException, OpenDataException, MalformedObjectNameException, MalformedURLException, NullPointerException, IOException {
		return api.viewQueue(command.getQueueName());
	}

	/**
	 * Method to remove a specific set of entries from a queue.
	 * 
	 * @param command The command to remove elements. Contains the JMS ID of the elements to delete and the queue name.
	 * @return A Map defining the current set of elements.
	 * @throws Exception
	 */
	public Map<String, String> removeQueueElements(@Body RemoveQueueElements command) throws Exception {
		return api.removeQueueElements(command.getQueueName(), command.getPattern());
	}


	/**
	 * Method to delete all entries in a queue.
	 * 
	 * @param command The clear command. Contains the name of the queue to be purged.
	 * @return True
	 * @throws Exception
	 */
	public Boolean clearQueue(@Body ClearQueue command) throws Exception {
		return api.clearQueue(command.getQueueName());
	}
}
