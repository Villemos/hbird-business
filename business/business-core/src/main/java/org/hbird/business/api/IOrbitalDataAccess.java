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
package org.hbird.business.api;

import java.util.List;

import org.hbird.business.api.exceptions.ArchiveException;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.TleOrbitalParameters;

public interface IOrbitalDataAccess {

    /**
     * Method to retrieve the last orbital state of a satellite as derived from the last TLE. The method will return <li>
     * The last orbital state sample applicable to the satellite, derived from the last TLE parameters.</li>
     * 
     * @param satelliteID The ID of the satellite
     */
    public abstract OrbitalState getOrbitalStateFor(String satelliteID) throws ArchiveException;

    /**
     * Method to retrieve the orbital state of a satellite, identified throughs its name, within
     * a given interval. The method will return <li>All orbital state samples applicable to the satellite</li> <li>With
     * a sample timestamp between 'from' and 'to'</li> <li>and sorted on timestamp in ASCENDING order</li>
     * 
     * @param satelliteID ID of the satellite
     * @param from The earliest time (Java time).
     * @param to The latest time (Java time)
     * @return A list of all orbital states applicable to the satellites
     */
    public abstract List<OrbitalState> getOrbitalStatesFor(String satelliteID, long from, long to) throws ArchiveException;

    /**
     * @return Last version of a TLE for a given satellite
     */
    public abstract TleOrbitalParameters getTleFor(String satelliteID) throws ArchiveException;

    /**
     * Method to retrieve the orbital state of a satellite, identified throughs its name, within
     * a given interval. The method will return <li>All orbital state samples applicable to the satellite</li> <li>With
     * a sample timestamp between 'from' and 'to'</li> <li>and sorted on timestamp in ASCENDING order</li>
     * 
     * @param satelliteID ID of the satellite
     * @param from The earliest time (Java time).
     * @param to The latest time (Java time)
     * @return A list of all orbital states applicable to the satellites
     */
    public abstract List<TleOrbitalParameters> getTleFor(String satelliteID, long from, long to) throws ArchiveException;

    /**
     * Method to retrieve the next location contact event.
     * 
     * @param groundStationID ID of the ground station
     * @return
     */
    public abstract LocationContactEvent getNextLocationContactEventForGroundStation(String groundStationID) throws ArchiveException;

    public abstract List<LocationContactEvent> getLocationContactEventsForGroundStation(String groundStationID, long from, long to) throws ArchiveException;

    /**
     * Method to retrieve the next location contact event.
     * 
     * @param groundStationID ID of the ground station
     * @param satelliteID ID of the satellite
     * @return
     */
    public abstract LocationContactEvent getNextLocationContactEventFor(String groundStationID, String satelliteID) throws ArchiveException;

    /**
     * Method to retrieve the next location contact event.
     * 
     * @param groundStationID ID of the ground station
     * @param satelliteID ID of the satellite
     * @return
     */
    public abstract List<LocationContactEvent> getLocationContactEventsFor(String groundStationID, String satelliteID, long from, long to)
            throws ArchiveException;

}
