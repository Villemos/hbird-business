package org.hbird.business.systemtest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jms.InvalidSelectorException;
import javax.management.MalformedObjectNameException;
import javax.management.openmbean.OpenDataException;

import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.IQueueManagement;
import org.hbird.business.navigation.controller.OrbitPropagationController;
import org.hbird.exchange.configurator.StartAntennaControllerComponent;
import org.hbird.exchange.navigation.LocationContactEvent;

public class AntennaControlTester extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(AntennaControlTester.class);

    @Handler
    public void process() throws InterruptedException, MalformedObjectNameException, MalformedURLException, NullPointerException, IOException,
            InvalidSelectorException, OpenDataException {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        startMonitoringArchive();
        startOrbitPredictor();

        /** Start a task executor. */
        startTaskComponent("TaskExecutor1");

        Thread.sleep(1000);

        publishGroundStationsAndSatellites();
        publishTleParameters();

        List<String> locations = new ArrayList<String>();
        locations.add("ES5EC");
        locations.add("Darmstadt");

        /** Send command to commit all changes. */
        forceCommit();

        /** Create a controller task and inject it. */
        OrbitPropagationController task = new OrbitPropagationController("SystemTest", "ESTcubeNavigation", "", 60 * 1000, 12 * 60 * 60 * 1000, "ESTCube-1", locations);
        injection.sendBody(task);

        Thread.sleep(1000);

        /** Now start the antenna controller. */
        injection.sendBody(new StartAntennaControllerComponent("TestAntennaController", "ES5EC", "ESTCube-1"));

        Thread.sleep(5000);

        /**
         * The antenna controller will only generate the schedule AFTER the first 2 contact events have been generated.
         * We check this .
         */

        /** Retrieve the next set of contact events (start-end) for this station. */
         int totalDelay = 0;
        while (totalDelay < 130000) {
            Thread.sleep(5000);
            totalDelay += 5000;

            /** Check if we have contact events */
            List<LocationContactEvent> contactEvents = accessApi.retrieveNextLocationContactEventsFor("ES5EC", "ESTCube-1");
            if (contactEvents.size() > 2) {
                break;
            }
        }

        /** Give the antenna controller a chance to react. */
        Thread.sleep(60000);

        /** Check that the antenna schedule has been filled. */
        IQueueManagement api = ApiFactory.getQueueManagementApi("SystemTest");

        List<String> queues = api.listQueues();
        boolean found = false;
        for (String queue : queues) {
            if (queue.equals("hbird.antennaschedule.ES5EC")) {
                found = true;
                break;
            }
        }

        azzert(found, "The queue 'hbird.antennaschedule.TARTU' exist. ");

        Map<String, String> schedule = api.viewQueue("hbird.antennaschedule.ES5EC");
        azzert(schedule.size() > 0, "Queue contains messages. Contains " + schedule.size());

        LOG.info("Finished");
    }
}
