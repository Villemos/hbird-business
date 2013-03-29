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

/**
 * @author Admin
 * 
 *         A Label is a String value used to denote something. It can for example be a
 *         calibrated value describing something as 'ON' or 'OFF'.
 * 
 */
public class Label extends Issued {

    private static final long serialVersionUID = -6613944008898640558L;

    /** The label value. */
    protected String value = "";

    /**
     * Creates a Parameter with a timestamp set to 'now'.
     * 
     * @param issuedBy The name of the source of this label.
     * @param name The name of the label
     * @param type The Ontology type of this object. Is not 'String', but for example 'Calibration'.
     * @param description A description of the label.
     * @param value An object holding the value of the label.
     */
    public Label(String issuedBy, String name, String description, String value) {
        super(issuedBy, name, description);
        this.value = value;
    }

    public Label(Label base) {
        this(base.issuedBy, base.name, base.description, base.value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String prettyPrint() {
        return String.format("Label[name=%s, value=%s, timestamp=%s]", name, value, timestamp);
    }
}
