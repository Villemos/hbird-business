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
import org.hbird.exchange.interfaces.IGroundStationSpecific;

/**
 * An object representing a ground station.
 * 
 * A ground station has a specific location. The location is defined through a 3D vector centered
 * on earth.
 * 
 * Notice that the antennas are assigned to the ground station location. The actual antenna
 * location might thus be slightly off, depending on how far the antenna is from the actual
 * ground station.
 *
 * The ground station model is loosely based on the concepts of the 
 * mercury reference model (http://mgsn.sourceforge.net/docs/modelv0.2.0.php)
 */
public class GroundStation extends Named implements IGroundStationSpecific {

    private static final long serialVersionUID = -8558418536858999621L;

	/** Default description */
    public static final String DESCRIPTION = "An antenna.";
    
    /** Default type of the antenna. */
    public static final String TYPE = "Primary Antenna"; 

    /** The Antenna(s) at this site. */
    protected List<Antenna> antennas = new ArrayList<Antenna>();

    /** The geo location of the ground station. */
    protected D3Vector geoLocation;
    
    /**
     * 
     */
    public GroundStation() {
        super();
        setDescription(DESCRIPTION);
        setType(TYPE);
    }

    
    /**
     * 
     * 
     * @param geoLocation
     * @param antennas
     */
    public GroundStation(D3Vector geoLocation, List<Antenna> antennas) {
        super();
        setDescription(DESCRIPTION);
        setType(TYPE);
        this.antennas = antennas;
        this.geoLocation = geoLocation;
    }

    public GroundStation(String issuedBy, String name, String type, String description, D3Vector geoLocation, RotatorProperties rotatorProperties, List<RadioChannel> radioChannels) {
    	super(issuedBy, name, type, description);
    	
    	antennas.add(new Antenna(issuedBy, "", "Primary Antenna", "Antenna of ground station " + name, rotatorProperties, radioChannels));
    	this.geoLocation = geoLocation;
    }

    /**
     * @see org.hbird.exchange.interfaces.IGroundStationSpecific#getGroundStationName()
     */
    @Override
    public String getGroundStationName() {
        return name;
    }

    /**
     * @see org.hbird.exchange.interfaces.IGroundStationSpecific#setGroundStationName(java.lang.String)
     */
    @Override
    public void setGroundStationName(String groundStationName) {
        this.name = groundStationName;
    }

	public D3Vector getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(D3Vector geoLocation) {
		this.geoLocation = geoLocation;
	}

	public List<Antenna> getAntennas() {
		return antennas;
	}

	public void setAntennas(List<Antenna> antennas) {
		this.antennas = antennas;
	}
	
	public Antenna getAntenna() {
		return antennas.get(0);
	}	
}
