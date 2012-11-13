package org.hbird.business.simpleparametersimulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hbird.exchange.navigation.D3Vector;
import org.hbird.exchange.navigation.Location;
import org.hbird.exchange.navigation.OrbitalState;
import org.hbird.exchange.navigation.Satellite;
import org.junit.Test;

public class OrbitPredictionRequestTest {

	@Test
	public void test() {
		
		System.setProperty("orekit.data.path", "src/test/resources");
		
		Satellite satellite = new Satellite("", "ESTcube", "The ESTcube cube satellite.");
		
		List<Location> locations = new ArrayList<Location>();
		locations.add(new Location("", "ES5EC", "ESTcube ground station", 26.7147224, 58.3708465, 0d));
		
		OrbitalState state = new OrbitalState();
		
		state.position = new D3Vector("", "Initial Position", "Position", "Initial position of ESTcube", -6142438.668, 3492467.560, -25767.25680);
		state.velocity = new D3Vector("", "Initial Velocity", "Velocity", "Initial velocity of ESTcube", 505.8479685, 942.7809215, 7435.922231);
		
		OrbitSimulator sim = new OrbitSimulator("JUnitTest", "Measured Orbital State", satellite, locations, state);
		
		boolean first =  false;
		for (int index = 1; index < 150; index++) {
			OrbitalState newState = sim.process();

			if (first == false) {
				System.out.println("Printing Orbital States.");
				System.out.println("   issuedBy : " + newState.getIssuedBy());
				System.out.println("   name     : " + newState.getName());
				System.out.println("   type     : " + newState.getType());
				System.out.println("   satellite: " + newState.getSatelitte().getName());
				
				first = true;
			}
			
			System.out.println("Step #" + index);
			System.out.println("   time: " + (new Date(newState.getTimestamp()).toGMTString()));
			System.out.println("   Step #" + index);
			System.out.println("   Position: " + newState.position.p1 + ", " + newState.position.p2 + ", " + newState.position.p3);
			System.out.println("   Velocity: " + newState.velocity.p1 + ", " + newState.velocity.p2 + ", " + newState.velocity.p3);
		}
	}
}
