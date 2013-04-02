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
 * 
 */
package org.hbird.exchange.constants;

/**
 * Constant values for standard destinations in Hummingbird based systems.
 */
public class StandardComponents {

    public static final String ARCHIVE_NAME = "Archive";
    public static final String ARCHIVE_DESC = "The archive of data";
    
    public static final String COMMAND_RELEASER_NAME = "CommandReleaser";
    public static final String COMMAND_RELEASER_DESC = "A component for managing the release of commands.";

    public static final String CONFIGURATOR_NAME = "Configurator";
    public static final String CONFIGURATOR_DESC = "A component for starting other components.";

	public static final String IMPORTER_NAME = "Importer";
	public static final String IMPORTER_DESC = "A component for importing data from files.";

    public static final String ORBIT_PROPAGATOR_NAME = "OrbitPropagator";
    public static final String ORBIT_PROPAGATOR_DESC = "Component for performing TLE based orbit prediction, including contact events and orbital states.";

    public static final String SYSTEM_MONITORING_NAME = "SystemMonitoring";
    public static final String SYSTEM_MONITORING_DESC = "Component for monitoring the system resources, such as CPU and memory usages.";

    public static final String TASK_EXECUTOR_NAME = "TaskExecutor";
    public static final String TASK_EXECUTOR_DESC = "Component for executing scheduled tasks.";

    public static final String WEB_SOCKET_NAME = "Websockets";
    public static final String WEB_SOCKET_DESC = "Component which will publish data on websockets";

}
