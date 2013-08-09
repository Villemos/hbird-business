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

import java.util.Date;
import java.util.List;

import org.apache.camel.Handler;
import org.hbird.business.api.IPublisher;
import org.hbird.business.api.IdBuilder;
import org.hbird.business.navigation.configuration.OrbitalStatePredictionConfiguration;
import org.hbird.business.navigation.orekit.IPropagatorProvider;
import org.hbird.business.navigation.orekit.OrbitalStateCollector;
import org.hbird.business.navigation.request.OrbitalStatePredictionRequest;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.orekit.errors.OrekitException;
import org.orekit.propagation.Propagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class OrekitOrbitalStatePredictor {

    private static final Logger LOG = LoggerFactory.getLogger(OrekitContactPredictor.class);

    private final IPublisher publisher;
    private final IPropagatorProvider propagatorProvider;
    private final IdBuilder idBuilder;

    public OrekitOrbitalStatePredictor(IPropagatorProvider propagatorProvider, IPublisher publisher, IdBuilder idBuilder) {
        this.propagatorProvider = propagatorProvider;
        this.publisher = publisher;
        this.idBuilder = idBuilder;
    }

    @Handler
    public OrbitalStatePredictionRequest predict(OrbitalStatePredictionRequest request) throws OrekitException {
        Propagator propagator = propagatorProvider.getPropagator(request);
        OrbitalStatePredictionConfiguration conf = request.getConfiguration();
        TleOrbitalParameters tleParameters = request.getTleParameters();
        String satelliteId = conf.getSatelliteId();
        double predictionStep = conf.getPredictionStep() / 1000.0D; // from milliseconds to seconds
        OrbitalStateCollector collector = new OrbitalStateCollector(satelliteId, tleParameters.getInstanceID(), publisher, idBuilder);
        propagator.setMasterMode(predictionStep, collector);

        long start = request.getStartTime();
        long end = request.getEndTime();
        AbsoluteDate startDate = new AbsoluteDate(new Date(start), TimeScalesFactory.getUTC());
        AbsoluteDate endDate = new AbsoluteDate(new Date(end), TimeScalesFactory.getUTC());
        LOG.debug("Predicting orbital states for the satelliteId '{}'", satelliteId);
        long startPredcition = System.currentTimeMillis();
        propagator.propagate(startDate, endDate);
        long endPredcition = System.currentTimeMillis();
        List<OrbitalState> result = collector.getDataSet();
        request.setResult(result);
        LOG.debug("Prediciont completed in {} ms; calculated {} OrbitalStates", (endPredcition - startPredcition), result.size());

        return request;
    }
}
