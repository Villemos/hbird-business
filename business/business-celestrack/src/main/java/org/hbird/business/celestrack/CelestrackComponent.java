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
import org.hbird.exchange.configurator.StartablePart;

/**
 * @author Gert Villemos
 *
 */
public class CelestrackComponent extends StartablePart {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6670616270121780868L;

	protected String elements = "cubesat";
	
	protected long period = 60 * 60 * 1000;

	protected String proxyHost = null;

	protected int proxyPort = 0;

	public static final String DEFAULT_NAME = "CelestrackReader";
	public static final String DEFAULT_DESCRIPTION = "Component to automatically read satellte TLEs from NOAAs celestrack";
	public static final String DEFAULT_DRIVER = CelestrackComponentDriver.class.getName();

	public CelestrackComponent() {
		super(DEFAULT_NAME, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_DRIVER);
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
}
