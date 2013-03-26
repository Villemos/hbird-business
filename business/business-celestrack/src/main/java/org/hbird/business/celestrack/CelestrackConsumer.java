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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ProxySelector;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.ScheduledPollConsumer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.hbird.exchange.navigation.TleOrbitalParameters;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;


public class CelestrackConsumer extends ScheduledPollConsumer  {

	private static final Log LOG = LogFactory.getLog(CelestrackConsumer.class);

	protected Endpoint endpoint = null;

	protected DefaultHttpClient client = null;

	protected Pattern pattern = Pattern.compile("(.*?)\n(.*?)\n(.*?)", Pattern.MULTILINE);
	
	protected List<String> uris = new ArrayList<String>();
	{
		uris.add("http://www.celestrak.com/NORAD/elements/cubesat.txt");
	}

	public CelestrackConsumer(DefaultEndpoint endpoint, Processor processor) {
		super(endpoint, processor);
		this.endpoint = endpoint;
	}

	public CelestrackConsumer(Endpoint endpoint, Processor processor, ScheduledExecutorService executor) {
		super(endpoint, processor, executor);
		this.endpoint = endpoint;
	}

	protected CelestrackEndpoint getCelestractEndpoint() {
		return (CelestrackEndpoint) endpoint;
	}
	
	@Override
	protected int poll() throws Exception {

		client = new DefaultHttpClient();

		String proxyHost = getCelestractEndpoint().getProxyHost();
		Integer proxyPort = getCelestractEndpoint().getProxyPort();

		if (proxyHost != null && proxyPort != null) {
			HttpHost proxy = new HttpHost(proxyHost, proxyPort);
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
		else {
			ProxySelectorRoutePlanner routePlanner = new ProxySelectorRoutePlanner(client.getConnectionManager().getSchemeRegistry(), ProxySelector.getDefault());  
			client.setRoutePlanner(routePlanner);
		}


		for (String uri : uris) {
			HttpResponse response = client.execute(new HttpGet(uri));
			
			if (response.getStatusLine().getStatusCode() == 200) {
				Matcher matcher = pattern.matcher(readFully(response.getEntity().getContent()));
				while (matcher.find()) {
					send(new TleOrbitalParameters("Celestrack", matcher.group(1), matcher.group(1), matcher.group(2), matcher.group(3)));
				}
			}
		}

		return 0;
	}

	protected static String readFully(InputStream input) throws IOException {

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
		StringBuffer result = new StringBuffer();
		char[] buffer = new char[4 * 1024];
		int charsRead;
		while ((charsRead = bufferedReader.read(buffer)) != -1) {
			result.append(buffer, 0, charsRead);
		}
		input.close();
		bufferedReader.close();

		return result.toString();
	}
	
	protected void send(TleOrbitalParameters parameters) {
		Exchange exchange = new DefaultExchange(getEndpoint().getCamelContext());
		exchange.getIn().setBody(parameters);

		getAsyncProcessor().process(exchange, new AsyncCallback() {
			public void done(boolean doneSync) {
				LOG.trace("Done processing URL");
			}
		});
	}
}
