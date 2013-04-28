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
package org.hbird.exchange.core;

import org.hbird.exchange.interfaces.IState;

/**
 * The state of an .
 * 
 * Any other type may have a (set of) states associated. The association is
 * not through direct dependency, but through the states 'isstateOff' field.
 * 
 * @author Gert Villemos
 */
public class State extends EntityInstance implements IState {

    /** The unique UID. */
    private static final long serialVersionUID = 3658873252806807331L;

    /**
     * The ID of the Named object that this state parameter is the state of.
     * 
     * The attribute has the format; [name]:[type]:[timestamp].
     */
    protected String isStateOf;

    /** The value of the state. */
    protected Boolean state = true;

    /**
     * Constructor of the state parameter. The timestamp will be set to the current time.
     * 
     * @param stateName the name of this state.
     * @param description A description of this state.
     * @param isStateOff The object that this state is a state off.
     * @param state The current state.
     */
    public State(String issuedBy, String name, String description, String isStateOff, boolean state) {
        super(issuedBy, name, description);
        this.isStateOf = isStateOff;
        this.state = state;
    }

    /**
     * Constructor of the state parameter. The timestamp will be set to the current time.
     * 
     * @param stateName the name of this state.
     * @param description A description of this state.
     * @param timestamp The time at which the state parameter was calculated. Should be after the object
     *            that this parameter is a state off.
     * @param isStateOff The object that this state is a state off.
     * @param state The current state.
     */
    public State(String issuedBy, String name, String description, String isStateOff, boolean state, long timestamp) {
        this(issuedBy, name, description, isStateOff, state);
        setTimestamp(timestamp);
    }

    @Override
    public String getIsStateOf() {
        return isStateOf;
    }

    public void setIsStateOf(String isStateOf) {
        this.isStateOf = isStateOf;
    }

    /**
     * Method similar to the Parameter::getValue(), where the return value is
     * case to a Booolean. The StateParameter value must be a Boolean value.
     * 
     * @return The Parameter value cast to a Boolean.
     */
    @Override
    public Boolean getValue() {
        return state;
    }

    @Override
    public void setValid() {
        state = true;
    }

    @Override
    public void setInvalid() {
        state = false;
    }

    /**
     * Type safe setter for the StateParameter type. Checks that the value being set is
     * indeed an instance of Boolean prior to setting it. If this is not the case, then
     * the value is not set and a System.out warning is printed.
     * 
     * @param Object The value to be set. Must be a Boolean.
     */
    public void setValue(Boolean value) {
        this.state = value;
    }

    @Override
    public String prettyPrint() {
        return String.format("State {name=%s, state=%s, isStateOf=%s, timestamp=%s}", name, state, isStateOf, timestamp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hbird.exchange.interfaces.IIsStateOf#setIsStateOf(org.hbird.exchange.core.Named)
     */
    @Override
    public void setIsStateOf(EntityInstance isStateOf) {
        this.isStateOf = isStateOf == null ? null : isStateOf.getID();
    }
}
