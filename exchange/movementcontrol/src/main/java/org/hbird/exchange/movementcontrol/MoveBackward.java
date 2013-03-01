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
package org.hbird.exchange.movementcontrol;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Parameter;


public class MoveBackward extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3800714830282328882L;

	public MoveBackward(String issuedBy) {
		super(issuedBy, "", "MoveBackward", "Move the bot backwards. The distance (in meter) is set by the argument 'Distance'.");
		setArgumentValue("Distance", new Parameter(issuedBy, "Distance", "Argument", "The distance in meter.", null, "Meter"));
	}

	public MoveBackward(String issuedBy, long executionTime) {
		super(issuedBy, "", "MoveBackward", "Move the bot backwards. The distance (in meter) is set by the argument 'Distance'.");
		this.executionTime = executionTime;
	}
}
