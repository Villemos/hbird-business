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
package org.hbird.business.navigation;

import org.hbird.exchange.core.DataSet;
import org.hbird.exchange.navigation.D3Vector;
import org.hbird.exchange.navigation.OrbitalState;
import org.orekit.errors.OrekitException;
import org.orekit.errors.PropagationException;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.sampling.OrekitFixedStepHandler;
import org.orekit.time.TimeScalesFactory;
import org.orekit.time.UTCScale;


/**
 * Callback class of the orekit propagator. The propagator will call the 
 * 'handleStep' method on this object at intervals, providing the next orbital state.
 * This class transforms the orbital state provided by orekit into a generic
 * type, and parses it to the 'seda:OrbitPredictions' route. This route must be
 * configured as part of the system.
 */
public class OrbitalStateCollector implements OrekitFixedStepHandler {

	/** The unique UID */
	private static final long serialVersionUID = 3944670616542918255L;

	protected DataSet dataSet = null;

	protected UTCScale scale = null;
	
	public OrbitalStateCollector(String satellite, long generationTime, String datasetIdentifier) {
		dataSet = new DataSet("OrbitPredictor", "OrbitalStates", "OrbitalStates", "The orbital states of a satellite.", generationTime, datasetIdentifier);
		dataSet.setSatellite(satellite);
		
		try {
			scale = TimeScalesFactory.getUTC();
		} catch (OrekitException e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.orekit.propagation.sampling.OrekitFixedStepHandler#handleStep(org.orekit.propagation.SpacecraftState, boolean)
	 */
	public void handleStep(SpacecraftState currentState, boolean isLast) throws PropagationException {

		/** Create position vector. */
		D3Vector position = new D3Vector("", "Position", "Position", "The orbital position of the satellite at the given time.", 
				currentState.getOrbit().getPVCoordinates().getPosition().getX(),
				currentState.getOrbit().getPVCoordinates().getPosition().getY(),
				currentState.getOrbit().getPVCoordinates().getPosition().getZ());

		/** Create velocity vector. */
		D3Vector velocity = new D3Vector("", "Velocity", "Velocity", "The orbital velocity of the satellite at the given time", 
				currentState.getOrbit().getPVCoordinates().getVelocity().getX(),
				currentState.getOrbit().getPVCoordinates().getVelocity().getY(),
				currentState.getOrbit().getPVCoordinates().getVelocity().getZ());

		/** Create momentum vector. */
		D3Vector momentum = new D3Vector("", "Velocity", "Velocity", "The orbital velocity of the satellite at the given time", 
				currentState.getOrbit().getPVCoordinates().getMomentum().getX(),
				currentState.getOrbit().getPVCoordinates().getMomentum().getY(),
				currentState.getOrbit().getPVCoordinates().getMomentum().getZ());

		dataSet.addData(new OrbitalState("OrbitPredictor", "OrbitalState", "Orbital state of satellite", currentState.getDate().toDate(scale).getTime(), dataSet.getGenerationTime(), dataSet.getSatellite(), position, velocity, momentum));
	}

	public DataSet getDataSet() {
		return dataSet;
	}
}
