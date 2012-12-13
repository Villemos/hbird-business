package org.hbird.business.systemtest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.camel.Handler;
import org.hbird.exchange.core.IGenerationTimestamped;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.dataaccess.CommitRequest;
import org.hbird.exchange.navigation.ContactData;
import org.hbird.exchange.navigation.Location;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.hbird.exchange.navigation.TlePropagationRequest;

public class NavigationTester extends Tester {

	@Handler
	public void process() throws InterruptedException {

		startMonitoringArchive();
		startOrbitPredictor();

		Thread.sleep(2000);

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
		locations.add("Aalborg");

		String tleLine1 = "1 27842U 03031C   12330.56671446  .00000340  00000-0  17580-3 0  5478";
		String tleLine2 = "2 27842 098.6945 336.9241 0009991 090.9961 269.2361 14.21367546487935";
		TleOrbitalParameters tleParameter = new TleOrbitalParameters("SystemTest", "ESTcube", tleLine1, tleLine2);
		injection.sendBody(tleParameter);

		/** Send command to commit all changes. */
		injection.sendBody(new CommitRequest("SystemTest", "ParameterArchive"));	

		Thread.sleep(2000);

		monitoringListener.elements.clear();

		/** Send a TLE request for a satellite and a subset of locations */
		TlePropagationRequest request = new TlePropagationRequest("SystemTest", "ESTcube", tleLine1, tleLine2, 1355385448149l, locations);
		request.addArgument("stream", true);
		request.setDatasetidentifier("TEST_SET_1");

		int totalSleep = 0;

		injection.sendBody(request);


		while (totalSleep < 120000 && monitoringListener.elements.size() != 604) {
			Thread.sleep(2000);
			totalSleep += 2000;
		}
		azzert(monitoringListener.elements.size() == 1086, "Received orbital states, events and contact data. Expected 1086. Received " + monitoringListener.elements.size());
	
		print(monitoringListener.elements);
		monitoringListener.elements.clear();		
		
		/** Send a request without the TLE and without locations. The latest TLE and all locations should be taken. */
		request = new TlePropagationRequest("SystemTest", "ESTcube");
		request.addArgument("starttime", 1355385448149l);
		request.addArgument("stream", true);
		request.setDatasetidentifier("TEST_SET_2");
		injection.sendBody(request);

		totalSleep = 0;
		while (totalSleep < 120000 && monitoringListener.elements.size() != 604) {
			Thread.sleep(2000);
			totalSleep += 2000;
		}
		azzert(monitoringListener.elements.size() == 1086, "Received orbital states, events and contact data. Expected 1086. Received " + monitoringListener.elements.size());
		
		print(monitoringListener.elements);		
	}
	
	protected void print(List<Named> elements) {
		
		for (Named element : elements) {

			if (element instanceof TleOrbitalParameters) {
				continue;
			}
			
			if (element instanceof OrbitalState) {
				OrbitalState state = (OrbitalState) element;
				System.out.println(new Date(element.getTimestamp()) +"   Orbital State:  " + state.getPosition().p1 + ", " + state.getPosition().p2);
			}
			else if (element instanceof LocationContactEvent) {
				LocationContactEvent event = (LocationContactEvent) element;
				if (event.isVisible == true) {
					System.out.println(new Date(element.getTimestamp()) +" Location Event: " + event.location + " got contact to " + event.satellite);
				}
				else {
					System.out.println(new Date(element.getTimestamp()) +" Location Event: " + event.location + " lost contact to " + event.satellite);
				}
			}
			else if (element instanceof ContactData) {
				ContactData event = (ContactData) element;
				System.out.println(new Date(element.getTimestamp()) +"     Contact Data: azm=" + event.getAzimuth() + ", ele=" + event.getElevation() + ", dop=" + event.getDoppler() + ", dopshift=" + event.getDopplerShift());
			}
		}
	}
}
