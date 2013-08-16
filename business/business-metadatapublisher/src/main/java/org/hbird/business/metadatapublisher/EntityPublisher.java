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

import org.hbird.business.api.IPublisher;
import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.interfaces.IStartableEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author Gert Villemos
 */
public class EntityPublisher extends EntityInstance {

    private static final long serialVersionUID = 335582386333954011L;

    public static final String DEFAULT_CONFIGURATOR_NAME = "Configurator"; // has to be same as
                                                                           // ConfiguratorComponent.CONFIGURATOR_NAME

    /** The class logger. */
    protected static Logger LOG = LoggerFactory.getLogger(EntityPublisher.class);

    protected final String destination;

    protected final List<EntityInstance> objects;

    protected final IPublisher publisher;

    public EntityPublisher(String ID, String name, IPublisher publisher, List<EntityInstance> objects) {
        this(ID, name, DEFAULT_CONFIGURATOR_NAME, publisher, objects);
    }

    /**
     * Constructor.
     * 
     * @param filename Name of the file to be read at intervals.
     */
    public EntityPublisher(String ID, String name, String destination, IPublisher publisher, List<EntityInstance> objects) {
        super(ID, name);
        this.destination = destination;
        this.objects = objects;
        this.publisher = publisher;
    }

    /**
     * 
     */
    public void start() {
        LOG.info("EntityPublisher '{}' starting", instanceID);
        for (EntityInstance entityInstance : objects) {
            try {
                if (entityInstance instanceof IStartableEntity) {
                    LOG.info("Creating StartComponent command for StartableEntity '{}' to destination '{}'.", entityInstance.getID(), destination);
                    if (destination == null) {
                        LOG.warn("The destination is null; most likely the start command will be ignored. Check your application setup configuration");
                    }

                    // TODO - 16.08.2013, kimmell - fix this; use IdBuilder
                    StartComponent startCommand = new StartComponent(entityInstance.getID() + "/StartRequest");
                    startCommand.setEntity((IStartableEntity) entityInstance);
                    startCommand.setDestination(destination);
                    startCommand.setIssuedBy(instanceID);
                    publisher.publish(startCommand);
                }
                else {
                    LOG.info("Publishing EntityInstance '{}'.", entityInstance.getInstanceID());
                    entityInstance.setIssuedBy(instanceID);
                    publisher.publish(entityInstance);
                }
            }
            catch (Exception e) {
                LOG.error("Failed to publish {}", entityInstance.getInstanceID(), e);
            }
        }

        LOG.info("EntityPublisher '{}' finished", instanceID);
    }
}
