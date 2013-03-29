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
package org.hbird.business.celestrack.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ProxySelector;

import org.apache.camel.Handler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.IPublish;
import org.hbird.exchange.navigation.TleOrbitalParameters;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;


public class CelestrackReader  {

	private static final Log LOG = LogFactory.getLog(CelestrackReader.class);

	protected DefaultHttpClient client = new DefaultHttpClient();

	protected String name = "Celestrack";
	
	protected String proxyHost = null;
	
	protected int proxyPort = 0;

	protected String elements = "cubesat";
	
	@Handler
	public int read() throws Exception {

		if (proxyHost != null) {
			HttpHost proxy = new HttpHost(proxyHost, proxyPort);
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
		else {
			ProxySelectorRoutePlanner routePlanner = new ProxySelectorRoutePlanner(client.getConnectionManager().getSchemeRegistry(), ProxySelector.getDefault());  
			client.setRoutePlanner(routePlanner);
		}

		IPublish api = ApiFactory.getPublishApi(name);
		
		for (String uri : elements.split(":")) {
			HttpResponse response = client.execute(new HttpGet("http://www.celestrak.com/NORAD/elements/" + uri + ".txt"));
			
			if (response.getStatusLine().getStatusCode() == 200) {
				String text = readFully(response.getEntity().getContent());
				String elements[] = text.split("\n");
				
				for (int index = 0; index < elements.length; index += 3) {
					api.publish(new TleOrbitalParameters("Celestrack", elements[index].trim(), elements[index].trim(), elements[index + 1].trim(), elements[index + 2].trim()));
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
}
