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
package org.hbird.business.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.camel.Body;
import org.apache.camel.Headers;

/** Maps all POJO fields to JMS header fields, leaving the POJO unchanged. If
 * you only want specific fields, consider using the 'SelectedFields' mapper.
 *
 * This class can be added to a camel route, to map the fields of a POJO to
 * JMS message header fields. The JMS message, containing the POJO in its 
 * body, will thereafter also list all POJO fields in its header. The mapper
 * will take public, protected as well as private fields, from this class as
 * well as all super classes.
 * 
 * This is useful for filtering in for example routes using ActiveMQ, where only
 * the header fields can be used for filtering / routing.
 *  
 * NOTE: This class is strictly not needed. The same effect can be gained by
 * using Camel scripting the the route, for example 
 * 
 * <route>
 *   <...>
 *   <setHeader name="Name">
 *     <simple>${body.getName}</simple>
 *   </setHeader>
 *   <...>
 * </route>
 *  
 * Which will create a header field 'Name' set to the value of the body objects
 * return of 'getName'. Mapping many fields in many different routes can however 
 * seriously over complicate the route definitions and lead to a bloat of XML.
 * In addition the header fields of any new types of data transfered in a route
 * will have to be reflected in all script based mappings as well. 
 *   
 * Using a single instance of this class, reused in different routes, may thus
 * be a better alternative
 *
 * <bean id="mapper" class="org.hbird.exchange.utils.AllFields"/>
 * 
 * <route id="Route1">
 *   <...>
 *   <to uri="bean:mapper"/>
 *   <...>
 * </route>
 *
 * <route id="Route2">
 *   <...>
 *   <to uri="bean:mapper"/>
 *   <...>
 * </route>
 */ 
public class AllFields {

	/**
	 * The method will access the IN message of the exchange, and read the
	 * body. The body will typically contain a POJO to be transfered in the
	 * route. All fields of the POJO will be read using reflection, and the 
	 * exchange IN message edited, setting a header field for each POJO field.
	 * The header field will become;
	 * - [field name]:[field value as String]
	 * The exchange and the IN body is not edited.
	 * 
	 * @param exchange The exchange carrying the message to be mapped.
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public synchronized void process(@Body Object body, @Headers Map<String, Object> headers) throws IllegalArgumentException, IllegalAccessException {

		/** Find all fields of this class as well as all super classes. */
		Map<String, Field> fields = new HashMap<String, Field>();
		recursiveGet(body.getClass(), fields);

		/** Iterate through all fields, and map them to header fields. */
		Iterator<Entry<String, Field>> it = fields.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Field> entry = it.next();

			/** Ensure that the field is accessible, i.e. if its protected or private we
			 * could else not be able to read the value. */
			entry.getValue().setAccessible(true);

			/** If the value is null, then we dont map. */
			if (entry.getValue().get(body) != null) {
				/** Set the header field name to the name of the field and the 
				 * value to the value of the field. */		
				headers.put(entry.getKey(), entry.getValue().get(body).toString());
			}
		}		
	}


	/**
	 * Method to list all fields of the 'clazz', including all inherited fields of all
	 * super classes, private as well as protected.
	 * 
	 * @param clazz The name of the class of which all fields should be listed.
	 * @param fields The map of all private, protected and public fields of the clazz and all its super classes. 
	 * The map is keyed on the field name, and the value is the field.
	 */
	protected void recursiveGet(Class<?> clazz, Map<String, Field> fields) {

		for (Field field : clazz.getDeclaredFields()) {
			fields.put(field.getName(), field);
		}
		if (clazz.getSuperclass() != null) {
			recursiveGet(clazz.getSuperclass(), fields);
		}
	}
}
