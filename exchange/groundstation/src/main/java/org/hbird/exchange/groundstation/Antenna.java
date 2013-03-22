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
 * 
 */
package org.hbird.exchange.groundstation;

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.core.Part;

/**
 * An antenna of a groundstation.
 * 
 * Each antenna is part of one (and only one) ground station.
 *
 * @author Lauri Kimmel
 * @author Gert Villemos
 * 
 */
public class Antenna extends Part {

	private static final long serialVersionUID = -8558418536858999621L;

	/**
	 * A list of commandable antenna parts.
	 */
	protected List<ICommandableAntennaPart> parts = new ArrayList<ICommandableAntennaPart>();

	public Antenna(String name, String description, Rotator rotator, RadioDevice radioDevice) {
		super(name, description);

		parts.add(rotator);
		parts.add(radioDevice);
		
		rotator.setIsPartOf(this);
		radioDevice.setIsPartOf(this);
	}
	
	public Antenna(String name, String description, Rotator rotator, RadioDevice radioDevice, Modem modemDevice, SoftwareDefinedRadio softwareDefinedRadio) {
		super(name, description);

		parts.add(rotator);
		parts.add(radioDevice);
		parts.add(modemDevice);
		parts.add(softwareDefinedRadio);
	}

	public List<ICommandableAntennaPart> getParts() {
		return parts;
	}

	public void setParts(List<ICommandableAntennaPart> parts) {
		this.parts = parts;
	}

	public Rotator getRotator() {
		for (ICommandableAntennaPart part : parts) {
			if (part instanceof Rotator) {
				return (Rotator) part;
			}
		}

		return null;
	}
}
