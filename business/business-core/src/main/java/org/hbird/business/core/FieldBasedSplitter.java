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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.Handler;

/**
 * The class can be used as the method for a Camel splitter.
 * 
 * @author Gert Villemos
 *
 */
public class FieldBasedSplitter {

	/**
	 * The name of the field in the body object to be accessed as a time. 
	 */
	protected String fieldName = "tasks";

	/**
	 * Method to return objects to be split.
	 * 
	 * @param body The object that contain the objects
	 * @return A Collection of the objects
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@Handler
	public List<Object> split(@Body Object body) throws IllegalArgumentException, IllegalAccessException {

		/** Get all fields of this class, including all superclass fields. */
		Map<String, Field> fields = new HashMap<String, Field>();
		recursiveGet(body.getClass(), fields);

		if (fields.containsKey(fieldName)) {
			fields.get(fieldName).setAccessible(true);

			if (fields.get(fieldName).get(body) instanceof List) {
				return (List<Object>) fields.get(fieldName).get(body);
			}
		}

		return new ArrayList<Object>();
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
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
