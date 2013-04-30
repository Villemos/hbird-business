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

import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.IPublish;
import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.interfaces.IStartablePart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author Gert Villemos
 * 
 */
public class NamedObjectPublisher {

    public static final String DEFAULT_CONFIGURATOR_NAME = "Configurator"; // has to be same as
                                                                           // ConfiguratorComponent.CONFIGURATOR_NAME

    /** The class logger. */
    protected static Logger LOG = LoggerFactory.getLogger(NamedObjectPublisher.class);

    protected String name;

    protected String destination;

	protected List<EntityInstance> objects = null;

    public NamedObjectPublisher(String name, List<EntityInstance> objects) {
        this(name, DEFAULT_CONFIGURATOR_NAME, objects);
    }

    /**
     * Constructor.
     * 
     * @param filename Name of the file to be read at intervals.
     */
	public NamedObjectPublisher(String name, String destination, List<EntityInstance> objects) {
        this.name = name;
        this.destination = destination;
        this.objects = objects;
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
                LOG.info("Creating StartComponent command for part '{}' to destination '{}'.", object.getID(), destination);
                if (destination == null) {
                    LOG.warn("The destination is null; most likely the start command will be ignored. Check your application setup configuration");
                }
                StartComponent startCommand = new StartComponent(object.getName(), (IStartablePart) object);
                startCommand.setDestination(destination);
                api.publish(startCommand);
            }
            else {
                LOG.info("Publishing Named object '" + object.getID() + "'.");
                api.publish(object);
            }
        }
    }
}
