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

import org.apache.camel.model.ProcessorDefinition;
import org.hbird.business.api.ICatalogue;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IPublisher;
import org.hbird.business.core.SoftwareComponentDriver;
import org.hbird.business.navigation.PredictionComponent;
import org.hbird.business.navigation.configuration.ContactPredictionConfiguration;
import org.hbird.business.navigation.processors.GroundStationResolver;
import org.hbird.business.navigation.processors.SatelliteResolver;
import org.hbird.business.navigation.processors.TimeRangeCalulator;
import org.hbird.business.navigation.processors.TleResolver;
import org.hbird.business.navigation.processors.orekit.AzimuthCalculator;
import org.hbird.business.navigation.processors.orekit.ContactDataExtractor;
import org.hbird.business.navigation.processors.orekit.ContactPredictionRequestCreator;
import org.hbird.business.navigation.processors.orekit.DopplerCalculator;
import org.hbird.business.navigation.processors.orekit.EclipseCalculator;
import org.hbird.business.navigation.processors.orekit.ElevationCalculator;
import org.hbird.business.navigation.processors.orekit.LocationContactEventExtractor;
import org.hbird.business.navigation.processors.orekit.OrekitContactPredictor;
import org.hbird.business.navigation.processors.orekit.RangeCalculator;
import org.hbird.business.navigation.processors.orekit.SignalDelayCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
public class ContactPredictionDriver extends SoftwareComponentDriver<PredictionComponent> {

    private static final Logger LOG = LoggerFactory.getLogger(ContactPredictionDriver.class);
    
    protected IDataAccess dao;
    protected ICatalogue catalogue;
    
    @Autowired
    public ContactPredictionDriver(IDataAccess dao, ICatalogue catalogue, IPublisher publisher) {
    	super(publisher);
    	
    	this.dao = dao;
    	this.catalogue = catalogue;
    }

    /**
     * @see org.hbird.business.core.SoftwareComponentDriver#doConfigure()
     */
    @Override
    protected void doConfigure() {

        // setup from component
        PredictionComponent component = entity;
        ContactPredictionConfiguration config = (ContactPredictionConfiguration) component.getConfiguration();

        // dependencies
        String componentId = component.getID();
        IPropagatorProvider propagatorProvider = new TlePropagatorProvider();
        IFrameProvider frameProvider = new Cirf2000FrameProvider();
        long predictionInterval = config.getPredictionInterval();
        double calculationStep = config.getDetailsCalculationStep() / 1000D; // from millis to seconds

        // processors
        ContactPredictionRequestCreator requestCreator = new ContactPredictionRequestCreator(config);
        SatelliteResolver satelliteResolver = new SatelliteResolver(dao);
        TleResolver tleResolver = new TleResolver(dao);
        GroundStationResolver gsResolver = new GroundStationResolver(dao, catalogue);
        TimeRangeCalulator timeRangeCalculator = new TimeRangeCalulator();
        OrekitContactPredictor predictor = new OrekitContactPredictor(componentId, propagatorProvider, publisher, frameProvider);
        ContactDataExtractor dataExtractor = new ContactDataExtractor();
        LocationContactEventExtractor eventExtractor = new LocationContactEventExtractor();

        AzimuthCalculator azimuthCalculator = new AzimuthCalculator();
        ElevationCalculator elevationCalculator = new ElevationCalculator(calculationStep);
        DopplerCalculator dopplerCalculator = new DopplerCalculator();
        RangeCalculator rangeCalculator = new RangeCalculator(calculationStep);
        SignalDelayCalculator signalDelayCalculator = new SignalDelayCalculator();
        EclipseCalculator eclipseCalculator = new EclipseCalculator();
        // TODO - 30.05.2013, kimmell - missing signal loss calculator

        LOG.info("Starting {}; using '{}' and '{}' with interval {} ms", new Object[] { getClass().getSimpleName(),
                propagatorProvider.getClass().getSimpleName(), frameProvider.getClass().getSimpleName(), predictionInterval });

        // actual route
        // @formatter:off
        ProcessorDefinition<?> route = from(addTimer(componentId, predictionInterval)) // execute using timer
                .bean(requestCreator)               // create request object
                .bean(satelliteResolver)            // resolve satellite
                .bean(tleResolver)                  // resolve TLE for the satellite
                .bean(gsResolver)                   // resolve ground stations from IDs
                .bean(timeRangeCalculator)          // calculate time ranges
                .bean(predictor)                    // predict the contacts
                .bean(dataExtractor)                // extract ContactData objects
                .split(body())                      // process each contact data object separately
                    .bean(azimuthCalculator)        // calculate azimuth
                    .bean(elevationCalculator)      // calculate elevation
                    .bean(dopplerCalculator)        // calculate Doppler
                    .bean(rangeCalculator)          // calculate range
                    .bean(signalDelayCalculator)    // calculate signal delay
                    .bean(eclipseCalculator)        // calculate eclipse
                    .bean(eventExtractor)           // extract LocationContactEvent objects
                    .to("log:org.hbird.prediction.contact.stats?level=DEBUG&groupInterval=60000&groupDelay=60000&groupActiveOnly=false");
        // @formatter:on

        route.bean(publisher, "publish");
    }
}
