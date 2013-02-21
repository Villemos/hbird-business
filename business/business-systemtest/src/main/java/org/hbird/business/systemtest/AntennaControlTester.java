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
import org.hbird.business.api.IQueueManagement;
import org.hbird.business.api.impl.QueueManagerApi;
import org.hbird.business.navigation.controller.OrbitPropagationController;
import org.hbird.exchange.configurator.StartAntennaControllerComponent;
import org.hbird.exchange.navigation.Location;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.hbird.exchange.tasking.Task;

public class AntennaControlTester extends SystemTest {

	private static org.apache.log4j.Logger LOG = Logger.getLogger(AntennaControlTester.class);
	
	@Handler
	public void process() throws InterruptedException, MalformedObjectNameException, MalformedURLException, NullPointerException, IOException, InvalidSelectorException, OpenDataException {
		
		LOG.info("------------------------------------------------------------------------------------------------------------");
		LOG.info("Starting");
		
		startMonitoringArchive();
		startOrbitPredictor();
		
		/** Start a task executor. */
		startTaskComponent("TaskExecutor1");

		Thread.sleep(1000);
		
		/** Store a set of locations */
		Location tartu = new Location("SystemTest", "TARTU", "Test location 1", Math.toRadians(58.3000D), Math.toRadians(26.7330D), 59.0D, 146.92 * 1000000);
		injection.sendBody(tartu);
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
		Task task = new OrbitPropagationController("SystemTest", "ESTcubeNavigation", "", 60 * 1000, 12 * 60 * 60 * 1000, "ESTcube", locations);
		injection.sendBody(task);
		
		Thread.sleep(1000);

		/** Now start the antenna controller. */
		injection.sendBody(new StartAntennaControllerComponent("TestAntenna", tartu, "ESTcube"));
		
		Thread.sleep(5000);
		
		/** Check that the antenna schedule has been filled. */
		IQueueManagement api = new QueueManagerApi("SystemTest");

		List<String> queues = api.listQueues();
		azzert(queues.contains("org.apache.activemq:BrokerName=localhost,Destination=hbird.antennaschedule.TARTU,Type=Queue"));
		
		Map<String, String> schedule = api.viewQueue("org.apache.activemq:BrokerName=localhost,Destination=hbird.antennaschedule.TARTU,Type=Queue");

		Thread.sleep(60000);
		
		LOG.info("Finished");
	}
}
