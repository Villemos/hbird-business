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

import org.apache.camel.CamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.hbird.business.api.IPublisher;
import org.hbird.business.api.IdBuilder;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.core.SoftwareComponentDriver;
import org.hbird.business.navigation.PredictionComponent;
import org.hbird.business.navigation.configuration.OrbitalStatePredictionConfiguration;
import org.hbird.business.navigation.processors.OrbitalStateExtractor;
import org.hbird.business.navigation.processors.OrbitalStatePredictionRequestCreator;
import org.hbird.business.navigation.processors.SatelliteResolver;
import org.hbird.business.navigation.processors.TimeRangeCalulator;
import org.hbird.business.navigation.processors.TleResolver;
import org.hbird.business.navigation.processors.orekit.OrekitOrbitalStatePredictor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
public class OrbitalStatePredictionDriver extends SoftwareComponentDriver<PredictionComponent> {

    private static final Logger LOG = LoggerFactory.getLogger(OrbitalStatePredictionDriver.class);
    
    protected IDataAccess dao;
    protected IPublisher publisher;
    protected IdBuilder idBuilder;
    
    @Autowired
    public OrbitalStatePredictionDriver(IDataAccess dao, IPublisher publisher, IdBuilder idBuilder) {
    	this.dao = dao;
    	this.publisher = publisher;
    	this.idBuilder = idBuilder;
    }

    /**
     * @see org.hbird.business.core.SoftwareComponentDriver#doConfigure()
     */
    @Override
    protected void doConfigure() {
        // setup from component
        PredictionComponent component = entity;
        OrbitalStatePredictionConfiguration config = (OrbitalStatePredictionConfiguration) component.getConfiguration();

        // dependencies
        String componentId = component.getID();
        CamelContext ctx = component.getContext();
        IPropagatorProvider propagatorProvider = new TlePropagatorProvider();
        long predictionInterval = config.getPredictionInterval();

        // processors
        OrbitalStatePredictionRequestCreator requestCreator = new OrbitalStatePredictionRequestCreator(config);
        TleResolver tleResolver = new TleResolver(dao);
        SatelliteResolver satelliteResolver = new SatelliteResolver(dao);
        TimeRangeCalulator timeRangeCalculator = new TimeRangeCalulator();
        OrekitOrbitalStatePredictor predictor = new OrekitOrbitalStatePredictor(propagatorProvider, publisher, idBuilder);
        OrbitalStateExtractor extractor = new OrbitalStateExtractor();

        LOG.info("Starting {}; using '{}' with interval {} ms", new Object[] { getClass().getSimpleName(), propagatorProvider.getClass().getSimpleName(),
                predictionInterval });

        // actual route
        ProcessorDefinition<?> route = from(addTimer(componentId, predictionInterval))
                .bean(requestCreator)
                .bean(satelliteResolver)
                .bean(tleResolver)
                .bean(timeRangeCalculator)
                .bean(predictor)
                .bean(extractor)
                .split(body())
                .to("log:org.hbird.prediction.orbit.stats?level=DEBUG&groupInterval=60000&groupDelay=60000&groupActiveOnly=false");

        addInjectionRoute(route);
    }
}
