package org.hbird.business.systemtest;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.business.navigation.controller.OrbitPropagationController;
import org.hbird.exchange.navigation.Location;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;

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
		
		/** Store a set of locations */
		injection.sendBody(new Location("SystemTest", "TARTU", "Test location 1", Math.toRadians(58.3000D), Math.toRadians(26.7330D), 59.0D, 146.92 * 1000000));
		injection.sendBody(new Location("SystemTest", "Aalborg", "Test location 2", Math.toRadians(55.659306D), Math.toRadians(12.587585D), 59.0D, 136.92 * 1000000));
		injection.sendBody(new Location("SystemTest", "Darmstadt", "Test location 3", Math.toRadians(49.831605D), Math.toRadians(8.673706D), 59.0D, 126.92 * 1000000));
		injection.sendBody(new Location("SystemTest", "New York", "Test location 4", Math.toRadians(40.66564D), Math.toRadians(-74.036865D), 59.0D, 116.92 * 1000000));

		/** Store a set of satellites */
		injection.sendBody(new Satellite("SystemTest", "ESTcube", "Test satellite 1"));
		injection.sendBody(new Satellite("SystemTest", "DKcube", "Test satellite 2"));
		injection.sendBody(new Satellite("SystemTest", "DEcube", "Test satellite 3"));

		List<String> locations = new ArrayList<String>();
		locations.add("TARTU");
		locations.add("Darmstadt");
		
		String tleLine1 = "1 27842U 03031C   12330.56671446  .00000340  00000-0  17580-3 0  5478";
		String tleLine2 = "2 27842 098.6945 336.9241 0009991 090.9961 269.2361 14.21367546487935";
		TleOrbitalParameters tleParameter = new TleOrbitalParameters("SystemTest", "ESTcube", tleLine1, tleLine2);
		tleParameter.setDatasetidentifier("TLE/test");
		injection.sendBody(tleParameter);

		/** Send command to commit all changes. */
		forceCommit();
		
		/** Create a controller task and inject it. */
		OrbitPropagationController task = new OrbitPropagationController("SystemTest", "ESTcubeNavigation", "", 60 * 1000, 180 * 1000, "ESTcube", locations);
		task.setRepeat(3);
		injection.sendBody(task);
		
		Thread.sleep(2000);
		
		LOG.info("Finishing");
	}
}
