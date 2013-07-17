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

import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.core.Binary;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.core.Label;
import org.hbird.exchange.core.Metadata;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.hbird.exchange.tasking.Task;

/**
 * API Interface for publishing data to the system.
 * 
 * The API should be used by any element which needs to publish data that shall be distributed through
 * the hummingbird system. The API will create the data object and publish it to the underlying
 * protocol, typically being activemq. The further distribution of the object depends on the
 * assembly of the system.
 * 
 * @author Gert Villemos
 * 
 */
public interface IPublish extends IHbirdApi {

    /**
     * Method to force a commit of all data. Any process should flush buffers and cashes.
     */
    public void commit(String ID) throws Exception;

    public EntityInstance publish(EntityInstance object) throws Exception;

    public Parameter publishParameter(String ID, String name, String description, Number value, String unit) throws Exception;

    public Parameter publishParameter(String ID, String name, String description, Number value, String unit, long timestamp) throws Exception;

    public State publishState(String ID, String name, String description, String applicableTo, Boolean state) throws Exception;

    public State publishState(String ID, String name, String description, String applicableTo, Boolean state, long timestamp) throws Exception;

    public Label publishLabel(String ID, String name, String description, String value) throws Exception;

    public Binary publishBinary(String ID, String name, String description, byte[] rawdata) throws Exception;

    public Command publishCommand(String ID, String name, String description, List<CommandArgument> arguments) throws Exception;

    public CommandRequest publishCommandRequest(String ID, String name, String description, Command command) throws Exception;

    public CommandRequest publishCommandRequest(String ID, String name, String description, Command command, List<String> lockStates, List<Task> tasks) throws Exception;

    /**
     * Method to create and publish a piece of metadata associated to a Named object.
     * 
     * @param subject The subject of this metadata, i.e. the Named object being described.
     * @param key The key of the metadata.
     * @param metadata The value of the metadata
     */
    public Metadata publishMetadata(String ID, String name, EntityInstance subject, String key, String metadata) throws Exception;

    public TleOrbitalParameters publishTleParameters(String ID, String name, String satellite, String tle1, String tle2) throws Exception;
}
