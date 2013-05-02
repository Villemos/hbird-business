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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Gert Villemos
 *
 */
public class HierachicalView extends EntityInstance {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7751452991750905686L;

	/**
	 * @param ID
	 * @param name
	 */
	public HierachicalView(String ID, String name) {
		super(ID, name);
	}
	
	/** Map where key is CHILD, value is PARENT. As each child can have only one parent (in a specific View), the map entry is unique. */
	protected Map<String, String> relationships = new HashMap<String, String>();
	
	public void addRelationship(String child, String parent) {
		relationships.put(child, parent);
	}

	/**
	 * @return the relationships
	 */
	public Map<String, String> getRelationships() {
		return relationships;
	}

	/**
	 * @param relationships the relationships to set
	 */
	public void setRelationships(Map<String, String> relationships) {
		this.relationships = relationships;
	}
}
