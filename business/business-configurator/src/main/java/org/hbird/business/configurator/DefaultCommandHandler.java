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
package org.hbird.business.configurator;

import org.apache.camel.Handler;
import org.hbird.exchange.core.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class used to report erroneously routed commands.
 * 
 * @author Gert Villemos
 *
 */
public class DefaultCommandHandler {

	/** Class logger */
	private static final transient Logger LOG = LoggerFactory.getLogger(ComponentBuilder.class);
	
	/* (non-Javadoc)
	 * @see org.hbird.business.configurator.ICommandable#receiveCommand(org.hbird.exchange.core.Command)
	 */
	@Handler
	public void receiveCommand(Command command) {
		LOG.error("Received command. This type of component does not provide a commandable interface. Command discarded.");
	}
}
