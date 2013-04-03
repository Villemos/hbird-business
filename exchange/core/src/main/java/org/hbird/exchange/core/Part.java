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
    /** The Part that this part is part of. */

    private static final Logger LOG = LoggerFactory.getLogger(Part.class);

    /**
     * Unique ID of this part. The ID should be unique accross instantiations of the Part, i.e. be set as
     * part of the construction of the Part.
     */
    protected String ID = "";

    protected IPart parent;

    public Part(String ID, String name, String description) {
        super(name, description);
        this.ID = ID;

    }

    public Part(String ID, String name, String description, IPart isPartOf) {
        super(name, description);
        this.ID = ID;
        parent = isPartOf;

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

    @Override
    public String getQualifiedName(String separator) {
        // XXX - 03.04.2013, kimmell - overriding Named logic in here?
        return ID;
    }

    @Override
    public String getAbsoluteName() {
        return parent == null ? "/" + name : parent.getAbsoluteName() + "/" + name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hbird.exchange.interfaces.IIDed#getID()
     */
    @Override
    public String getID() {
        return ID;
    }

    public IPart getParent() {
        return parent;
    }

    public void setParent(IPart parent) {
        this.parent = parent;
    }

    public void setID(String iD) {
        ID = iD;
    }
}
