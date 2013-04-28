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
package org.hbird.business.metadatapublisher;

import java.util.List;

import org.apache.log4j.Logger;
import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.IPublish;
import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.interfaces.IStartablePart;

/**
 * 
 * 
 * @author Gert Villemos
 *
 */
public class NamedObjectPublisher {

	/** The class logger. */
	protected static Logger LOG = Logger.getLogger(NamedObjectPublisher.class);

	protected List<EntityInstance> objects = null;

	protected String name;
	
	/**
	 * Constructor.
	 * 
	 * @param filename Name of the file to be read at intervals.
	 */
	public NamedObjectPublisher(String name, List<EntityInstance> objects) {
		this.objects = objects;
		this.name = name;
	}

	/**
	 * Method to split the message. The returned message list is actually loaded
	 * from a Spring file, i.e. the original Exchange is ignored.
	 * 
	 * @return A list of messages, carrying as the body a command definition.
	 */
	public void start() {
		IPublish api = ApiFactory.getPublishApi(name);
		
		for (EntityInstance object : objects) {
			if (object instanceof IStartablePart) {
				LOG.info("Creating StartComponent command for part '" + object.getID() + "'.");
				api.publish(new StartComponent(object.getName(), (IStartablePart) object));
			}
			else {
				LOG.info("Publishing Named object '" + object.getID() + "'.");
				api.publish(object);
			}
		}
	}
}
