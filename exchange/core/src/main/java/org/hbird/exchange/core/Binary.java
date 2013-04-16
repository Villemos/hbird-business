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
 * Class holding binary data.
 * 
 * @author Admin
 * 
 */
public class Binary extends Named {

    private static final long serialVersionUID = -6631068705173628757L;

    /** The raw binary data */
    protected byte[] rawData = null;

    /** String to describe the binary format. */
    protected String format;

    /**
     * Constructor
     * 
     * @param issuedBy The name of the system element that issued this binary data
     * @param name Name of this binary data
     * @param type The type of the data
     * @param description A description of the data
     * @param rawData The raw data
     */
    public Binary(String issuedBy, String name, String description, byte[] rawData) {
        this(issuedBy, name, description, rawData, null);
    }

    /**
     * Constructor
     * 
     * @param issuedBy The name of the system element that issued this binary data
     * @param name Name of this binary data
     * @param type The type of the data
     * @param description A description of the data
     * @param rawData The raw data
     * @param format format of the raw data
     */
    public Binary(String issuedBy, String name, String description, byte[] rawData, String format) {
        super(issuedBy, name, description);
        this.rawData = rawData;
        this.format = format;
    }

    public byte[] getRawData() {
        return rawData;
    }

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
