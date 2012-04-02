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
package org.hbird.business.taskexecutor;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.apache.log4j.Logger;

import org.hbird.exchange.tasking.Task;


/**
 * The executor performs the tasks scheduled for a task. A task may have an
 * execution time delay set, i.e. a latency period before it should be executed.
 * This delay must be applied prior to this class being invoked, i.e. this class
 * does not apply the task execution delay constrain, it simply execute the task
 * 
 * The tasks should be read for example from an activemq queue dedicated for scheduled 
 * tasks. Multiple task executers may run in parallel, each processing separate tasks. 
 * 
 * Example of Usage;
 * 
 *   <bean id="taskExecutor" class="org.hbird.business.command.task.Executor"/>
 *   
 *   ...
 *   <route>
 *     <from uri="activemq:queue:Tasks"/>
 *     <to uri="bean:taskExecutor"/>
 *   </route>
 *   
 */
public class TaskExecutor {

	/** The class logger. */
	protected static Logger logger = Logger.getLogger(TaskExecutor.class);
	
	/** 
	 * Method for actually executing the task. The task will be extracted from the
	 * exchange body, which is expected to contain a task object.
	 * 
	 * @param arg0 The exchange carrying a task as its body.
	 * @throws InterruptedException 
	 */
	@Handler
	public void receive(@Body Object body) throws InterruptedException {

		/** Execute the task. The task may return a new object which is the body of the exchange. */
		body = ((Task) body).execute();
	}
}
