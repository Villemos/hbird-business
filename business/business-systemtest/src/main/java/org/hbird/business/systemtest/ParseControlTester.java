package org.hbird.business.systemtest;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.jms.InvalidSelectorException;
import javax.management.MalformedObjectNameException;
import javax.management.openmbean.OpenDataException;

import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.navigation.LocationContactEvent;

public class ParseControlTester extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(ParseControlTester.class);

    @Handler
    public void process() throws InterruptedException, MalformedObjectNameException, MalformedURLException, NullPointerException, IOException,
            InvalidSelectorException, OpenDataException {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        startMonitoringArchive();
        // startOrbitPredictor();

        /** Start a task executor. */
        startTaskComponent("TaskExecutor1");

        Thread.sleep(1000);

        publishTleParameters();
        publishGroundStationsAndSatellites();

        /** Send command to commit all changes. */
        forceCommit();

        Thread.sleep(1000);

        startEstcubeOrbitPropagator();

        /** Create a controller task and inject it. */

        Thread.sleep(1000);

        /** Now start the antenna controller. */
        startAntennaController();

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
            LocationContactEvent contactEvent = accessApi.getNextLocationContactEventFor(es5ec.getID(), estcube1.getID());
            if (contactEvent != null) {
                System.out.println("start p1 = " + contactEvent.getSatelliteStateAtStart().getPosition().getP1());
                System.out.println("start p2 = " + contactEvent.getSatelliteStateAtStart().getPosition().getP2());
                System.out.println("start p3 = " + contactEvent.getSatelliteStateAtStart().getPosition().getP3());

                System.out.println("start v1 = " + contactEvent.getSatelliteStateAtStart().getVelocity().getP1());
                System.out.println("start v2 = " + contactEvent.getSatelliteStateAtStart().getVelocity().getP2());
                System.out.println("start v3 = " + contactEvent.getSatelliteStateAtStart().getVelocity().getP3());

                System.out.println("start m1 = " + contactEvent.getSatelliteStateAtStart().getMomentum().getP1());
                System.out.println("start m2 = " + contactEvent.getSatelliteStateAtStart().getMomentum().getP2());
                System.out.println("start m3 = " + contactEvent.getSatelliteStateAtStart().getMomentum().getP3());
                break;
            }
        }

        /** Give the parse controller a bit of time to react. */
        totalDelay = 0;
        boolean found = false;
        while (totalDelay < 80000 && found == false) {
            Thread.sleep(5000);
            totalDelay += 5000;

            for (EntityInstance command : commandingListener.elements) {
                if (command.getName().equals("Track")) {
                    found = true;
                }
            }
        }

        azzert(found, "Received 'Track' command");

        stopOrbitPredictor();
        stopTaskComponent("TaskExecutor1");
        stopEstcubeOrbitPropagator();
        stopAntennaController();

        LOG.info("Finished");
    }
}
