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
package org.hbird.exchange.rangecheck;


import java.io.Serializable;

import org.apache.log4j.Logger;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;


/**
 * This validation step validates that a specific parameter has a specific value
 * at the expected time.
 *
 */
public class ParameterRange implements Serializable {

	/** The unique UID. */
	private static final long serialVersionUID = 7471839659877039842L;

	/** The class logger. */
	protected static Logger logger = Logger.getLogger(ParameterRange.class);

	/** The lower value of the range. If NULL, then there is no lower range. */
	protected Parameter lowerValue = null;
	
	/** The upper value of the range. If NULL, then there is no upper range. */
	protected Parameter upperValue = null;

	/** The state parameter to be created if range fails. */
	protected State state = null;
	
	public ParameterRange(Parameter lowerValue, Parameter upperValue) {
		this.lowerValue = lowerValue;
		this.upperValue = upperValue;
	}
}
