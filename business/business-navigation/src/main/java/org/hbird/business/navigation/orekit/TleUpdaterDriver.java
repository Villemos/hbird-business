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
import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IPublisher;
import org.hbird.business.api.IdBuilder;
import org.hbird.business.core.SoftwareComponentDriver;
import org.hbird.business.navigation.TleUpdaterComponent;
import org.hbird.business.navigation.configuration.TleUpdaterConfiguration;
import org.hbird.business.navigation.processors.SatelliteRequestor;
import org.hbird.business.navigation.processors.TleRequestor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Builds route to update TLEs
 */
public class TleUpdaterDriver extends
        SoftwareComponentDriver<TleUpdaterComponent> {

    private static final Logger LOG = LoggerFactory.getLogger(TleUpdaterDriver.class);

    protected IDataAccess dao;
    protected IdBuilder idBuilder;

    @Autowired
    public TleUpdaterDriver(IDataAccess dao, IdBuilder idBuilder, IPublisher publisher) {
        super(publisher);

        this.dao = dao;
        this.idBuilder = idBuilder;
    }

    /**
     * @see org.hbird.business.core.SoftwareComponentDriver#doConfigure()
     */
    @Override
    protected void doConfigure() {

        // setup from component
        TleUpdaterComponent component = entity;
        TleUpdaterConfiguration config = (TleUpdaterConfiguration) component.getConfiguration();

        if (config.getUserName() == null || config.getPassword() == null || config.getUserName().isEmpty()
                || config.getPassword().isEmpty()) {
            LOG.warn("Space-Track authentication isn't set up");
            return;
        }

        if (config.getUpdateInterval() < 3600000) {
            LOG.error("Space-Track does not allow exceeding data retreival rate of one hour for TLEs");
            return;
        }

        // dependencies
        String componentId = component.getID();
        long updateInterval = config.getUpdateInterval();

        SatelliteRequestor requestSatellites = new SatelliteRequestor(dao);
        TleRequestor requestTLEs = new TleRequestor(config.getUserName(), config.getPassword(), idBuilder);

        LOG.info("Starting {}; with interval {} ms", new Object[] { getClass().getSimpleName(),
                updateInterval });
        // actual route
        // @formatter:off
		ProcessorDefinition<?> route = from(
				addTimer(componentId, updateInterval)) // execute using timer
				.bean(requestSatellites) // request satellites
				.bean(requestTLEs) // request and parse TLE for all satellites
				.split(body())
				.to("log:org.hbird.tle.request.stats?level=DEBUG&groupInterval=60000&groupDelay=60000&groupActiveOnly=false");
		// @formatter:on

        route.bean(publisher, "publish");
    }
}
