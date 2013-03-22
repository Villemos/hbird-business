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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.impl.ScheduledPollConsumer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import org.hbird.business.archive.solr.ResultSet;
import org.hbird.exchange.core.Named;

public class SolrConsumer extends ScheduledPollConsumer {

	private static final Log LOG = LogFactory.getLog(SolrConsumer.class);
	/** Time stamp of the last time a retrieval was performed. Can be used to do
	 * incremental retrievals. */

	protected Date lastRetrievalTime = new Date(0);

	protected DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	public SolrConsumer(DefaultEndpoint endpoint, Processor processor) {
		super(endpoint, processor);		
	}

	public SolrConsumer(Endpoint endpoint, Processor processor,
			ScheduledExecutorService executor) {
		super(endpoint, processor, executor);
	}

	@Override
	protected int poll() throws Exception {

		String queryString = getSolrEndpoint().getQuery();

		queryString = queryString.replaceAll("FROMLAST", format.format(lastRetrievalTime));
		lastRetrievalTime = new Date();

		SolrQuery query = new SolrQuery(queryString);		

		if (getSolrEndpoint().getQueryHandler() != null) {
			query.setQueryType(getSolrEndpoint().getQueryHandler());
		}

		/** Search and set result set. Notice that this will return the results upto the 
		 * configured number of rows. More results may thus be in the repository. */
		QueryResponse response = getSolrEndpoint().getServer().query(query);
		if (response.getStatus() != 0) {
			log.error("Failed to execute retrieval request. Failed with status '" + response.getStatus() + "'.");
		}

		/** Get the result set. */
		ResultSet results = Utilities.getResultSet(response, (int) response.getResults().getNumFound());

		/** Either deliver the complete result set as on batch, or as a stream. */
		if (getSolrEndpoint().getDeliveryMode().equals("batch")) {

			Exchange exchange = getEndpoint().createExchange();
			exchange.getIn().setBody(results);

			getAsyncProcessor().process(exchange, new AsyncCallback() {
				public void done(boolean doneSync) {
					LOG.trace("Done processing sending Batch.");
				}
			});
		}
		else {
			/** Iterate through the result set and inject the io objects. */			
			for (Named io : results.getResults()) {
				Exchange exchange = getEndpoint().createExchange();
				exchange.getIn().setBody(io);

				getAsyncProcessor().process(exchange, new AsyncCallback() {
					public void done(boolean doneSync) {
						LOG.trace("Done processing streaming information objects.");
					}
				});
			}			
		}

		return 0;
	}

	protected SolrEndpoint getSolrEndpoint() {
		return (SolrEndpoint) getEndpoint();
	}
}
