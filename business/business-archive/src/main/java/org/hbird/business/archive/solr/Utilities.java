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

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.hbird.exchange.core.EntityInstance;

import com.thoughtworks.xstream.XStream;

/**
 * Utilitity to read all results from SOLR and create a list of Named objects.
 * 
 * @author Gert Villemos
 *
 */
public class Utilities {
	
	protected static XStream xstream = new XStream();
	
	public static synchronized List<EntityInstance> getResultSet(QueryResponse response, int rows) {
		List<EntityInstance> results = new ArrayList<EntityInstance>();

		for (SolrDocument document : response.getResults()) {
			String inputString = (String) document.get("serialization");
			inputString = inputString.replaceAll("<!\\[CDATA\\[", "").replaceAll("\\]\\]", "");
			try {
				results.add((EntityInstance) xstream.fromXML(inputString));
			} catch (Exception e) {
				e.printStackTrace();
			}		
		}

		return results;
	}
}
