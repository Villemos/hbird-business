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

public class NamedInstanceIdentifier implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2768378707513305322L;

    private final String name;
    private final long timestamp;
    private final String type;

    public NamedInstanceIdentifier(String name, long timestamp, String type) {
        this.name = name;
        this.timestamp = timestamp;
        this.type = type;
    }

    public NamedInstanceIdentifier(Named parent) {
        this.name = parent.getName();
        this.timestamp = parent.getTimestamp();
        this.type = parent.getType();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
}
