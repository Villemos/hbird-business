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
package org.hbird.business.navigation.orekit;

import java.util.List;

import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.ICatalogue;
import org.hbird.business.navigation.ContactEventComponent;
import org.hbird.exchange.core.D3Vector;
import org.hbird.exchange.groundstation.GroundStation;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.frames.TopocentricFrame;
import org.orekit.propagation.events.EventDetector;

/**
 * @author Gert Villemos
 *
 */
public class ContactEventBean extends NavigationBean {

	protected List<GroundStation> locations = null;

	protected ICatalogue catalogue = null;

	/**
	 * @param configuration
	 */
	public ContactEventBean(ContactEventComponent configuration) {
		super(configuration);		
		
		this.catalogue = ApiFactory.getCatalogueApi(conf.getName(), conf.getContext());
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.navigation.NavigationBean#prePropagation()
	 */
	@Override
	public void preparePropagator() {

		// Register the locations		
		List<String> locationNames = ((ContactEventComponent) conf).getLocations();
		if (locationNames == null) {
			/* Get the definition of all Locations. */
			locations = catalogue.getGroundStations();
		}
		else {
			/* Get the definition of the Locations. */
			locations = catalogue.getGroundStationsByName(locationNames);
		}            

		/** Register the visibility events for the requested locations. */
		for (GroundStation groundStation : locations) {
			D3Vector location = groundStation.getGeoLocation();
			GeodeticPoint point = new GeodeticPoint(location.getP1(), location.getP2(), location.getP3());
			TopocentricFrame sta1Frame = new TopocentricFrame(Constants.earth, point, location.getName());
			EventDetector sta1Visi = new ContactEventCollector(0, sta1Frame, conf.getSatellite(), groundStation.getID(), tleParameters, publisher);
			propagator.addEventDetector(sta1Visi);
		}
		
		/** Disable publication of OrbitalStates */
		orbitalStateCollector.setPublisher(null);
	}
}
