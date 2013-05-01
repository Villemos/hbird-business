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

import java.util.ArrayList;
import java.util.List;

import org.hbird.business.api.IPublish;
import org.hbird.exchange.core.D3Vector;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.dataaccess.TlePropagationRequest;
import org.hbird.exchange.groundstation.GroundStation;
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
public class KeplerianOrbitPredictor {

    public List<EntityInstance> predictOrbit(TlePropagationRequest request, List<GroundStation> groundStations, PVCoordinates pvCoordinates,
            AbsoluteDate startTime, TleOrbitalParameters parameters, IPublish publisher) throws OrekitException {

        List<EntityInstance> results = new ArrayList<EntityInstance>();

        // TODO - 01.05.2013, kimmell - check which Orbit and frame to use here
        Orbit initialOrbit = new KeplerianOrbit(pvCoordinates, Constants.FRAME, startTime, Constants.MU);

        Propagator propagator = new KeplerianPropagator(initialOrbit);
        String satelliteId = request.getSatelliteId();

        OrbitalStateCollector injector = new OrbitalStateCollector(satelliteId, parameters, publisher);

        /** Register the visibility events for the requested locations. */
        for (GroundStation groundStation : groundStations) {
            D3Vector location = groundStation.getGeoLocation();
            GeodeticPoint point = new GeodeticPoint(location.p1, location.p2, location.p3);
            TopocentricFrame sta1Frame = new TopocentricFrame(Constants.earth, point, location.getName());
            EventDetector sta1Visi = new LocationContactEventCollector(0, sta1Frame, satelliteId, groundStation.getID(), parameters, publisher);
            propagator.addEventDetector(sta1Visi);
        }

        propagator.setMasterMode(request.getStepSize(), injector);
        propagator.propagate(new AbsoluteDate(startTime, request.getDeltaPropagation()));

        /** Add the data set with the orbital data. */
        results.addAll(injector.getDataSet());

        /** Add all the data sets with contact data. */
        for (EventDetector detector : propagator.getEventsDetectors()) {
            results.addAll(((LocationContactEventCollector) detector).getDataSet());
        }

        return results;
    }
}
