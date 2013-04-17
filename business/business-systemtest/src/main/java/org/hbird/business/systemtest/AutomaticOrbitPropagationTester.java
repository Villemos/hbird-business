package org.hbird.business.systemtest;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Handler;
import org.apache.log4j.Logger;

public class AutomaticOrbitPropagationTester extends SystemTest {
	
	private static org.apache.log4j.Logger LOG = Logger.getLogger(AutomaticOrbitPropagationTester.class);
	
	@Handler
	public void process() throws InterruptedException {
	
		LOG.info("------------------------------------------------------------------------------------------------------------");
		LOG.info("Starting");
		
		/** Start the archive. */
		startMonitoringArchive();		

		/** Start the navigation component. */
		startOrbitPredictor();		

		Thread.sleep(1000);

		publishGroundStationsAndSatellites();
		publishTleParameters();
			
		List<String> locations = new ArrayList<String>();
		locations.add(es5ec.getID());
		locations.add(gsDarmstadt.getID());
		

		/** Send command to commit all changes. */
		forceCommit();
		
		/** Create a controller task and inject it. */
		startEstcubeOrbitPropagator();
		
		Thread.sleep(20000);
		
		LOG.info("Finishing");
	}
}
