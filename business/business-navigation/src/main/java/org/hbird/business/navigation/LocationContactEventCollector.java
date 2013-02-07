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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.math.geometry.Vector3D;
import org.hbird.exchange.core.DataSet;
import org.hbird.exchange.navigation.ContactData;
import org.hbird.exchange.navigation.Location;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
import org.orekit.frames.LocalOrbitalFrame;
import org.orekit.frames.LocalOrbitalFrame.LOFType;
import org.orekit.frames.TopocentricFrame;
import org.orekit.orbits.CartesianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.propagation.events.ElevationDetector;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.PVCoordinates;

/**
 * This camel route processor will receive callbacks from the orekit library 
 * notifying of events such as the establishment / loss of contact. The processor
 * will create the corresponding OrbitalEvent and send it to the consumer.
 */
public class LocationContactEventCollector extends ElevationDetector {

	/** The unique UID */
	private static final long serialVersionUID = 801203905525890103L;

	/** The location that comes into contact. */
	protected Location location = null;
	
	/** The satellite. */
	protected String satellite = null;
	
	/** ... in milliseconds. */
	protected long contactDataStepSize = 500;
	
	/** FIXME I don't know what this does but OREKIT needs it...*/
	public static final double maxcheck = 1.;

	protected SpacecraftState contactStartState = null;

	protected List<DataSet> datasets = new ArrayList<DataSet>();
	
	protected long generationTime = 0;
	
	protected String datasetIdentifier = "";
	
	/**
	 * COnstructor of an injector of location contact events.
	 * 
	 * @param elevation The degrees above the horizon that the satellite must be to be visible from this location.
	 * @param topo The topocentric framework used.
	 * @param satellite The satellite whose orbit we are predicting.
	 * @param location The location to which contact has been established / lost if this event occurs.
	 * @param contactDataStepSize
	 */
	public LocationContactEventCollector(double elevation, TopocentricFrame topo, String satellite, Location location, long contactDataStepSize, long generationTime, String datasetIdentifier) {
		super(maxcheck, elevation, topo);
		this.satellite = satellite;
		this.location = location;
		this.contactDataStepSize = contactDataStepSize;
		this.generationTime = generationTime;
		this.datasetIdentifier = datasetIdentifier;
	}

	/* (non-Javadoc)
	 * @see org.orekit.propagation.events.ElevationDetector#eventOccurred(org.orekit.propagation.SpacecraftState, boolean)
	 */
	public int eventOccurred(final SpacecraftState currentState, final boolean increasing) throws OrekitException {
		
		/** If a START event, then register. */
		if (increasing == true) {
			contactStartState = currentState;
		}
		/** If an end event and we already had a start event. */
		else if (increasing == false && contactStartState != null) {
			
			long startTime = contactStartState.getDate().toDate(TimeScalesFactory.getUTC()).getTime();
			long endTime = currentState.getDate().toDate(TimeScalesFactory.getUTC()).getTime();
			
			DataSet dataset = new DataSet("OrbitPredictor", "ContactData", "ContactData", "Contact data between a specific location and satellite.", generationTime, datasetIdentifier);
			dataset.setLocation(location.getName());
			dataset.setSatellite(satellite);
			
			/** Register start event. */
			dataset.getDataset().add(new LocationContactEvent("OrbitPredictor", "Visibility", "", startTime, generationTime, datasetIdentifier, location.getName(), satellite, true));

			GeodeticPoint point = new GeodeticPoint(location.p1, location.p2, location.p3);
			TopocentricFrame locationOnEarth = new TopocentricFrame(Constants.earth, point, "");

			/** Calculate contact data. */
			for (int i = 0; startTime + contactDataStepSize * i < endTime; i++) {
				AbsoluteDate date = new AbsoluteDate(new Date(startTime + contactDataStepSize*i), TimeScalesFactory.getUTC());

				double azimuth = calculateAzimuth(locationOnEarth, date, currentState.getPVCoordinates());
				double elevation = calculateElevation(currentState.getPVCoordinates(), locationOnEarth, date);
				double doppler = calculateDoppler(currentState.getPVCoordinates(), locationOnEarth, date);
				double dopplerShift = calculateDopplerShift(doppler, location.getFrequency());

				dataset.addData(new ContactData("OrbitPredictor", "ContactData", "Contact Data", "The contact data between a satellite and a location", startTime + contactDataStepSize*i, generationTime, datasetIdentifier, azimuth, elevation, doppler, dopplerShift, satellite, location.getName()));

			}
			
			/** Register end event. */
			dataset.getDataset().add(new LocationContactEvent("OrbitPredictor", "Visibility", "", endTime, generationTime, datasetIdentifier, location.getName(), satellite, false));			

			datasets.add(dataset);
			
			contactStartState = null;
		}
		
		/** Continue listening for events. */
		return CONTINUE;
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

	public List<DataSet> getDatasets() {
		return datasets;
	}
}