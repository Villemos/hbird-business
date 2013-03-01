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

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.navigation.GroundStation;
import org.hbird.exchange.navigation.Satellite;

/**
 * Interface to retrieve catalogue type data from a central archive. With catalogue data is
 * here understood summaries of the data in the archive.
 * 
 * The API should be used by any component which needs to obtain lists of which types of data
 * is available in the system.
 * 
 * @author Gert Villemos
 * 
 */
public interface ICatalogue {

    /**
     * Method to return a 'catalogue' (a list) of all parameters in the archive.
     * The list contains the last sample of each parameter.
     * 
     * @return List containing one sample of each parameter in the archive.
     */
    public List<Parameter> getParameters();

    /**
     * Method to return a 'catalogue' (a list) of all states in the archive.
     * The list contains the last sample received value of each state.
     * 
     * @return List containing one sample of each parameter in the archive.
     */
    public List<State> getStates();

    /**
     * Method to retrieve a definition of each command supported by the system.
     * 
     * @return List of commands supported by the system.
     */
    public List<Command> getCommands();

    /**
     * Method to retrieve all {@link GroundStation} stored in the archive.
     * 
     * @return List of all {@link GroundStation}s.
     */
    public List<GroundStation> getGroundStations();

    /**
     * Method to retrieve all {@link Satellite}s stored in the archive.
     * 
     * @return List of all {@link Satellite}s.
     */
    public List<Satellite> getSatellites();

    /**
     * Method to retrieve {@link GroundStation} by name from archive.
     * 
     * @param name name of {@link GroundStation}
     * @return {@link GroundStation}
     */
    public GroundStation getGroundStationByName(String name);

    /**
     * Method to retrieve {@link Satellite} by name from archive.
     * 
     * @param name name of {@link Satellite}
     * @return {@link Satellite}
     */
    public Satellite getSatelliteByName(String name);
}
