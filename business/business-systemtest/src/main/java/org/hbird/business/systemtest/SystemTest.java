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
import org.hbird.business.api.ICatalogue;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IPartManager;
import org.hbird.business.api.IPublish;
import org.hbird.business.archive.ArchiveComponent;
import org.hbird.business.commanding.CommandingComponent;
import org.hbird.business.navigation.NavigationComponent;
import org.hbird.business.systemmonitoring.SystemMonitorComponent;
import org.hbird.business.taskexecutor.TaskExecutionComponent;
import org.hbird.business.tracking.TrackingComponent;
import org.hbird.business.websockets.WebsocketInterfaceComponent;
import org.hbird.exchange.constants.StandardComponents;
import org.hbird.exchange.core.D3Vector;
import org.hbird.exchange.core.Part;
import org.hbird.exchange.dataaccess.CommitRequest;
import org.hbird.exchange.dataaccess.DeletionRequest;
import org.hbird.exchange.groundstation.Antenna;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.groundstation.RadioDevice;
import org.hbird.exchange.groundstation.Rotator;
import org.hbird.exchange.interfaces.IStartablePart;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;

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

    protected static Satellite estcube1 = null;
    protected static Satellite dkCube1 = null;
    protected static Satellite deCube1 = null;
    protected static Satellite strand = null;

    protected static GroundStation es5ec = null;
    protected static GroundStation gsAalborg = null;
    protected static GroundStation gsDarmstadt = null;
    protected static GroundStation gsNewYork = null;

    static {
        /** Build the system model, starting from 'the mission' */
        Part mission = new Part("ESTCUBE", "ESTCUBE", "The root system. Complete, everything.");

        /** Define the satellites */
        Part satellites = new Satellite("Satellites", "Satellites", "The satellite(s) of the mission");
        satellites.setIsPartOf(mission);

        estcube1 = new Satellite("ESTCube-1", "ESTCube-1", "ESTcube, the student satellite from TARTU");
        estcube1.setIsPartOf(satellites);

        /** Define the ground station */
        Part groundstations = new Part("Ground Station", "Ground Station", "The groundstation(s) of the mission");
        groundstations.setIsPartOf(mission);

        D3Vector geoLocationTartu = new D3Vector("SystemTest", "GeoLocation", D3Vector.class.getSimpleName(), "Tartu, Tähe 4", Math.toRadians(58.3000D),
                Math.toRadians(26.7330D), 59.0D);
        es5ec = new GroundStation("ES5EC", "The main control centre", geoLocationTartu);
        es5ec.setIsPartOf(groundstations);

        Rotator rotator = new HamlibRotatorPart("Rotator_ES5EC", "Rotator", 0, -90, 360, 0, 180, 4533, "localhost");
        RadioDevice radio = new HamlibRadioPart("Radio_ES5EC", "Radio", 136920000l, 136920000l, true, true, 20l, 4532, "localhost");
        Antenna antenna = new Antenna("Antenna1_ES5EC", "Antenna1", "The prime antenna", rotator, radio);
        es5ec.addAntenna(antenna);
        antenna.setIsPartOf(es5ec);

        /** Define the ground segment control */
        Part mof = new Part("MOC", "Mission Operation Center", "The groundsystem(s) of the mission");
        mof.setIsPartOf(mission);

        Part trackAutomation = new Part("Track Automation", "Track Automation", "The component automating the track of ESTCube-1 by ES5EC.");
        trackAutomation.setIsPartOf(mof);

        ArchiveComponent archive = new ArchiveComponent();
        archive.setIsPartOf(mof);

        CommandingComponent comComponent = new CommandingComponent();
        comComponent.setIsPartOf(mof);

        NavigationComponent navComponent = new NavigationComponent();
        navComponent.setIsPartOf(mof);

        Part scripts = new Part("Synthetic Parameters", "Synthetic Parameters", "The synthetic parameters / scripts");
        scripts.setIsPartOf(mof);

        SystemMonitorComponent sysMon = new SystemMonitorComponent();
        sysMon.setIsPartOf(mof);

        Part taskComponent = new Part("Task Executor", "Task Executor", "");
        taskComponent.setIsPartOf(mof);

        Part limits = new Part("Limits", "Limits", "The limit checkers of the system");
        limits.setIsPartOf(mof);

        WebsocketInterfaceComponent webComponent = new WebsocketInterfaceComponent();
        webComponent.setIsPartOf(mof);

        /** Setup the external satellites and ground stations. */
        Part external = new Part("Externals", "Externals", "External parts which we are interested in.");

        Part eSatellites = new Part("Satellites_EXT", "Satellites", "External satellites.");
        eSatellites.setIsPartOf(external);

        dkCube1 = new Satellite("DKCube-1", "DKCube-1", "DKcube, the student satellite from AALBORG");
        dkCube1.setIsPartOf(eSatellites);

        deCube1 = new Satellite("DECube-1", "DECube-1", "DEcube, the student satellite from BERLINE");
        deCube1.setIsPartOf(eSatellites);

        strand = new Satellite("STRaND-1", "STRaND-1", "SSTL Smartphone nanosatellite");
        strand.setIsPartOf(eSatellites);

        Part eGs = new Part("GroundStations_EXT", "Ground Stations", "External ground stations.");
        eGs.setIsPartOf(external);

        D3Vector geoLocationAalborg = new D3Vector("SystemTest", "GeoLocation", D3Vector.class.getSimpleName(), "Aalborg", Math.toRadians(55.659306D),
                Math.toRadians(12.587585D), 59.0D);
        gsAalborg = new GroundStation("Aalborg", "Supportive antenna from Aalborg university", geoLocationAalborg);
        gsAalborg.setIsPartOf(eGs);
        gsAalborg.addAntenna(antenna);

        Rotator darmstadtRotator = new HamlibRotatorPart("Rotator_DAR", "Rotator", 0, -90, 360, 0, 180, 4533, "localhost");
        RadioDevice darmstadtRadio = new HamlibRadioPart("Radio_DAR", "Radio", 136920000l, 136920000l, true, true, 20l, 4532, "localhost");
        Antenna darmstadtAntenna = new Antenna("Antenna1_DAR", "Antenna1", "The prime antenna of DARMSTADT", darmstadtRotator, darmstadtRadio);
        // D3Vector geoLocationDarmstadt = new D3Vector("SystemTest", "GeoLocation", D3Vector.class.getSimpleName(),
        // "Darmstadt", Math.toRadians(49.831605D), Math.toRadians(8.673706D), 59.0D);
        D3Vector geoLocationDarmstadt = new D3Vector("SystemTest", "GeoLocation", D3Vector.class.getSimpleName(), "Darmstadt", Math.toRadians(49.87D),
                Math.toRadians(8.64D), 59.0D);
        gsDarmstadt = new GroundStation("Darmstadt", "Supportive antenna from Darmstadt university", geoLocationDarmstadt);
        darmstadtAntenna.setIsPartOf(gsDarmstadt);
        gsDarmstadt.setIsPartOf(eGs);
        gsDarmstadt.addAntenna(darmstadtAntenna);
        // 49,87 LON:8,64

        D3Vector geoLocationNewYork = new D3Vector("SystemTest", "GeoLocation", D3Vector.class.getSimpleName(), "New York", Math.toRadians(40.66564D),
                Math.toRadians(-74.036865D), 59.0D);
        gsNewYork = new GroundStation("NewYork", "Supportive antenna from NewYork university", geoLocationNewYork);
        gsNewYork.setIsPartOf(eGs);
        gsNewYork.addAntenna(antenna);
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

            IStartablePart part = (IStartablePart) Part.getAllParts().get(StandardComponents.ARCHIVE);
            partmanagerApi.start(part);

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

            Part parent = Part.getAllParts().get("Task Executor");

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
            IStartablePart part = (IStartablePart) Part.getAllParts().get(StandardComponents.COMMANDING_CHAIN);
            partmanagerApi.start(part);

            Thread.sleep(2000);

            commandingChainStarted = true;
        }
    }

    public void stopCommandingChain() throws InterruptedException {

        if (commandingChainStarted) {
            LOG.info("Issuing command to stop a commanding chain.");

            /** Create command component. */
            IStartablePart part = (IStartablePart) Part.getAllParts().get(StandardComponents.COMMANDING_CHAIN);
            partmanagerApi.stop(part.getQualifiedName());

            Thread.sleep(2000);

            commandingChainStarted = false;
        }

    }

    protected static boolean orbitPredictorStarted = false;

    public void startOrbitPredictor() throws InterruptedException {

        if (orbitPredictorStarted == false) {
            LOG.info("Issuing command for start of a orbital predictor.");

            /** Create command component. */
            IStartablePart part = (IStartablePart) Part.getAllParts().get(StandardComponents.ORBIT_PREDICTOR);
            partmanagerApi.start(part);

            Thread.sleep(2000);

            orbitPredictorStarted = true;
        }
    }

    protected static boolean websocketsStarted = false;

    public void startWebSockets() throws InterruptedException {

        if (websocketsStarted == false) {
            LOG.info("Issuing command for start of a orbital predictor.");

            /** Create command component. */
            IStartablePart part = (IStartablePart) Part.getAllParts().get(StandardComponents.WEB_SOCKET);
            partmanagerApi.start(part);

            Thread.sleep(2000);

            websocketsStarted = true;
        }
    }

    protected static boolean antennaControllerStarter = false;

    public void startAntennaController() throws InterruptedException {

        if (antennaControllerStarter == false) {
            LOG.info("Issuing command for start of an antenna controller.");

            Part parent = Part.getAllParts().get("Track Automation");

            /** Create command component. */
            TrackingComponent antennaController = new TrackingComponent("ES5EC_ESTCUBE1", "ES5EC -> ESTCUBE", "The component automating the track of ESTCube-1 by ES5EC.",
                    estcube1.getQualifiedName(), es5ec.getQualifiedName());
            antennaController.setIsPartOf(parent);

            partmanagerApi.start(antennaController);

            Thread.sleep(2000);

            antennaControllerStarter = true;
        }
    }

    public void startStrandAntennaController() throws InterruptedException {

        if (antennaControllerStarter == false) {
            LOG.info("Issuing command for start of an Strand -> Darmstadt antenna controller.");

            Part parent = Part.getAllParts().get("Track Automation");

            /** Create command component. */
            TrackingComponent antennaController = new TrackingComponent("DARMSTADT_STRAND", "Darmstadt -> STRAND", "The component automating the track of Strand-1 by Darmstadt.",
                    strand.getQualifiedName(), gsDarmstadt.getQualifiedName());
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

        for (Part part : Part.getAllParts().values()) {
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
        injection.sendBody(new CommitRequest("SystemTest", StandardComponents.PARAMETER_ARCHIVE));
        Thread.sleep(2000);
    }

    protected TleOrbitalParameters publishTleParameters() {
        /** Store TLE */
        String tleLine1 = "1 27842U 03031C   12330.56671446  .00000340  00000-0  17580-3 0  5478";
        String tleLine2 = "2 27842 098.6945 336.9241 0009991 090.9961 269.2361 14.21367546487935";
        TleOrbitalParameters parameters = publishApi
                .publishTleParameters(estcube1.getQualifiedName() + "/TLE", estcube1.getQualifiedName(), tleLine1, tleLine2);

        publishApi.publishMetadata(parameters, "Author", "This file was approved by Gert Villemos the " + (new Date()).toString());

        tleLine1 = "1 39090U 13009E   13083.97990177  .00000074  00000-0  42166-4 0   342";
        tleLine2 = "2 39090  98.6360 274.2877 0009214 187.6058 172.4992 14.34286321  3925";
        parameters = publishApi.publishTleParameters(strand.getQualifiedName() + "/TLE", strand.getQualifiedName(), tleLine1, tleLine2);

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
