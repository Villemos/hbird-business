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
package org.hbird.exchange.core;

import java.util.ArrayList;
import java.util.List;

/**
 * A command block is a set of commands that are released and transfered to the destination as one block.
 * 
 * The command block can can be treated as a command that is a single command until released to the 
 * destination, where it becomes many commands. 
 * 
 * @author Gert Villemos
 *
 */
public class CommandBlock extends Command {

	private static final long serialVersionUID = -5681255679860543896L;

	/** The commands that this command block carries. */
	protected List<Command> commands = new ArrayList<Command>();
	
	public CommandBlock(String issuedBy, String destination, String name, String description) {
		super(issuedBy, destination, name, description);
	}
	
	/**
	 * Adds a command to the command block
	 * 
	 * @param command The command to be added.
	 */
	public void addCommand(Command command) {
		commands.add(command);
	}	
}
