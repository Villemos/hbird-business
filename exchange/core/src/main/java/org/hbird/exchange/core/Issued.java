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

import java.util.Date;

import org.hbird.exchange.interfaces.IIssued;

/**
 * An issued object is an object that represents an instance. It is issued by a Part 
 * of the system and have a timestamp.
 * 
 * Each Issued object is described through;
 * <li>name (from Named). The name of the object. </li>
 * <li>issuedBy. The name of the Part that has created / issued the object.</li>
 * <li>timestamp. The time at which the object is valid.</li>
 *
 * Each of these attributes are in themselves not unique. But the combination is always unique; Each part can
 * only have one Named object with a given name. And each of these can only be instantiated at a 
 * millisecond interval.
 * 
 * As an example consider the following example; Three parts, RotorA, RotorB and RadioA. RotorA and RotorB are of
 * the same 'type' i.e. both are rotors with the monitoring parameters 'Azimuth' and 'Elevation'.
 * 
 * The Named object instance 'name=Azimuth, issuedBy=Rotor1, timestamp=1111111111' can be uniquely allocated
 * to Rotor1. It can be destinquished from the parameter 'name=Azimuth, issuedBy=Rotor2, timestamp=1111111111'
 * 
 * @author Gert Villemos
 *
 */
public abstract class Issued extends Named implements IIssued {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2386750538796156605L;

	/** The ID (not Name) of the Part that has issued this object. */
    protected String issuedBy = "";

    /**
     * The time at which this object represented a valid state of the system. Default value is the
     * time of creation.
     */
    protected long timestamp = (new Date()).getTime();

    /**
	 * @param name
	 * @param description
	 */
	public Issued(String issuedBy, String name, String description) {
		super(name, description);
		this.issuedBy = issuedBy;
	}

	public Issued(String issuedBy, String name, String description, long timestamp) {
		super(name, description);
		this.issuedBy = issuedBy;
		this.timestamp = timestamp;
	}

    public String getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String prettyPrint() {
        return String.format("%s[name=%s, issuedBy=%s, timestamp=%s]", this.getClass().getSimpleName(), getQualifiedName(), timestamp);
    }
	
	public String getID() {
		return issuedBy + ":" + name + ":" + timestamp;
	}
}
