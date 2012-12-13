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

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math.geometry.Vector3D;
import org.hbird.exchange.navigation.ContactData;
import org.hbird.exchange.navigation.D3Vector;
import org.hbird.exchange.navigation.Location;
import org.hbird.exchange.navigation.OrbitalState;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
import org.orekit.errors.PropagationException;
import org.orekit.frames.LocalOrbitalFrame;
import org.orekit.frames.LocalOrbitalFrame.LOFType;
import org.orekit.frames.TopocentricFrame;
import org.orekit.orbits.CartesianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.propagation.sampling.OrekitFixedStepHandler;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.PVCoordinates;


/**
 * Callback class of the orekit propagator. The propagator will call the 
 * 'handleStep' method on this object at intervals, providing the next orbital state.
 * This class transforms the orbital state provided by orekit into a generic
 * type, and parses it to the 'seda:OrbitPredictions' route. This route must be
 * configured as part of the system.
 */
public class OrbitalStateInjector implements OrekitFixedStepHandler {

	/** The unique UID */
	private static final long serialVersionUID = 3944670616542918255L;

	/** Unique identifier assigned to all orbital data events that generated
	 *  as part of this run. Will be used to identify a complete orbit, and replace
	 *  it with new series when available. */
	protected String datasetidentifier;

	protected String orbitalStateName = "Predicted Orbital State";
	protected String orbitalStateDescription = "A predicted orbital state";

	protected KeplianOrbitPredictor predictor = null;

	protected String satellite = null;

	protected Map<String, Location> locations = new HashMap<String, Location>();
	protected Map<String, Boolean> visibleLocations = new HashMap<String, Boolean>();

	protected long generationTime = 0;
	
	/** The delta propagation between each orbital state and step size of the Contact Data calculation. The orbital states will be calculated with
	 * one frequency, the contact data with another (typically being much higher). 
	 * 
	 * The delta propagation is in SECONDS the step size in MILLISECONDS */
	protected double stepSize = 0;
	protected long contactDataStepSize = 500;

	public OrbitalStateInjector(KeplianOrbitPredictor predictor, String name, String description, String satellite, List<Location> locations, double stepSize, long contactDataStepSize) {
		this.predictor = predictor;
		this.orbitalStateName = name;
		this.orbitalStateDescription = description;
		this.satellite = satellite;

		for (Location loc : locations) {
			this.locations.put(loc.getName(), loc);
		}

		this.stepSize = stepSize;
		this.contactDataStepSize = contactDataStepSize;
	}

	protected int step = 0;

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

		try {
			/** Create orbital state. */
			OrbitalState state = new OrbitalState(predictor.name, orbitalStateName, orbitalStateDescription, currentState.getDate().toDate(TimeScalesFactory.getUTC()).getTime(), generationTime, satellite, position, velocity, momentum);

			/** Add the orbital state to the result set. */
			predictor.addResult(state);

			/** For each currently visible location, calculate the ContactData between the satellite and each location for the orbital state. */
			Iterator<Entry<String, Boolean>> it = visibleLocations.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Boolean> entry = it.next();
				if (entry.getValue() == true) {

					for (int i = 0; contactDataStepSize * i < stepSize * 1000; i++) {

						Location loc = locations.get(entry.getKey());
						GeodeticPoint point = new GeodeticPoint(loc.p1, loc.p2, loc.p3);
						TopocentricFrame locationOnEarth = new TopocentricFrame(Constants.earth, point, "");

						AbsoluteDate date = new AbsoluteDate(new Date(state.getTimestamp() + contactDataStepSize*i), TimeScalesFactory.getUTC());

						double azimuth = calculateAzimuth(locationOnEarth, date, currentState.getPVCoordinates());
						double elevation = calculateElevation(currentState.getPVCoordinates(), locationOnEarth, date);
						double doppler = calculateDoppler(currentState.getPVCoordinates(), locationOnEarth, date);
						double dopplerShift = calculateDopplerShift(doppler, loc.getFrequency());

						ContactData data = new ContactData("OrbitPredictor", "ContactData", "Contact Data", "The contact data between a satellite and a location", state.getTimestamp() + contactDataStepSize*i, generationTime, azimuth, elevation, doppler, dopplerShift, satellite, loc.getName());
						predictor.addResult(data);
					}
				}
			}
		} catch (OrekitException e) {
			e.printStackTrace();
		}
	}

	public void registerVisibleLocation(String location, boolean visibility) {
		visibleLocations.put(location, visibility);
	}

	public String getDatasetidentifier() {
		return datasetidentifier;
	}

	public void setDatasetidentifier(String datasetidentifier) {
		this.datasetidentifier = datasetidentifier;
	}

	protected double calculateAzimuth(TopocentricFrame locationOnEarth, AbsoluteDate absoluteDate, PVCoordinates state) throws OrekitException {
		return locationOnEarth.getAzimuth(state.getPosition(), Constants.frame, absoluteDate);
	}

	protected double calculateElevation(PVCoordinates satellite, TopocentricFrame locationOnEarth, AbsoluteDate absoluteDate) throws OrekitException {
		return Math.toDegrees(locationOnEarth.getElevation(satellite.getPosition(), Constants.frame, absoluteDate));
	}

	protected double calculateDoppler(PVCoordinates satellite, TopocentricFrame locationOnEarth, AbsoluteDate absoluteDate) throws OrekitException {
		Orbit initialOrbit = new CartesianOrbit(satellite, Constants.frame, absoluteDate, Constants.MU); //an orbit defined by the position and the velocity of the satellite in the inertial frame at the date.
		Propagator propagator = new KeplerianPropagator(initialOrbit);//as a propagator, we consider a simple KeplerianPropagator.  
		LocalOrbitalFrame lof = new LocalOrbitalFrame(Constants.frame, LOFType.QSW, propagator, "QSW"); //local orbital frame.
		PVCoordinates pv = locationOnEarth.getTransformTo(lof, absoluteDate).transformPVCoordinates(PVCoordinates.ZERO);
		return Vector3D.dotProduct(pv.getPosition(), pv.getVelocity()) / pv.getPosition().getNorm();
	}

	protected double calculateDopplerShift(double doppler, double frequency) {
		return ((1 - (doppler / Constants.SPEED_OF_LIGHT)) * frequency) - frequency;
	}
}
