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

import java.util.Date;

import org.hbird.business.api.IPublisher;
import org.hbird.business.api.IdBuilder;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.navigation.NavigationComponent;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.orekit.errors.OrekitException;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.PVCoordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Gert Villemos
 * 
 */
public abstract class NavigationBean {

    protected static final Logger LOG = LoggerFactory.getLogger(NavigationBean.class);

    protected TleOrbitalParameters tleParameters;

    protected Propagator propagator;

    protected final NavigationComponent conf;

    protected final IDataAccess dao;

    protected final IPublisher publisher;

    protected final IdBuilder idBuilder;

    protected OrbitalStateCollector orbitalStateCollector;

    public NavigationBean(NavigationComponent configuration, IDataAccess dao, IPublisher publisher, IdBuilder idBuilder) {
        this.conf = configuration;
        this.dao = dao;
        this.publisher = publisher;
        this.idBuilder = idBuilder;
    }

    public synchronized void execute() throws OrekitException {

        /**
         * Propagate the orbit from NOW to 'NOW + leadTime + executionDelay. This ensures that
         * there will always be as a minimum 'leadTime' orbit prediction available and as a
         * maximum 'leadTime + executionDelay'.
         * 
         * Notice that if the task is started again, then the same prediction might be created. There
         * are two conditions
         * 1. The TLE is the same. In this case the same orbits will be calculated again, injected into
         * the system and stored. The values will override the existing values (... but will be exactly the same).
         * 2. The TLE is different. In this case a new orbit is calculated. There might thus be multiple sets of
         * orbital data at the same time, corresponding to different TLEs. It is the responsibility of the
         * users of the orbit data to selected the latest set.
         * 
         * */

        LOG.info("Propagating orbit of satellite '" + conf.getSatelliteId() + "'.");

        /** Get the latest TLE parameters. */
        TleOrbitalParameters newTleParameters = null;
        
        try {
        	newTleParameters = dao.getTleFor(conf.getSatelliteId());
        } catch(Exception e) {
            LOG.error("Failed to find TLE for satellite '" + conf.getSatelliteId() + "'. Cannot propagate orbital state, sorry.", e);
            return;
        }

        /** Check if the TLE has changed. */
        if (tleParameters != null && newTleParameters.getVersion() != tleParameters.getVersion()) {
            /** Reset. */
            propagator = null;
            orbitalStateCollector = null;
        }

        tleParameters = newTleParameters;

        /** Calculate the time we need to propagate */
        long from = 0;
        long to = 0;
        long now = System.currentTimeMillis();

        if (conf.getFrom() != 0) {
            from = conf.getFrom();
            to = conf.getTo();
        }
        else if (orbitalStateCollector == null || orbitalStateCollector.getDataSet().isEmpty()) {
            from = now;
            to = now + conf.getLeadTime() + conf.getExecutionDelay();
            LOG.info("No state or old state. Requesting TLE based propagating from '" + from + "' (NOW) to '" + to + "'");
        }
        else {
            from = orbitalStateCollector.getLatestState().getTimestamp();
            to = now + conf.getLeadTime() + conf.getExecutionDelay(); 
            LOG.info("Need to extend. Requesting TLE based from '" + from + "' to '" + to + "'");
        }

        AbsoluteDate startDate = new AbsoluteDate(new Date(from), TimeScalesFactory.getUTC());

        /** Create propagator. */
        if (propagator == null) {
            TLE tle = new TLE(tleParameters.getTleLine1(), tleParameters.getTleLine2());
            PVCoordinates initialOrbitalState = TLEPropagator.selectExtrapolator(tle).getPVCoordinates(startDate);
            Orbit initialOrbit = new KeplerianOrbit(initialOrbitalState, Constants.FRAME, startDate, Constants.MU);

            orbitalStateCollector = new OrbitalStateCollector(conf.getSatelliteId(), tleParameters.getInstanceID(), publisher, idBuilder);

            propagator = new KeplerianPropagator(initialOrbit);
            propagator.setMasterMode(conf.getStepSize(), orbitalStateCollector);
            preparePropagator();
        }

        propagator.propagate(new AbsoluteDate(startDate, (to - from) / 1000));
    }

    public abstract void preparePropagator() throws OrekitException;
}
