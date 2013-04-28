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
package org.hbird.business.systemtest;

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.tasking.Task;

/**
 * @author Admin
 *
 */
public class DummyTask extends Task {

	/**
	 * @param issuedBy
	 * @param name
	 * @param description
	 * @param executionDelay
	 */
	public DummyTask(String issuedBy, String name, String description, long executionTime) {
		super(issuedBy, name, description, 0);
		this.executionTime = executionTime;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7340698055940915661L;

	/* (non-Javadoc)
	 * @see org.hbird.exchange.tasking.Task#execute()
	 */
	@Override
	public List<EntityInstance> execute() {
		System.out.println("Dummy task executed.");
		return new ArrayList<EntityInstance>();
	}
	
	public long getDeliveryTime() {
		return executionTime;
	}
}
