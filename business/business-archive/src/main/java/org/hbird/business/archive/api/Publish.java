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
package org.hbird.business.archive.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.hbird.business.api.HbirdApi;
import org.hbird.business.api.IPublish;
import org.hbird.business.archive.ArchiveComponent;
import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.core.Binary;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.core.Label;
import org.hbird.exchange.core.Metadata;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.dataaccess.CommitRequest;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.hbird.exchange.tasking.Task;

/**
 * API for publishing different kinds of objects to the system.
 * 
 * @author Gert Villemos
 * 
 */
public class Publish extends HbirdApi implements IPublish {

    /**
     * Constructor.
     * 
     * @param issuedBy The name/ID of the component that is using the API to send requests.
     */
    public Publish(String issuedBy) {
        super(issuedBy, ArchiveComponent.ARCHIVE_NAME);
    }

    public Publish(String issuedBy, CamelContext context) {
        super(issuedBy, ArchiveComponent.ARCHIVE_NAME, context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hbird.business.api.IPublish#publish(org.hbird.exchange.core.Named)
     */
    @Override
    public EntityInstance publish(EntityInstance object) {
        object.setIssuedBy(issuedBy);
        if (object instanceof Command && ((Command) object).getDestination() == null) {
            ((Command) object).setDestination(destination);
        }

        template.sendBody(inject, object);
        return object;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hbird.business.api.IPublish#publishParameter(java.lang.String, java.lang.String, java.lang.Number,
     * java.lang.String)
     */
    @Override
    public Parameter publishParameter(String ID, String name, String description, Number value, String unit) {
        return publishParameter(ID, name, description, value, unit, System.currentTimeMillis());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hbird.business.api.IPublish#publishParameter(java.lang.String, java.lang.String, java.lang.Number,
     * java.lang.String, long)
     */
    @Override
    public Parameter publishParameter(String ID, String name, String description, Number value, String unit, long timestamp) {
        Parameter parameter = new Parameter(ID, name);
        parameter.setDescription(description);
        parameter.setValue(value);
        parameter.setUnit(unit);
        parameter.setTimestamp(timestamp);

        return (Parameter) publish(parameter);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hbird.business.api.IPublish#publishState(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.Boolean)
     */
    @Override
    public State publishState(String ID, String name, String description, String isStateOf, Boolean state) {
        return publishState(ID, name, description, isStateOf, state, System.currentTimeMillis());
    }

    @Override
    public State publishState(String ID, String name, String description, String isStateOf, Boolean state, long timestamp) {
        State newState = new State(ID, name);
        newState.setDescription(description);
        newState.setApplicableTo(isStateOf);
        newState.setValue(state);
        newState.setTimestamp(timestamp);

        return (State) publish(newState);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hbird.business.api.IPublish#publishLabel(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Label publishLabel(String ID, String name, String description, String value) {
        Label label = new Label(ID, name);
        label.setDescription(description);
        label.setValue(value);
        return (Label) publish(label);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hbird.business.api.IPublish#publishBinary(java.lang.String, java.lang.String, byte[])
     */
    @Override
    public Binary publishBinary(String ID, String name, String description, byte[] rawData) {
        Binary binary = new Binary(ID, name);
        binary.setDescription(description);
        binary.setRawData(rawData);
        return (Binary) publish(binary);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hbird.business.api.IPublish#publishCommand(java.lang.String, java.lang.String, java.util.List)
     */
    @Override
    public Command publishCommand(String ID, String name, String description, List<CommandArgument> arguments) {
        Command command = new Command(ID, name);
        command.setDescription(description);
        command.setArgumentList(arguments);
        return (Command) publish(command);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hbird.business.api.IPublish#publishCommandRequest(java.lang.String, java.lang.String,
     * org.hbird.exchange.core.Command)
     */
    @Override
    public CommandRequest publishCommandRequest(String ID, String name, String description, Command command) {
        CommandRequest request = new CommandRequest(ID, name);
        request.setDescription(description);
        request.setCommand(command);
        return (CommandRequest) publish(request);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hbird.business.api.IPublish#publishCommandRequest(java.lang.String, java.lang.String,
     * org.hbird.exchange.core.Command, java.util.List, java.util.List)
     */
    @Override
    public CommandRequest publishCommandRequest(String ID, String name, String description, Command command, List<String> lockStates, List<Task> tasks) {
        CommandRequest request = new CommandRequest(ID, name);
        request.setDescription(description);
        request.setCommand(command);
        request.setLockStates(lockStates);
        request.setTasks(tasks);
        request.setIssuedBy(description);
        return (CommandRequest) publish(request);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hbird.business.api.IPublish#commit()
     */
    @Override
    public void commit(String ID) {
        template.sendBody(inject, new CommitRequest(ID));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hbird.business.api.IPublish#publichMetadata(org.hbird.exchange.core.Named, java.lang.String,
     * java.lang.String)
     */
    @Override
    public Metadata publishMetadata(String ID, String name, EntityInstance subject, String key, String value) {
        Map<String, Object> metadata = new HashMap<String, Object>();
        metadata.put(key, value);

        Metadata obj = new Metadata(ID, name);
        obj.setApplicableTo(subject.getID());
        obj.setIssuedBy(value);
        obj.setMetadata(metadata);

        return (Metadata) publish(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hbird.business.api.IPublish#publishTleParameters(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public TleOrbitalParameters publishTleParameters(String ID, String name, String satellite, String tleLine1, String tleLine2) {
        TleOrbitalParameters request = new TleOrbitalParameters(ID, name);
        request.setSatelliteId(satellite);
        request.setTleLine1(tleLine1);
        request.setTleLine2(tleLine2);

        return (TleOrbitalParameters) publish(request);
    }
}
