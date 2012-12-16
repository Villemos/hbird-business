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

import java.util.Date;

public class Statistics {

	/** The time at which the request was issued. */
	public Date timestamp = new Date();
	
	/** Total number of entries in the repository matching the query. The number returned may
	 * be less, depending on the number of 'rows' the retrieval request was issued with. */
	public long totalFound = 0;

	/** Total number of entries requested, i.e. the 'rows'. */
	public long totalRequested = 0;
	
	/** The total number of results returned. The value will be the lowest of 'rows' or the 
	 * 'totalfound'. */
	public long totalReturned = 0;
	
	/** Time in milliseconds it took to retrieve the results. This delta time does not include the 
	 * time spend in the iSpace system. */
	public long queryTime = 0;

	/** The maximum score returned. */
	public Float maxScore = 0f;
}
