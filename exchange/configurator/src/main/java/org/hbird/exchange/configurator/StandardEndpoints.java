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

	/** Monitoring data. */
	public static String monitoring = "activemq:topic:hbird.monitoring";

	/** Tasks scheduled for execution. */
	public static String tasks = "activemq:queue:hbird.tasks";
	
	/** Command requests that are scheduled for verification. */
	public static String requests = "activemq:queue:hbird.requests";	

	/** Requests which failed verification, i.e. the commands inside were NOT released. */
	public static String failedRequests = "activemq:topic:hbird.failedRequests";
	
	/** Commands which have been verified and released. */
	public static String commands = "activemq:topic:hbird.commands";
	
	/** A topic for everybody monitoring what occurs in the system. */
	public static String notification = "activemq:topic:hbird.notification";

}
