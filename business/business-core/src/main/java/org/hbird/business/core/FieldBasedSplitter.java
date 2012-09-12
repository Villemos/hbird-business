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

public class FieldBasedSplitter extends FieldBasedAccessor {

	/**
	 * The name of the field in the body object to be accessed as a time. 
	 */
	protected String fieldName = "tasks";

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

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
