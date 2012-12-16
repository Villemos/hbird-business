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
package org.hbird.business.solr;

import java.util.HashMap;
import java.util.Map;

import org.hbird.exchange.core.Named;


/**
 * Data structure holding the definition of a facet. A facet is a specific field
 * with all the values associated with the field. 
 *
 */
public class Facet extends Named {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2469230224084701038L;

	/** The field for which the facet values are applicable. */
	public String field;
	
	/** Map keyed on facet value, with the value being a count of how often it occurs. */
	public Map<String, Long> values = new HashMap<String, Long>();
}
