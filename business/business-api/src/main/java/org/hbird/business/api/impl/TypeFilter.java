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
package org.hbird.business.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.core.Named;

public class TypeFilter<T> {

	protected Class<?> clazz = null;
	
	public TypeFilter(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public List<T> getObjects(List<Named> elements) {
		List<T> results = new ArrayList<T>();
		for (Named entry : elements) {
			if (entry.getClass() == clazz) {
				results.add((T) entry);
			}
		}
		
		return results;	
	}	
}
