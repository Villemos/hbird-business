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
    private final String issuedBy;

    public NamedInstanceIdentifier(String issuedBy, String name, long timestamp) {
        this.name = name;
        this.timestamp = timestamp;
        this.issuedBy = issuedBy;
    }

    public NamedInstanceIdentifier(Issued issuedBy) {
    	this.issuedBy = issuedBy.getIssuedBy();
    	this.name = issuedBy.getName();
        this.timestamp = issuedBy.getTimestamp();
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

	public String getIssuedBy() {
		return issuedBy;
	}
}
