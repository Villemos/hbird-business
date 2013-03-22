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
package org.hbird.exchange.groundstation;

import java.util.Date;

import org.hbird.exchange.core.Named;
import org.hbird.exchange.interfaces.IScheduled;

/**
 * @author Admin
 *
 */
public abstract class NativeCommand extends Named implements IScheduled {

    /**
	 * @param issuedBy
	 */
	public NativeCommand(String issuedBy) {
		super(issuedBy);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -734384324427713527L;
	
	protected long executionTime = 0;

	/* (non-Javadoc)
	 * 
	 * @see org.hbird.exchange.interfaces.IScheduled#getDelay()
	 */
	@Override
	public long getDelay() {
		long now = (new Date()).getTime();
		return now < executionTime ? executionTime - now : 0;
	}

	/* (non-Javadoc)
	 * @see org.hbird.exchange.interfaces.IScheduled#getDeliveryTime()
	 */
	@Override
	public long getDeliveryTime() {
		return executionTime;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}    
}