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
package org.hbird.estcube.mibreader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ProxySelector;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.IPublish;
import org.hbird.business.core.CommandablePart;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;


/**
 * 
 * 
 * @author Gert Villemos
 *
 */
public class Parser {

	private static final Log LOG = LogFactory.getLog(Parser.class);

	protected DefaultHttpClient client = new DefaultHttpClient();

	protected String proxyHost = null;

	protected int proxyPort = 0;

	protected List<CommandablePart> parts = null;
	
	public void parse() throws Exception {

		if (proxyHost != null) {
			HttpHost proxy = new HttpHost(proxyHost, proxyPort);
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
		else {
			ProxySelectorRoutePlanner routePlanner = new ProxySelectorRoutePlanner(client.getConnectionManager().getSchemeRegistry(), ProxySelector.getDefault());  
			client.setRoutePlanner(routePlanner);
		}

		HttpResponse response = client.execute(new HttpGet("http://tudengisatelliit.ut.ee:8001/svn/MCS/trunk/eu.estcube.sdc/src/main/resources/commands.xml"));

		Pattern commandPattern = Pattern.compile("<command>(.*?)</command>");
		Pattern commandNamePattern = Pattern.compile("<name>(.*?)</name>");
		Pattern commandDecriptionPattern = Pattern.compile("<description>(.*?)</description>");
		Pattern commandSystemPattern = Pattern.compile("<subsys>(.*?)</subsys>");
		Pattern commandArgumentPattern = Pattern.compile("<param><description>(.*?)</description><name>(.*?)</name><type little_endian=\"true\">(.*?)</type></param>");
		
		if (response.getStatusLine().getStatusCode() == 200) {
			String text = readFully(response.getEntity().getContent()).replaceAll("(\\n|\\t|\\r)", "");

			LOG.info("File length with comments=" + text.length());
			/** Remove all comments */
			text = text.replaceAll("<!--(.*?)-->", "");
			LOG.info("File length without comments=" + text.length());

			/** Turn the part list into something easier to use. */
			Map<String, CommandablePart> partsMap = new HashMap<String, CommandablePart>();
			for (CommandablePart part : parts) {
				partsMap.put(part.getName(), part);
			}			
			
			/** Parse the XML */
			Matcher commandMatcher = commandPattern.matcher(text);
			while (commandMatcher.find()) {
				String commandDefintion = commandMatcher.group(1);
				
				Matcher nameMatcher = commandNamePattern.matcher(commandDefintion);
				nameMatcher.find();
				String name = nameMatcher.group(1);
				
				Matcher descriptionMatcher = commandDecriptionPattern.matcher(commandDefintion);
				descriptionMatcher.find();
				String description = descriptionMatcher.group(1);

				/** Create the command */
				Command command = new Command(name, description);
				
				Matcher subsysMatcher = commandSystemPattern.matcher(commandDefintion);
				while (subsysMatcher.find() ) {
					String subsys = subsysMatcher.group(1);

					if (partsMap.containsKey(subsys)) {
						LOG.info("Adding command '" + name + "' to part '" + subsys + "'.");
						partsMap.get(subsys).addCommand(command);
					}
					else {
						LOG.error("Found command for part '" + subsys + "'. Parts is unknown. Check the Spring XML assembly.");
					}
				}
				
				Matcher argumentMatcher = commandArgumentPattern.matcher(commandDefintion);
				while (argumentMatcher.find() ) {
					String argumentDescription = argumentMatcher.group(1);
					String argumentName = argumentMatcher.group(2);
					String argumentType = argumentMatcher.group(3);
					
					Class<?> type = null;
					if (argumentType.contains("int")) {
						type = Integer.class;
					}
					else if (argumentType.contains("string")) {
						type = String.class;
					}
					else {
						LOG.error("Unknown type '" + argumentType + "'.");
					}

					LOG.info("Adding argument '" + argumentName + "' to command '" + name + "'.");
					command.addArgument(new CommandArgument(argumentName, argumentDescription, type, true));
				}				
			}
		}
		
		IPublish api = ApiFactory.getPublishApi("parser");
		for (CommandablePart part : parts) {
			LOG.info("Publishing satellite part (subsystem) '" + part.getName() + "'");
			api.publish(part);
		}
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

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public List<CommandablePart> getParts() {
		return parts;
	}

	public void setParts(List<CommandablePart> parts) {
		this.parts = parts;
	}
}
