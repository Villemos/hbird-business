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

import java.lang.management.ManagementFactory;
import java.net.UnknownHostException;

import org.hbird.exchange.core.Parameter;

public class CpuMonitor extends Monitor {

	public CpuMonitor(String componentId) {
		super(componentId);
	}
	
	/**
	 * Method to create a new instance of the memory parameter. The body of the 
	 * exchange will be updated.
	 * 
	 * @param exchange The exchange to hold the new value.
	 * @throws UnknownHostException 
	 */
	public Parameter check() {		
		return new Parameter(componentId, "Average CPU Usage", "MonitoredResource", "The average CPU usage the last minute.", ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage(), "Percentage/CPU");
	}	
}
