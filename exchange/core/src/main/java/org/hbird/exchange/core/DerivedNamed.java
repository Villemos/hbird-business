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


public class DerivedNamed extends Named implements IDerived {

	/**
	 * 
	 */
	private static final long serialVersionUID = -585663387325032094L;

	protected NamedInstanceIdentifier from = null;
	
	/**
	 * Constructor of a Named object. The timestamp will be set to the creation time.
	 * 
	 * @param name The name of the object.
	 * @param description The description of the object.
	 */
	public DerivedNamed(String issuedBy, String name, String type, String description, String derivedFromName, long derivedFromTimestamp, String derivedFromType) {
		super(issuedBy, name, type, description);
		this.issuedBy = issuedBy;
		this.name = name;
		this.type = type;
		this.description = description;

		this.from = new NamedInstanceIdentifier(derivedFromName, derivedFromTimestamp, derivedFromType);
	}

	/**
	 * Constructor of a Named object with a specific timestamp. 
	 * 
	 * @param name The name of the object.
	 * @param description The description of the object.
	 * @param timestamp The timestamp of the object.
	 */
	public DerivedNamed(String issuedBy, String name, String type, String description, long timestamp, String derivedFromName, long derivedFromTimestamp, String derivedFromType) {
		super(issuedBy, name, type, description);
		this.issuedBy = issuedBy;
		this.name = name;
		this.type = type;
		this.description = description;
		this.timestamp = timestamp;

		this.from = new NamedInstanceIdentifier(derivedFromName, derivedFromTimestamp, derivedFromType);
	}
	
	public NamedInstanceIdentifier from() {
		return from;
	}
}
