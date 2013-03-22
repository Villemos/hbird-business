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
package org.hbird.exchange.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hbird.exchange.interfaces.IPart;

/**
 * A part is something that is 'part' of something else (... most likely).
 * 
 * @author Gert Villemos
 *
 */
public class Part extends Named implements IPart {

	/**
	 * 
	 */
	private static final long serialVersionUID = 577393462361927408L;

	protected IPart parent = null;
	
	protected BusinessCard card = null;

	protected List<Command> commands = new ArrayList<Command>();
	
	/** A list of all parts. */
	protected static Map<String, Part> parts = new HashMap<String, Part>();
		
	public Part(String name, String description) {
		super("System", name, "Part", description);
		this.card = new BusinessCard(name, null);
		Part.parts.put(name, this);
	}

	public Part(String name, String description, List<Command> commands) {
		super("System", name, "Part", description);
		this.commands = commands;
		this.card = new BusinessCard(name, commands);
		Part.parts.put(name, this);
	}

	/* (non-Javadoc)
	 * @see org.hbird.exchange.interfaces.IPartOf#getIsPartOf()
	 */
	@Override
	public IPart getIsPartOf() {
		return parent;
	}

	/* (non-Javadoc)
	 * @see org.hbird.exchange.interfaces.IPartOf#setIsPartOf(org.hbird.exchange.core.Named)
	 */
	@Override
	public void setIsPartOf(IPart parent) {
		this.parent = parent;
	}

	/* (non-Javadoc)
	 * @see org.hbird.exchange.interfaces.IPart#getPartName()
	 */
	@Override
	public String getQualifiedName() {
		return getQualifiedName("/");
	}

	/* (non-Javadoc)
	 * @see org.hbird.exchange.interfaces.IPart#getPartName()
	 */
	@Override
	public String getQualifiedName(String separator) {
		return parent == null ? separator + name : parent.getQualifiedName(separator) + separator + name;
	}

	public List<Command> getCommands() {
		return commands;
	}

	public void setCommands(List<Command> commands) {
		this.commands = commands;
	}

	public static Map<String, Part> getAllParts() {
		return parts;
	}
}
