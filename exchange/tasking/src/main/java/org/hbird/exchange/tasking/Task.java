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
import java.util.List;

import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.interfaces.IScheduled;





/**
 * An abstract implementation of a task. Contains only the execution time.
 */
public abstract class Task extends EntityInstance implements IScheduled {

	/** The unique UID */
	private static final long serialVersionUID = 6287812296391672915L;
	
	/** The ABSOLUTE execution time of the task. */
	protected long executionTime = 0;
	
	/** The RELATIVE execution time of the task. Can be used to calculate the ABSOLUTE execution time based
	 *  on a start time as; START_TIME + executionDelay = executionTime. */
	protected long executionDelay = 0;	
	
	/** 
	 * 0 = Continuously
	 * X = (X > 0) X time
	 * 
	 * */
	protected long repeat = 1;
	protected long count = 0;
	
	/**
	 * Constructor
	 * 
	 * @param name Name of the task
	 * @param description A description of the task
	 * @param executionTime The java time (ms) when the task should be executed. 0 is immediately.
	 */
	public Task(String ID, String name) {
		super(ID, name);
	}
	
	public abstract List<EntityInstance> execute();
	
	/* (non-Javadoc)
	 * @see org.hbird.exchange.commanding.Task#getExecutionDelay()
	 */
	public long getExecutionDelay() {
		return executionDelay; 
	}
	
	public void setExecutionDelay(long executionDelay) {
		this.executionDelay = executionDelay;
	}
	
	public long getExecutionTime() {
		return executionTime == 0 ? (new Date()).getTime() + executionDelay : executionTime;
	}

	public long getExecutionTime(long startTime) {
		return startTime + executionDelay;
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}

	public long getRepeat() {
		return repeat;
	}

	public void setRepeat(long repeat) {
		this.repeat = repeat;
	};

	public boolean isRepeat() {
		return repeat == 0 || count +1 < repeat;
	}
	
	public Task reschedule() {
		count++;
		
		if (executionTime == 0) {
			executionTime = ((new Date()).getTime());
		}
		
		executionTime = executionTime + executionDelay;
		return this;
	}
	
	public long getDelay() {
		Date now = new Date();
		long delay = executionTime - now.getTime();
		return delay <= 0 ? 0 : delay;
	}
	
	public long getDeliveryTime() {
		return executionTime;
	}
	

	public long getCount() {
		return count;
	}
}
