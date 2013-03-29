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
package org.hbird.exchange.core;

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.constants.StandardComponents;
import org.hbird.exchange.interfaces.IPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A part is something that is 'part' of something else (... most likely).
 * 
 * @author Gert Villemos
 * 
 */
public class Part extends Named implements IPart {

    private static final long serialVersionUID = 4961124159238983376L;

    private static final Logger LOG = LoggerFactory.getLogger(Part.class);

    /** Parent of this {@link Part}. */
    protected IPart parent;

    /** Commands accepted by this {@link Part}. */
    protected List<Command> commands = new ArrayList<Command>(0);

    public Part(String name, String description) {
        this(name, description, (IPart) null);
    }

    public Part(String name, String description, IPart isPartOf) {
        this(name, description, isPartOf, new ArrayList<Command>(0));
    }

    public Part(String name, String description, List<Command> commands) {
        this(name, description, null, commands);
    }

    public Part(String name, String description, IPart isPartOf, List<Command> commands) {
        super(StandardComponents.SYSTEM, name, Part.class.getSimpleName(), description);
        this.commands = commands;
        this.parent = isPartOf;
    }

    /**
     * @see org.hbird.exchange.interfaces.IPartOf#getIsPartOf()
     */
    @Override
    public IPart getIsPartOf() {
        return parent;
    }

    /**
     * @see org.hbird.exchange.interfaces.IPartOf#setIsPartOf(org.hbird.exchange.core.Named)
     */
    @Override
    public void setIsPartOf(IPart parent) {
        if (parent == null) {
            // TODO - 29.03.2013, kimmell - throw IllegalArgumentException or RuntimeException here?
            LOG.error("Attempting to set NULL parent for Part '{}'. Likely a configuration error.", this.getName());
        }
        else {
            this.parent = parent;
        }
    }

    /**
     * @see org.hbird.exchange.interfaces.IPart#getPartName()
     */
    @Override
    public String getQualifiedName(String separator) {
        return parent == null ? separator + name : parent.getQualifiedName(separator) + separator + name;
    }

    @Override
    public List<Command> getCommands() {
        return commands;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }
}
