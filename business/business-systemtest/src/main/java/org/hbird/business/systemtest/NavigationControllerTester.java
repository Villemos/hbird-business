package org.hbird.business.systemtest;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.business.navigation.controller.OrbitPropagationController;

public class NavigationControllerTester extends SystemTest {
	
	private static org.apache.log4j.Logger LOG = Logger.getLogger(NavigationControllerTester.class);
	
	@Handler
	public void process() throws InterruptedException {
	
		LOG.info("------------------------------------------------------------------------------------------------------------");
		LOG.info("Starting");
		
		/** Start the archive. */
		startMonitoringArchive();		

		/** Start the navigation component. */
		startOrbitPredictor();		

		/** Start a task executor. */
		startTaskComponent("TaskExecutor1");

		Thread.sleep(1000);

		publishGroundStationsAndSatellites();
		publishTleParameters();
			
		List<String> locations = new ArrayList<String>();
		locations.add(es5ec.getQualifiedName());
		locations.add(gsDarmstadt.getQualifiedName());
		

		/** Send command to commit all changes. */
		forceCommit();
		
		/** Create a controller task and inject it. */
		OrbitPropagationController task = new OrbitPropagationController("SystemTest", "ESTcubeNavigation", "", 60 * 1000, 180 * 1000, estcube1, locations);
		task.setRepeat(3);
		injection.sendBody(task);
		
		Thread.sleep(20000);
		
		LOG.info("Finishing");
	}
}
