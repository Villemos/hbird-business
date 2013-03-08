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
 * 
 */
package org.hbird.exchange.navigation;

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.core.Named;
import org.hbird.exchange.interfaces.IPartOf;

/**
 * An antenna of a groundstation.
 * 
 * Each antenna is part of one (and only one) ground station.
 *
 */
public class Antenna extends Named implements IPartOf {

    public static final String DESCRIPTION = "Specification of an antenna.";
    public static final String TYPE = "Antenna";

    private static final long serialVersionUID = -8558418536858999621L;

    protected RotatorProperties rotatorProperties;
    protected List<RadioChannel> radioChannels = new ArrayList<RadioChannel>();

    protected Named isPartOf = null;
    
    public Antenna() {
        super();
        setDescription(DESCRIPTION);
        setType(TYPE);
    }

    public Antenna(String issuedBy, String name, String type, String description, RotatorProperties rotatorProperties, List<RadioChannel> radioChannels) {
    	super(issuedBy, name, type, description);

    	this.rotatorProperties = rotatorProperties;
    	this.radioChannels = radioChannels;
    }

    /**
     * @return the rotatorProperties
     */
    public RotatorProperties getRotatorProperties() {
        return rotatorProperties;
    }

    /**
     * @param rotatorProperties
     *            the rotatorProperties to set
     */
    public void setRotatorProperties(RotatorProperties rotatorProperties) {
        this.rotatorProperties = rotatorProperties;
    }

    /**
     * @return the radioChannels
     */
    public List<RadioChannel> getRadioChannels() {
        return radioChannels;
    }
    
    /* (non-Javadoc)
     * @see org.hbird.exchange.core.IPartOf#getIsPartOf()
     */
    public Named getIsPartOf() {
    	return isPartOf;
    }
    
    /* (non-Javadoc)
     * @see org.hbird.exchange.core.IPartOf#setIsPartOf(org.hbird.exchange.core.Named)
     */
    public void setIsPartOf(Named parent) {
    	this.isPartOf = parent;
    }
}
