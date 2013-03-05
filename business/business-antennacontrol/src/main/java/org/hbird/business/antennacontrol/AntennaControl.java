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
package org.hbird.business.antennacontrol;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.hbird.business.api.ICatalogue;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IQueueManagement;
import org.hbird.business.navigation.NavigationUtilities;
import org.hbird.exchange.movementcontrol.PointingRequest;
import org.hbird.exchange.movementcontrol.SetRadioFrequencyRequest;
import org.hbird.exchange.navigation.GroundStation;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.PointingData;
import org.hbird.exchange.navigation.Satellite;
import org.orekit.errors.OrekitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The antenna control will generate a schedule for a specific antenna (location) for tracking of a
 * satellite based on navigation data.
 * 
 * The antenna control will poll the archive at intervals to get the next contact event pair (Start/End of Contact) of
 * the location/satellite. Based on the events it will generate the detailed contact data (pointing info as well
 * as doppler) and an antenna schedule.
 * 
 * The antenna control depends on the navigation data being generated and published by another component. The
 * antenna control retrieves the data. It does not trigger its generation. This also means that the navigation data
 * may change in the background; a new TLE file may arrive, which leads to the generation of a new set of
 * contact events.
 * 
 * To keep the component as simple as possible the component is not event based. It will instead poll the archive
 * to check whether new contact events are available.
 * 
 * @author Gert Villemos
 * 
 */
public class AntennaControl {

    private static final Logger LOG = LoggerFactory.getLogger(AntennaControl.class);

    /** The definition of {@link GroundStation}. */
    protected final String groundStationName;

    /** The definition of the {@link Satellite} to track. */
    protected final String satelliteName;

    /** The name of this component. */
    protected final String name;

    /** The name of the activemq queue holding the antenna schedule. */
    protected String queueUri = "hbird.antennaschedule";

    /** The name of the route used to inject data to the schedule. The route must be available in the context. */
    protected String injectUri = "seda:inject.schedule.";

    /** API for retrieving contact data. */
    protected IDataAccess api = null;

    /** API to manage the antenna schedule queue. */
    protected IQueueManagement queueApi = null;

    /** API for retrieving Satellite and GroundStation objects. */
    protected ICatalogue catalogueApi = null;

    /** The last retrieved contact events for the location / satellite. */
    protected List<LocationContactEvent> nextContactEvents = new ArrayList<LocationContactEvent>();

    /**
     * Constructor.
     * 
     * @param name The name of this component. Used when issue requests (issuedBy).
     * @param location The location (antenna) that this controller is controlling.
     * @param satelliteName The satellite that the controller manages the schedule for.
     * @param queueName The name of the queue into which the antenna control commands shall be injected.
     */
    public AntennaControl(String name, String groundStationName, String satelliteName, String queueUri) {
        this.name = name;
        this.groundStationName = groundStationName;
        this.satelliteName = satelliteName;
        this.queueUri = queueUri;
        injectUri += name;
    }

    /**
     * Method to be called at intervals. The method will, based on the next set of contact events for
     * the location and satellite, create a schedule of time-tagged commands and inject them into the
     * queue of the antenna.
     * 
     * @param context The context of the processor. Must contain a 'from' route as defined by the 'injectName'
     *            attribute.
     * @throws OrekitException
     */
    public void process(CamelContext context) throws OrekitException {

        /** Retrieve the next set of contact events (start-end) for this station. */
        List<LocationContactEvent> contactEvents = api.retrieveNextLocationContactEventsFor(groundStationName, satelliteName);

        /** If there are contact events. */
        if (contactEvents.size() == 2) {

            /** Check if we already have events. If yes; see if they are different. */
            if (nextContactEvents.isEmpty() || nextContactEvents.get(0).getTimestamp() != contactEvents.get(0).getTimestamp()) {

                /** Purge the existing schedule. */
                try {
                    queueApi.clearQueue(queueUri);
                }
                catch (Exception e) {
                    LOG.error("Failed to clear command queue at {}", queueUri, e);
                }

                /** Create the new schedule. */
                nextContactEvents = contactEvents;

                ProducerTemplate template = context.createProducerTemplate();

                GroundStation groundStation = catalogueApi.getGroundStationByName(groundStationName);
                if (groundStation == null) {
                    LOG.error("No Ground Station available for the name {}", groundStationName);
                    return;
                }

                Satellite satellite = catalogueApi.getSatelliteByName(satelliteName);
                if (satellite == null) {
                    LOG.error("No Satellite available for the name {}", satelliteName);
                    return;
                }

                /** Create the commands to be executed PRE parse, i.e. for setup / configuration. */
                // TODO

                /** Calculate the contact data details based on the contact events. Create the WHILE parse commands. */
                for (PointingData point : NavigationUtilities.calculateContactData(contactEvents.get(0), contactEvents.get(1), groundStation, satellite, 500)) {
                    PointingRequest antennCommand = new PointingRequest(this.name, "", point.getAzimuth(), point.getElevation(), point.getDoppler());

                    // TODO - 05.03.2013, kimmell - actually there can be more than one frequency to set: up-link &
                    // down-link
                    double baseFrequency = getFrequency(groundStation, satellite);
                    double frequencyToSet = NavigationUtilities.calculateDopplerShift(point.getDoppler(), baseFrequency);
                    SetRadioFrequencyRequest radioCommand = new SetRadioFrequencyRequest(this.name, "", frequencyToSet);

                    antennCommand.setReleaseTime(point.getTimestamp());
                    radioCommand.setReleaseTime(point.getTimestamp());
                    template.sendBody(injectUri, antennCommand);
                    template.sendBody(injectUri, radioCommand);
                }

                /** Create the commands to be executed AFTER parse. */
                // TODO
            }
        }
    }

    double getFrequency(GroundStation groundStation, Satellite satellite) {
        // TODO - 05.03.2013, kimmell - implement
        return -1.0D;
    }

    /**
     * Getter of the URI of the route starting point used to inject the schedule.
     * 
     * @return The URI of the 'from' in the route.
     */
    public String getInjectUri() {
        return injectUri;
    }

    public IDataAccess getApi() {
        return api;
    }

    public void setApi(IDataAccess api) {
        this.api = api;
    }

    public IQueueManagement getQueueApi() {
        return queueApi;
    }

    public void setQueueApi(IQueueManagement queueApi) {
        this.queueApi = queueApi;
    }

    public void setCatalogueApi(ICatalogue catalogueApi) {
        this.catalogueApi = catalogueApi;
    }

    public ICatalogue getCatalogueApi() {
        return catalogueApi;
    }
}
