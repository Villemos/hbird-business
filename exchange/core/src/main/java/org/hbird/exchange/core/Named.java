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
import java.util.Date;
import java.util.UUID;

/** The super class of all types being exchanged. Contains the information needed to uniquely identify
 *  an object as well as describe it.
 *  
 *  The class is intended to be subtyped, into specific types exchanged within the system such as 
 *  parameters, commands and tasks. Each subtype thus share a set of attributes.
 *  
 *  Some values are generated as a set of values, that belongs together, such as a
 *  orbit prediction which consists of multiple orbital states. A user will typically
 *  want the complete data set, and replace the data set when a new becomes available. 
 *  To support this objects can be assigned a datasetidentifier, which will be the 
 *  same for all entries in the data set. The data set identifier will typically be
 *  the generation time of the data set, and can thus also be used to identify which data
 *  set is the most recent.
 * */
public abstract class Named implements Serializable {

	/** The unique UID of this class. */
	private static final long serialVersionUID = -5803219773253020746L;

	/** The unique UID of this object. */
	protected String uuid = UUID.randomUUID().toString();
	
	/** The ID / Name of the component that has issued this object. */
	protected String issuedBy = "";
	
	/** The time at which this object represented a valid state of the system. Default value is the
	 *  time of creation. */
	protected long timestamp = (new Date()).getTime();
	
	/** The name of the object. */
	protected String name;

	/** A description of the object. */
	protected String description;

	/** A definition of what this parameter represents. Should not be 'double' or 'integer', but the
	 *  meaning of the type such as 'Temperature' or 'Charge'. */
	protected String type;
	
	/** A unique identifier of a set of data. All elements of the data set should be
	 *  assigned the same datasetidentifier, marking the data as being one logical
	 *  group. The identifier can be any number, but should in practice always be
	 *  the time at which the series was generated. Its the responsibility of the creator
	 *  of the objects to ensure that the identifier is unique and consistent. A 
	 *  value of 0 means no data series. */
	protected String datasetidentifier = null;

	public Named() {};
	
	/**
	 * Constructor of a Named object. The timestamp will be set to the creation time.
	 * 
	 * @param name The name of the object.
	 * @param description The description of the object.
	 */
	public Named(String issuedBy, String name, String type, String description) {
		this.issuedBy = issuedBy;
		this.name = name;
		this.type = type;
		this.description = description;
	}

	/**
	 * Constructor of a Named object with a specific timestamp. 
	 * 
	 * @param name The name of the object.
	 * @param description The description of the object.
	 * @param timestamp The timestamp of the object.
	 */
	public Named(String issuedBy, String name, String type, String description, long timestamp) {
		this.issuedBy = issuedBy;
		this.name = name;
		this.type = type;
		this.description = description;
		this.timestamp = timestamp;
	}

	/**
	 * Constructor of a Named object with a specific timestamp. 
	 * 
	 * @param name The name of the object.
	 * @param description The description of the object.
	 * @param timestamp The timestamp of the object.
	 */
	public Named(String issuedBy, String name, String type, String description, long timestamp, String datasetidentifier) {
		this.issuedBy = issuedBy;
		this.name = name;
		this.type = type;
		this.description = description;
		this.timestamp = timestamp;
		this.datasetidentifier = datasetidentifier;
	}

	public Named(String issuedBy) {
		this.issuedBy = issuedBy;
	}

	/**
	 * Getter of the object name.
	 * 
	 * @return The name of the object.
	 */
	public String getName() {
		return name;
	}
		
	/**
	 * Getter of the description of the object.
	 * 
	 * @return The description of the object.
	 */
	public String getDescription() {
		return description;
	}

	
	/**
	 * Setter of the object timestamp.
	 * 
	 * @param timestamp The time of the object. Default value is the creation time of the object.
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Getter of the dataset identifier.
	 * 
	 * @return The data set identifier, identifying the data set this object is part of.
	 * */
	public String getDatasetidentifier() {
		return datasetidentifier;
	}

	/**
	 * In case that an already created Parameter object is reused, i.e. is use to
	 * create a new object, then the objectid and the timestamp must be changed. Else
	 * the object will be considered the same as the previous object.
	 * 
	 * The method mark this object as a new object. The objectid is set to a new 
	 * value and the timestamp set to 'now'.
	 */
	public void newInstance() {
		timestamp = (new Date()).getTime();		
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDatasetidentifier(String datasetidentifier) {
		this.datasetidentifier = datasetidentifier;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	

	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Named instance() {
		uuid = UUID.randomUUID().toString();
		timestamp = (new Date()).getTime();
		
		return this;
	}
	
	public String prettyPrint() {
		return "class=" + this.getClass().getSimpleName() + ", name=" + name + ", timestamp=" + timestamp;
	}
}
