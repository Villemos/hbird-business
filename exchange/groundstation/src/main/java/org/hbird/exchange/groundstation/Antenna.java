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
package org.hbird.exchange.groundstation;

import org.hbird.exchange.core.Part;

/**
 * An antenna of a groundstation.
 * 
 * Each antenna is part of one (and only one) ground station.
 * 
 * @author Lauri Kimmel
 * @author Gert Villemos
 * 
 */
public class Antenna extends Part {

    private static final long serialVersionUID = -8558418536858999621L;

    protected int thresholdElevation = 0;

    /** Default description */
    public static final String DESCRIPTION = "An antenna.";
    
    public Antenna(String ID, String name) {
        super(ID, name);
        setDescription(DESCRIPTION);
    }

    public int getThresholdElevation() {
        return thresholdElevation;
    }

    public void setThresholdElevation(int thresholdElevation) {
        this.thresholdElevation = thresholdElevation;
    }
}
