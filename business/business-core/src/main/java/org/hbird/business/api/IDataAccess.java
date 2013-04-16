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
import java.util.Map;

import org.hbird.exchange.core.Event;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.dataaccess.DataRequest;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.PointingData;
import org.hbird.exchange.navigation.TleOrbitalParameters;

/**
 * API for retrieving data from a hbird based system.
 * 
 * This API should be used by any component which needs to access previously published data (see the IPublish
 * API interface). The API access the underlying data store. It does not trigger the creation of data. Data 
 * that has not been stored cannot be requested through this interface.
 * 
 * The API wraps the underlying hbird control mechanisms. In its basic form a request in hbird consists of
 * <li>Creation of a DataRequest object from the org.hbird.exchange.dataaccess module.</li>
 * <li>The publishing of the request to the system as a Request-Reply (in-out) exchange</li>
 * <li>The reception and processing of the request by the archive and the return of the reply</li>
 * <li>The clients reception of the reply</li>
 * 
 * Implementations of this API encapsulates step 1, 2 and 4. It simplifies the creation and configuration of
 * the data request, it submits the request to the system, it receives the reply and format it in an
 * easy to use format.
 * 
 * The API supports two different 'modes' of operation
 * <li>Initialization. An initialization retrieval is to get the sample(s) of one or
 * more parameters applicable at a given time. For each parameter, X samples are returned
 * which was the value of the parameter at the given time. Typically this is used to initialize a
 * display or component with the latest value of a set of parameters. </li>
 * <li>Stepping. A stepping retrieval is to get a chronologically complete list of samples of
 * a set of parameters from a time to another time. The list may contain samples for each
 * parameter, but might not. This is typically used to step through history of one or
 * more parameters from a starting time to an end time.
 * 
 * In both modes the States of each parameter sample can also be retrieved. This is done using
 * the methods which name ends with 'AndStates'.
 * 
 * Each of the two modes are supported by a set of methods. Each method takes a combination / subset
 * of the following arguments
 * <li>name(s). A Sting, or a List of Strings, with the name(s) of the parameter(s).</li>
 * <li>from. The start time of the sample set.</li>
 * <li>to/at. The end time of the sample set. For initialization requests this is called 'at'.</to>
 * <li>rows. Limits the maximal number of entries to be returned.</li>
 * 
 * Notice that data is injected through the 'IPublish' interface. An archive
 * is one of the subscribers to the published data. This interface only supports retrieval.
 * 
 * Notice that the metadata, i.e. a list of available parameters, can be retrieved through APIs
 * implementing the ICatalogue interface.
 * 
 * Examples of Usage;
 * 
 * Initialize multiple parameters at current time
 *   IDataAccess api = new DataAccess("MySystem");
 *   List<String> names = new ArrayList<String>();
 *   names.add("PARA1");
 *   names.add("PARA2");
 *   List<Parameter> parameters = api.getParameters(names);
 * 
 * Initialize multiple parameters at Time X
 *   IDataAccess api = new DataAccess("MySystem");
 *   List<String> names = new ArrayList<String>();
 *   names.add("PARA1");
 *   names.add("PARA2");
 *   List<Parameter> parameters = api.getParameters(names, X);
 * 
 * Initialize multiple parameters at Time X, then step forwards to time Y
 *   IDataAccess api = new DataAccess("MySystem");
 *   List<String> names = new ArrayList<String>();
 *   names.add("PARA1");
 *   names.add("PARA2");
 *   List<Parameter> initialParameters = api.getParameters(names, X);
 *   // Use initial values.
 *   List<Parameter> stepParameters = api.getParameters(names, X, Y);
 *   // Iterate over list and use values.
 * 
 * Initialize multiple parameters, including their states, at current time
 *   IDataAccess api = new DataAccess("MySystem");
 *   List<String> names = new ArrayList<String>();
 *   names.add("PARA1");
 *   names.add("PARA2");
 *   List<Parameter> parameters = api.getParametersAndStates(names);
 * 
 * Initialize multiple parameters, including their states, at Time X, then step forwards to time Y
 *   IDataAccess api = new DataAccess("MySystem");
 *   List<String> names = new ArrayList<String>();
 *   names.add("PARA1");
 *   names.add("PARA2");
 *   List<Parameter> initialParameters = api.getParametersAndStates(names, X);
 *   // Use initial values.
 *   List<Parameter> stepParameters = api.getParametersAndStates(names, X, Y);
 *   // Iterate over list and use values.
 * 
 * @author Gert Villemos
 *
 */
public interface IDataAccess {

	public List<Named> getData(DataRequest request);

	public Named resolveNamed(String ID);

	/**
	 * Method to retrieve the last sample of a single parameter. The method will return
	 * <li>One parameter sample</li>
	 * <li>With the highest possible timestamp</li>
	 * 
	 * @param name The name of the parameter
	 * @return The parameter sample. May be 'null' if no parameter sample was found.
	 */
	public Parameter getParameter(String name);

	/**
	 * Method to retrieve the last sample of a single parameter. The method will return
	 * <li>One parameter sample</li>
	 * <li>With the highest possible timestamp</li>
	 * 
	 * @param name The name of the parameter
	 * @return The parameter sample. May be 'null' if no parameter sample was found.
	 */
	public Parameter getParameter(String source, String name);

	/**
	 * Method to retrieve the last samples of a single parameter. The method will return
	 * <li>A maximum of 'rows' entries</li>
	 * <li>Starting from the last received sample of the parameter</li>
	 * <li>and sorted on timestamp in DECENDING order</li>
	 * 
	 * @param name The name of the parameter
	 * @param rows The maximum number of entries to be retrieved
	 * @return List of parameter samples
	 */
	public List<Parameter> getParameter(String name, int rows);

	/**
	 * Method to retrieve the last samples of a single parameter. The method will return
	 * <li>A maximum of 'rows' entries</li>
	 * <li>Starting from the last received sample of the parameter</li>
	 * <li>and sorted on timestamp in DECENDING order</li>
	 * 
	 * @param name The name of the parameter
	 * @param rows The maximum number of entries to be retrieved
	 * @return List of parameter samples
	 */
	public List<Parameter> getParameter(String source, String name, int rows);

	/**
	 * Method to retrieve the samples of a single parameter. The method will return
	 * <li>One parameter sample</li>
	 * <li>With time T < 'at', being the sample closest to the time 'at'</li>
	 * 
	 * Parameter samples are received at time intervals. The sample applicable at a given
	 * time is the next sample with a time less than the given time.
	 * 
	 * @param name The name of the parameter
	 * @param at Java time which the sample value must be less than
	 * @return The parameter sample. May be 'null' if no parameter sample was found.
	 */
	public Parameter getParameterAt(String name, long at);

	/**
	 * Method to retrieve the samples of a single parameter. The method will return
	 * <li>One parameter sample</li>
	 * <li>With time T < 'at', being the sample closest to the time 'at'</li>
	 * 
	 * Parameter samples are received at time intervals. The sample applicable at a given
	 * time is the next sample with a time less than the given time.
	 * 
	 * @param name The name of the parameter
	 * @param at Java time which the sample value must be less than
	 * @return The parameter sample. May be 'null' if no parameter sample was found.
	 */
	public Parameter getParameterAt(String source, String name, long at);

	/**
	 * Method to retrieve a set of samples of a single parameter. The retrieval will be
	 * <li>A maximum of 'rows' entries</li>
	 * <li>Starting from the 'at' time</li>
	 * <li>and sorted on timestamp in DECENDING order</li>
	 * 
	 * @param name The name of the parameter
	 * @param at Java time which the sample value must be less than
	 * @param rows The maximum number of entries to be retrieved
	 * @return List of parameter samples
	 */
	public List<Parameter> getParameterAt(String name, long at, int rows);

	/**
	 * Method to retrieve a set of samples of a single parameter. The retrieval will be
	 * <li>A maximum of 'rows' entries</li>
	 * <li>Starting from the 'at' time</li>
	 * <li>and sorted on timestamp in DECENDING order</li>
	 * 
	 * @param name The name of the parameter
	 * @param at Java time which the sample value must be less than
	 * @param rows The maximum number of entries to be retrieved
	 * @return List of parameter samples
	 */
	public List<Parameter> getParameterAt(String source, String name, long at, int rows);

	
	/**
	 * Method to retrieve the last sample of a single parameter. The method will return
	 * <li>One parameter sample</li>
	 * <li>With the highest possible timestamp</li>
	 * 
	 * @param names The names of the parameters
	 * @return List of parameter samples
	 */
	public List<Parameter> getParameters(List<String> names);


	/**
	 * Method to retrieve the last samples of a single parameter. The method will return
	 * <li>A maximum of 'rows' entries</li>
	 * <li>Starting from the last received sample of the parameter</li>
	 * <li>and sorted on timestamp in DECENDING order</li>
	 * 
	 * @param names The names of the parameters
	 * @param rows The maximum number of entries to be retrieved
	 * @return List of parameter samples
	 */
	public List<Parameter> getParameters(List<String> names, int rows);

	/**
	 * Method to retrieve the samples of a single parameter. The method will return
	 * <li>One parameter sample</li>
	 * <li>With time T < 'at', being the sample closest to the time 'at'</li>
	 * 
	 * Parameter samples are received at time intervals. The sample applicable at a given
	 * time is the next sample with a time less than the given time.
	 * 
	 * @param names The names of the parameters
	 * @param at Java time which the sample value must be less than
	 * @return List of parameter samples
	 */
	public List<Parameter> getParametersAt(List<String> names, long at);


	/**
	 * Method to retrieve a set of samples of a single parameter. The retrieval will be
	 * <li>A maximum of 'rows' entries</li>
	 * <li>Starting from the 'at' time</li>
	 * <li>and sorted on timestamp in DECENDING order</li>
	 * 
	 * @param names The names of the parameters
	 * @param at Java time which the sample value must be less than
	 * @param rows The maximum number of entries to be retrieved
	 * @return List of parameter samples
	 */
	public List<Parameter> getParametersAt(List<String> name, long at, int rows);




	/**
	 * Method to retrieve the last sample and all states of a single parameter. The method will return
	 * <li>One parameter sample</li>
	 * <li>With the highest possible timestamp</li>
	 * 
	 * @param name The name of the parameter
	 * @return Map keyed on the parameter sample and containing as key a list of states applicable to the specific sample.
	 */
	public Map<Parameter, List<State>> getParameterAndStates(String name);


	/**
	 * Method to retrieve the last samples and all states and all states of a single parameter. The method will return
	 * <li>A maximum of 'rows' entries</li>
	 * <li>Starting from the last received sample of the parameter</li>
	 * <li>and sorted on timestamp in DECENDING order</li>
	 * 
	 * @param name The name of the parameter
	 * @param rows The maximum number of entries to be retrieved
	 * @return Map keyed on the parameter sample and containing as key a list of states applicable to the specific sample.
	 */
	public Map<Parameter, List<State>> getParameterAndStates(String name, int rows);

	/**
	 * Method to retrieve the samples and all states of a single parameter. The method will return
	 * <li>One parameter sample</li>
	 * <li>With time T < 'at', being the sample closest to the time 'at'</li>
	 * 
	 * Parameter samples are received at time intervals. The sample applicable at a given
	 * time is the next sample with a time less than the given time.
	 * 
	 * @param name The name of the parameter
	 * @param at Java time which the sample value must be less than
	 * @return Map keyed on the parameter sample and containing as key a list of states applicable to the specific sample.
	 */
	public Map<Parameter, List<State>> getParameterAndStatesAt(String name, long at);


	/**
	 * Method to retrieve a set of samples and all states of a single parameter. The retrieval will be
	 * <li>A maximum of 'rows' entries</li>
	 * <li>Starting from the 'at' time</li>
	 * <li>and sorted on timestamp in DECENDING order</li>
	 * 
	 * @param name The name of the parameter
	 * @param at Java time which the sample value must be less than
	 * @param rows The maximum number of entries to be retrieved
	 * @return Map keyed on the parameter sample and containing as key a list of states applicable to the specific sample.
	 */
	public Map<Parameter, List<State>> getParameterAndStatesAt(String name, long at, int rows);

	/**
	 * Method to retrieve the last sample and all states of a set of parameters. The method will return
	 * <li>One parameter sample</li>
	 * <li>With the highest possible timestamp</li>
	 * 
	 * @param names The names of the parameters
	 * @return Map keyed on the parameter sample and containing as key a list of states applicable to the specific sample.
	 */
	public Map<Parameter, List<State>> getParametersAndStates(List<String> names);


	/**
	 * Method to retrieve the last samples and all states of a set of parameters. The method will return
	 * <li>A maximum of 'rows' entries</li>
	 * <li>Starting from the last received sample of the parameter</li>
	 * <li>and sorted on timestamp in DECENDING order</li>
	 * 
	 * @param names The names of the parameters
	 * @param rows The maximum number of entries to be retrieved
	 * @return Map keyed on the parameter sample and containing as key a list of states applicable to the specific sample.
	 */
	public Map<Parameter, List<State>> getParametersAndStates(List<String> names, int rows);

	/**
	 * Method to retrieve the samples and all states of a set of parameters. The method will return
	 * <li>One parameter sample per parameter</li>
	 * <li>With time T < 'at', being the sample closest to the time 'at'</li>
	 * 
	 * Parameter samples are received at time intervals. The sample applicable at a given
	 * time is the next sample with a time less than the given time.
	 * 
	 * @param names The names of the parameters
	 * @param at Java time which the sample value must be less than
	 * @return Map keyed on the parameter sample and containing as key a list of states applicable to the specific sample.
	 */
	public Map<Parameter, List<State>> getParametersAndStateAt(List<String> names, long at);

	/**
	 * Method to retrieve a set of samples and all states of a set of parameters. The retrieval will be
	 * <li>A maximum of 'rows' entries</li>
	 * <li>Starting from the 'at' time</li>
	 * <li>and sorted on timestamp in DECENDING order</li>
	 * 
	 * @param name The name of the parameter
	 * @param at Java time which the sample value must be less than
	 * @param rows The maximum number of entries to be retrieved
	 * @return Map keyed on the parameter sample and containing as key a list of states applicable to the specific sample.
	 */
	public Map<Parameter, List<State>> getParametersAndStateAt(List<String> name, long at, int rows);

	/**
	 * Method to retrieve the samples of a single parameter. The retrieval will be
	 * <li>All samples</li>
	 * <li>With a sample timestamp between 'from' and 'to'</li>
	 * <li>and sorted on timestamp in ASCENDING order</li>
	 * 
	 * @param name The name of the parameter
	 * @param from The earliest time (Java time).
	 * @param to The latest time (Java time)
	 * @return List<Parameter> of Parameter samples. May be empty if no samples matching the criterions were found.
	 */
	public List<Parameter> getParameter(String name, long from, long to);


	/**
	 * Method to retrieve the samples of a single parameter. The retrieval will be
	 * <li>A maximum of 'rows' samples</li>
	 * <li>With a sample timestamp between 'from' and 'to'</li>
	 * <li>and sorted on timestamp in ASCENDING order</li>
	 *
	 *  Notice that if the 'rows' is below the number of samples between 'from' and 'to'
	 *  then only a subset of the total samples will be returned. The sampling will start
	 *  at the 'from' time and progress for 'rows' entries on until the 'to' time.
	 * 
	 * @param name The name of the parameter
	 * @param from The earliest time (Java time).
	 * @param to The latest time (Java time)
	 * @return List<Parameter> of Parameter samples. May be empty if no samples matching the criterions were found.
	 */
	public List<Parameter> getParameter(String name, long from, long to, int rows);

	/**
	 * Method to retrieve the samples of a set of parameters. The retrieval will be
	 * <li>All samples</li>
	 * <li>With a sample timestamp between 'from' and 'to'</li>
	 * <li>and sorted on timestamp in ASCENDING order</li>
	 * 
	 * @param names The names of the parameters
	 * @param from The earliest time (Java time).
	 * @param to The latest time (Java time)
	 * @return List<Parameter> of Parameter samples. May be empty if no samples matching the criterions were found.
	 */
	public List<Parameter> getParameters(List<String> names, long from, long to);

	/**
	 * Method to retrieve the samples of a set of parameters. The retrieval will be
	 * <li>A maximum of 'rows' parameter samples</li>
	 * <li>With a sample timestamp between 'from' and 'to'</li>
	 * <li>and sorted on timestamp in ASCENDING order</li>
	 *
	 *  Notice that if the 'rows' is below the number of samples between 'from' and 'to'
	 *  then only a subset of the total samples will be returned. The sampling will start
	 *  at the 'from' time and progress for 'rows' entries on until the 'to' time.
	 * 
	 * @param names The names of the parameters
	 * @param from The earliest time (Java time).
	 * @param to The latest time (Java time)
	 * @return List<Parameter> of Parameter samples. May be empty if no samples matching the criterions were found.
	 */
	public List<Parameter> getParameters(List<String> names, long from, long to, int rows);

	/**
	 * Method to retrieve the samples of a single parameter and all states applicable to each sample. The method will return
	 * <li>All parameter samples and their states</li>
	 * <li>With a sample timestamp between 'from' and 'to'</li>
	 * <li>and sorted on timestamp in ASCENDING order</li>
	 * 
	 * @param name The name of the parameter.
	 * @param from The earliest time (Java time).
	 * @param to The latest time (Java time)
	 * @return Map keyed on the parameter sample and containing as key a list of states applicable to the specific sample.
	 */
	public Map<Parameter, List<State>> getParameterAndStates(String name, long from, long to);

	/**
	 * Method to retrieve the samples of a single parameter and all states applicable to each sample. The method will return
	 * <li>A maximum of 'rows' entries</li>
	 * <li>With a sample timestamp between 'from' and 'to'</li>
	 * <li>and sorted on timestamp in ASCENDING order</li>
	 * 
	 * NOTE that the 'rows' parameter counts the parameter samples and the states. The method will return
	 * X parameter samples and Y state samples, where X+Y=rows.
	 * 
	 * @param name The name of the parameter.
	 * @param from The earliest time (Java time).
	 * @param to The latest time (Java time)
	 * @param rows The maximum number of entries to be returned
	 * @return Map keyed on the parameter sample and containing as key a list of states applicable to the specific sample.
	 */
	public Map<Parameter, List<State>> getParameterAndStates(String name, long from, long to, int rows);

	/**
	 * Method to retrieve the samples of a set of parameters and all states applicable to each sample. The method will return
	 * <li>All parameter samples and their states</li>
	 * <li>With a sample timestamp between 'from' and 'to'</li>
	 * <li>and sorted on timestamp in ASCENDING order</li>
	 * 
	 * @param names The names of the parameters
	 * @param from The earliest time (Java time).
	 * @param to The latest time (Java time)
	 * @return Map keyed on the parameter sample and containing as key a list of states applicable to the specific sample.
	 */
	public Map<Parameter, List<State>> getParametersAndStates(List<String> names, long from, long to);

	/**
	 * Method to retrieve the samples of a set of parameters and all states applicable to each sample. The method will return
	 * <li>A maximum of 'rows' entries</li>
	 * <li>With a sample timestamp between 'from' and 'to'</li>
	 * <li>and sorted on timestamp in ASCENDING order</li>
	 * 
	 * NOTE that the 'rows' parameter counts the parameter samples and the states. The method will return
	 * X parameter samples and Y state samples, where X+Y=rows.
	 * 
	 * @param names The names of the parameters
	 * @param from The earliest time (Java time).
	 * @param to The latest time (Java time)
	 * @param rows The maximum number of entries to be returned
	 * @return Map keyed on the parameter sample and containing as key a list of states applicable to the specific sample.
	 */
	public Map<Parameter, List<State>> getParametersAndStates(List<String> names, long from, long to, int rows);


	/**
	 * Method to retrieve all states applicable to a Named object (such as a Parameter).
	 * <li>A maximum of 1000 entries (parameter samples and applicable states)</li>
	 * <li>Starting from the last received sample of the parameter</li>
	 * <li>and sorted on timestamp in DECENDING order</li>
	 * 
	 * @param isStateOf The name of the Named object that the state must be a state of
	 * @return A list of all states applicable to the named object
	 */
	public List<State> getState(String isStateOf);

	/**
	 * Method to retrieve all states applicable to a Named object (such as a Parameter).
	 * <li>All state samples</li>
	 * <li>With a sample timestamp between 'from' and 'to'</li>
	 * <li>and sorted on timestamp in ASCENDING order</li>
	 * 
	 * @param isStateOf The name of the Named object that the state must be a state of
	 * @param from The earliest time (Java time).
	 * @param to The latest time (Java time)
	 * @return A list of all states applicable to the Named object
	 */
	public List<State> getState(String isStateOf, long from, long to);

	/**
	 * Method to retrieve the current value of a set of states.
	 * <li>All state samples</li>
	 * <li>With a sample timestamp between 'from' and 'to'</li>
	 * <li>and sorted on timestamp in ASCENDING order</li>
	 * 
	 * @param isStateOf The name of the Named object that the state must be a state of
	 * @param from The earliest time (Java time).
	 * @param to The latest time (Java time)
	 * @return A list of all states applicable to the Named object
	 */
	public List<State> getStates(List<String> states);


	/**
	 * Method to retrieve the last orbital state of a satellite as derived from a specific TLE. The method will return
	 * <li>The last orbital state sample applicable to the satellite</li>
	 * 
	 * @param satellite The name of the satellite
	 * @param derivedFromTleNamed The name of the TleParameters object the state must be derived from
	 * @param derivedFromTleTimestamped The timestamp of the TleParameter object that the state must be derived from
	 */
	public OrbitalState getOrbitalStateFor(String satellite, String derivedIssuedBy, String derivedFromTleNamed, long derivedFromTleTimestamped);

	/**
	 * Method to retrieve the last orbital state of a satellite as derived from the last TLE. The method will return
	 * <li>The last orbital state sample applicable to the satellite, derived from the last TLE parameters.</li>
	 * 
	 * @param satellite The name of the satellite
	 */
	public OrbitalState getOrbitalStateFor(String satellite);


	/**
	 * Method to retrieve the orbital state of a satellite, identified throughs its name, within
	 * a given interval. The method will return
	 * <li>All orbital state samples applicable to the satellite</li>
	 * <li>With a sample timestamp between 'from' and 'to'</li>
	 * <li>and sorted on timestamp in ASCENDING order</li>
	 * 
	 * @param satellite Unique name of the satellite
	 * @param from The earliest time (Java time).
	 * @param to The latest time (Java time)
	 * @return A list of all orbital states applicable to the satellites
	 */
	public List<OrbitalState> getOrbitalStatesFor(String satellite, long from, long to);

	/**
	 * Method to retrieve the orbital state of a satellite, identified throughs its name, within
	 * a given interval. The method will return
	 * <li>All orbital state samples applicable to the satellite</li>
	 * <li>With a sample timestamp between 'from' and 'to'</li>
	 * <li>and sorted on timestamp in ASCENDING order</li>
	 * 
	 * @param satellite Unique name of the satellite
	 * @param from The earliest time (Java time).
	 * @param to The latest time (Java time)
	 * @param derivedTimestamp  The timestamp of the TLE parameters object that this must have been derived from
	 * @return A list of all orbital states applicable to the satellites
	 */
	public List<OrbitalState> getOrbitalStatesFor(String satellite, long from, long to, String derivedIssuedBy, String derivedFromName, long derivedFromTimestamp);

	/**
	 * Method to retrieve the TLE of a satellite, identified throughs its name. The method will return
	 * <li>The TLE (if existent) of the satellite</li>
	 * <li>With the latest timestamp</li>
	 * 
	 * @param satellite Unique name of the satellite
	 * @return A list of all orbital states applicable to the satellites
	 */
	public TleOrbitalParameters getTleFor(String satellite);

	/**
	 * Method to retrieve the orbital state of a satellite, identified throughs its name, within
	 * a given interval. The method will return
	 * <li>All orbital state samples applicable to the satellite</li>
	 * <li>With a sample timestamp between 'from' and 'to'</li>
	 * <li>and sorted on timestamp in ASCENDING order</li>
	 * 
	 * @param satellite Unique name of the satellite
	 * @param from The earliest time (Java time).
	 * @param to The latest time (Java time)
	 * @return A list of all orbital states applicable to the satellites
	 */
	public List<TleOrbitalParameters> getTleFor(String satellite, long from, long to);


	/**
	 * Method to retrieve the next contact start/end event pair. The method will return
	 * <li>Two events, the first being the start event and the next being the following end event.</li>
	 * 
	 * @param location
	 * @return
	 */
	public List<LocationContactEvent> getNextLocationContactEventsForLocation(String location);

	/**
	 * Method to retrieve the next contact start/end event pair. The method will return
	 * <li>Two events, the first being the start event and the next being the following end event.</li>
	 * 
	 * @param location
	 * @return
	 */
	public List<LocationContactEvent> getNextLocationContactEventsForLocation(String location, long from);

	/**
	 * Method to retrieve the next contact start/end event pair. The method will return
	 * <li>Two events, the first being the start event and the next being the following end event.</li>
	 * 
	 * @param location
	 * @return
	 */
	public List<LocationContactEvent> getNextLocationContactEventsFor(String location, String satellite);

	/**
	 * Method to retrieve the next contact start/end event pair. The method will return
	 * <li>Two events, the first being the start event and the next being the following end event.</li>
	 * 
	 * @param location
	 * @return
	 */
	public List<LocationContactEvent> getNextLocationContactEventsFor(String location, String satellite, long from);

	/**
	 * Method to retrieve the next pointing data to a specific satellite. The method will return
	 * <li>All contact data related to the next set of contact events (start-end of contact).</li>
	 * <li>Of the specified location</li>
	 * <li>And ANY satellite</li>
	 * <li>Sorted ASCENDING on timestamp</li>
	 * 
	 * @param location The location for which to retrieve the contact data for. 
	 * 
	 * @return List containing the next set of contact data
	 */
	public List<PointingData> getNextLocationPointingDataForLocation(String location);

	/**
	 * @param satellite
	 * @return
	 */
	public List<PointingData> getNextLocationPointingDataForSatellite(String satellite);

	/**
	 * Method to retrieve the next pointing data to a specific satellite. The method will return
	 * <li>All contact data related to the next set of contact events (start-end of contact).</li>
	 * <li>Of the specified location</li>
	 * <li>And the specified satellite</li>
	 * <li>Sorted ASCENDING on timestamp</li>
	 * 
	 * @param location The location for which to retrieve the contact data for. 
	 * @param satellite The name of the satellite 
	 * 
	 * @return List containing the next set of contact data
	 */
	public List<PointingData> getNextLocationPointingDataFor(String location, String satellite);

	/**
	 * Method to retrieve the next pointing data to a specific satellite. The method will return
	 * <li>All contact data related to the set of contact events (start-end of contact).</li>
	 * <li>Lying in the from-to period.</li>
	 * <li>Of the specified location</li>
	 * <li>And the specified satellite</li>
	 * <li>Sorted ASCENDING on timestamp</li>
	 * 
	 * @param location The location for which to retrieve the contact data for. 
	 * @param satellite The name of the satellite 
	 * 
	 * @return List containing the next set of contact data
	 */	
	public List<PointingData> getLocationPointingDataFor(String location, long from, long to);

	
	/**
	 * Method to retrieve the metadata of an object.
	 * 
	 * @param subject The Named object that the metadata must be applicable to.
	 * @return A list with zero or more metadata objects applicable to the subject
	 */
	public List<Named> getMetadata(Named subject);

	
	
	/**
	 * Retrieve all events from the given period.
	 * 
	 * @param from Start time of request (null means 'no lower bound')
	 * @param to End time of request (null means 'no upper bound')
	 * @return
	 */
	public List<Event> getEvents(Long from, Long to);
}
