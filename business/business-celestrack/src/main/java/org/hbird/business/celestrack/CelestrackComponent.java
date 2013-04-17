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

import org.hbird.business.celestrack.http.CelestrackComponentDriver;
import org.hbird.business.core.StartablePart;

/**
 * Component for reading TLEs of satellites from the Celestrack website.
 * 
 * @author Gert Villemos
 *
 */
public class CelestrackComponent extends StartablePart {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6670616270121780868L;

	/** The celestrack file(s) to be extracted. In the format '[file 1]:[file 2]:...' */
	protected String elements = "cubesat";
	
	/** The period between pull of TLEs. */
	protected long period = 60 * 60 * 1000;

	/** The host name of the proxy if the system is running behind a firewall */
	protected String proxyHost = null;

	/** The host port of the proxy if the system is running behind a firewall */
	protected int proxyPort = 0;

	/** The default name of the component. */
	public static final String DEFAULT_NAME = "CelestrackReader";
	
	/** The default description of the component. */
	public static final String DEFAULT_DESCRIPTION = "Component to automatically read satellte TLEs from NOAAs celestrack";

	/** The name of the driver class. */
	public static final String DEFAULT_DRIVER = CelestrackComponentDriver.class.getName();

	
	/**
	 * Default constructor.
	 */
	public CelestrackComponent() {
		super(DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_DRIVER);
	}

	public String getElements() {
		return elements;
	}

	public void setElements(String elements) {
		this.elements = elements;
	}

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	/**
	 * @return the proxyHost
	 */
	public String getProxyHost() {
		return proxyHost;
	}

	/**
	 * @param proxyHost the proxyHost to set
	 */
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	/**
	 * @return the proxyPort
	 */
	public int getProxyPort() {
		return proxyPort;
	}

	/**
	 * @param proxyPort the proxyPort to set
	 */
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}
}
