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
package org.hbird.exchange.configurator;

public class StandardEndpoints {

    /**
     * Route managing the injection of data. Will ensure that the right headers are set and that
     * things like scheduling is performed. Route is defined in the 'injection.xml' Spring DSL.
     */
    public static final String INJECTION = "seda:injection";

    /** Monitoring data. */
    public static final String MONITORING = "activemq:topic:hbird.monitoring";

    /** Tasks scheduled for execution. */
    public static final String TASKS = "activemq:queue:hbird.tasks";

    /** Command requests that are scheduled for verification. */
    public static final String REQUESTS = "activemq:queue:hbird.requests";

    /** Requests which failed verification, i.e. the commands inside were NOT released. */
    public static final String FAILED_REQUESTS = "activemq:topic:hbird.failedRequests";

    /** Commands which have been verified and released. */
    public static final String COMMANDS = "activemq:topic:hbird.commands";

    /** Events in the system. */
    public static final String EVENTS = "activemq:topic:hbird.events";
}
