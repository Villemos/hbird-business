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

import org.hbird.exchange.core.DataSet;
import org.hbird.exchange.navigation.Location;
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
 * For an example of how to use, see https://www.orekit.org/forge/projects/orekit/repository/revisions/4decf40db88b02ce82ec9cac7629536c701ac535/entry/src/tutorials/fr/cs/examples/propagation/VisibilityCheck.java
 * 
 * NOTE NOTE NOTE NOTE
 * 
 * The following system property should be set orekit.data.path=[path to UTC-TAI.history file]
 * 
 * 
 */
public class KeplianOrbitPredictor {

	public List<DataSet> predictOrbit(List<Location> locations, PVCoordinates pvCoordinates, long startTime, String satellite, double stepSize, double deltaPropagation, long contactDataStepSize, long generationTime, String datasetIdentifier) throws OrekitException {

		List<DataSet> results = new ArrayList<DataSet>();
		
		AbsoluteDate initialDate = new AbsoluteDate(new Date(startTime), TimeScalesFactory.getUTC());

		// Initial date
		Orbit initialOrbit = new KeplerianOrbit(pvCoordinates, Constants.frame, initialDate, Constants.MU);

		Propagator propagator = new KeplerianPropagator(initialOrbit);

		OrbitalStateCollector injector = new OrbitalStateCollector(satellite, generationTime, datasetIdentifier);

		/** Register the visibility events for the requested locations. */
		for (Location location : locations) {
			GeodeticPoint point = new GeodeticPoint((Double) location.p1, (Double) location.p2, (Double) location.p3);				
			TopocentricFrame sta1Frame = new TopocentricFrame(Constants.earth, point, location.getName());

			/** Register the injector that will send the detected events, for this location, to the propagator. */
			EventDetector sta1Visi = new LocationContactEventCollector(location.getThresholdElevation(), sta1Frame, satellite, location, contactDataStepSize, generationTime, datasetIdentifier);
			propagator.addEventDetector(sta1Visi);				
		}

		propagator.setMasterMode(stepSize, injector);			
		propagator.propagate(new AbsoluteDate(initialDate, deltaPropagation));

		/** Add the data set with the orbital data. */
		results.add(injector.getDataSet());
		
		/** Add all the data sets with contact data. */
		for (EventDetector detector : propagator.getEventsDetectors()) {
			results.addAll( ((LocationContactEventCollector) detector).getDatasets() );
		}
		
		return results;
	}
}
