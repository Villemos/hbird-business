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

/**
 * @author Gert Villemos
 * 
 *         A Label is a String value used to denote something. It can for example be a
 *         calibrated value describing something as 'ON' or 'OFF'.
 * 
 */
public class Label extends ApplicableTo {

    private static final long serialVersionUID = 6064865233847879877L;

    /** The label value. */
    protected String value = "";
    protected String insertedBy = "";

    /**
     * Creates a Parameter with a timestamp set to 'now'.
     * 
     * @param issuedBy The name of the source of this label.
     * @param name The name of the label
     * @param type The Ontology type of this object. Is not 'String', but for example 'Calibration'.
     * @param description A description of the label.
     * @param value An object holding the value of the label.
     * @param insertedBy The name of the operator who inserted this label.
     */
    public Label(String ID, String name) {
        super(ID, name);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getInsertedBy() {
        return insertedBy;
    }

    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }
    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        builder.append("ID", getInstanceID());
        builder.append("name", name);
        builder.append("value", value);
        builder.append("timestamp", timestamp);
        builder.append("applicableTo", applicableTo);
        builder.append("issuedBy", issuedBy);
        builder.append("insertedBy", insertedBy);
        return builder.build();
    }
}
