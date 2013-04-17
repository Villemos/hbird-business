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
import org.hbird.business.api.ICatalogue;
import org.hbird.business.api.IPublish;
import org.hbird.business.celestrack.CelestrackComponent;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;


/**
 * Bean to access the Celestrack website and pull TLE specifications.
 * 
 * @author Gert Villemos
 *
 */
public class CelestrackReader  {

	/** The logger. */
	private static final Log LOG = LogFactory.getLog(CelestrackReader.class);

	/** The HTTP client used to access the remote URL */
	protected DefaultHttpClient client = new DefaultHttpClient();

	/** The Part that this bean is the implementation of. Holds the configuration variables. */
	protected CelestrackComponent part = null;

	
	/**
	 * Constructor registering the Part that this bean is implementing.
	 * 
	 * @param part The part that this bean is the implementation of
	 */
	public CelestrackReader(CelestrackComponent part) {
		this.part = part;
	}
	
	/**
	 * Method to access the Celestrack website, download the TLE file ('elements'), extract the TLEs and 
	 * publish them to the system.
	 * 
	 * @return 0 (always)
	 * @throws Exception
	 */
	@Handler
	public int read() throws Exception {

		if (part.getProxyHost() != null) {
			HttpHost proxy = new HttpHost(part.getProxyHost(), part.getProxyPort());
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
		else {
			ProxySelectorRoutePlanner routePlanner = new ProxySelectorRoutePlanner(client.getConnectionManager().getSchemeRegistry(), ProxySelector.getDefault());  
			client.setRoutePlanner(routePlanner);
		}

		IPublish api = ApiFactory.getPublishApi(part.getName());
		
		ICatalogue catalogueApi = ApiFactory.getCatalogueApi(part.getName());
		
		long now = System.currentTimeMillis();
		
		for (String uri : part.getElements().split(":")) {
			HttpResponse response = client.execute(new HttpGet("http://www.celestrak.com/NORAD/elements/" + uri + ".txt"));
			
			if (response.getStatusLine().getStatusCode() == 200) {
				String text = readFully(response.getEntity().getContent());
				String elements[] = text.split("\n");
				
				for (int index = 0; index < elements.length; index += 3) {
					Satellite satellite = null;
					Object object = catalogueApi.getSatelliteByName(elements[index].trim());
					if (object == null) {
						/** Satellite unknown. Create placeholder object. */
						satellite = new Satellite(elements[index].trim(), "Satellite in Celestrack");
						api.publish(satellite);
					}
					else {
						satellite = (Satellite) object;
					}
					
					api.publish(new TleOrbitalParameters("Celestrack", elements[index].trim() + "/TLE", satellite.getID(), elements[index + 1].trim(), elements[index + 2].trim()));
				}
			}
		}

		return 0;
	}

	/**
	 * Helper method to read a HTML byte stream returned through 
	 * 
	 * @param input The input stream
	 * @return A string of the complete data
	 * @throws IOException
	 */
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
