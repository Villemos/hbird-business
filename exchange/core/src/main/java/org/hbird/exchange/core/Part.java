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

    private static final Logger LOG = LoggerFactory.getLogger(Part.class);

    protected static final String DEFAULT_ISSUED_BY = "SystemAssembly";
    
    /** The full ID of the parent. */
    protected String isPartOf = null;
    
    /**
     * Constructor that will set the ID = name. Should only be used for Parts that are unique, i.e. the name
     * can be used as the unique identifier.
     * 
     * @param name
     * @param description
     */
    public Part(String name, String description) {
        super(DEFAULT_ISSUED_BY, name, description);
    }

    public Part(String name, String description, IPart isPartOf) {
        super(DEFAULT_ISSUED_BY, name, description);
        this.isPartOf = isPartOf.getID();
    }
    
    public Part(String issuedBy, String name, String description) {
        super(issuedBy, name, description);
    }

    public Part(String issuedBy, String name, String description, IPart isPartOf) {
        super(issuedBy, name, description);
        this.isPartOf = isPartOf.getID();
    }

    /**
     * @see org.hbird.exchange.interfaces.IPartOf#getIsPartOf()
     */
    @Override
    public String getIsPartOf() {
        return isPartOf;
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
            this.isPartOf = parent.getID();
        }
    }

    public void setIsPartOf(String parentId) {
    	this.isPartOf = parentId;
    }
    
    public String prettyPrint() {
        return String.format("%s[name=%s, isPartOf=%s]", this.getClass().getSimpleName(), getName(), getIsPartOf());
    }
}
