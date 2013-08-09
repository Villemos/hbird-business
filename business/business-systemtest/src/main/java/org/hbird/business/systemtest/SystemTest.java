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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.hbird.business.api.ICatalogue;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IPartManager;
import org.hbird.business.api.IPublisher;
import org.hbird.business.archive.ArchiveComponent;
import org.hbird.business.commanding.CommandingComponent;
import org.hbird.business.groundstation.base.GroundStationPart;
import org.hbird.business.groundstation.configuration.RadioDriverConfiguration;
import org.hbird.business.groundstation.configuration.RotatorDriverConfiguration;
import org.hbird.business.groundstation.hamlib.radio.HamlibRadioPart;
import org.hbird.business.groundstation.hamlib.rotator.HamlibRotatorPart;
import org.hbird.business.navigation.ContactEventComponent;
import org.hbird.business.navigation.OrbitPropagationComponent;
import org.hbird.business.systemmonitoring.SystemMonitorComponent;
import org.hbird.business.taskexecutor.TaskExecutionComponent;
import org.hbird.business.tracking.TrackingComponent;
import org.hbird.business.tracking.quartz.TrackingDriverConfiguration;
import org.hbird.business.websockets.WebsocketInterfaceComponent;
import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.core.Metadata;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.Part;
import org.hbird.exchange.core.State;
import org.hbird.exchange.dataaccess.CommitRequest;
import org.hbird.exchange.groundstation.Antenna;
import org.hbird.exchange.groundstation.GroundStation;
import org.hbird.exchange.navigation.GeoLocation;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.hbird.exchange.tasking.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.Mongo;

public abstract class SystemTest {

    private static Logger LOG = LoggerFactory.getLogger(SystemTest.class);

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

    protected MessageListener messageListener = null;

    protected CamelContext context = null;

    protected static IPublisher publishApi;
    protected static IDataAccess accessApi;
    protected static ICatalogue catalogueApi;
    protected static IPartManager partmanagerApi;

    protected static ArchiveComponent archive = null;
    protected static CommandingComponent comComponent = null;
    protected static ContactEventComponent navComponent = null;
    protected static SystemMonitorComponent sysMon = null;
    protected static WebsocketInterfaceComponent webComponent = null;
    protected static OrbitPropagationComponent estcubePropagationComponent = null;
    protected static ContactEventComponent estcubeLocationComponent = null;

    protected static Satellite estcube1 = null;
    protected static Satellite dkCube1 = null;
    protected static Satellite deCube1 = null;
    protected static Satellite strand = null;

    protected static GroundStation es5ec = null;
    protected static GroundStation gsAalborg = null;
    protected static GroundStation gsDarmstadt = null;
    protected static GroundStation gsNewYork = null;

    protected static Part taskComponent = null;
    protected static Part scripts = null;
    protected static Part limits = null;

    // protected static Part mof = null;

    protected static Map<String, Part> parts = new HashMap<String, Part>();

    protected static String IDGroundSegment = "/estcube/mof/";
    protected static String IDSatellites = "/estcube/satellites/";
    protected static String IDsystemTestRequest = "/systemtest/request/";
    protected static String IDsystemTestParameter = "/systemtest/parameter/";

    static {
        /** Build the system model, starting from 'the mission' */
        // Part mission = new Part("ESTCUBE", "ESTCUBE", "The root system. Complete, everything.");
        // registerPart(mission);
        //
        // /** Define the satellites */
        // Part satellites = new Satellite(mission, "Satellites", "The satellite(s) of the mission");
        // registerPart(satellites);
        //
        estcube1 = new Satellite("ESTCube-1", "ESTCube-1");
        estcube1.setDescription("ESTcube, the student satellite from TARTU");
        registerPart(estcube1);
        //
        // /** Define the ground station */
        // Part groundstations = new Part(mission, "Ground Station", "Ground Station",
        // "The groundstation(s) of the mission");
        // registerPart(groundstations);

        GeoLocation geoLocationTartu = new GeoLocation(58.37D, 26.73D, 59.0D);

        es5ec = new GroundStation("ES5EC", "ES5EC");
        es5ec.setDescription("The main control centre");
        es5ec.setGeoLocation(geoLocationTartu);
        registerPart(es5ec);

        Antenna antenna = new Antenna("Antenna1_ES5EC", "Antenna1_ES5EC");
        antenna.setDescription("The prime antenna");

        RotatorDriverConfiguration rotatorConfig = new RotatorDriverConfiguration();
        rotatorConfig.setDevicePort(4533);
        rotatorConfig.setMinAzimuth(-90D);
        GroundStationPart<RotatorDriverConfiguration> rotator = new HamlibRotatorPart("Rotator_ES5EC", "Rotator_ES5EC", rotatorConfig);

        RadioDriverConfiguration radioConfiguration = new RadioDriverConfiguration();
        radioConfiguration.setMinFrequency(136920000L);
        radioConfiguration.setMaxFrequency(136920000L);
        radioConfiguration.setGain(20L);
        radioConfiguration.setDevicePort(4532);
        GroundStationPart<RadioDriverConfiguration> radio = new HamlibRadioPart("Radio_ES5EC", "Radio_ES5EC", radioConfiguration);
        // registerPart(radio);
        //
        registerPart(antenna);
        es5ec.addAntenna(antenna);
        //
        // /** Define the ground segment control */
        // mof = new Part(mission, "MOC", "Mission Operation Center", "The groundsystem(s) of the mission");
        // registerPart(mof);
        //
        // Part orbitPropagationAutomation = new Part(mof, "OrbitPropagationAutomation", "Orbit Propagation Automation",
        // "The component automating the propagation of the ESTCube-1 orbit.");
        // registerPart(orbitPropagationAutomation);
        //
        // Part trackAutomation = new Part(mof, "Track Automation", "Track Automation",
        // "The component automating the track of ESTCube-1 by ES5EC.");
        // registerPart(trackAutomation);
        //
        // // OrbitPropagationComponent strandOrbitPropagator = new OrbitPropagationComponent("SystemTest",
        // // "ESTcubeNavigation", "", 60 * 60 * 1000, 6 * 60 * 60 * 1000, strand, locations);
        //
        archive = new ArchiveComponent("ARCHIVE");
        //
        comComponent = new CommandingComponent("COMMANDING");
        // registerPart(comComponent);
        //
        navComponent = new ContactEventComponent("NAVIGATION");
        // registerPart(navComponent);
        //
        // scripts = new Part(mof, "Synthetic Parameters", "Synthetic Parameters",
        // "The synthetic parameters / scripts");
        // registerPart(scripts);
        //
        sysMon = new SystemMonitorComponent("SYSTEM_MONITORING");
        // registerPart(sysMon);
        //
        // taskComponent = new Part(mof, "Task Executor", "Task Executor", "");
        // registerPart(taskComponent);
        //
        // limits = new Part(mof, "Limits", "Limits", "The limit checkers of the system");
        // registerPart(limits);
        //
        webComponent = new WebsocketInterfaceComponent("WEB_IF");
        // registerPart(webComponent);
        //
        // /** Setup the external satellites and ground stations. */
        // Part external = new Part("Externals", "Externals", "External parts which we are interested in.");
        // registerPart(external);
        //
        // Part eSatellites = new Part(external, "Satellites_EXT", "Satellites", "External satellites.");
        // registerPart(eSatellites);
        //
        dkCube1 = new Satellite("DKCube-1", "DKCube-1");
        dkCube1.setDescription("DKcube, the student satellite from AALBORG");
        registerPart(dkCube1);
        //
        deCube1 = new Satellite("DECube-1", "DECube-1");
        deCube1.setDescription("DEcube, the student satellite from BERLINE");
        registerPart(deCube1);
        //
        strand = new Satellite("STRaND-1", "STRaND-1");
        strand.setDescription("SSTL Smartphone nanosatellite");
        registerPart(strand);
        //
        // Part eGs = new Part(external, "GroundStations_EXT", "Ground Stations", "External ground stations.");
        // registerPart(eGs);
        //

        GeoLocation geoLocationAalborg = new GeoLocation(55.659306D, 12.587585D, 59.0D);
        gsAalborg = new GroundStation("AALBORG_GS", "AALBORG_GS");
        gsAalborg.setDescription("Supportive antenna from Aalborg university");
        gsAalborg.setGeoLocation(geoLocationAalborg);
        registerPart(gsAalborg);
        gsAalborg.addAntenna(antenna);

        GeoLocation geoLocationDarmstadt = new GeoLocation(49.87D, 8.64D, 59.0D);

        gsDarmstadt = new GroundStation("DARMSTADT_GS", "DARMSTADT_GS");
        gsDarmstadt.setDescription("Supportive antenna from Darmstadt university");
        gsDarmstadt.setGeoLocation(geoLocationDarmstadt);
        registerPart(gsDarmstadt);
        // // 49,87 LON:8,64
        //
        Antenna darmstadtAntenna = new Antenna("Antenna1_DAR", "Antenna1_DAR");
        darmstadtAntenna.setDescription("The prime antenna of DARMSTADT");
        GroundStationPart<RotatorDriverConfiguration> darmstadtRotator = new HamlibRotatorPart("Rotator_DAR", "Rotator_DAR", rotatorConfig);
        GroundStationPart<RadioDriverConfiguration> darmstadtRadio = new HamlibRadioPart("Radio_DAR", "Radio_DAR", radioConfiguration);
        // registerPart(darmstadtRadio);
        // registerPart(darmstadtRotator);
        gsDarmstadt.addAntenna(darmstadtAntenna);

        GeoLocation geoLocationNewYork = new GeoLocation(40.66564D, -74.036865D, 59.0D);
        gsNewYork = new GroundStation("NY_GS", "NY_GS");
        gsNewYork.setDescription("Supportive antenna from NewYork university");
        gsNewYork.setGeoLocation(geoLocationNewYork);
        // registerPart(gsNewYork);
        gsNewYork.addAntenna(antenna);
        //
        estcubePropagationComponent = new OrbitPropagationComponent("ESTCUBE_ORBIT_PROPAGATOR");
        estcubePropagationComponent.setSatelliteId(estcube1.getID());

        List<String> locations = new ArrayList<String>();
        locations.add(es5ec.getName());
        locations.add(gsDarmstadt.getName());
        estcubeLocationComponent = new ContactEventComponent("ESTCUBE_ES5EC_CONTACT_PREDICTOR");
        estcubeLocationComponent.setSatelliteId(estcube1.getID());
        estcubeLocationComponent.setLocations(locations);
        estcubeLocationComponent.setSatelliteId(estcube1.getID());
        // registerPart(estcubePropagationComponent);

    }

    protected static void registerPart(Part part) {
        parts.put(part.getName(), part);
    }

    protected void azzert(boolean assertion) {
        if (assertion == false) {
            LOG.error("FAILED in {}.", getClass().getSimpleName());
            if (exitOnFailure) {
                System.exit(1);
            }
        }
    }

    protected void azzert(boolean assertion, String message) {
        if (assertion == false) {
            LOG.error("SYSTEM TEST: {} (FAILED) in {}", message, getClass().getSimpleName());
            if (exitOnFailure) {
                System.exit(1);
            }
        }
        else {
            LOG.info("SYSTEM TEST: {} (OK)", message);
        }
    }

    void publishParameter(String ID, String name, String description, Number value, String unit) throws Exception {
        Parameter param = new Parameter(ID, name);

        param.setDescription(description);
        param.setValue(value);
        param.setUnit(unit);

        publishApi.publish(param);
    }

    void publishParameter(String ID, String name, String description, Number value, String unit, long timestamp) throws Exception {
        Parameter param = new Parameter(ID, name);

        param.setDescription(description);
        param.setValue(value);
        param.setUnit(unit);
        param.setTimestamp(timestamp);
        param.setVersion(timestamp);

        publishApi.publish(param);
    }

    void publishState(String ID, String name, String description, String applicableTo, boolean value) throws Exception {
        State state = new State(ID, name);

        state.setDescription(description);
        state.setApplicableTo(applicableTo);
        state.setValue(value);

        publishApi.publish(state);
    }

    void publishState(String ID, String name, String description, String applicableTo, boolean value, long timestamp) throws Exception {
        State state = new State(ID, name);

        state.setDescription(description);
        state.setApplicableTo(applicableTo);
        state.setValue(value);
        state.setTimestamp(timestamp);
        state.setVersion(timestamp);

        publishApi.publish(state);
    }

    void publishCommandRequest(String ID, String name, String description, Command command, List<String> lockStates, List<Task> tasks) throws Exception {
        CommandRequest request = new CommandRequest(ID, name);

        request.setDescription(description);
        request.setCommand(command);
        request.setLockStates(lockStates);
        request.setTasks(tasks);

        publishApi.publish(request);
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
        LOG.info("startMonitoringArchive()");

        if (monitoringArchiveStarted == false) {
            LOG.info("Issuing command for start of a parameter archive.");

            partmanagerApi.start(archive);

            /** Give the component time to startup. */
            Thread.sleep(3000);

            monitoringArchiveStarted = true;
        }
        else {
            LOG.info("Monitoring archive already started");
        }

        /** TODO Send command to the archive to delete all data. */
        // DeletionRequest request = new DeletionRequest("SystemTest");
        // request.setDeleteAll(true);
        // injection.sendBody(request);

        LOG.info("Dropping database hbird_test");
        Mongo mongo = getContext().getRegistry().lookup("mongo", Mongo.class);
        mongo.dropDatabase("hbird_test");

        Thread.sleep(2000);

        /** Send command to commit all changes. */
        injection.sendBody(new CommitRequest("SystemTest"));

    }

    protected static List<String> startedTaskComponents = new ArrayList<String>();

    public void startTaskComponent(String ID) throws InterruptedException {

        if (startedTaskComponents.contains(ID) == false) {
            LOG.info("Issuing command for start of a task executor component '" + ID + "'.");

            TaskExecutionComponent taskPart = new TaskExecutionComponent(ID);
            taskPart.setName(ID);

            /** Publish the knowledge of the part. */
            try {
                publishApi.publish(taskPart);
            }
            catch (Exception e) {
                LOG.error("Failed to publish TaskExecutionComponent", e);
            }

            /** Start the part. */
            partmanagerApi.start(taskPart);

            /** Give the component time to startup. */
            Thread.sleep(1000);

            startedTaskComponents.add(ID);
        }
    }

    public void stopTaskComponent(String ID) throws InterruptedException {

        if (startedTaskComponents.contains(ID) == true) {
            LOG.info("Issuing command for stop of a task executor component '" + ID + "'.");

            /** Stop the part. */
            partmanagerApi.stop(ID);

            /** Give the component time to startup. */
            Thread.sleep(1000);

            startedTaskComponents.remove(ID);
        }
    }

    protected static boolean commandingChainStarted = false;

    public void startCommandingChain() throws InterruptedException {
        LOG.info("startCommandingChain()");

        if (commandingChainStarted == false) {
            LOG.info("Issuing command for start of a commanding chain.");

            /** Create command component. */
            partmanagerApi.start(comComponent);

            Thread.sleep(4000);

            commandingChainStarted = true;
        }
        else {
            LOG.info("CommandingChain already started");
        }
    }

    public void stopCommandingChain() throws InterruptedException {

        if (commandingChainStarted) {
            LOG.info("Issuing command to stop a commanding chain.");

            /** Create command component. */
            partmanagerApi.stop(comComponent.getID());

            Thread.sleep(4000);

            commandingChainStarted = false;
        }

    }

    protected static boolean orbitPredictorStarted = false;

    public void startOrbitPredictor() throws InterruptedException {

        if (orbitPredictorStarted == false) {
            LOG.info("Issuing command for start of a orbital predictor.");

            partmanagerApi.start(navComponent);

            Thread.sleep(2000);

            orbitPredictorStarted = true;
        }
    }

    public void stopOrbitPredictor() throws InterruptedException {

        if (orbitPredictorStarted == true) {
            LOG.info("Issuing command for stop of a orbital predictor.");

            /** Create command component. */
            partmanagerApi.stop(navComponent.getID());

            Thread.sleep(2000);

            orbitPredictorStarted = false;
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

            TrackingDriverConfiguration config = new TrackingDriverConfiguration();
            config.setGroundstationId(es5ec.getID());
            config.setSatelliteIds(Arrays.asList(estcube1.getID()));

            /** Create command component. */

            TrackingComponent antennaController = new TrackingComponent("ES5EC_ESTCUBE1");
            antennaController.setDescription("The component automating the track of ESTCube-1 by ES5EC.");
            antennaController.setConfiguration(config);
            antennaController.setSatellite(estcube1.getID());
            antennaController.setLocation(es5ec.getID());
            partmanagerApi.start(antennaController);

            Thread.sleep(2000);

            antennaControllerStarter = true;
        }
    }

    public void stopAntennaController() throws InterruptedException {

        if (antennaControllerStarter == true) {
            LOG.info("Issuing command for stop of an antenna controller.");

            partmanagerApi.stop("ES5EC_ESTCUBE1");

            Thread.sleep(2000);

            antennaControllerStarter = false;
        }
    }

    protected static boolean estcube1OrbitPropagatorStarted = false;

    public void startEstcubeOrbitPropagator() throws InterruptedException {

        if (estcube1OrbitPropagatorStarted == false) {
            LOG.info("Issuing command for start of an ESTCube-1 orbit propagation.");

            /** Create command component. */
            partmanagerApi.start(estcubePropagationComponent);
            partmanagerApi.start(estcubeLocationComponent);

            Thread.sleep(2000);

            estcube1OrbitPropagatorStarted = true;
        }
    }

    public void stopEstcubeOrbitPropagator() throws InterruptedException {

        if (estcube1OrbitPropagatorStarted == true) {
            LOG.info("Issuing command for stop of an ESTCube-1 orbit propagation.");

            /** Create command component. */
            partmanagerApi.stop(estcubePropagationComponent.getID());

            Thread.sleep(2000);

            estcube1OrbitPropagatorStarted = false;
        }
    }

    public void startStrandAntennaController() throws InterruptedException {

        if (antennaControllerStarter == false) {
            LOG.info("Issuing command for start of an Strand -> Darmstadt antenna controller.");

            Part parent = parts.get("Track Automation");

            TrackingDriverConfiguration config = new TrackingDriverConfiguration();
            config.setGroundstationId(gsDarmstadt.getID());
            config.setSatelliteIds(Arrays.asList(strand.getID()));

            /** Create command component. */
            TrackingComponent antennaController = new TrackingComponent("DARMSTADT_STRAND");
            antennaController.setDescription("The component automating the track of Strand-1 by Darmstadt.");
            antennaController.setConfiguration(config);

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
                try {
                    publishApi.publish(part);
                }
                catch (Exception e) {
                    LOG.error("Failed to publish groundstation/satellite", e);
                }
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

    protected TleOrbitalParameters publishTLE(String ID, String name, String satelliteId, String line1, String line2) throws Exception {
        TleOrbitalParameters parameters = new TleOrbitalParameters(ID, name);
        parameters.setSatelliteId(satelliteId);
        parameters.setTleLine1(line1);
        parameters.setTleLine2(line2);

        return (TleOrbitalParameters) publishApi.publish(parameters);
    }

    protected Metadata publishMetadata(String ID, String name, EntityInstance applicableTo, String key, Object object) throws Exception {
        Metadata metadata = new Metadata(ID, name);
        metadata.setApplicableTo(applicableTo.getID());
        metadata.setMetadata(Collections.singletonMap(key, object));

        return (Metadata) publishApi.publish(metadata);
    }

    protected TleOrbitalParameters publishTleParameters() {
        /** Store TLE */
        String tleLine1 = "1 27842U 03031C   12330.56671446  .00000340  00000-0  17580-3 0  5478";
        String tleLine2 = "2 27842 098.6945 336.9241 0009991 090.9961 269.2361 14.21367546487935";

        try {
            TleOrbitalParameters parameters = publishTLE("/ESTCUBE1/TLE", "ESTCUBE1/TLE", estcube1.getID(), tleLine1, tleLine2);

            publishMetadata("/ESTCUBE1/TLE/metadata", "/ESTCUBE1/TLE/metadata", parameters, "Author", "This file was approved by Gert Villemos the "
                    + (new Date()).toString());

            tleLine1 = "1 39090U 13009E   13083.97990177  .00000074  00000-0  42166-4 0   342";
            tleLine2 = "2 39090  98.6360 274.2877 0009214 187.6058 172.4992 14.34286321  3925";
            parameters = publishTLE("/STRAND/TLE", "/STRAND/TLE", strand.getID(), tleLine1, tleLine2);

            publishMetadata("/STRAND/TLE/metadata", "/STRAND/TLE/metadata", parameters, "Author", "This file was approved by Gert Villemos the "
                    + (new Date()).toString());

            return parameters;
        }
        catch (Exception e) {
            LOG.error("Failed to publish TLE", e);

            return null;
        }
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

    public MessageListener getMessageListener() {
        return messageListener;
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public static IPublisher getPublishApi() {
        return publishApi;
    }

    public static void setPublishApi(IPublisher publishApi) {
        SystemTest.publishApi = publishApi;
    }

    public static IDataAccess getAccessApi() {
        return accessApi;
    }

    public static void setAccessApi(IDataAccess accessApi) {
        SystemTest.accessApi = accessApi;
    }

    public static ICatalogue getCatalogueApi() {
        return catalogueApi;
    }

    public static void setCatalogueApi(ICatalogue catalogueApi) {
        SystemTest.catalogueApi = catalogueApi;
    }

    public static IPartManager getPartmanagerApi() {
        return partmanagerApi;
    }

    public static void setPartmanagerApi(IPartManager partmanagerApi) {
        SystemTest.partmanagerApi = partmanagerApi;
    }
}
