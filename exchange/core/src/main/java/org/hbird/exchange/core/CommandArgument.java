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

import java.io.Serializable;

public class CommandArgument implements Serializable {

    private static final long serialVersionUID = 8198716970891183855L;

    private final String name;
    private final String description;
    private final Class<?> type;
    private final String unit;
    private Object value;
    private final Boolean mandatory;

    public CommandArgument(String name, String description, Class<?> type, String unit, Object value, Boolean mandatory) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.unit = unit;
        this.value = value;
        this.mandatory = mandatory;
    }

    public CommandArgument(String name, String description, Class<?> type, Boolean mandatory) {
        this(name, description, type, null, null, mandatory);
    }

    public CommandArgument(CommandArgument copyFrom) {
        this(copyFrom.getName(), copyFrom.getDescription(), copyFrom.getType(), copyFrom.getUnit(), copyFrom.getValue(), copyFrom.getMandatory());
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the type
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @return the mandatory
     */
    public Boolean getMandatory() {
        return mandatory;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }
}
