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

import java.util.Map;


/**
 * Class for metadata to another Named object. The object 'points' to the subject through the
 * 'subject' field.
 * 
 * @author Admin
 *
 */
public class Metadata extends Named {

	private static final long serialVersionUID = 2804335094757225970L;

	/** The object(s) that this metadata is applicable to (the subject of the metadata). The value must be in the format
	 *       [type]:[name]:[timestamp]
	 *       
	 *  Any of the fields can contain and/or be a wildcard (*). This can be used to apply the metadata to
	 *     [type]:*:*      All objects of a given type ('All Parameters')
	 *     [type]:[name]:* All objects with a specific type and name, regardless of timestamp ('All PARA1 instances'). 
	 *     *:*[name]:*      All objects with a name containing [name] 
	 *     
	 */
	protected String subject = "";
	
	/** The metadata itself. Can be any type, keyed on string. */
	protected Map<String, Object> metadata = null;

	public Metadata(String issuedBy, String name, String type, String description, String subject, Map<String, Object> metadata) {
		super(issuedBy, name, type, description);
		
		this.subject = subject;
		this.metadata = metadata;
	}
}
