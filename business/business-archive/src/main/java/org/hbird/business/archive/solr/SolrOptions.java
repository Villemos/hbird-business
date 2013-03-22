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

public class SolrOptions {

	/** Header field indicating that this query is a 'normal' query. */
	public static final String query = "solr.option.query";
	
	public static final String delete = "solr.delete";
	
	public static final String facets = "solr.option.facets";
	
	public static final String insert = "solr.option.insert";

	public static final String comments = "solr.option.comments";
	
	public static final String commit = "solr.option.commit";
	
	/** Flag indicating that only the count should be returned. */
	public static final String count = "solr.option.count";

	/** Header field indicating that the results should be streamed back. The field value must be a ICallback object. */
	public static final String stream = "solr.option.stream";
		
	/** The offset of the retrieval. Can be used to retrieve 'pages' of data, i.e. first 10 results (offset=0), then 10 more (offset=10), then 10 more (offset=20), etc. */
	public static final String offset = "solr.option.start";
	public static final String facetsort = "solr.option.facetsort";
	public static final String facetlimit = "solr.option.facetlimit";
	public static final String facetprefix = "solr.option.facetprefix";
	public static final String facetfield = "solr.option.facetfield";	
	public static final String rows = "solr.option.rows";

}
