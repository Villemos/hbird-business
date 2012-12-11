package org.hbird.business.simpleparametersimulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hbird.business.navigation.KeplianOrbitPredictor;
import org.hbird.business.navigation.OrbitPredictor;
import org.hbird.exchange.navigation.D3Vector;
import org.hbird.exchange.navigation.Location;
import org.hbird.exchange.navigation.OrbitPredictionRequest;
import org.hbird.exchange.navigation.KeplianOrbitalState;
import org.hbird.exchange.navigation.Satellite;
import org.orekit.errors.OrekitException;

public class OrbitSimulator {

	/** The class logger. */
	protected static Logger LOG = Logger.getLogger(OrbitSimulator.class);
	
	protected List<KeplianOrbitalState> orbitalstates = null;

	protected int index = 0;

	protected Satellite satellite = null;

	protected String type = "Measured Orbital State";
	
	protected List<Location> locations = null;

	protected KeplianOrbitalState initialState = null;

	protected String issuedBy = "ES5EC";
	
	/**
	 * Default constructor. Will create a simulator with a single satellite 'ESTcube' and propagate
	 * its orbit for 2 hours with 1 minute steps.
	 * 
	 */
	public OrbitSimulator() {	
		satellite = new Satellite("", "ESTcube", "The ESTcube cube satellite.");
		
		locations = new ArrayList<Location>();
		locations.add(new Location("", "ES5EC", "ESTcube ground station", 26.7147224, 58.3708465, 0d));

		D3Vector position = new D3Vector("", "Initial Position", "Position", "Initial position of ESTcube", -6142438.668, 3492467.560, -25767.25680);
		D3Vector velocity = new D3Vector("", "Initial Velocity", "Velocity", "Initial velocity of ESTcube", 505.8479685, 942.7809215, 7435.922231);

		initialState = new KeplianOrbitalState("Measured Orbital State", "Initial state", "", (new Date()).getTime(), "Test Data", satellite, position, velocity);
	}

	public OrbitSimulator(String issuedBy, String type, Satellite satellite, List<Location> locations, KeplianOrbitalState initialState) {
		this.issuedBy = issuedBy;
		this.type = type;
		this.satellite = satellite;
		this.locations = locations;
		this.initialState = initialState;
	}

	public KeplianOrbitalState process() {		

		/** If first time, then initzialize*/
		if (orbitalstates == null) {
			initialize();
		}
		/** If we have read all orbits, then take the next one. */
		else if (index == orbitalstates.size() - 1) {
			initialState = orbitalstates.get(index);
			orbitalstates.clear();
			index = 0;
			initialize();
		}

		/** TODO add error, to simulate that the orbit is not as predicted. */

		LOG.debug("Sending Orbital Message.");

		return orbitalstates.get(index++);
	}

	protected void initialize() {
		OrbitPredictionRequest request = new OrbitPredictionRequest(issuedBy, "", "Measured Orbital State", "A simulated orbit.", satellite, initialState, locations);

		OrbitPredictor orbitPredictor = new KeplianOrbitPredictor();
		try {
			/** The results will contain Orbital States as well as orbital events. Only take the states. 
			 * 
			 * TODO Update to also allow the simulator to issue the other events. */
			orbitalstates = new ArrayList<KeplianOrbitalState>();
			for (Object obj : orbitPredictor.predictOrbit(request)) {
				if (obj instanceof KeplianOrbitalState) {
					orbitalstates.add((KeplianOrbitalState) obj);
				}
			}
		} catch (OrekitException e) {
			e.printStackTrace();
		}	
	}
}
