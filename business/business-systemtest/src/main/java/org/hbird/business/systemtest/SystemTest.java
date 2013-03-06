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
package org.hbird.business.systemtest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;
import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IPublish;
import org.hbird.exchange.configurator.StartArchiveComponent;
import org.hbird.exchange.configurator.StartCommandComponent;
import org.hbird.exchange.configurator.StartNavigationComponent;
import org.hbird.exchange.configurator.StartQueueManagerComponent;
import org.hbird.exchange.configurator.StartTaskExecutorComponent;
import org.hbird.exchange.constants.StandardComponents;
import org.hbird.exchange.dataaccess.CommitRequest;
import org.hbird.exchange.dataaccess.DeletionRequest;
import org.hbird.exchange.navigation.D3Vector;
import org.hbird.exchange.navigation.RadioChannel;
import org.hbird.exchange.navigation.RotatorProperties;
import org.hbird.exchange.navigation.TleOrbitalParameters;

public abstract class SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(SystemTest.class);

    protected boolean exitOnFailure = true;

    @Produce(uri = "direct:injection")
    protected ProducerTemplate injection;

    protected Listener monitoringListener = null;

    protected Listener parameterListener = null;

    protected Listener commandingListener = null;

    protected Listener failedCommandRequestListener = null;

    protected Listener businessCardListener = null;

    protected Listener orbitalStateListener = null;

    protected Listener locationEventListener = null;

    protected Listener labelListener = null;

    protected Listener stateListener = null;

    protected CamelContext context = null;

    protected static IPublish publishApi = ApiFactory.getPublishApi("SystemTest");
    
    protected static IDataAccess accessApi = ApiFactory.getDataAccessApi("SystemTest");
    
    protected void azzert(boolean assertion) {
        if (assertion == false) {
            LOG.error("FAILED.");
            if (exitOnFailure) {
                System.exit(1);
            }
        }
    }

    protected void azzert(boolean assertion, String message) {
        if (assertion == false) {
            LOG.error("SYSTEM TEST: " + message + " (FAILED)");
            if (exitOnFailure) {
                System.exit(1);
            }
        }
        else {
            LOG.info("SYSTEM TEST: " + message + " (OK)");
        }
    }

    public Listener getMonitoringListener() {
        return monitoringListener;
    }

    public void setMonitoringListener(Listener monitoringListener) {
        this.monitoringListener = monitoringListener;
    }

    public Listener getCommandingListener() {
        return commandingListener;
    }

    public void setCommandingListener(Listener commandingListener) {
        this.commandingListener = commandingListener;
    }

    public Listener getFailedCommandRequestListener() {
        return failedCommandRequestListener;
    }

    public void setFailedCommandRequestListener(
            Listener failedCommandRequestListener) {
        this.failedCommandRequestListener = failedCommandRequestListener;
    }

    protected static boolean monitoringArchiveStarted = false;

    public void startMonitoringArchive() throws InterruptedException {

        if (monitoringArchiveStarted == false) {
            LOG.info("Issuing command for start of a parameter archive.");

            StartArchiveComponent request = new StartArchiveComponent(StandardComponents.ARCHIVE);
            injection.sendBody(request);

            /** Give the component time to startup. */
            Thread.sleep(1000);

            monitoringArchiveStarted = true;
        }

        /** TODO Send command to the archive to delete all data. */
        injection.sendBody(new DeletionRequest("SystemTest", StandardComponents.ARCHIVE, "*:*"));

        /** Send command to commit all changes. */
        injection.sendBody(new CommitRequest("SystemTest", StandardComponents.ARCHIVE));

    }

    protected static List<String> startedTaskComponents = new ArrayList<String>();

    public void startTaskComponent(String name) throws InterruptedException {

        if (startedTaskComponents.contains(name) == false) {
            LOG.info("Issuing command for start of a task executor component '" + name + "'.");

            injection.sendBody(new StartTaskExecutorComponent(name));

            /** Give the component time to startup. */
            Thread.sleep(1000);

            startedTaskComponents.add(name);
        }
    }

    protected static boolean commandingChainStarted = false;

    public void startCommandingChain() throws InterruptedException {

        if (commandingChainStarted == false) {
            LOG.info("Issuing command for start of a commanding chain.");

            /** Create command component. */
            injection.sendBody(new StartCommandComponent("CommandingChain1"));
            commandingChainStarted = true;
        }
    }

    protected static boolean orbitPredictorStarted = false;

    public void startOrbitPredictor() throws InterruptedException {

        if (orbitPredictorStarted == false) {
            LOG.info("Issuing command for start of a orbital predictor.");

            /** Create command component. */
            injection.sendBody(new StartNavigationComponent(StandardComponents.ORBIT_PREDICTOR));
            orbitPredictorStarted = true;
        }
    }

    protected static boolean queueManagerStarted = false;

    public void startQueueManager() throws InterruptedException {

        if (queueManagerStarted == false) {
            LOG.info("Issuing command for start of a queue manager.");

            /** Create command component. */
            injection.sendBody(new StartQueueManagerComponent("CommandingQueueManager"));
            queueManagerStarted = true;
        }
    }

    public Listener getBusinessCardListener() {
        return businessCardListener;
    }

    public void setBusinessCardListener(Listener businessCardListener) {
        this.businessCardListener = businessCardListener;
    }

    public Listener getParameterListener() {
        return parameterListener;
    }

    public void setParameterListener(Listener parameterListener) {
        this.parameterListener = parameterListener;
    }

    public Listener getOrbitalStateListener() {
        return orbitalStateListener;
    }

    public void setOrbitalStateListener(Listener orbitalStateListener) {
        this.orbitalStateListener = orbitalStateListener;
    }

    public Listener getLocationEventListener() {
        return locationEventListener;
    }

    public void setLocationEventListener(Listener locationEventListener) {
        this.locationEventListener = locationEventListener;
    }

    public Listener getLabelListener() {
        return labelListener;
    }

    public void setLabelListener(Listener labelListener) {
        this.labelListener = labelListener;
    }

    public Listener getStateListener() {
        return stateListener;
    }

    public void setStateListener(Listener orbitalListener) {
        this.stateListener = orbitalListener;
    }

    public CamelContext getContext() {
        return context;
    }

    public void setContext(CamelContext context) {
        this.context = context;
    }

    protected void forceCommit() throws InterruptedException {
        /** Send command to commit all changes. */
        injection.sendBody(new CommitRequest("SystemTest", StandardComponents.PARAMETER_ARCHIVE));
        Thread.sleep(2000);
    }
    
    protected void publishGroundStationsAndSatellites() {
        /** Store a set of Ground Stations */
        RotatorProperties rotatorProperties = new RotatorProperties(0, -90, 360, 0, 180);
        RadioChannel channel = new RadioChannel(136920000l, 136920000l, true, true, 20l);
        List<RadioChannel> radioChannels = new ArrayList<RadioChannel>();
        radioChannels.add(channel);
        
        D3Vector geoLocationTartu = new D3Vector("SystemTest", "GeoLocation", D3Vector.class.getSimpleName(), "Tartu, Tähe 4", Math.toRadians(58.3000D), Math.toRadians(26.7330D), 59.0D);
        publishApi.publishGroundStation("ES5EC", "Main Control", "the main control centre", geoLocationTartu, rotatorProperties, radioChannels);

        D3Vector geoLocationAalborg = new D3Vector("SystemTest", "GeoLocation", D3Vector.class.getSimpleName(), "Aalborg", Math.toRadians(55.659306D), Math.toRadians(12.587585D), 59.0D);
        publishApi.publishGroundStation("Aalborg", "Supporting Receiver", "the main control centre", geoLocationAalborg, rotatorProperties, radioChannels);

        D3Vector geoLocationDarmstadt = new D3Vector("SystemTest", "GeoLocation", D3Vector.class.getSimpleName(), "Darmstadt", Math.toRadians(49.831605D), Math.toRadians(8.673706D), 59.0D);
        publishApi.publishGroundStation("Darmstadt", "Supporting Receiver", "the main control centre", geoLocationDarmstadt, rotatorProperties, radioChannels);

        D3Vector geoLocationNewYork = new D3Vector("SystemTest", "GeoLocation", D3Vector.class.getSimpleName(), "New York", Math.toRadians(40.66564D), Math.toRadians(-74.036865D), 59.0D);
        publishApi.publishGroundStation("NewYork", "Supporting Receiver", "the main control centre", geoLocationNewYork, rotatorProperties, radioChannels);


        /** Store a set of satellites */
        publishApi.publishSatellite("ESTCube-1", "Controlled Satellite", "ESTcube, the student satellite from TARTU");
        publishApi.publishSatellite("DKCube-1", "Monitored Satellite", "DKcube, the student satellite from AALBORG");
        publishApi.publishSatellite("DECube-1", "Monitored Satellite", "DEcube, the student satellite from BERLINE");        
    }
    
    protected TleOrbitalParameters publishTleParameters() {
        /** Store TLE*/
        String tleLine1 = "1 27842U 03031C   12330.56671446  .00000340  00000-0  17580-3 0  5478";
        String tleLine2 = "2 27842 098.6945 336.9241 0009991 090.9961 269.2361 14.21367546487935";
        TleOrbitalParameters parameters = publishApi.publishTleParameters("ESTCube-1", "Measured", tleLine1, tleLine2);
        
		publishApi.publishMetadata(parameters, "Author", "This file was approved by Gert Villemos the " + (new Date()).toString());
		
		return parameters;
    }
}
