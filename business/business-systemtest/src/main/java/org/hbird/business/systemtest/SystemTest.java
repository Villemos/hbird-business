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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;
import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.ICatalogue;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IOrbitPrediction;
import org.hbird.business.api.IPartManager;
import org.hbird.business.api.IPublish;
import org.hbird.business.archive.ArchiveComponent;
import org.hbird.business.commanding.CommandingComponent;
import org.hbird.business.navigation.NavigationComponent;
import org.hbird.business.navigation.OrbitPropagationComponent;
import org.hbird.business.systemmonitoring.SystemMonitorComponent;
import org.hbird.business.taskexecutor.TaskExecutionComponent;
import org.hbird.business.tracking.TrackingComponent;
import org.hbird.business.websockets.WebsocketInterfaceComponent;
import org.hbird.exchange.core.D3Vector;
import org.hbird.exchange.core.Part;
import org.hbird.exchange.dataaccess.CommitRequest;
import org.hbird.exchange.dataaccess.DeletionRequest;
import org.hbird.exchange.groundstation.Antenna;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;

import eu.estcube.gs.base.GroundStationTrackingDevice;
import eu.estcube.gs.base.IPointingDataOptimizer;
import eu.estcube.gs.configuration.RadioDriverConfiguration;
import eu.estcube.gs.configuration.RotatorDriverConfiguration;
import eu.estcube.gs.radio.HamlibRadioPart;
import eu.estcube.gs.rotator.HamlibRotatorPart;

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

    protected Listener groundStationPartListener = null;

    protected Listener inMemoryTestListener = null;

    protected Listener eventListener = null;

    protected CamelContext context = null;

    protected static IPublish publishApi = ApiFactory.getPublishApi("SystemTest");

    protected static IDataAccess accessApi = ApiFactory.getDataAccessApi("SystemTest");

    protected static ICatalogue catalogueApi = ApiFactory.getCatalogueApi("SystemTest");

    protected static IPartManager partmanagerApi = ApiFactory.getPartManagerApi("SystemTest");

    protected static IOrbitPrediction predictionApi = ApiFactory.getOrbitPredictionApi("SystemTest");

    protected static ArchiveComponent archive = null;
    protected static CommandingComponent comComponent = null;
    protected static NavigationComponent navComponent = null;
    protected static SystemMonitorComponent sysMon = null;
    protected static WebsocketInterfaceComponent webComponent = null;
    protected static OrbitPropagationComponent estcubePropagationComponent = null;

    protected static Satellite estcube1 = null;
    protected static Satellite dkCube1 = null;
    protected static Satellite deCube1 = null;
    protected static Satellite strand = null;

    protected static GroundStation es5ec = null;
    protected static GroundStation gsAalborg = null;
    protected static GroundStation gsDarmstadt = null;
    protected static GroundStation gsNewYork = null;

    protected static Part mof = null;

    protected static Map<String, Part> parts = new HashMap<String, Part>();

    private static IPointingDataOptimizer<RotatorDriverConfiguration> rotatorOptimizer = null;
    private static IPointingDataOptimizer<RadioDriverConfiguration> radioOptimizer = null;

    static {
        /** Build the system model, starting from 'the mission' */
        Part mission = new Part("ESTCUBE", "ESTCUBE", "The root system. Complete, everything.");
        registerPart(mission);

        /** Define the satellites */
        Part satellites = new Satellite("Satellites", "The satellite(s) of the mission");
        registerPart(satellites);
        satellites.setIsPartOf(mission);

        estcube1 = new Satellite("ESTCube-1", "ESTcube, the student satellite from TARTU");
        registerPart(estcube1);
        estcube1.setIsPartOf(satellites);

        /** Define the ground station */
        Part groundstations = new Part("Ground Station", "Ground Station", "The groundstation(s) of the mission");
        registerPart(groundstations);
        groundstations.setIsPartOf(mission);

        D3Vector geoLocationTartu = new D3Vector("SystemTest", "GeoLocation", D3Vector.class.getSimpleName(), "Tartu, Tähe 4", Math.toRadians(58.3000D),
                Math.toRadians(26.7330D), 59.0D);
        es5ec = new GroundStation("ES5EC", "The main control centre", geoLocationTartu);
        registerPart(es5ec);
        es5ec.setIsPartOf(groundstations);

        RotatorDriverConfiguration rotatorConfig = new RotatorDriverConfiguration();
        rotatorConfig.setDevicePort(4533);
        rotatorConfig.setMinAzimuth(-90D);
        GroundStationTrackingDevice<RotatorDriverConfiguration> rotator = new HamlibRotatorPart("Rotator_ES5EC", rotatorConfig, accessApi, predictionApi,
                rotatorOptimizer);

        RadioDriverConfiguration radioConfiguration = new RadioDriverConfiguration();
        radioConfiguration.setMinFrequency(136920000L);
        radioConfiguration.setMaxFrequency(136920000L);
        radioConfiguration.setGain(20L);
        radioConfiguration.setDevicePort(4532);
        GroundStationTrackingDevice<RadioDriverConfiguration> radio = new HamlibRadioPart("Radio_ES5EC", radioConfiguration, accessApi, predictionApi,
                radioOptimizer);
        Antenna antenna = new Antenna("Antenna1_ES5EC", "The prime antenna");
        registerPart(radio);
        rotator.setIsPartOf(antenna);
        radio.setIsPartOf(antenna);

        registerPart(antenna);
        es5ec.addAntenna(antenna);
        antenna.setIsPartOf(es5ec);

        /** Define the ground segment control */
        mof = new Part("MOC", "Mission Operation Center", "The groundsystem(s) of the mission");
        registerPart(mof);
        mof.setIsPartOf(mission);

        Part orbitPropagationAutomation = new Part("OrbitPropagationAutomation", "Orbit Propagation Automation",
                "The component automating the propagation of the ESTCube-1 orbit.");
        registerPart(orbitPropagationAutomation);
        orbitPropagationAutomation.setIsPartOf(mof);

        Part trackAutomation = new Part("Track Automation", "Track Automation", "The component automating the track of ESTCube-1 by ES5EC.");
        registerPart(trackAutomation);
        trackAutomation.setIsPartOf(mof);

        // OrbitPropagationComponent strandOrbitPropagator = new OrbitPropagationComponent("SystemTest",
        // "ESTcubeNavigation", "", 60 * 60 * 1000, 6 * 60 * 60 * 1000, strand, locations);

        archive = new ArchiveComponent();
        registerPart(archive);
        archive.setIsPartOf(mof);

        comComponent = new CommandingComponent();
        registerPart(comComponent);
        comComponent.setIsPartOf(mof);

        navComponent = new NavigationComponent();
        registerPart(navComponent);
        navComponent.setIsPartOf(mof);

        Part scripts = new Part("Synthetic Parameters", "Synthetic Parameters", "The synthetic parameters / scripts");
        registerPart(scripts);
        scripts.setIsPartOf(mof);

        sysMon = new SystemMonitorComponent();
        registerPart(sysMon);
        sysMon.setIsPartOf(mof);

        Part taskComponent = new Part("Task Executor", "Task Executor", "");
        registerPart(taskComponent);
        taskComponent.setIsPartOf(mof);

        Part limits = new Part("Limits", "Limits", "The limit checkers of the system");
        registerPart(limits);
        limits.setIsPartOf(mof);

        webComponent = new WebsocketInterfaceComponent();
        registerPart(webComponent);
        webComponent.setIsPartOf(mof);

        /** Setup the external satellites and ground stations. */
        Part external = new Part("Externals", "Externals", "External parts which we are interested in.");
        registerPart(external);

        Part eSatellites = new Part("Satellites_EXT", "Satellites", "External satellites.");
        registerPart(eSatellites);
        eSatellites.setIsPartOf(external);

        dkCube1 = new Satellite("DKCube-1", "DKcube, the student satellite from AALBORG");
        registerPart(dkCube1);
        dkCube1.setIsPartOf(eSatellites);

        deCube1 = new Satellite("DECube-1", "DEcube, the student satellite from BERLINE");
        registerPart(deCube1);
        deCube1.setIsPartOf(eSatellites);

        strand = new Satellite("STRaND-1", "SSTL Smartphone nanosatellite");
        registerPart(strand);
        strand.setIsPartOf(eSatellites);

        Part eGs = new Part("GroundStations_EXT", "Ground Stations", "External ground stations.");
        registerPart(eGs);
        eGs.setIsPartOf(external);

        D3Vector geoLocationAalborg = new D3Vector("SystemTest", "GeoLocation", D3Vector.class.getSimpleName(), "Aalborg", Math.toRadians(55.659306D),
                Math.toRadians(12.587585D), 59.0D);
        gsAalborg = new GroundStation("Aalborg", "Supportive antenna from Aalborg university", geoLocationAalborg);
        registerPart(gsAalborg);
        gsAalborg.setIsPartOf(eGs);
        gsAalborg.addAntenna(antenna);

        GroundStationTrackingDevice<RotatorDriverConfiguration> darmstadtRotator = new HamlibRotatorPart("Rotator_DAR", rotatorConfig, accessApi,
                predictionApi, rotatorOptimizer);
        GroundStationTrackingDevice<RadioDriverConfiguration> darmstadtRadio = new HamlibRadioPart("Radio_DAR", radioConfiguration, accessApi, predictionApi,
                radioOptimizer);
        Antenna darmstadtAntenna = new Antenna("Antenna1_DAR", "The prime antenna of DARMSTADT");
        registerPart(darmstadtRadio);
        registerPart(darmstadtRotator);
        darmstadtRotator.setIsPartOf(darmstadtAntenna);
        darmstadtRadio.setIsPartOf(darmstadtAntenna);

        // D3Vector geoLocationDarmstadt = new D3Vector("SystemTest", "GeoLocation", D3Vector.class.getSimpleName(),
        // "Darmstadt", Math.toRadians(49.831605D), Math.toRadians(8.673706D), 59.0D);
        D3Vector geoLocationDarmstadt = new D3Vector("SystemTest", "GeoLocation", D3Vector.class.getSimpleName(), "Darmstadt", Math.toRadians(49.87D),
                Math.toRadians(8.64D), 59.0D);
        gsDarmstadt = new GroundStation("Darmstadt", "Supportive antenna from Darmstadt university", geoLocationDarmstadt);
        registerPart(gsDarmstadt);
        darmstadtAntenna.setIsPartOf(gsDarmstadt);
        gsDarmstadt.setIsPartOf(eGs);
        gsDarmstadt.addAntenna(darmstadtAntenna);
        // 49,87 LON:8,64

        D3Vector geoLocationNewYork = new D3Vector("SystemTest", "GeoLocation", D3Vector.class.getSimpleName(), "New York", Math.toRadians(40.66564D),
                Math.toRadians(-74.036865D), 59.0D);
        gsNewYork = new GroundStation("NewYork", "Supportive antenna from NewYork university", geoLocationNewYork);
        registerPart(gsNewYork);
        gsNewYork.setIsPartOf(eGs);
        gsNewYork.addAntenna(antenna);

        List<String> locations = new ArrayList<String>();
        locations.add(es5ec.getName());
        locations.add(gsDarmstadt.getName());
        estcubePropagationComponent = new OrbitPropagationComponent("ESTcubeNavigation", "", 60 * 1000, 12 * 60 * 60 * 1000, estcube1, locations);
        registerPart(estcubePropagationComponent);
        estcubePropagationComponent.setIsPartOf(orbitPropagationAutomation);

    }

    protected static void registerPart(Part part) {
        parts.put(part.getName(), part);
    }

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

            partmanagerApi.start(archive);

            /** Give the component time to startup. */
            Thread.sleep(3000);

            monitoringArchiveStarted = true;
        }

        /** TODO Send command to the archive to delete all data. */
        injection.sendBody(new DeletionRequest("SystemTest", true));

        Thread.sleep(2000);

        /** Send command to commit all changes. */
        injection.sendBody(new CommitRequest("SystemTest"));

    }

    protected static List<String> startedTaskComponents = new ArrayList<String>();

    public void startTaskComponent(String name) throws InterruptedException {

        if (startedTaskComponents.contains(name) == false) {
            LOG.info("Issuing command for start of a task executor component '" + name + "'.");

            Part parent = parts.get("Task Executor");

            TaskExecutionComponent taskPart = new TaskExecutionComponent();
            taskPart.setName(name);
            taskPart.setIsPartOf(parent);

            /** Publish the knowledge of the part. */
            publishApi.publish(taskPart);

            /** Start the part. */
            partmanagerApi.start(taskPart);

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
            partmanagerApi.start(comComponent);

            Thread.sleep(4000);

            commandingChainStarted = true;
        }
    }

    public void stopCommandingChain() throws InterruptedException {

        if (commandingChainStarted) {
            LOG.info("Issuing command to stop a commanding chain.");

            /** Create command component. */
            partmanagerApi.stop(comComponent.getName());

            Thread.sleep(4000);

            commandingChainStarted = false;
        }

    }

    protected static boolean orbitPredictorStarted = false;

    public void startOrbitPredictor() throws InterruptedException {

        if (orbitPredictorStarted == false) {
            LOG.info("Issuing command for start of a orbital predictor.");

            /** Create command component. */
            partmanagerApi.start(navComponent);

            Thread.sleep(2000);

            orbitPredictorStarted = true;
        }
    }

    protected static boolean websocketsStarted = false;

    public void startWebSockets() throws InterruptedException {

        if (websocketsStarted == false) {
            LOG.info("Issuing command for start of a orbital predictor.");

            /** Create command component. */
            partmanagerApi.start(webComponent);

            Thread.sleep(2000);

            websocketsStarted = true;
        }
    }

    protected static boolean antennaControllerStarter = false;

    public void startAntennaController() throws InterruptedException {

        if (antennaControllerStarter == false) {
            LOG.info("Issuing command for start of an antenna controller.");

            Part parent = parts.get("Track Automation");

            /** Create command component. */
            TrackingComponent antennaController = new TrackingComponent("ES5EC_ESTCUBE1", "ES5EC -> ESTCUBE",
                    "The component automating the track of ESTCube-1 by ES5EC.",
                    estcube1.getID(), es5ec.getID());
            antennaController.setIsPartOf(parent);

            partmanagerApi.start(antennaController);

            Thread.sleep(2000);

            antennaControllerStarter = true;
        }
    }

    protected static boolean estcube1OrbitPropagatorStarted = false;

    public void startEstcubeOrbitPropagator() throws InterruptedException {

        if (estcube1OrbitPropagatorStarted == false) {
            LOG.info("Issuing command for start of an ESTCube-1 orbit propagation.");

            /** Create command component. */
            partmanagerApi.start(estcubePropagationComponent);

            Thread.sleep(2000);

            estcube1OrbitPropagatorStarted = true;
        }
    }

    public void startStrandAntennaController() throws InterruptedException {

        if (antennaControllerStarter == false) {
            LOG.info("Issuing command for start of an Strand -> Darmstadt antenna controller.");

            Part parent = parts.get("Track Automation");

            /** Create command component. */
            TrackingComponent antennaController = new TrackingComponent("DARMSTADT_STRAND", "Darmstadt -> STRAND",
                    "The component automating the track of Strand-1 by Darmstadt.",
                    strand.getName(), gsDarmstadt.getName());
            antennaController.setIsPartOf(parent);

            partmanagerApi.start(antennaController);

            Thread.sleep(2000);

            antennaControllerStarter = true;
        }
    }

    /**
     * @throws InterruptedException
     * 
     */
    protected void publishGroundStationsAndSatellites() throws InterruptedException {

        for (Part part : parts.values()) {
            if (part instanceof Satellite || part instanceof GroundStation) {
                publishApi.publish(part);
            }
        }

        forceCommit();
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

    public Listener getGroundStationPartListener() {
        return groundStationPartListener;
    }

    public void setGroundStationPartListener(Listener groundStationPartListener) {
        this.groundStationPartListener = groundStationPartListener;
    }

    public CamelContext getContext() {
        return context;
    }

    public void setContext(CamelContext context) {
        this.context = context;
    }

    protected void forceCommit() throws InterruptedException {
        /** Send command to commit all changes. */
        injection.sendBody(new CommitRequest("SystemTest"));
        Thread.sleep(2000);
    }

    protected TleOrbitalParameters publishTleParameters() {
        /** Store TLE */
        String tleLine1 = "1 27842U 03031C   12330.56671446  .00000340  00000-0  17580-3 0  5478";
        String tleLine2 = "2 27842 098.6945 336.9241 0009991 090.9961 269.2361 14.21367546487935";
        TleOrbitalParameters parameters = publishApi
                .publishTleParameters(estcube1.getName() + "/TLE", estcube1.getID(), tleLine1, tleLine2);

        publishApi.publishMetadata(parameters, "Author", "This file was approved by Gert Villemos the " + (new Date()).toString());

        tleLine1 = "1 39090U 13009E   13083.97990177  .00000074  00000-0  42166-4 0   342";
        tleLine2 = "2 39090  98.6360 274.2877 0009214 187.6058 172.4992 14.34286321  3925";
        parameters = publishApi.publishTleParameters(strand.getName() + "/TLE", strand.getID(), tleLine1, tleLine2);

        publishApi.publishMetadata(parameters, "Author", "This file was approved by Gert Villemos the " + (new Date()).toString());

        return parameters;
    }

    public Listener getInMemoryTestListener() {
        return inMemoryTestListener;
    }

    public void setInMemoryTestListener(Listener inMemoryTestListener) {
        this.inMemoryTestListener = inMemoryTestListener;
    }

    public Listener getEventListener() {
        return eventListener;
    }

    public void setEventListener(Listener eventListener) {
        this.eventListener = eventListener;
    }
}
