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

import java.io.Serializable;

import org.hbird.exchange.interfaces.INamed;

/**
 * The super class of all types being exchanged. Contains a name and a description.
 * The class is intended to be subtyped, into specific types exchanged within the system such as
 * parameters, commands and tasks. Each subtype thus share a set of attributes.
 * 
 * */
public abstract class Named implements INamed, Serializable, Comparable<Named> {

    /** Default separator for the qualified name. */
    public static final String DEFAULT_QUALIFIED_NAME_SEPARATOR = "/";

    /** The unique UID of this class. */
    private static final long serialVersionUID = -5803219773253020746L;

    /** The name of the object. */
    protected String name;

    /** A description of the object. */
    protected String description;

    /**
     * Constructor of a Named object. The timestamp will be set to the creation time.
     * 
     * @param name The name of the object.
     * @param description The description of the object.
     */
    public Named(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Getter of the object name.
     * 
     * @return The name of the object.
     */
    public String getName() {
        return name;
    }

    public String getQualifiedName() {
        return getQualifiedName(DEFAULT_QUALIFIED_NAME_SEPARATOR);
    }

    public String getQualifiedName(String separator) {
        return separator + name;
    }

    /**
     * Getter of the description of the object.
     * 
     * @return The description of the object.
     */
    public String getDescription() {
        return description;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getID() {
    	return name;
    }
    
    public String prettyPrint() {
        return String.format("%s[name=%s]", this.getClass().getSimpleName(), getQualifiedName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Named o) {
        if (this == o) {
            return 0;
        }

        return 1;
    }
}
