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

import org.apache.camel.CamelContext;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.navigation.Antenna;
import org.hbird.exchange.navigation.D3Vector;
import org.hbird.exchange.navigation.GroundStation;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
import org.orekit.frames.TopocentricFrame;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.propagation.events.EventDetector;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.PVCoordinates;

/**
 * Processor of 'OrbitalPredictionRequests', implemented based on the OREKIT open source
 * tool.
 * 
 * For an example of how to use, see
 * https://www.orekit.org/forge/projects/orekit/repository/revisions/4decf40db88b02ce82ec9cac7629536c701ac535
 * /entry/src/tutorials/fr/cs/examples/propagation/VisibilityCheck.java
 * 
 * NOTE NOTE NOTE NOTE
 * 
 * The following system property should be set orekit.data.path=[path to UTC-TAI.history file]
 * 
 * 
 */
public class KeplianOrbitPredictor {

	public List<Named> predictOrbit(List<GroundStation> groundStations, PVCoordinates pvCoordinates, long startTime, String satellite, double stepSize,
			double deltaPropagation, long contactDataStepSize, TleOrbitalParameters parameters, CamelContext context, boolean publish) throws OrekitException {

		List<Named> results = new ArrayList<Named>();

		AbsoluteDate initialDate = new AbsoluteDate(new Date(startTime), TimeScalesFactory.getUTC());

		// Initial date
		Orbit initialOrbit = new KeplerianOrbit(pvCoordinates, Constants.frame, initialDate, Constants.MU);

		Propagator propagator = new KeplerianPropagator(initialOrbit);

		OrbitalStateCollector injector = new OrbitalStateCollector(satellite, parameters, context, publish);

		/** Register the visibility events for the requested locations. */
		for (GroundStation groundStation : groundStations) {
			D3Vector location = groundStation.getGeoLocation();
			GeodeticPoint point = new GeodeticPoint(location.p1, location.p2, location.p3);
			TopocentricFrame sta1Frame = new TopocentricFrame(Constants.earth, point, location.getName());

			/** The ground station may have multiple antennas, with different properties. */
			for (Antenna antenna : groundStation.getAntennas()) {

				/** Register the injector that will send the detected events, for this location and antenna, to the propagator. */
				EventDetector sta1Visi = new LocationContactEventCollector(antenna.getRotatorProperties().getThresholdElevation(), sta1Frame, satellite, groundStation.getName(), antenna.getName(), parameters, context, publish);
				propagator.addEventDetector(sta1Visi);
			}
		}

		propagator.setMasterMode(stepSize, injector);
		propagator.propagate(new AbsoluteDate(initialDate, deltaPropagation));

		/** Add the data set with the orbital data. */
		results.addAll(injector.getDataSet());

		/** Add all the data sets with contact data. */
		for (EventDetector detector : propagator.getEventsDetectors()) {
			results.addAll(((LocationContactEventCollector) detector).getDataSet());
		}

		return results;
	}
}
