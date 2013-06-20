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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.hbird.business.navigation.configuration.ContactPredictionConfiguration;
import org.hbird.business.navigation.orekit.Cirf2000FrameProvider;
import org.hbird.business.navigation.orekit.IFrameProvider;
import org.hbird.business.navigation.orekit.IPropagatorProvider;
import org.hbird.business.navigation.orekit.TlePropagatorProvider;
import org.hbird.business.navigation.request.ContactPredictionRequest;
import org.hbird.business.navigation.request.orekit.ContactData;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.navigation.GeoLocation;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class OrekitContactTest {

    private static final Logger LOG = LoggerFactory.getLogger(OrekitContactTest.class);

    private static final String ISSUER = "TEST";
    private static final String SATELLITE_ID = "SAT-1";
    private static final String GS_ID = "GS-1";
    private static final long NOW = 1371625120011L; // fixed time! 2013-170 06:58:40.011 UTC; 2013-170 = 2013-06-19

    private IPropagatorProvider propagatorProvider;
    private IFrameProvider frameProvider;
    private ContactPredictionRequest<ContactData> request;
    private ContactPredictionConfiguration config;
    private List<GroundStation> gsList;
    private TleOrbitalParameters tleParams;
    private Satellite sat;

    private OrekitContactPredictor predictor;
    private DateTime start;

    @Before
    public void setup() {
        System.setProperty("orekit.data.path", "src/main/resources/orekit-data.zip");

        propagatorProvider = new TlePropagatorProvider();
        frameProvider = new Cirf2000FrameProvider();

        start = new DateTime(NOW, DateTimeZone.UTC).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
        config = new ContactPredictionConfiguration();
        config.setSatelliteId(SATELLITE_ID);

        GroundStation gs = new GroundStation(GS_ID, GS_ID);
        gs.setGeoLocation(new GeoLocation(58.37D, 26.73D, 59.0D));
        gsList = new ArrayList<GroundStation>();
        gsList.add(gs);

        tleParams = new TleOrbitalParameters(SATELLITE_ID + "/TLE", "");
        tleParams.setSatelliteId(SATELLITE_ID);
        tleParams.setTleLine1("1 39161U 13021C   13136.21724948  .00001020  00000-0  18275-3 0   280");
        tleParams.setTleLine2("2 39161  98.1285 214.6400 0009123 209.0111 151.0590 14.68904954  1306");

        sat = new Satellite(SATELLITE_ID, SATELLITE_ID);

        request = new ContactPredictionRequest<ContactData>(start.getMillis());
        request.setConfiguration(config);
        request.setGroundStations(gsList);
        request.setTleParameters(tleParams);
        request.setSatellite(sat);

        predictor = new OrekitContactPredictor(ISSUER, propagatorProvider, null, frameProvider);
    }

    LocationContactEvent getByOrbitNumber(List<ContactData> list, long orbitNumber) {
        for (ContactData cd : list) {
            LocationContactEvent event = cd.getEvent();
            if (event.getOrbitNumber() == orbitNumber) {
                return event;
            }
        }
        return null;
    }

    @Test
    public void test() throws Exception {
        List<LocationContactEvent> results = new ArrayList<LocationContactEvent>();
        // predict contacts 16 times in a row with one hour shift
        for (int i = 0; i < 16; i++) {
            DateTime end = start.plusHours(24); // use 24h period for prediction
            request.setStartTime(start.getMillis());
            request.setEndTime(end.getMillis());
            predictor.predict(request);
            LocationContactEvent event = getByOrbitNumber(request.getPredictedEvents(), 639L); // use orbit 639
            results.add(event);
            start = start.plusHours(1); // shift by one hour
        }

        long startTime = results.get(0).getStartTime();

        for (LocationContactEvent e : results) {
            assertEquals(startTime, e.getStartTime());
        }
    }
}
