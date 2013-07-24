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

import org.hbird.business.api.ICatalogue;
import org.hbird.business.api.IPublisher;
import org.hbird.business.api.IdBuilder;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.navigation.ContactEventComponent;
import org.hbird.business.navigation.NavigationComponent;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.navigation.GeoLocation;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
import org.orekit.frames.TopocentricFrame;
import org.orekit.propagation.events.EventDetector;

/**
 * @author Gert Villemos
 * 
 */
public class ContactEventBean extends NavigationBean {

    protected final ICatalogue catalogue;

    /**
     * @param configuration
     * @param dao
     * @param publisher
     * @param idBuilder
     */
    public ContactEventBean(NavigationComponent configuration, IDataAccess dao, IPublisher publisher, IdBuilder idBuilder, ICatalogue catalogue) {
        super(configuration, dao, publisher, idBuilder);
        this.catalogue = catalogue;
    }

    /**
     * @see org.hbird.business.navigation.NavigationBean#prePropagation()
     */
    @Override
    public void preparePropagator() throws OrekitException {

        List<GroundStation> locations;

        // Register the locations
        List<String> locationNames = ((ContactEventComponent) conf).getLocations();
        if (locationNames.isEmpty()) {
            /* Get the definition of all Locations. */
            locations = catalogue.getGroundStations();
        }
        else {
            /* Get the definition of the Locations. */
            locations = catalogue.getGroundStationsByName(locationNames);
        }

        /* Register the visibility events for the requested locations. */
        for (GroundStation groundStation : locations) {
            GeoLocation location = groundStation.getGeoLocation();
            GeodeticPoint point = NavigationUtilities.toGeodeticPoint(location);
            TopocentricFrame sta1Frame = new TopocentricFrame(Constants.earth, point, groundStation.getName());
            EventDetector sta1Visi = new ContactEventCollector(conf.getID(), 0, sta1Frame, conf.getSatelliteId(), groundStation.getID(), tleParameters,
                    publisher, new Cirf2000FrameProvider().getInertialFrame());
            propagator.addEventDetector(sta1Visi);
        }

        /* Disable publication of OrbitalStates */
        orbitalStateCollector.setPublisher(null);
    }
}
