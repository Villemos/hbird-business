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

import java.net.MalformedURLException;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.ScheduledPollEndpoint;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.hbird.exchange.constants.StandardArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The endpoint for production of solr producers. The endpoint does
 * not support consumers.
 * 
 * The endpoint manages connections to a underlying Solr {@link http://lucene.apache.org/solr/} repository. The
 * repository must be preinstalled and running. The Solr repository will be
 * accessed through HTTP, using the SolrJ API {@link http://wiki.apache.org/solr/Solrj} .
 * 
 * The preinstalled Solr server must have a Solr schema file, defining the fields allowed for
 * a document submitted for indexing. The endpoint assume per default that the following fields
 * are available
 * 'content'. The main field holding a large portion of text to be indexed.
 * 
 * The body of each insert exchanges are inserted in the content field.
 * 
 * In addition the exchange can contain any number of additional fields, set in the header.
 * Each field must have the format 'solr.field.[name]'. It must as value contain a value object or
 * a list of value objects. Setting the header field 'solr.field.url' with the value
 * 'file:c:/foo/baa.txt' will thus lead to the solr field 'url' being added to the solr document
 * prior to storage.
 *
 * @author Gert Villemos
 */
public class SolrEndpoint extends ScheduledPollEndpoint {

	/** Da' logger! */
	private static final transient Logger LOG = LoggerFactory.getLogger(SolrEndpoint.class);

	/** The name of the JVM property that can set the SOLR URL */
	public static final String PROPERTY_SOLR_URL = "solr.url";

	/** Default value of the SOLR URL*/
	public static final String DEFAULT_SOLR_URL = "http://localhost:8080/apache-solr-3.5.0/";

	/**
	 * The url of the SOLR server. The URL must have the format:
	 * [protocol]://[host IP or DNS name]:[port]/[path to solr]
	 */
	protected String solrServerUrl = System.getProperty(PROPERTY_SOLR_URL, DEFAULT_SOLR_URL);

	/** The server. Will be initialized upon first access. */
	protected SolrServer server = null;

	/** The default number of rows to be reteieved. */
	protected int rows = 100;

	/** The default sort field. */
	protected String sortField = StandardArguments.TIMESTAMP;

	/** The default sort order. */
	protected ORDER sortOrder = ORDER.desc;

	/** Default constructor. */
	public SolrEndpoint() {
	}

	/**
	 * Constructor for a specific URI and component
	 * 
	 * @param uri The URI of the endpoint
	 * @param component The SolrComponent building this endpoint
	 */
	public SolrEndpoint(String uri, SolrComponent component) {
		super(uri, component);
	}

	/**
	 * Constructor taking a full Camel URI
	 * 
	 * @param endpointUri The Camel URI
	 */
	public SolrEndpoint(String endpointUri) {
		super(endpointUri);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.camel.Endpoint#createProducer()
	 */
	@Override
	public Producer createProducer() throws Exception {
		return new SolrProducer(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.camel.Endpoint#createConsumer(org.apache.camel.Processor)
	 */
	@Override
	public Consumer createConsumer(Processor processor) throws Exception {
		throw new Exception();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.camel.IsSingleton#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Returns the server instance, providing a connection to the SOLR
	 * repository. The method will lazy load the connection if necessary.
	 * 
	 * @return A connection to the SolrServer or null upon connection failure.
	 */
	public synchronized SolrServer getServer() {

		if (server == null) {
			try {
				LOG.info("Using standalone solr server at URL '" + solrServerUrl + "'.");
				server = new CommonsHttpSolrServer(solrServerUrl);
			}
			catch (MalformedURLException e) {
				LOG.error("Failed to contact the solr server on URL '" + solrServerUrl + "'. Is the server running?");
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		return server;
	}

	/**
	 * Method to set the SolrServer object managing the Solr repository connection.
	 * 
	 * @param server The instance to be used. Setting 'null' will reinitialize the connection.
	 */
	public synchronized void setServer(SolrServer server) {
		this.server = server;
	}

	/**
	 * Sets the URL to the Solr server. Should be in the format
	 * http://[host IP or DSN][:[port]]/[path]
	 * 
	 * @param solrServerUrl
	 */
	public void setSolrServerUrl(String solrServerUrl) {
		this.solrServerUrl = solrServerUrl;
	}

	public String getSolrServerUrl() {
		return solrServerUrl;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public ORDER getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(ORDER sortOrder) {
		this.sortOrder = sortOrder;
	}
}
