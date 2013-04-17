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
package org.hbird.business.systemmonitoring;

import org.hbird.business.core.StartablePart;
import org.hbird.business.systemmonitoring.bean.SystemMonitorComponentDriver;

/**
 * @author Gert Villemos
 *
 */
public class SystemMonitorComponent extends StartablePart {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3066458562440110882L;

    public static final String SYSTEM_MONITORING_NAME = "SystemMonitoring";
    public static final String SYSTEM_MONITORING_DESC = "Component for monitoring the system resources, such as CPU and memory usages.";
	public static final String DEFAULT_DRIVER = SystemMonitorComponentDriver.class.getName();
	
	public SystemMonitorComponent() {
		super(SYSTEM_MONITORING_NAME, SYSTEM_MONITORING_DESC, DEFAULT_DRIVER);
	}
}
