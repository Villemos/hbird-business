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

import org.hbird.exchange.interfaces.IEntity;

/**
 * An ‚entity‘ is a specific ‘thing’ in the system, such as a Part, or a parameter. 
 * 
 * The value at a given point in time is represented by an entity instance. An instance 
 * hold a timestamp and the value(s) of the entity at the time. The timestamp define 
 * when the instance is valid. Each entity is only represented by one instance at any point 
 * in time; the instance always supersedes all previously created instances. As an example 
 * the entity ‘/ESTcube/WeatherStations/Station1/Temperature’ (a Parameter measuring the 
 * temperature of a specific location) can have different values at different points in 
 * time, represented by different instances of the entity. But it cannot have two 
 * valid values at the same time. Consider the timeline below
 * <li>Time X. Instance A with value = 22 Degrees.</li>
 * <li>Time Y (> X). Instance B with value = 24 Degrees.</li>
 * 
 * At time Y only instance B is valid; instance A has been superseded.
 * 
 * @author Gert Villemos
 *
 */
public class Entity implements IEntity, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3604923675901537443L;

    /** The ID of the entity. */
    protected String ID;
	
    /** The name of the object. */
    protected String name;

    /** A description of the object. */
    protected String description;

	/** The Entity ID (not the instance ID) of the Part that has issued this object. */
    protected String issuedBy;
    
    /**
	 * @param iD
	 * @param name
	 * @param description
	 * @param issuedBy
	 */
	public Entity(String ID, String name, String description, String issuedBy) {
		this.ID = ID;
		this.name = name;
		this.description = description;
		this.issuedBy = issuedBy;
	}

	/**
     * Getter of the object name.
     * 
     * @return The name of the object.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter of the description of the object.
     * 
     * @return The description of the object.
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getID() {
    	return ID;
    }

    public void setID(String ID) {
    	this.ID = ID;
    } 
    
    /**
	 * @return the issuedBy
	 */
	public String getIssuedBy() {
		return issuedBy;
	}

	/**
	 * @param issuedBy the issuedBy to set
	 */
	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}	
}
