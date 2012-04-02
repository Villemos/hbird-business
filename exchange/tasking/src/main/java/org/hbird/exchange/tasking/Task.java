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
package org.hbird.exchange.tasking;

import java.util.Date;

import org.hbird.exchange.core.Named;





/**
 * An abstract implementation of a task. Contains only the execution time.
 */
public class Task extends Named {

	/** The unique UID */
	private static final long serialVersionUID = 6287812296391672915L;
	
	/** The execution time of the task. */
	protected long executionTime = 0;
	
	{
		this.name = "";
		this.description = "";
	}
	
	public Task() {};
	
	/**
	 * Constructor
	 * 
	 * @param name Name of the task
	 * @param description A description of the task
	 * @param executionTime The java time (ms) when the task should be executed. 0 is immediately.
	 */
	public Task(String issuedBy, String name, String description, long executionTime) {
		super(issuedBy, name, description);
		this.executionTime = executionTime;
	}
	
	/* (non-Javadoc)
	 * @see org.hbird.exchange.commanding.Task#getExecutionDelay()
	 */
	public long getExecutionDelay() {
		Date now = new Date();
		return now.getTime() > executionTime ? 0 : executionTime - now.getTime();
	}
	
	public Object execute() {
		return null;
	};
}
