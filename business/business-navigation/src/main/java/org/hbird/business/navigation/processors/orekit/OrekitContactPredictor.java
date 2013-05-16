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
package org.hbird.business.navigation.processors.orekit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.camel.Handler;
import org.hbird.business.api.IPublish;
import org.hbird.business.navigation.configuration.ContactPredictionConfiguration;
import org.hbird.business.navigation.orekit.Constants;
import org.hbird.business.navigation.orekit.ContactEventCollector;
import org.hbird.business.navigation.orekit.IPropagatorProvider;
import org.hbird.business.navigation.request.ContactPredictionRequest;
import org.hbird.exchange.core.D3Vector;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.interfaces.IEntityInstance;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
import org.orekit.frames.TopocentricFrame;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.events.EventDetector;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class OrekitContactPredictor {

    private static final Logger LOG = LoggerFactory.getLogger(OrekitContactPredictor.class);

    private final IPublish publisher;
    private final IPropagatorProvider propagatorProvider;

    public OrekitContactPredictor(IPropagatorProvider propagatorProvider, IPublish publisher) {
        this.propagatorProvider = propagatorProvider;
        this.publisher = publisher;
    }

    @Handler
    public ContactPredictionRequest predict(ContactPredictionRequest request) throws OrekitException {
        Propagator propagator = propagatorProvider.getPropagator(request);
        ContactPredictionConfiguration conf = request.getConfiguration();
        List<GroundStation> gsList = request.getGroundStations();
        TleOrbitalParameters tleParameters = request.getTleParameters();
        String satelliteId = conf.getSatelliteId();
        for (GroundStation gs : gsList) {
            String gsId = gs.getID();
            String gsName = gs.getName();
            int elevation = 0; // TODO - 16.05.2013, kimmell - make it configurable
            D3Vector location = gs.getGeoLocation();
            GeodeticPoint point = new GeodeticPoint(location.getP1(), location.getP2(), location.getP3());
            TopocentricFrame frame = new TopocentricFrame(Constants.earth, point, gsName);
            EventDetector detector = new ContactEventCollector(elevation, frame, satelliteId, gsId, tleParameters, publisher);
            propagator.addEventDetector(detector);
        }

        long end = request.getEndTime();
        AbsoluteDate endDate = new AbsoluteDate(new Date(end), TimeScalesFactory.getUTC());
        LOG.debug("Predicting contacts for satelliteId '{}' and {} ground stations", satelliteId, gsList.size());
        long startPredcition = System.currentTimeMillis();
        propagator.propagate(endDate);
        long endPredcition = System.currentTimeMillis();

        ArrayList<IEntityInstance> result = new ArrayList<IEntityInstance>();
        for (EventDetector ed : propagator.getEventsDetectors()) {
            ContactEventCollector cec = (ContactEventCollector) ed;
            result.addAll(cec.getDataSet());
        }
        LOG.debug("Prediciont completed in {} ms; calculated {} LocationContactEvents", (endPredcition - startPredcition), result.size());

        request.setResult(result);
        return request;
    }
}
