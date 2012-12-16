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
package org.hbird.business.celestrack;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.hbird.exchange.navigation.Satellite;

import com.villemos.ispace.httpcrawler.HttpAccessor;
import com.villemos.ispace.httpcrawler.HttpClientConfigurer;
import com.villemos.ispace.httpcrawler.HttpCrawlerConsumer;

/**
 * Specialized crawler for crawling the ktree website.
 * 
 * The crawler will access a initial folder, thereafter iterate through all found folders
 * collecting folder and document information as it goes. 
 * 
 * The end result is a Map, containing a set of lists describing the found data.
 * 
 */
public class CelestrackAccessor extends HttpAccessor {

	private static final Log Logger = LogFactory.getLog(CelestrackAccessor.class);

	protected CamelContext camelContext = null;

	protected AsyncProcessor asyncProcessor = null;
	
	public CelestrackAccessor(Endpoint endpoint, Object object, AsyncProcessor asyncProcessor) {
		super(endpoint);
		this.asyncProcessor = asyncProcessor;
	}

	public CelestrackAccessor(Endpoint endpoint, HttpCrawlerConsumer consumer) {
		super(endpoint, consumer);
	}

	public void doPoll() throws Exception {
		this.poll();
	}

	

	/** Setup the initial login form request. This method is called by
	 * the main method in the iSpace http crawler, to create the form
	 * login. The framework will thereafter call 'processSite'. 
	 * 
	 * The authentication is managed by the iSpace framework.
	 *  
	 */
	@Override
	protected HttpUriRequest createInitialRequest(URI uri) {
		return new HttpGet("http://www.celestrak.com/NORAD/elements/master.asp");
	}

	/** Method called by the iSpace httpcrawler when the initial request has
	 * been performed and succeeded. The page corresponds to the first
	 * page AFTER the login. */
	@Override
	protected void processSite(URI uri, HttpResponse response) throws IOException {

		/** read the complete page. */
		String page = HttpClientConfigurer.readFully(response.getEntity().getContent());
		
		/** Readin the sat catalogue. */
		HttpGet get = new HttpGet("http://celestrak.com/pub/satcat.txt");
		response = client.execute(get);
		String satCatalogue = HttpClientConfigurer.readFully(response.getEntity().getContent());
		String[] objects = satCatalogue.split("\n");
		
		Map<String, String> objectCatalogue = new HashMap<String, String>();
		for (String object : objects) {
			objectCatalogue.put(object.substring(14, 16).trim(), object.substring(0, 13).trim());
		}		
		
		/** Read the txt files containing the line elements. */
		Pattern pattern = Pattern.compile("(<A HREF=\"(.*?).txt\">(.*?).txt</A>)");
		Matcher matcher = pattern.matcher(page);
		while (matcher.find()) {
			String data = HttpClientConfigurer.readFully(response.getEntity().getContent());
			
			String[] elements = data.split("\n");
			
			int index = 0;
			while (index < elements.length) {
				Satellite satellite = new Satellite();
				
				String name = elements[index];				
				satellite.setName(name);
				
				String satelliteNumber = elements[index+1].substring(1, 6);
				satellite.setSatelliteNumber(satelliteNumber);
				
				/** TODO Extract the orbit*/
		
				/** Retrieve the description. */
				get = new HttpGet("http://nssdc.gsfc.nasa.gov/nmc/masterCatalog.do?sc=" + objectCatalogue.get(satelliteNumber));
				response = client.execute(get);
				String descriptionPage = HttpClientConfigurer.readFully(response.getEntity().getContent());				
				
				Pattern descriptionPattern = Pattern.compile("<div class=\"urone\">//s+<h2>Description</h2>(.*?)</div><div class=\"urtwo\">");
				Matcher descriptionMatcher = descriptionPattern.matcher(descriptionPage);
				if (descriptionMatcher.find()) {
					satellite.setDescription(descriptionMatcher.group(1));
				}
				
				index += 3;
			}
		}		
	}
	
	protected void send(Object body) {
		Exchange exchange = getEndpoint().createExchange();
		exchange.getIn().setBody(body);
		
		asyncProcessor.process(exchange, new AsyncCallback() {
			public void done(boolean doneSync) {
				Logger.trace("Done processing URL");
			}
		});
	} 
}
