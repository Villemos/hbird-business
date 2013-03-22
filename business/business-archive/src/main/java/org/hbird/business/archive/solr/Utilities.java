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
package org.hbird.business.archive.solr;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.hbird.business.archive.solr.ResultSet;
import org.hbird.exchange.core.Named;

import com.thoughtworks.xstream.XStream;

public class Utilities {
	
	private static final transient Logger LOG = LoggerFactory.getLogger(Utilities.class);

	protected static XStream xstream = new XStream();
	
	public static synchronized ResultSet getResultSet(QueryResponse response, int rows) {

		ResultSet set = new ResultSet();

		for (SolrDocument document : response.getResults()) {
			String inputString = (String) document.get("serialization");
			inputString = inputString.replaceAll("<!\\[CDATA\\[", "").replaceAll("\\]\\]", "");
			try {
				set.objects.add((Named) xstream.fromXML(inputString));
			} catch (Exception e) {
				e.printStackTrace();
			}		}

		set.statistics.queryTime = response.getQTime();
		set.statistics.totalFound = response.getResults().getNumFound();
		set.statistics.totalRequested = rows;
		set.statistics.totalReturned = response.getResults().size();
		set.statistics.maxScore = response.getResults().getMaxScore() == null ? 0f : response.getResults().getMaxScore();

		return set;
	}

	protected static Map<String, Field> getAllFields(Class clazz, Map<String, Field> fields) {
		/** See if there is a super class. */
		if (clazz.getSuperclass() != null) {
			getAllFields(clazz.getSuperclass(), fields);
		}

		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			fields.put(field.getName(), field);
		}

		return fields;
	}

	public static boolean isPrimitive(Object value) {
		return value instanceof String || 
		value instanceof Boolean ||
		value instanceof Long ||
		value instanceof Double ||
		value.getClass().isPrimitive();
	}
}
