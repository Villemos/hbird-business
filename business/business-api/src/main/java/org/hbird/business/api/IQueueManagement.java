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

import javax.jms.InvalidSelectorException;
import javax.management.MalformedObjectNameException;
import javax.management.openmbean.OpenDataException;

public interface IQueueManagement {
	public List<String> listQueues() throws MalformedObjectNameException, MalformedURLException, NullPointerException, IOException;
	public Map<String, String> viewQueue(String queueName) throws InvalidSelectorException, OpenDataException, MalformedObjectNameException, MalformedURLException, NullPointerException, IOException;
	public Map<String, String> removeQueueElements(String queueName, String elementIdPattern) throws Exception;
	public Boolean clearQueue(String queueName) throws Exception;
}
