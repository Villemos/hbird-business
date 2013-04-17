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
package org.hbird.exchange.navigation;

import org.hbird.exchange.core.Part;
import org.hbird.exchange.interfaces.ISatelliteSpecific;

/**
 * Class representing a satellite.
 * 
 */
public class Satellite extends Part implements ISatelliteSpecific {

    /** The unique UUID. */
    private static final long serialVersionUID = 6169559659135516782L;

    protected String designator;

    protected String satelliteNumber;

    protected Double frequency = 100000D;
    
    /**
     * Constructor.
     * 
     * @param name The name of the satellite.
     * @param description The description of the satellite.
     */
    public Satellite(String ID, String name, String description) {
        super(ID, name, description);
    }

    public Satellite(String ID, String description) {
        super(ID, ID, description);
    }

    public String getDesignator() {
        return designator;
    }

    public void setDesignator(String designator) {
        this.designator = designator;
    }

    public String getSatelliteNumber() {
        return satelliteNumber;
    }

    public void setSatelliteNumber(String satelliteNumber) {
        this.satelliteNumber = satelliteNumber;
    }

    @Override
    public String getSatelliteId() {
        return name;
    }

	public Double getFrequency() {
		return frequency;
	}

	public void setFrequency(Double frequency) {
		this.frequency = frequency;
	}
    
    
}
