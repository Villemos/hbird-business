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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hbird.exchange.interfaces.IEntity;
import org.hbird.exchange.interfaces.IState;

/**
 * The state of an .
 * 
 * Any other type may have a (set of) {@link IEntity}'s associated. The association is
 * not through direct dependency, but through the value of 'applicableTo' field.
 * 
 * @author Gert Villemos
 */
public class State extends ApplicableTo implements IState {

    private static final long serialVersionUID = 5139362988505657954L;

    /** The value of the state. */
    protected Boolean value = true;

    /**
     * Constructor of the state parameter. The timestamp will be set to the current time.
     * 
     */
    public State(String ID, String name) {
        super(ID, name);
    }

    /**
     * Method similar to the Parameter::getValue(), where the return value is
     * case to a Booolean. The StateParameter value must be a Boolean value.
     * 
     * @return The Parameter value cast to a Boolean.
     */
    @Override
    public Boolean getValue() {
        return value;
    }

    /**
     * Type safe setter for the StateParameter type. Checks that the value being set is
     * indeed an instance of Boolean prior to setting it. If this is not the case, then
     * the value is not set and a System.out warning is printed.
     * 
     * @param Object The value to be set. Must be a Boolean.
     */
    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        builder.append("ID", getInstanceID());
        builder.append("name", name);
        builder.append("state", value);
        builder.append("applicableTo", applicableTo);
        builder.append("issuedBy", issuedBy);
        return builder.build();
    }
}
