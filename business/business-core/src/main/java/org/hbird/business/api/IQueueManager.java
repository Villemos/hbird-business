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
package org.hbird.business.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import javax.management.MalformedObjectNameException;
import javax.management.openmbean.OpenDataException;

/**
 * API for managing the topics and queues of the system.
 * 
 * This API should be used by component who needs to be aware of the messages published to the system
 * and currently being brokered by the underlying transport.
 * 
 * @author Gert Villemos
 * 
 */
public interface IQueueManager {

    /**
     * Method to list all queues currently existing.
     * 
     * @return List of the queue names.
     * @throws MalformedObjectNameException
     * @throws MalformedURLException
     * @throws NullPointerException
     * @throws IOException
     */
    public List<String> listQueues() throws Exception;

    /**
     * Method to view all data objects in a queue.
     * 
     * @param queueName The name of the queue to be viewed.
     * @return A map keyed on JMS message ID and holding the message as an encoded string.
     * @throws InvalidSelectorException
     * @throws OpenDataException
     * @throws MalformedObjectNameException
     * @throws MalformedURLException
     * @throws NullPointerException
     * @throws IOException
     */
    public Map<String, String> viewQueue(String queueName) throws Exception;

    /**
     * Method to remove a specific (set of) message(s) from a queue.
     * 
     * @param queueName The name of the queue to delete from.
     * @param elementIdPattern The JMS ID (or pattern) which shall be used to delete.
     * @return A new view of the queue
     * @throws Exception
     */
    public Map<String, String> removeQueueElements(String queueName, String elementIdPattern) throws Exception;

    /**
     * Method to completly clear a queue. All messages will be removed.
     * 
     * @param queueName The name of the queue
     * @return The status
     * @throws Exception
     */
    public Boolean clearQueue(String queueName) throws Exception;

    /**
     * Method to list all topics in the system.
     * 
     * @return List containing the name of the topics.
     * 
     * @throws MalformedObjectNameException
     * @throws MalformedURLException
     * @throws NullPointerException
     * @throws IOException
     */
    public List<String> listTopics() throws Exception;

    /**
     * Method to clear a topic. All messages on the topic will be removed.
     * 
     * @param topicName The name of the topic.
     * @return The status
     * 
     * @throws Exception
     */
    public Boolean clearTopic(String topicName) throws Exception;

    /**
     * Method to clear all topics and queues
     * 
     * @return The status
     */
    public boolean clearAll();
}
