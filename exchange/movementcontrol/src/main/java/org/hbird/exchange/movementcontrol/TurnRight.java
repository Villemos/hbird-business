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

public class TurnRight extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2747536543216032446L;

	public TurnRight(String issuedBy) {
		super(issuedBy, "", "TurnLeft", "Turns the bot left. The Angle (in degrees) is set by the argument 'Angle'.");
		addArgument("Angle", new Parameter(issuedBy, "Angle", "Argument", "Angle to turn", 45,  "Degree"));	
	}

	public TurnRight(String issuedBy, long executionTime) {
		super(issuedBy, "", "TurnLeft", "Turns the bot left. The Angle (in degrees) is set by the argument 'Angle'.");
		this.executionTime = executionTime;
	}
}
