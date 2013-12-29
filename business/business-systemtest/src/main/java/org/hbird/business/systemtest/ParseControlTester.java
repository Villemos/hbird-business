package org.hbird.business.systemtest;

import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.navigation.LocationContactEvent;

public class ParseControlTester extends SystemTest {

    private static org.apache.log4j.Logger LOG = Logger.getLogger(ParseControlTester.class);

    @Handler
    public void process() throws Exception {

        LOG.info("------------------------------------------------------------------------------------------------------------");
        LOG.info("Starting");

        startMonitoringArchive();
        // startOrbitPredictor();

        /** Start a task executor. */
        startTaskComponent("TaskExecutor1");

        Thread.sleep(1000);

        publishTleParameters();
        publishGroundStationsAndSatellites();

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
            LocationContactEvent contactEvent = orbitalAccessApi.getNextLocationContactEventFor(es5ec.getID(), estcube1.getID());
            if (contactEvent != null) {
                System.out.println("start pX = " + contactEvent.getSatelliteStateAtStart().getPosition().getX());
                System.out.println("start pY = " + contactEvent.getSatelliteStateAtStart().getPosition().getY());
                System.out.println("start pZ = " + contactEvent.getSatelliteStateAtStart().getPosition().getZ());

                System.out.println("start vX = " + contactEvent.getSatelliteStateAtStart().getVelocity().getX());
                System.out.println("start vY = " + contactEvent.getSatelliteStateAtStart().getVelocity().getY());
                System.out.println("start vZ = " + contactEvent.getSatelliteStateAtStart().getVelocity().getZ());

                System.out.println("start mX = " + contactEvent.getSatelliteStateAtStart().getMomentum().getX());
                System.out.println("start mY = " + contactEvent.getSatelliteStateAtStart().getMomentum().getY());
                System.out.println("start mZ = " + contactEvent.getSatelliteStateAtStart().getMomentum().getZ());
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
