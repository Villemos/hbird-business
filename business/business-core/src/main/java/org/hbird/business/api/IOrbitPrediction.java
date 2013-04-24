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

import org.hbird.exchange.core.Named;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.PointingData;
import org.hbird.exchange.navigation.Satellite;

/**
 * API interface offering methods to request the propagation (prediction) of an satellites orbit.
 * 
 * This API should be used by any component which requires orbital prediction.-
 * 
 * The API underneath create orbital request messages of the hbird exchange tier and submit these
 * to the activemq commanding topic for distribution.
 * 
 * The orbit propagation will calculate <li>The Orbital State of the satellite, i.e.the position, velocity and momentum
 * at a specific time.</li> <li>Contact events (Start-End of visibility) to specific (sets of) locations, i.e. ground
 * stations.</li>
 * 
 * @author Gert Villemos
 * 
 */
public interface IOrbitPrediction extends IHbirdApi {

    /**
     * Propagate the orbit of a satellite <li>From NOW using the last available TLE for the satellite.</li> <li>Delta
     * propagation for 12 hours.</li> <li>Calculating contact events to all known locations.</li> <li>Returning the
     * results as a Dataset.</li>
     * 
     * @param satellite The name of the satellite whose orbit shall be propagated.
     * @return A DataSet containing all orbital events.
     */
    public List<Named> requestOrbitPropagation(String satellite);

    /**
     * Propagate the orbit of a satellite <li>From [from].</li> <li>Delta propagation until [to].</li> <li>Calculating
     * contact events to all known locations.</li> <li>Returning the results as a Dataset.</li>
     * 
     * @param satellite The name of the satellite whose orbit shall be propagated.
     * @param from The start time of the propagation
     * @param to The end time of the propagation. Delta propagation = [to] - [from]
     * @return A DataSet containing all orbital events.
     */
    public List<Named> requestOrbitPropagation(String satellite, long from, long to);

    /**
     * Propagate the orbit of a satellite <li>From [from].</li> <li>Delta propagation until [to].</li> <li>Calculating
     * contact events to the specified location.</li> <li>Returning the results as a Dataset.</li>
     * 
     * @param satellite The name of the satellite whose orbit shall be propagated.
     * @oaram location Name of the location to which contact events (start-end of visibility) shall be calculated.
     * @param from The start time of the propagation
     * @param to The end time of the propagation. Delta propagation = [to] - [from]
     * @return A DataSet containing all orbital events.
     */
    public List<Named> requestOrbitPropagation(String satellite, String location, long from, long to);

    /**
     * Propagate the orbit of a satellite <li>From [from].</li> <li>Delta propagation until [to].</li> <li>Calculating
     * contact events to the listed set of locations.</li> <li>Returning the results as a Dataset.</li>
     * 
     * @param satellite The name of the satellite whose orbit shall be propagated.
     * @oaram locations A list of locations to which contact events (start-end of visibility) shall be calculated.
     * @param from The start time of the propagation
     * @param to The end time of the propagation. Delta propagation = [to] - [from]
     * @return A DataSet containing all orbital events.
     */
    public List<Named> requestOrbitPropagation(String satellite, List<String> locations, long from, long to);

    /**
     * Propagate the orbit of a satellite <li>From NOW using the last available TLE for the satellite.</li> <li>Delta
     * propagation for 12 hours.</li> <li>Calculating contact events to all known locations.</li> <li>Returning the
     * results as a stream on the monitoring topic.</li>
     * 
     * @param satellite The name of the satellite whose orbit shall be propagated.
     * @return A DataSet containing all orbital events.
     */
    public void requestOrbitPropagationStream(String satellite);

    /**
     * Propagate the orbit of a satellite <li>From [from].</li> <li>Delta propagation until [to].</li> <li>Calculating
     * contact events to all known locations.</li> <li>Returning the results as a stream on the monitoring topic.</li>
     * 
     * @param satellite The name of the satellite whose orbit shall be propagated.
     * @param from The start time of the propagation
     * @param to The end time of the propagation. Delta propagation = [to] - [from]
     * @return A DataSet containing all orbital events.
     */
    public void requestOrbitPropagationStream(String satellite, long from, long to);

    /**
     * Propagate the orbit of a satellite <li>From [from].</li> <li>Delta propagation until [to].</li> <li>Calculating
     * contact events to the specified location.</li> <li>Returning the results as a stream on the monitoring topic.</li>
     * 
     * @param satellite The name of the satellite whose orbit shall be propagated.
     * @oaram location Name of the location to which contact events (start-end of visibility) shall be calculated.
     * @param from The start time of the propagation
     * @param to The end time of the propagation. Delta propagation = [to] - [from]
     * @return A DataSet containing all orbital events.
     */
    public void requestOrbitPropagationStream(String satellite, String location, long from, long to);

    /**
     * Propagate the orbit of a satellite <li>From [from].</li> <li>Delta propagation until [to].</li> <li>Calculating
     * contact events to the listed set of locations.</li> <li>Returning the results as a stream on the monitoring
     * topic.</li>
     * 
     * @param satellite The name of the satellite whose orbit shall be propagated.
     * @oaram locations A list of locations to which contact events (start-end of visibility) shall be calculated.
     * @param from The start time of the propagation
     * @param to The end time of the propagation. Delta propagation = [to] - [from]
     * @return A DataSet containing all orbital events.
     */
    public void requestOrbitPropagationStream(String satellite, List<String> locations, long from, long to);

    /**
     * Method to request pointing data for a location to a satellite based on a start-end of contact event pair.
     * 
     * @param startContactEvent The start of contact event
     * @param endContactEvent The end of contact event
     * @param groundStation {@link GroundStation} to command
     * @param satellite {@link Satellite} to track
     * @param contactDataStepSize The step size (ms) when propagating the pointing.
     * @return Sorted list of Pointing Data.
     * @throws Exception
     */
    public List<PointingData> requestPointingDataFor(LocationContactEvent startContactEvent, LocationContactEvent endContactEvent, GroundStation groundStation,
            Satellite satellite, long contactDataStepSize) throws Exception;
}
