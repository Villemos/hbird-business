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
package org.hbird.business.systemmonitoring;

import java.io.File;
import java.net.UnknownHostException;

import org.hbird.exchange.core.Parameter;

public class HarddiskMonitor extends Monitor {

	public HarddiskMonitor(String componentId) {
		super(componentId);
	}

	protected File rootFile = null;
	
	{
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			rootFile = new File("c://");
		}
		else {
			rootFile = new File("/");
		}
	}
	
	/**
	 * Method to create a new instance of the memory parameter. The body of the 
	 * exchange will be updated.
	 * 
	 * @param exchange The exchange to hold the new value.
	 * @throws UnknownHostException 
	 */
	public Parameter check() throws UnknownHostException {
		return new Parameter(componentId, "Free Harddisk", "MonitoredResource", "The free harddisk space.", rootFile.getFreeSpace(), "Byte");
	}
}
